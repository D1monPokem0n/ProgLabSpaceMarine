package ru.prog.itmo.control;

import ru.prog.itmo.command.Command;
import ru.prog.itmo.reader.ConsoleReader;
import ru.prog.itmo.reader.InvalidCommandException;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.speaker.ConsoleSpeaker;
import ru.prog.itmo.speaker.Speaker;

import java.util.LinkedList;

public class Controller {
    private final ClientState clientState;
    protected CommandMap commandMap;
    protected ConsoleArgument argument;
    protected final Speaker speaker;
    private final LinkedList<String> lastCommands;
    protected final CommandReader commandReader;
    protected ConnectionModule connectionModule;

    public Controller() {
        speaker = new ConsoleSpeaker();
        Reader reader = new ConsoleReader();
        lastCommands = new LinkedList<>();
        argument = new ConsoleArgument();
        clientState = new ClientState(true);
        commandReader = new CommandReader(reader, argument);
        try {
            connectionModule = new ConnectionModule(speaker);
            commandMap = new CommandMap(connectionModule, clientState, argument, lastCommands, speaker, reader);
        } catch (InvalidConnectionException e){
            speaker.speak(e.getMessage());
            clientState.setWorkStatus(false);
        }
    }

    public Controller(ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        this.connectionModule = connectionModule;
        this.speaker = speaker;
        clientState = new ClientState(true);
        lastCommands = new LinkedList<>();
        ConsoleArgument argument = new ConsoleArgument();
        commandReader = new CommandReader(reader, argument);
        commandMap = new CommandMap(connectionModule, clientState, argument, lastCommands, speaker, reader);
    }

    public void run() {
        String commandName;
        while (isWork()) {
            speaker.speak("Введите команду");
            try {
                commandName = commandReader.read();
                executeCommand(commandName);
            } catch (InvalidCommandException e) {
                speaker.speak(e.getMessage());
            }
        }
    }

    public void executeCommand(String commandName) {
        Command command = commandMap.getCommand(commandName);
        if (lastCommands.size() == 14) lastCommands.removeFirst();
        lastCommands.add(commandName);
        command.execute();
    }

    public boolean isWork() {
        return clientState.isWorkStatus();
    }

}
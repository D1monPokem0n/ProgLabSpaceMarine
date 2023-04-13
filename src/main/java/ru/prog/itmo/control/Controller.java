package ru.prog.itmo.control;


import ru.prog.itmo.command.Command;
import ru.prog.itmo.reader.CommandReader;
import ru.prog.itmo.reader.InvalidCommandException;
import ru.prog.itmo.reader.SpaceMarineConsoleReader;
import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.speaker.ConsoleSpeaker;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.storage.WrongStorageFileException;

import java.util.LinkedList;

public class Controller {
    private final ProgramState programState;
    protected CommandMap commandMap;
    protected ConsoleArgument argument;
    protected final Speaker speaker;
    private final LinkedList<String> lastCommands;
    protected final CommandReader commandReader;

    public Controller() {
        speaker = new ConsoleSpeaker();
        SpaceMarineReader reader = new SpaceMarineConsoleReader();
        lastCommands = new LinkedList<>();
        argument = new ConsoleArgument();
        programState = new ProgramState(true);
        commandReader = new CommandReader(reader, argument);
        try {
            Storage storage = new Storage();
            commandMap = new CommandMap(storage, programState, argument, lastCommands, speaker, reader);
        } catch (WrongStorageFileException e){
            speaker.speak(e.getMessage());
            programState.setWorkStatus(false);
        }

    }
    public Controller(Storage storage, Speaker speaker, SpaceMarineReader reader){
        this.speaker = speaker;
        programState = new ProgramState(true);
        lastCommands = new LinkedList<>();
        ConsoleArgument argument = new ConsoleArgument();
        commandReader = new CommandReader(reader, argument);
        commandMap = new CommandMap(storage, programState, argument, lastCommands, speaker, reader);
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
        return programState.isWorkStatus();
    }

}

package ru.prog.itmo.control;


import ru.prog.itmo.command.Command;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.reader.ConsoleReader;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.ConsoleSpeaker;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.storage.WrongStorageFileException;

public class Controller {
    private final ServerState serverState;
    protected ClientCommandsMap clientCommandsMap;
    protected final Speaker speaker;
    private ConnectionModule connectionModule;

    public Controller() {
        speaker = new ConsoleSpeaker();
        Reader reader = new ConsoleReader();
        serverState = new ServerState(true);
        try {
            Storage storage = new Storage();
            connectionModule = new ConnectionModule();
            clientCommandsMap = new ClientCommandsMap(storage, connectionModule, serverState, speaker, reader);
        } catch (WrongStorageFileException  e) {
            speaker.speak(e.getMessage());
            serverState.setWorkStatus(false);
        }

    }

    public void run() {
        try {
            while (isWork()){
                connectionModule.connect();
                connectionModule.receiveRequest();
                Request<?> request = connectionModule.getRequest();
                String command = request.getCommandType();
                executeCommand(command);
            }
        } catch (InvalidConnectionException e){
            speaker.speak(e.getMessage());
            serverState.setWorkStatus(false);
        }
    }

    public void executeCommand(String commandName) {
        Command command = clientCommandsMap.getCommand(commandName);
        command.execute();
    }

    public boolean isWork() {
        return serverState.isWorkStatus();
    }

}

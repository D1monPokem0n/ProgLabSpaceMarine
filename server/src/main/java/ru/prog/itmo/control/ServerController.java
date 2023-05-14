package ru.prog.itmo.control;

import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.reader.InvalidCommandException;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

import java.util.NoSuchElementException;

public class ServerController implements Runnable {
    private final ServerState serverState;
    protected ServerCommandMap commandsMap;
    protected final Speaker speaker;
    private Reader reader;
    private ConnectionModule connectionModule;

    public ServerController(ConnectionModule connectionModule, ServerState serverState, Speaker speaker, Storage storage, Reader reader) {
        this.connectionModule = connectionModule;
        this.serverState = serverState;
        this.speaker = speaker;
        this.reader = reader;
        commandsMap = new ServerCommandMap(storage, connectionModule, serverState, speaker, reader);
    }

    @Override
    public void run() {
        CommandReader commandReader = new CommandReader(reader);
        while (serverState.isWorkStatus()) {
            try {
                String command = commandReader.read();
                commandsMap.getCommand(command).execute();
            } catch (InvalidCommandException e) {
                speaker.speak("Вы ввели неверную команду");
            } catch (NoSuchElementException e){
                //
            }
        }
    }
    public ServerCommandMap commandMap(){
        return commandsMap;
    }
}

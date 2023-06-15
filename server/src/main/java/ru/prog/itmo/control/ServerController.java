package ru.prog.itmo.control;

import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.reader.ConsoleReader;
import ru.prog.itmo.reader.InvalidCommandException;
import ru.prog.itmo.reader.NoLineException;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.ConsoleSpeaker;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public class ServerController implements Runnable {
    private final ServerState serverState;
    protected ServerCommandMap commandsMap;
    private final Speaker speaker;
    private final Reader reader;
    private final CommandReader commandReader;

    public ServerController(ConnectionManager connectionManager,
                            ClientCommandsMap clientCommandsMap,
                            ServerState serverState,
                            Storage storage) {
        this.serverState = serverState;
        this.speaker = new ConsoleSpeaker();
        this.reader = new ConsoleReader();
        this.commandReader = new CommandReader(reader);
        commandsMap = new ServerCommandMap(storage, connectionManager, clientCommandsMap, serverState, speaker, reader);
    }

    @Override
    public void run() {
        while (serverState.isWorkStatus()) {
            try {
                String command = commandReader.read();
                commandsMap.getCommand(command).execute();
            } catch (InvalidCommandException e) {
                speaker.speak("Вы ввели неверную команду");
            } catch (NoLineException e) {
                speaker.speak("Завершение...");
            }
        }
    }
}

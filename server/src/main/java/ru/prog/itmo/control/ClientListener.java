package ru.prog.itmo.control;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import ru.prog.itmo.command.Command;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.reader.ConsoleReader;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.ConsoleSpeaker;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.NotWritableFileException;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.storage.WrongStorageFileException;

import java.io.File;


public class ClientListener {
    private final ServerState serverState;
    protected ClientCommandsMap commandsMap;
    protected final Speaker speaker;
    private final Reader reader;
    private ConnectionModule connectionModule;
    private Storage storage;
    private final static Logger LOGGER = LogManager.getLogger(ClientListener.class);


    public ClientListener() {
        speaker = new ConsoleSpeaker();
        reader = new ConsoleReader();
        serverState = new ServerState(true);
        try {
            storage = new Storage();
            LOGGER.log(Level.INFO, "Файл успешно считан.");
            connectionModule = new ConnectionModule();
            commandsMap = new ClientCommandsMap(storage, connectionModule, serverState, speaker, reader);
        } catch (WrongStorageFileException e) {
            speaker.speak(e.getMessage());
            LOGGER.log(Level.WARN, e.getMessage());
            serverState.setWorkStatus(false);
        }
        addShutdownHook(storage, speaker);
        setLogConfig();
    }

    private void addShutdownHook(Storage storage, Speaker speaker){
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    try {
                        storage.getFile().save(storage);
                    } catch (NotWritableFileException e) {
                        speaker.speak("Не возможно записать в файл...");
                    }
                    speaker.speak("Файл сохранён...");
                })
        );
    }

    public void run() {
        speaker.speak("Ждём запрос клиента");
        connectionModule.connect();
        while (isWork()) {
            try {
                connectionModule.receiveRequest();
                Request<?> request = connectionModule.getRequest();
                String command = request.getCommandType();
                speaker.speak(command);
                LOGGER.log(Level.INFO, "Получен запрос на исполение команды " + command);
                executeCommand(command);
            } catch (InvalidConnectionException e) {
                if (isWork()) {
                    speaker.speak(e.getMessage());
                    LOGGER.log(Level.WARN, e.getMessage());
                    Response<String> response = new Response<>(e.getMessage());
                    connectionModule.sendResponse(response);
                }
            }
        }
    }

    public void executeCommand(String commandName) {
        Command command = commandsMap.getCommand(commandName);
        command.execute();
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public Reader getReader() {
        return reader;
    }

    public ServerState getServerState() {
        return serverState;
    }

    public ConnectionModule getConnectionModule() {
        return connectionModule;
    }

    public Storage getStorage() {
        return storage;
    }

    public boolean isWork() {
        return serverState.isWorkStatus();
    }
    private void setLogConfig(){
        String log4JFilePath = "C:\\Users\\Димон Покемон\\Desktop\\test\\log4j2.xml";
        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        File file = new File(log4JFilePath);
        loggerContext.setConfigLocation(file.toURI());
    }
}

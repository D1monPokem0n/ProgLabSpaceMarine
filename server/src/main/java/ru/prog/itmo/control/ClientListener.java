package ru.prog.itmo.control;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.speaker.ConsoleSpeaker;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.storage.StorageDBException;
import ru.prog.itmo.storage.StorageDBFatalError;
import ru.prog.itmo.storage.WrongDataBaseException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;


public class ClientListener {
    private final ServerState serverState;
    protected ClientCommandsMap commandsMap;
    private ConnectionManager connectionManager;
    private Storage storage;
    public static Logger LOGGER;
    private static final String DEFAULT_LOG4J2_FILE = "log4j2.xml";
    private static final String LOG_ENV = "LOGGER";


    public ClientListener() {
        serverState = new ServerState(true);
        try {
            initLogger();
            storage = new Storage();
            initConnectionAndCommandMap();
        } catch (WrongDataBaseException | StorageDBException | InvalidConnectionException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            serverState.setWorkStatus(false);
        } catch (LogException e) {
            Speaker speaker = new ConsoleSpeaker();
            speaker.speak(e.getMessage());
            serverState.setWorkStatus(false);
        }
    }

    private void initConnectionAndCommandMap() {
        connectionManager = new ConnectionManager(commandsMap, serverState, storage);
        commandsMap = new ClientCommandsMap(storage, connectionManager);
        connectionManager.setCommandMap(commandsMap);
    }


    public void run() {
        try {
            connectionManager.startModules();
        } catch (StorageDBFatalError e) {
            LOGGER.log(Level.ERROR, e);
            LOGGER.log(Level.ERROR, "Требуется внешнее вмешательство");
        }
    }

    public ServerState getServerState() {
        return serverState;
    }

    public ConnectionManager getConnectionModule() {
        return connectionManager;
    }

    public Storage getStorage() {
        return storage;
    }

    private void initLogger() {
        String logFile = getLogFile();
        initLogContext(logFile);
        LOGGER = LogManager.getRootLogger();
    }

    private String getLogFile() {
        String logFile = System.getenv(LOG_ENV);
        if (logFile == null) {
            if (!Files.exists(Path.of(DEFAULT_LOG4J2_FILE)))
                throw new LogException("Не задана переменная окружения LOGGER.");
            logFile = DEFAULT_LOG4J2_FILE;
        }
        return logFile;
    }

    private void initLogContext(String log4JFilePath) {
        var loggerContext = (LoggerContext) LogManager.getContext(false);
        File file = new File(log4JFilePath);
        loggerContext.setConfigLocation(file.toURI());
    }

    public ClientCommandsMap getCommandsMap() {
        return commandsMap;
    }
}

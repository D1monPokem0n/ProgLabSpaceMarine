package ru.prog.itmo.control;

import ru.prog.itmo.command.authorization.LogOutException;
import ru.prog.itmo.command.Command;
import ru.prog.itmo.command.authorization.LoginCancelledException;
import ru.prog.itmo.command.authorization.LoginCommand;
import ru.prog.itmo.command.authorization.NotAuthorizedException;
import ru.prog.itmo.connection.*;
import ru.prog.itmo.reader.ConsoleReader;
import ru.prog.itmo.reader.InvalidCommandException;
import ru.prog.itmo.reader.NoLineException;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.ConsoleSpeaker;
import ru.prog.itmo.speaker.Speaker;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Properties;

public class Controller {
    protected CommandMap commandMap;
    protected ConsoleArgument argument;
    protected Speaker speaker;
    protected Reader reader;
    protected CommandReader commandReader;
    protected ConnectionModule connectionModule;
    protected SendModule sendModule;
    protected ReceiveModule receiveModule;
    protected static Properties properties;
    private final ClientState clientState;
    private final LinkedList<String> lastCommands;
    private static boolean isLogged;
    private static User user;
    private static String refreshToken;
    private static final Path TOKEN_FILE = Path.of("users.token");

    public Controller() {
        argument = new ConsoleArgument();
        initReadAndSpeak();
        lastCommands = new LinkedList<>();
        clientState = new ClientState(true);
        try {
            initConnection(speaker);
            loadTokens();
            commandMap = new CommandMap(
                    properties,
                    connectionModule,
                    sendModule,
                    receiveModule,
                    clientState,
                    argument,
                    lastCommands,
                    speaker,
                    reader
            );
        } catch (InvalidConnectionException | IOException e) {
            clientState.setWorkStatus(false);
            speaker.speak(e.getMessage());
        }
    }

    private void initReadAndSpeak() {
        speaker = new ConsoleSpeaker();
        reader = new ConsoleReader();
        commandReader = new CommandReader(reader, argument);
    }

    private void initConnection(Speaker speaker) {
        connectionModule = new ConnectionModule(speaker);
        sendModule = new SendModule(connectionModule.getSocket(), connectionModule.getHost(), speaker);
        receiveModule = new ReceiveModule(connectionModule.getSocket());
    }

    private void loadTokens() throws IOException {
        if (!Files.exists(TOKEN_FILE)) {
            speaker.speak("Created users.token");
            Files.createFile(TOKEN_FILE);
        }
        properties = new Properties();
        properties.load(new FileInputStream(TOKEN_FILE.toFile()));
    }

    public Controller(Properties properties,
                      ConnectionModule connectionModule,
                      SendModule sendModule,
                      ReceiveModule receiveModule,
                      Speaker speaker,
                      Reader reader) {
        initConnection(properties, connectionModule, sendModule, receiveModule);
        ConsoleArgument argument = new ConsoleArgument();
        initSpeakAndRead(speaker, reader, argument);
        clientState = new ClientState(true);
        lastCommands = new LinkedList<>();
        commandMap = new CommandMap(
                properties,
                connectionModule,
                sendModule,
                receiveModule,
                clientState,
                argument,
                lastCommands,
                speaker,
                reader);
    }

    private void initSpeakAndRead(Speaker speaker, Reader reader, ConsoleArgument argument) {
        this.speaker = speaker;
        this.reader = reader;
        commandReader = new CommandReader(reader, argument);
    }

    private void initConnection(Properties properties,
                                ConnectionModule connectionModule,
                                SendModule sendModule,
                                ReceiveModule receiveModule) {
        Controller.properties = properties;
        this.connectionModule = connectionModule;
        this.sendModule = sendModule;
        this.receiveModule = receiveModule;
    }

    public void run() {
        try {
            initUser();
            while (isWork()) {
                try {
                    readAndExecuteCommand();
                } catch (InvalidCommandException e) {
                    speaker.speak(e.getMessage());
                } catch (NotAuthorizedException e) {
                    isLogged = false;
                    repeatLogin();
                } catch (LogOutException e){
                    login();
                }
            }
        } catch (InvalidConnectionException | InvalidUserException e) {
            speaker.speak(e.getMessage());
            clientState.setWorkStatus(false);
        } catch (LoginCancelledException e) {
            clientState.setWorkStatus(false);
        } catch (NoLineException e){
            speaker.speak("Завершение...");
        } catch (NotAuthorizedException e) {
            speaker.speak("Проблемы с соединением...");
        }
    }

    private void readAndExecuteCommand() throws InvalidCommandException {
        speaker.speak("Введите команду");
        var commandName = commandReader.read();
        executeCommand(commandName);
    }

    private void initUser() {
        if (isWork()) {
            connect();
            login();
        }
    }

    private void connect() {
        connectionModule.connect();
        sendModule.setSocket(connectionModule.getSocket());
        sendModule.setHost(connectionModule.getHost());
        receiveModule.setSocket(connectionModule.getSocket());
    }

    private void login() {
        LoginCommand loginCommand = new LoginCommand(sendModule, receiveModule, speaker, reader);
        loginCommand.execute();
    }

    protected void repeatLogin() {
        var request = new Request<>("token_update", refreshToken);
        sendModule.submitSending(request);
        var response = receiveModule.getResponse();
        if ((Boolean) response.getData()) {
            returnToProgram(response);
        } else {
            setRefreshToken("null");
            speaker.speak("Требуется повторная авторизация.");
            login();
        }
    }

    private void returnToProgram(Response<?> response) {
        setRefreshToken(response.getRefreshToken());
        user.setToken(response.getAccessToken());
        var lastCommand = lastCommands.getLast();
        speaker.speak("Была произведена повторная аутентификация.\n" + lastCommand);
        if (!lastCommand.equals("execute_script"))
            executeCommand(lastCommand);
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

    public static void setUser(User user) {
        Controller.user = user;
    }

    public static User getUser() {
        if (user == null)
            return new User("defaultLogin", "defaultPassword");
        return user;
    }

    public static void setRefreshToken(String refreshToken) {
        refreshToken = Objects.isNull(refreshToken) ? "null" : refreshToken;
        Controller.refreshToken = refreshToken;
        properties.setProperty(user.getLogin(), refreshToken);
        try (var outputStream = new FileOutputStream(TOKEN_FILE.toFile())) {
            properties.store(outputStream, "=)");
        } catch (IOException e) {
            throw new InvalidUserException("Проблемы с файлом для токенов.");
        }

    }

    public static void clearUserInfo(){
        user = null;
        refreshToken = null;
    }


    public static Properties getProperties() {
        return properties;
    }

    public static void setIsLogged(boolean isLogged) {
        Controller.isLogged = isLogged;
    }

    public static boolean isNotLogged() {
        return !isLogged;
    }
}
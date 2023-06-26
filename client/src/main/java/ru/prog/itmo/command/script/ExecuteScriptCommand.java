package ru.prog.itmo.command.script;

import ru.prog.itmo.command.Command;
import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.command.ServerOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.command.authorization.NotAuthorizedException;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.ReceiveModule;
import ru.prog.itmo.connection.SendModule;
import ru.prog.itmo.control.ConsoleArgument;
import ru.prog.itmo.control.Controller;
import ru.prog.itmo.control.ScriptReader;
import ru.prog.itmo.reader.InvalidCommandException;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.builder.script.InvalidScriptException;
import ru.prog.itmo.speaker.Speaker;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ExecuteScriptCommand extends ServerOCommand {
//    private final Properties properties;
    private final ConsoleArgument argument;
    private final ConnectionModule connectionModule;
    public static ArrayList<Path> startedScripts = new ArrayList<>();

    public ExecuteScriptCommand(/*Properties properties,*/
                                ConnectionModule connectionModule,
                                SendModule sendModule,
                                ReceiveModule receiveModule,
                                Speaker speaker,
                                ConsoleArgument argument) {
        super("execute_script", sendModule, receiveModule, speaker);
        this.argument = argument;
        this.connectionModule = connectionModule;
//        this.properties = properties;
    }

    @Override
    public void execute() {
        try {
            var path = getScriptPath();
            startedScripts.add(path);
            var microController = createMicroController();
            try {
                while (microController.isWork())
                    microController.run();
            } catch (EndOfScriptException e) {
                speaker().speak("Исполнение скрипта " + path + " завершено.");
            } catch (InvalidScriptException e) {
                speaker().speak(e.getMessage());
                speaker().speak("Исполение скрипта " + path + " прервано.");
            } catch (NotAuthorizedException e){
                speaker().speak("Исполнение скрипта " + path + "прервано, т.к. требуется повторная аутентификация.");
                startedScripts.remove(path);
                throw e;
            }
            startedScripts.remove(path);
        } catch (InvalidPathException | IOException e) {
            speaker().speak("Неверный путь к файлу.");
        } catch (InvalidScriptException | RecursiveScriptException e) {
            speaker().speak(e.getMessage());
        }
    }

    private MicroController createMicroController() throws FileNotFoundException {
        FileInputStream fileStream = new FileInputStream(argument.getValue());
        InputStreamReader inputStream = new InputStreamReader(fileStream);
        ScriptReader scriptReader = new ScriptReader(speaker(), inputStream);
        return new MicroController(
//                properties,
                connectionModule,
                sendModule(),
                receiveModule(),
                speaker(),
                scriptReader);
    }

    private Path getScriptPath() throws IOException, RecursiveScriptException {
        String fineName = argument.getValue();
        if (fineName == null)
            throw new InvalidScriptException("Вы ввели пустой аргумент");
        Path path = Paths.get(fineName);
       validatePath(path);
        return path;
    }

    private void validatePath(Path path) throws IOException, RecursiveScriptException {
        if (!Files.exists(path)) throw new InvalidScriptException("Данного файла не существует.");
        if (!Files.isReadable(path)) throw new InvalidScriptException("Невозможно прочесть файл.");
        for (Path otherPath : startedScripts)
            if (Files.isSameFile(path, otherPath))
                throw new RecursiveScriptException("Обнаружен рекурсивный скрипт.");
    }

    static class MicroController extends Controller {
        public MicroController(/*Properties properties,*/
                                ConnectionModule connectionModule,
                               SendModule sendModule,
                               ReceiveModule receiveModule,
                               Speaker speaker,
                               Reader reader) {
            super(/*properties,*/ connectionModule, sendModule, receiveModule, speaker, reader);
            for (String key : commandMap.getCommandHashMap().keySet()) {
                if (commandMap.getCommand(key) instanceof UserAsking) {
                    for (Command realization : commandMap.getCommandsRealizationsMap().get(key)) {
                        if (realization instanceof ScriptExecutable) {
                            commandMap.put(key, realization);
                        }
                    }
                }
            }
        }

        @Override
        public void run() {
            String commandName;
            while (isWork()) {
                try {
                    synchronized (sendModule) {
                        commandName = commandReader.read();
                        executeCommand(commandName);
                    }
                    TimeUnit.SECONDS.sleep(1);
                } catch (InvalidCommandException | InterruptedException e) {
                    JOptionPane.showMessageDialog(null, "Oops.");
                }
            }
        }

    }

    @Override
    public String getDescription() {
        return "считать и исполнить скрипт из указанного файла.";
    }
}

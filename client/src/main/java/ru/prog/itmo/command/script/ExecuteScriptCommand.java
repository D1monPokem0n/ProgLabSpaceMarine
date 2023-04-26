package ru.prog.itmo.command.script;

import ru.prog.itmo.command.Command;
import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.command.ServerOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.control.ConsoleArgument;
import ru.prog.itmo.control.Controller;
import ru.prog.itmo.reader.InvalidCommandException;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.reader.ScriptReader;
import ru.prog.itmo.server.ConnectionModule;
import ru.prog.itmo.speaker.Speaker;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ExecuteScriptCommand extends ServerOCommand {
    private final ConsoleArgument argument;
    public static ArrayList<Path> startedScripts = new ArrayList<>();

    public ExecuteScriptCommand(ConnectionModule connectionModule, Speaker speaker, ConsoleArgument argument) {
        super("execute_script", connectionModule, speaker);
        this.argument = argument;
    }

    @Override
    public void execute() {
        try {
            String fineName = argument.getValue();
            if (fineName == null)
                throw new InvalidScriptException("Вы ввели пустой аргумент");
            Path path = Paths.get(fineName);
            if (!Files.exists(path)) throw new InvalidScriptException("Данного файла не существует.");
            if (!Files.isReadable(path)) throw new InvalidScriptException("Невозможно прочесть файл.");
            for (Path otherPath : startedScripts) {
                if (Files.isSameFile(path, otherPath))
                    throw new RecursiveScriptException("Обнаружен рекурсивный скрипт.");
            }
            startedScripts.add(path);
            FileInputStream fileStream = new FileInputStream(argument.getValue());
            InputStreamReader inputStream = new InputStreamReader(fileStream);
            ScriptReader scriptReader = new ScriptReader(speaker(), inputStream);
            MicroController microController = new MicroController(connectionModule(), speaker(), scriptReader);
            try {
                while (microController.isWork()) {
                    microController.run();
                }
            } catch (EndOfScriptException e) {
                speaker().speak("Исполнение скрипта " + path + " завершено.");
            } catch (InvalidScriptException e){
                speaker().speak(e.getMessage());
                speaker().speak("Исполение скрипта " + path + " прервано.");
            }
            startedScripts.remove(path);
        } catch (InvalidPathException |
                 IOException e) {
            speaker().speak("Неверный путь к файлу.");
        } catch (InvalidScriptException |
                 RecursiveScriptException e) {
            speaker().speak(e.getMessage());
        }
    }

    static class MicroController extends Controller {
        public MicroController(ConnectionModule connectionModule, Speaker speaker, Reader reader) {
            super(connectionModule, speaker, reader);
            for (String key : commandMap.getCommandHashMap().keySet()) {
                if (commandMap.getCommand(key) instanceof UserAsking) {
                    for (Command realization : commandMap.getCommandsRealizationsMap().get(key)){
                        if (realization instanceof ScriptExecutable){
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
                    commandName = commandReader.read();
                    executeCommand(commandName);
                } catch (InvalidCommandException e) {
                    throw new InvalidScriptException(e.getMessage());
                }
            }
        }

    }

    @Override
    public String getDescription() {
        return "считать и исполнить скрипт из указанного файла.";
    }
}

package ru.prog.itmo.command.script;

import ru.prog.itmo.command.Command;
import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.command.StorageOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.control.ConsoleArgument;
import ru.prog.itmo.control.Controller;
import ru.prog.itmo.reader.InvalidCommandException;
import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.reader.SpaceMarineScriptReader;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ExecuteScriptCommand extends StorageOCommand {
    private final ConsoleArgument argument;
    public static ArrayList<Path> startedScripts = new ArrayList<>();

    public ExecuteScriptCommand(Storage storage, Speaker speaker, ConsoleArgument argument) {
        super(storage, speaker);
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
            SpaceMarineScriptReader scriptReader = new SpaceMarineScriptReader(getSpeaker(), inputStream);
            MicroController microController = new MicroController(getStorage(), getSpeaker(), scriptReader);
            try {
                while (microController.isWork()) {
                    microController.run();
                }
            } catch (EndOfScriptException e) {
                getSpeaker().speak("Исполнение скрипта " + path + " завершено.");
            } catch (InvalidScriptException e){
                getSpeaker().speak(e.getMessage());
                getSpeaker().speak("Исполение скрипта " + path + " прервано.");
            }
            startedScripts.remove(path);
        } catch (InvalidPathException |
                 IOException e) {
            getSpeaker().speak("Неверный путь к файлу.");
        } catch (InvalidScriptException |
                 RecursiveScriptException e) {
            getSpeaker().speak(e.getMessage());
        }
    }

    static class MicroController extends Controller {
        public MicroController(Storage storage, Speaker speaker, SpaceMarineReader reader) {
            super(storage, speaker, reader);
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

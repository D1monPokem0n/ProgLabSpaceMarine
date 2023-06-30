package ru.prog.itmo.control;

import ru.prog.itmo.StorageInfo;
import ru.prog.itmo.connection.*;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.speaker.Speaker;

import javax.swing.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;

public class CommandManager {
    private final SendModule sendModule;
    private final ReceiveModule receiveModule;
    private final CommandMap commandMap;
    private final LinkedList<String> lastCommands;
    private final Speaker speaker;
    private final ConsoleArgument argument;

    public CommandManager(SendModule sendModule,
                          ReceiveModule receiveModule,
                          CommandMap commandMap,
                          Speaker speaker,
                          ConsoleArgument argument) {
        this.sendModule = sendModule;
        this.receiveModule = receiveModule;
        this.commandMap = commandMap;
        lastCommands = new LinkedList<>();
        this.speaker = speaker;
        this.argument = argument;
    }

    private void putInLastCommands(String command) {
        if (lastCommands.size() >= 14)
            lastCommands.removeFirst();
        lastCommands.add(command);
    }

    public String executeAddCommand(SpaceMarine marine) {
        sendModule.submitSending(new Request<>("add", marine));
        putInLastCommands("add");
        return getDataOrElseComment();
    }

    public String executeAddIfMinCommand(SpaceMarine marine) {
        sendModule.submitSending(new Request<>("add_if_min", marine));
        putInLastCommands("add_if_min");
        return getDataOrElseComment();
    }

    public String executeHelpCommand() {
        var stringBuilder = new StringBuilder();
        for (var commandName : commandMap.getCommandHashMap().keySet()) {
            stringBuilder.append(speaker.speak(commandName));
            stringBuilder.append(": ");
            stringBuilder.append(speaker.speak(commandMap.getCommand(commandName).getDescription()));
            stringBuilder.append('\n');
        }
        putInLastCommands("help");
        return stringBuilder.toString();
    }

    public String executeHistoryCommand() {
        var stringBuilder = new StringBuilder();
        for (var commandName : lastCommands) {
            stringBuilder.append(speaker.speak(commandName));
            stringBuilder.append('\n');
        }
        putInLastCommands("history");
        return stringBuilder.toString();
    }

    public String executeMaxByMeleeWeaponCommand() {
        sendModule.submitSending(new Request<>("max_by_melee_weapon", null));
        putInLastCommands("max_by_melee_weapon");
        return getDataOrElseComment();
    }

    public String executePrintFieldDescendingHealthCommand() {
        sendModule.submitSending(new Request<>("print_field_descending_health", null));
        putInLastCommands("print_field_descending_health");
        return getDataOrElseComment();
    }

    public String executeInfoCommand() {
        sendModule.submitSending(new Request<>("info", null));
        var response = receiveModule.getResponse();
        var info = (StorageInfo) response.getData();
        var builder = new StringBuilder();
//        speaker().speak("Количество элементов в коллекции: " + info.getElementsCount() +
//                        "\nТип коллекции: " + info.getCollectionType() +
//                        "\nТип файла коллекции: " + info.getDataBaseName());
        builder.append(speaker.speak("Elements count in collection: "));
        builder.append(info.getElementsCount());
        builder.append('\n');
        builder.append(speaker.speak("Collection type: "));
        builder.append(info.getCollectionType());
        builder.append('\n');
        builder.append(speaker.speak("Data base type: "));
        builder.append(info.getDataBaseName());
        putInLastCommands("info");
        return builder.toString();
    }

    public ArrayList<SpaceMarine> executeShowCommand() {
        synchronized (sendModule) {
            sendModule.submitSending(new Request<>("show", null));
            var response = receiveModule.getResponse();
            @SuppressWarnings("unchecked")
            var marines = (ArrayList<SpaceMarine>) response.getData();
            marines = marines == null ? new ArrayList<>() : marines;
            return marines;
        }
    }

    public String executeClearCommand() {
        sendModule.submitSending(new Request<>("clear", null));
        putInLastCommands("clear");
        return getDataOrElseComment();
    }

    public String executeRemoveAnyByChapterCommand(Chapter chapter) {
        sendModule.submitSending(new Request<>("remove_any_by_chapter", chapter));
        putInLastCommands("remove_any_by_chapter");
        return getDataOrElseComment();
    }

    public String executeRemoveByIdCommand(long id) {
        sendModule.submitSending(new Request<>("remove_by_id", id));
        putInLastCommands("remove_by_id");
        return getDataOrElseComment();
    }

    public String executeRemoveGreaterCommand(SpaceMarine marine) {
        sendModule.submitSending(new Request<>("remove_greater", marine));
        putInLastCommands("remove_greater");
        return getDataOrElseComment();
    }

    public SpaceMarine executeGetById(long id) {
        sendModule.submitSending(new Request<>("get_by_id", id));
        var response = receiveModule.getResponse();
        return (SpaceMarine) response.getData();
    }

    public String executeUpdateCommand(SpaceMarine updatedMarine) {
        sendModule.submitSending(new Request<>("update", updatedMarine));
        putInLastCommands("update");
        return getDataOrElseComment();
    }

    public Response<?> executeLoginCommand(User user) {
        sendModule.submitSending(new Request<>("login", user));
        var response = receiveModule.getResponse();
        if (response.getData() == null) throw new InvalidUserException(response.getComment());
        return response;
    }

    public Response<?> executeRegisterCommand(User user) {
        sendModule.submitSending(new Request<>("register", user));
        return receiveModule.getResponse();
    }

    public void executeScript(Path filePath) {
        var scriptThread = new Thread(() -> {
            argument.setArgument(String.valueOf(filePath));
            commandMap.getCommand("execute_script").execute();
            JOptionPane.showMessageDialog(null, speaker.speak("Script finished."));
        });
        scriptThread.start();
    }

    private String getDataOrElseComment() {
        var response = receiveModule.getResponse();
        var answer = response.getData() == null ? response.getComment() : response.getData();
        return speaker.speak((String) answer);
    }

}

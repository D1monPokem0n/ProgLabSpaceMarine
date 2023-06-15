package ru.prog.itmo.command.info;

import ru.prog.itmo.command.ConsoleOCommand;
import ru.prog.itmo.speaker.Speaker;

import java.util.LinkedList;

public class HistoryCommand extends ConsoleOCommand {
    private final LinkedList<String> lastCommands;
    public HistoryCommand(Speaker speaker, LinkedList<String> lastCommands) {
        super("help", speaker);
        this.lastCommands = lastCommands;
    }

    @Override
    public void execute() {
        super.execute();
        speaker().speak("Последние команды (макс. 14): ");
        for(String command: lastCommands){
            speaker().speak(command);
        }
    }

    @Override
    public String getDescription() {
        return "вывести последние 14 команд (без их аргументов)";
    }
}

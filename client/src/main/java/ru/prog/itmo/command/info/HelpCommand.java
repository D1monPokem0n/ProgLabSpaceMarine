package ru.prog.itmo.command.info;

import ru.prog.itmo.command.ConsoleOCommand;
import ru.prog.itmo.control.CommandMap;
import ru.prog.itmo.speaker.Speaker;

public class HelpCommand extends ConsoleOCommand {
    private final CommandMap commands;

    public HelpCommand(Speaker speaker, CommandMap commands) {
        super("help", speaker);
        this.commands = commands;
    }

    @Override
    public void execute() {
        for (String key : commands.getCommandHashMap().keySet()) {
            speaker().speak(key + ": " + commands.getCommand(key).getDescription());
        }
    }

    @Override
    public String getDescription() {
        return "вывести справку по доступным командам";
    }
}

package ru.prog.itmo.command.info;

import ru.prog.itmo.control.CommandMap;
import ru.prog.itmo.command.ConsoleOCommand;
import ru.prog.itmo.speaker.Speaker;

public class HelpCommand extends ConsoleOCommand {
    private final CommandMap commands;

    public HelpCommand(Speaker speaker, CommandMap commands) {
        super(speaker);
        this.commands = commands;
    }

    @Override
    public void execute() {
        super.execute();
        for (String commandName : commands.getCommandHashMap().keySet()) {
            getSpeaker().speak(commandName + " " + commands.getCommand(commandName).getDescription());
        }
    }

    @Override
    public String getDescription() {
        return "вывести справку по доступным командам";
    }
}

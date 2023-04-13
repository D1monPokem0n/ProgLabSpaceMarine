package ru.prog.itmo.reader;

import ru.prog.itmo.control.ConsoleArgument;

import java.util.regex.Pattern;

public class CommandReader {
    private SpaceMarineReader reader;
    private ConsoleArgument consoleArgument;
    private final String FORMAT = ("(help|info|show|add(_if_min)?|update|remove_by_id|clear|save|execute_script|exit|remove(_greater|_any_by_chapter)|history|max_by_melee_weapon|print_field_descending_health)");
    public String read() throws InvalidCommandException {
        String value = reader.read();
        if (value == null)
            value = "";
        String[] commandAndArgument = value.replaceAll(" +", " ").split(" ");
        if (commandAndArgument.length > 2) throw new InvalidCommandException("Вы ввели слишком много слов.");
        String command = commandAndArgument[0];
        if (!Pattern.matches(FORMAT, command)) throw new InvalidCommandException("Вы ввели неверную команду");
        String argument = commandAndArgument.length != 1 ? commandAndArgument[1] : null;
        consoleArgument.setArgument(argument);
        return command;
    }
    public CommandReader(SpaceMarineReader reader, ConsoleArgument consoleArgument){
        this.reader = reader;
        this.consoleArgument = consoleArgument;
    }
}

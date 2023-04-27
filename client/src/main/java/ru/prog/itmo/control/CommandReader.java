package ru.prog.itmo.control;

import ru.prog.itmo.reader.InvalidCommandException;
import ru.prog.itmo.reader.Reader;

import java.util.regex.Pattern;

public class CommandReader {
    private Reader reader;
    private ConsoleArgument consoleArgument;
    private final String FORMAT = ("(help|info|show|add(_if_min)?|update|remove_by_id|clear|execute_script|exit|remove(_greater|_any_by_chapter)|history|max_by_melee_weapon|print_field_descending_health)");
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
    public CommandReader(Reader reader, ConsoleArgument consoleArgument){
        this.reader = reader;
        this.consoleArgument = consoleArgument;
    }
}

package ru.prog.itmo.control;

import ru.prog.itmo.reader.InvalidCommandException;
import ru.prog.itmo.reader.Reader;

import java.util.regex.Pattern;

public class CommandReader {
    private Reader reader;
    private final String FORMAT = "(exit|save|help|change_access_token_time|change_refresh_token_time|invalidate_token)";

    public CommandReader(Reader reader) {
        this.reader = reader;
    }

    public String read() throws InvalidCommandException {
        String command = reader.read();
        if (command == null)
            command = "";
        if (!Pattern.matches(FORMAT, command)) throw new InvalidCommandException("Вы ввели неверную команду");
        return command;
    }
}

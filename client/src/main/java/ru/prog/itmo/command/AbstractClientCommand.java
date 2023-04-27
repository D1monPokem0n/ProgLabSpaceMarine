package ru.prog.itmo.command;

public abstract class AbstractClientCommand implements Command {
    protected final String COMMAND_TYPE;

    public AbstractClientCommand(String commandType) {
        COMMAND_TYPE = commandType;
    }
}


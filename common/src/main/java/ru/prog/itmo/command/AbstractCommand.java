package ru.prog.itmo.command;

public abstract class AbstractCommand implements Command{
    protected final String COMMAND_TYPE;
    public AbstractCommand(String commandType){
        COMMAND_TYPE = commandType;
    }
}

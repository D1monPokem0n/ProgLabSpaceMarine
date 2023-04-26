package ru.prog.itmo.command;

public abstract class ConsoleCommand extends AbstractCommand {
    public ConsoleCommand(String commandType) {
        super(commandType);
    }

    @Override
    public void execute() {

    }
}

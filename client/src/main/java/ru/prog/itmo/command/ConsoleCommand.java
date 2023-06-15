package ru.prog.itmo.command;

public abstract class ConsoleCommand extends AbstractClientCommand {
    public ConsoleCommand(String commandType) {
        super(commandType);
    }

    @Override
    public void execute() {
    }
}

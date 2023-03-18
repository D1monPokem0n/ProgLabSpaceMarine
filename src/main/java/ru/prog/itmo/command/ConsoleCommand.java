package ru.prog.itmo.command;

public abstract class ConsoleCommand implements Command {
    @Override
    public void execute() {
        System.out.println("This command is " + getClass());
    }
}

package ru.prog.itmo.command;

import ru.prog.itmo.Storage;

public abstract class StorageCommand implements Command{
    private Storage storage;
    public StorageCommand(Storage storage){
        this.storage = storage;
    }

    @Override
    public void execute() {
        System.out.println("This command is " + getClass());
    }
}

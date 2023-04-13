package ru.prog.itmo.command;

import ru.prog.itmo.storage.Storage;

public abstract class StorageCommand implements Command {
    private Storage storage;

    public StorageCommand(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void execute() {
    }

    public Storage getStorage() {
        return storage;
    }
}

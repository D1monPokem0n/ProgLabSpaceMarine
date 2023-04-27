package ru.prog.itmo.command;

import ru.prog.itmo.storage.Storage;

public abstract class StorageCommand extends AbstractServerCommand {
    private Storage storage;

    public StorageCommand(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void execute() {
    }

    public Storage storage() {
        return storage;
    }
}

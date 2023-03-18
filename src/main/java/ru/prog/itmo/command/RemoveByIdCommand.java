package ru.prog.itmo.command;

import ru.prog.itmo.Storage;

public class RemoveByIdCommand extends StorageCommand{
    public RemoveByIdCommand(Storage storage) {
        super(storage);
    }
}

package ru.prog.itmo.command;

import ru.prog.itmo.Storage;

public class ClearCommand extends StorageCommand{
    public ClearCommand(Storage storage) {
        super(storage);
    }
}

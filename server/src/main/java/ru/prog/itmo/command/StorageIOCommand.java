package ru.prog.itmo.command;

import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public abstract class StorageIOCommand extends StorageOCommand {
    private Reader reader;

    public StorageIOCommand(Storage storage, Speaker speaker, Reader reader) {
        super(storage, speaker);
        this.reader = reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Reader getReader() {
        return reader;
    }
}

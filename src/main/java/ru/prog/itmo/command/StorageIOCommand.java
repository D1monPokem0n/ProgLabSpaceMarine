package ru.prog.itmo.command;

import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.speaker.Speaker;

public abstract class StorageIOCommand extends StorageOCommand {
    private SpaceMarineReader reader;

    public StorageIOCommand(Storage storage, Speaker speaker, SpaceMarineReader reader) {
        super(storage, speaker);
        this.reader = reader;
    }

    public void setReader(SpaceMarineReader reader) {
        this.reader = reader;
    }

    public SpaceMarineReader getReader() {
        return reader;
    }
}

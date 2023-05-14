package ru.prog.itmo.command;

import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public abstract class StorageOCommand extends StorageCommand{
    private Speaker speaker;
    public StorageOCommand(Storage storage, Speaker speaker) {
        super(storage);
        this.speaker = speaker;
    }

    public Speaker speaker() {
        return speaker;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }
}

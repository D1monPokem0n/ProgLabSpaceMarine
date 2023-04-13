package ru.prog.itmo.command;

import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.speaker.Speaker;

public abstract class StorageOCommand extends StorageCommand{
    private Speaker speaker;
    public StorageOCommand(Storage storage, Speaker speaker) {
        super(storage);
        this.speaker = speaker;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }
}

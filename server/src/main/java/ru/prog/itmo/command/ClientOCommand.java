package ru.prog.itmo.command;

import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public abstract class ClientOCommand extends StorageOCommand {
    ConnectionModule connectionModule;
    public ClientOCommand(Storage storage, ConnectionModule connectionModule, Speaker speaker){
        super(storage, speaker);
        this.connectionModule = connectionModule;

    }
    public ConnectionModule connectionModule(){
        return connectionModule;
    }
}

package ru.prog.itmo.command;

import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public abstract class ClientOCommand extends StorageOCommand {
    ConnectionManager connectionManager;
    public ClientOCommand(Storage storage, ConnectionManager connectionManager, Speaker speaker){
        super(storage, speaker);
        this.connectionManager = connectionManager;

    }
    public ConnectionManager connectionManager(){
        return connectionManager;
    }
}

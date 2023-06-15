package ru.prog.itmo.command;

import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.storage.Storage;

public abstract class ClientCommand extends StorageCommand {
    ConnectionManager connectionManager;
    public ClientCommand(Storage storage, ConnectionManager connectionManager){
        super(storage);
        this.connectionManager = connectionManager;
    }
    public ConnectionManager connectionManager(){
        return connectionManager;
    }
}

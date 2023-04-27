package ru.prog.itmo.command;

import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.storage.Storage;

public abstract class ClientCommand extends StorageCommand {
    ConnectionModule connectionModule;
    public ClientCommand(Storage storage, ConnectionModule connectionModule){
        super(storage);
        this.connectionModule = connectionModule;
    }
    public ConnectionModule connectionModule(){
        return connectionModule;
    }
}

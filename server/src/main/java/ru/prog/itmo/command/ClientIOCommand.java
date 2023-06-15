package ru.prog.itmo.command;

import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public abstract class ClientIOCommand extends StorageIOCommand {
    ConnectionManager connectionManager;
    public ClientIOCommand(Storage storage, ConnectionManager connectionManager, Speaker speaker, Reader reader){
        super(storage, speaker, reader);
        this.connectionManager = connectionManager;
    }
    public ConnectionManager connectionManager(){
        return connectionManager;
    }
}

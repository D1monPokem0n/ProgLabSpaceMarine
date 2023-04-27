package ru.prog.itmo.command;

import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public abstract class ClientIOCommand extends StorageIOCommand {
    ConnectionModule connectionModule;
    public ClientIOCommand(Storage storage, ConnectionModule connectionModule, Speaker speaker, Reader reader){
        super(storage, speaker, reader);
        this.connectionModule = connectionModule;
    }
    public ConnectionModule connectionModule(){
        return connectionModule;
    }
}

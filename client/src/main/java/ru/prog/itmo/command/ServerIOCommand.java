package ru.prog.itmo.command;

import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.server.ConnectionModule;
import ru.prog.itmo.speaker.Speaker;

public abstract class ServerIOCommand extends ServerOCommand {
    private Reader reader;

    public ServerIOCommand(String commandType, ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        super(commandType, connectionModule, speaker);
        this.reader = reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Reader reader() {
        return reader;
    }
}

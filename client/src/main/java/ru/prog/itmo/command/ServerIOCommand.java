package ru.prog.itmo.command;

import ru.prog.itmo.connection.ReceiveModule;
import ru.prog.itmo.connection.SendModule;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;

public abstract class ServerIOCommand extends ServerOCommand {
    private Reader reader;

    public ServerIOCommand(String commandType,
                           SendModule sendModule,
                           ReceiveModule receiveModule,
                           Speaker speaker,
                           Reader reader) {
        super(commandType, sendModule, receiveModule, speaker);
        this.reader = reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Reader reader() {
        return reader;
    }
}

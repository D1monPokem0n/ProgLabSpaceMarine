package ru.prog.itmo.command;

import ru.prog.itmo.connection.ReceiveModule;
import ru.prog.itmo.connection.SendModule;
import ru.prog.itmo.speaker.Speaker;

public abstract class ServerOCommand extends ServerCommand {
    private Speaker speaker;
    public ServerOCommand(String commandType,
                          SendModule sendModule,
                          ReceiveModule receiveModule,
                          Speaker speaker) {
        super(commandType, sendModule, receiveModule);
        this.speaker = speaker;
    }

    public Speaker speaker() {
        return speaker;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }
}

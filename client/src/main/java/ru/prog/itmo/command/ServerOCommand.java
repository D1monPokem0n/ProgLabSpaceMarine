package ru.prog.itmo.command;

import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.speaker.Speaker;

public abstract class ServerOCommand extends ServerCommand {
    private Speaker speaker;
    public ServerOCommand(String commandType, ConnectionModule connectionModule, Speaker speaker) {
        super(commandType, connectionModule);
        this.speaker = speaker;
    }

    public Speaker speaker() {
        return speaker;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }
}

package ru.prog.itmo.command;

import ru.prog.itmo.connection.ReceiveModule;
import ru.prog.itmo.connection.SendModule;

public abstract class ServerCommand extends AbstractClientCommand {
    private final SendModule sendModule;
    private final ReceiveModule receiveModule;

    public ServerCommand(String commandType, SendModule sendModule, ReceiveModule receiveModule) {
        super(commandType);
        this.sendModule = sendModule;
        this.receiveModule = receiveModule;
    }

    @Override
    public void execute() {
    }

    public SendModule sendModule() {
        return sendModule;
    }

    public ReceiveModule receiveModule(){
        return receiveModule;
    }
}

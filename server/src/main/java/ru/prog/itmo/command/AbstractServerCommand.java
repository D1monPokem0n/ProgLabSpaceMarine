package ru.prog.itmo.command;

import java.net.SocketAddress;

public abstract class AbstractServerCommand{
    public void execute(SocketAddress address) {
    }
    public abstract String getDescription();
}

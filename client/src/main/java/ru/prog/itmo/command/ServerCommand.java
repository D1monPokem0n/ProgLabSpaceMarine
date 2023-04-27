package ru.prog.itmo.command;

import ru.prog.itmo.spacemarine.builder.script.InvalidScriptException;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.ConnectionModule;

import java.io.*;
import java.nio.ByteBuffer;

public abstract class ServerCommand extends AbstractClientCommand {
    private ConnectionModule connectionModule;

    public ServerCommand(String commandType, ConnectionModule connectionModule) {
        super(commandType);
        this.connectionModule = connectionModule;
    }

    @Override
    public void execute() {
    }

    public ConnectionModule connectionModule() {
        return connectionModule;
    }

    public ByteBuffer serializeRequest(Request<?> request) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(request);
            return ByteBuffer.wrap(baos.toByteArray());
        } catch (IOException e) {
            throw new InvalidScriptException(e.getMessage());
        }
    }

    public ObjectInputStream getDeserializedInputStream(ByteBuffer buffer) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
            return new ObjectInputStream(bais);
        } catch (IOException e){
            throw new InvalidScriptException(e.getMessage());
        }
    }
}

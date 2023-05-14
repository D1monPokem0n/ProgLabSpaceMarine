package ru.prog.itmo.command;

import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

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
            throw new InvalidConnectionException(e.getMessage());
        }
    }

    public Response<?> getDeserializedResponse(ByteBuffer buffer) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Response<?>) ois.readObject();
        } catch (IOException | ClassNotFoundException e){
            throw new InvalidConnectionException(Arrays.toString(e.getStackTrace()));
        }
    }

    public ObjectInputStream getDeserializedInputStream(ByteBuffer buffer){
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
            return new ObjectInputStream(bais);
        } catch (IOException e){
            throw new InvalidConnectionException(Arrays.toString(e.getStackTrace()));
        }
    }
}

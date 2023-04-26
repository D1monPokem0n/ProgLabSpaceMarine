package ru.prog.itmo.command.add;

import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.command.script.InvalidScriptException;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.server.ConnectionModule;
import ru.prog.itmo.server.InvalidConnectionException;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.CreateCancelledException;
import ru.prog.itmo.spacemarine.builder.script.SpaceMarineScriptCreator;
import ru.prog.itmo.speaker.Speaker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

public abstract class AbstractAddScriptCommand extends ServerIOCommand {
    public AbstractAddScriptCommand(String commandType, ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        super(commandType, connectionModule, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        try {
            SpaceMarineScriptCreator creator = new SpaceMarineScriptCreator(reader());
            SpaceMarine marine = creator.create();
            Request<SpaceMarine> request = new Request<>(COMMAND_TYPE, marine, true);
            ByteBuffer toServer = serializeRequest(request);
            connectionModule().sendRequest(toServer);
            ByteBuffer fromServer = connectionModule().receiveResponse();
            ObjectInputStream ois = getDeserializedInputStream(fromServer);
            @SuppressWarnings("unchecked")
            Response<String> response = (Response<String>) ois.readObject();
            speaker().speak(response.getData());
        } catch (CreateCancelledException | IOException | InvalidConnectionException | ClassNotFoundException e) {
            throw new InvalidScriptException("Проблемы с соединением...");
        }
    }
}

package ru.prog.itmo.command.add;

import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.CreateCancelledException;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.client.SpaceMarineClientCreator;
import ru.prog.itmo.speaker.Speaker;

import java.nio.ByteBuffer;

public abstract class AbstractAddCommand extends ServerIOCommand {
    public AbstractAddCommand(String commandType, ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        super(commandType, connectionModule, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        try {
            SpaceMarineClientCreator creator = new SpaceMarineClientCreator(speaker(), reader());
            SpaceMarine marine = creator.create();
            Request<Object> request = new Request<>(COMMAND_TYPE, marine);
            ByteBuffer toServer = serializeRequest(request);
            connectionModule().sendRequest(toServer);
            ByteBuffer fromServer = connectionModule().receiveResponse();
            Response<?> response = getDeserializedResponse(fromServer);
            if (response.getData() != null)
                speaker().speak((String) response.getData());
            else throw new InvalidConnectionException(response.getComment());
        } catch (CreateCancelledException | InvalidConnectionException  e) {
            speaker().speak("Проблемы с соединением...");
        }
    }
}

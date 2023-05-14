package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.command.ServerOCommand;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.spacemarine.builder.script.InvalidScriptException;
import ru.prog.itmo.speaker.Speaker;

import java.nio.ByteBuffer;

public class ClearScriptCommand extends ServerOCommand implements ScriptExecutable {
    public ClearScriptCommand(ConnectionModule connectionModule, Speaker speaker) {
        super("clear", connectionModule, speaker);
    }

    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }

    @Override
    public void execute() {
        super.execute();
        try {
            Request<Object> request = new Request<>(COMMAND_TYPE, null, true);
            ByteBuffer toServer = serializeRequest(request);
            connectionModule().sendRequest(toServer);
            ByteBuffer fromServer = connectionModule().receiveResponse();
            Response<?> response = getDeserializedResponse(fromServer);
            speaker().speak((String) response.getData());
        } catch (InvalidConnectionException e){
            throw new InvalidScriptException("Проблемы с соединением");
        }
    }
}

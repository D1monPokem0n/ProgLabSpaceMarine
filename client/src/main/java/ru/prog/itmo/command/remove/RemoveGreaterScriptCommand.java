package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.script.InvalidScriptException;
import ru.prog.itmo.spacemarine.builder.script.SpaceMarineScriptCreator;
import ru.prog.itmo.speaker.Speaker;

import java.nio.ByteBuffer;

public class RemoveGreaterScriptCommand extends ServerIOCommand implements ScriptExecutable {
    public RemoveGreaterScriptCommand(ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        super("remove_greater", connectionModule, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        SpaceMarineScriptCreator creator = new SpaceMarineScriptCreator(reader());
        try {
            SpaceMarine maxMarine = creator.create();
            Request<SpaceMarine> request = new Request<>(COMMAND_TYPE, maxMarine, true);
            ByteBuffer toServer = serializeRequest(request);
            connectionModule().sendRequest(toServer);
            ByteBuffer fromServer = connectionModule().receiveResponse();
            Response<?> response = getDeserializedResponse(fromServer);
            if (response.getData() != null)
                speaker().speak((String) response.getData());
            else speaker().speak(response.getComment());
        } catch (NullPointerException e){
            speaker().speak("В коллекции нет десантников, выше заданного Вами.");
        } catch (InvalidConnectionException e){
            throw new InvalidScriptException("Проблемы с соединением...");
        }
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, превышающие заданный";
    }
}

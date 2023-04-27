package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.CreateCancelledException;
import ru.prog.itmo.spacemarine.builder.user.SpaceMarineUserCreator;
import ru.prog.itmo.speaker.Speaker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

public class RemoveGreaterCommand extends ServerIOCommand implements UserAsking {
    public RemoveGreaterCommand(ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        super("remove_greater", connectionModule, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        SpaceMarineUserCreator creator = new SpaceMarineUserCreator(speaker(), reader());
        speaker().speak("Задайтие десантника, который доллжен стать максимальным в базе. \nВсе, кто выше будут удалены.");
        try {
            SpaceMarine maxMarine = creator.create();
            Request<SpaceMarine> request = new Request<>(COMMAND_TYPE, maxMarine);
            ByteBuffer toServer = serializeRequest(request);
            connectionModule().sendRequest(toServer);
            ByteBuffer fromServer = connectionModule().receiveResponse();
            ObjectInputStream inputStream = getDeserializedInputStream(fromServer);
            @SuppressWarnings("unchecked")
            Response<String> response = (Response<String>) inputStream.readObject();
            if (response.getData() != null)
                speaker().speak(response.getData());
            else speaker().speak(response.getComment());
        } catch (CreateCancelledException e) {
            speaker().speak("Вам не удалось задать максимального десантника. \nУдаление отменено.");
        } catch (IOException | ClassNotFoundException | InvalidConnectionException e){
            speaker().speak("Проблемы с соединением...");
        }
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, превышающие заданный";
    }
}

package ru.prog.itmo.command.info;

import ru.prog.itmo.command.ServerOCommand;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.server.ConnectionModule;
import ru.prog.itmo.server.InvalidConnectionException;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.Collection;

public class ShowCommand extends ServerOCommand {
    public ShowCommand(ConnectionModule connectionModule, Speaker speaker) {
        super("show", connectionModule, speaker);
    }

    @Override
    public void execute() {
        super.execute();
        try {
            Request<Object> request = new Request<>(COMMAND_TYPE, null);
            ByteBuffer toServer = serializeRequest(request);
            connectionModule().sendRequest(toServer);
            ByteBuffer fromServer = connectionModule().receiveResponse();
            ObjectInputStream inputStream = getDeserializedInputStream(fromServer);
            @SuppressWarnings("unchecked")
            Response<Collection<SpaceMarine>> response = (Response<Collection<SpaceMarine>>) inputStream.readObject();
            for (SpaceMarine marine : response.getData()){
                speaker().speak(marine.toString());
            }
        } catch (IOException | InvalidConnectionException | ClassNotFoundException e){
            speaker().speak("Проблемы с соединением...");
        }
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении.";
    }
}

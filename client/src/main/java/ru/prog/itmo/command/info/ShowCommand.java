package ru.prog.itmo.command.info;

import ru.prog.itmo.command.ServerOCommand;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.List;

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
            Response<List<SpaceMarine>> response = (Response<List<SpaceMarine>>) inputStream.readObject();
            List<SpaceMarine> marineList = response.getData();
            if (marineList.size() != 0)
                for (SpaceMarine marine : marineList){
                 speaker().speak(marine.toString());
             }
            else {
               speaker().speak(response.getComment());
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

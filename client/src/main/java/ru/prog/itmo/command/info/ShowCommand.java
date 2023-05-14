package ru.prog.itmo.command.info;

import ru.prog.itmo.command.ServerOCommand;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;

import java.nio.ByteBuffer;

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
            /*ByteBuffer fromServer = connectionModule().receiveResponse();
            Response<?> response = getDeserializedResponse(fromServer);
            if (response.getData() != null) {
                speaker().speak((String) response.getData());
            } else {
                speaker().speak(response.getComment());
            }*/
            ByteBuffer fromServer = connectionModule().receiveResponse();
            Response<?> response = getDeserializedResponse(fromServer);
            if (response.getData() != null) {
                int marinesCount = (Integer) response.getData();
                int i = 1;
                while (i <= marinesCount) {
                    ByteBuffer buffer = connectionModule().receiveResponse();
                    Response<?> marineResponse = getDeserializedResponse(buffer);
                    SpaceMarine currentMarine = (SpaceMarine) marineResponse.getData();
                    speaker().speak(currentMarine.toString());
                    speaker().speak("Получено " + i + "/" + marinesCount + " десантников.");
                    i++;
                }
            } else {
                speaker().speak(response.getComment());
            }
        } catch (InvalidConnectionException | ClassCastException e) {
            speaker().speak("Проблемы с соединением...");
        }
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении.";
    }
}

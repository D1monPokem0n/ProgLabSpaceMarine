package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;

import java.nio.ByteBuffer;

public class ClearCommand extends ServerIOCommand implements UserAsking {
    public ClearCommand(ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        super("clear", connectionModule, speaker, reader);
    }

    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }

    @Override
    public void execute() {
        super.execute();
        speaker().speak("Вы уверены, что хотите удалить?) Y/N");
        String answer = reader().read();
        answer = answer == null ? "null" : answer;
        if (answer.equals("Y")) {
            try {
                Request<Object> request = new Request<>(COMMAND_TYPE, null);
                ByteBuffer toServer = serializeRequest(request);
                connectionModule().sendRequest(toServer);
                ByteBuffer fromServer = connectionModule().receiveResponse();
                Response<?> response = getDeserializedResponse(fromServer);
                speaker().speak((String) response.getData());
            } catch (InvalidConnectionException  e){
                speaker().speak("Проблемы с соединением");
            }
        } else if (answer.equals("N")) {
            speaker().speak("Ну нет, так нет.");
        } else {
            speaker().speak("Вы ввели какую-то фигню, мы не уверены в вашей компетенции по удалению солдат Космического Десанта.");
        }
    }
}

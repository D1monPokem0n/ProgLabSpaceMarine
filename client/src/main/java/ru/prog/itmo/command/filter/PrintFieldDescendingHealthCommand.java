package ru.prog.itmo.command.filter;

import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;

import java.nio.ByteBuffer;

public class PrintFieldDescendingHealthCommand extends ServerIOCommand {
    public PrintFieldDescendingHealthCommand(ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        super("print_field_descending_health", connectionModule, speaker, reader);
    }

    @Override
    public String getDescription() {
        return "вывести значения поля health всех элементов в порядке убывания.";
    }

    @Override
    public void execute() {
        super.execute();
        try {
            Request<Object> request = new Request<>(COMMAND_TYPE, null);
            ByteBuffer toServer = serializeRequest(request);
            connectionModule().sendRequest(toServer);
            ByteBuffer fromServer = connectionModule().receiveResponse();
            Response<?> response = getDeserializedResponse(fromServer);
            if (response.getData() != null)
                speaker().speak((String) response.getData());
            else speaker().speak(response.getComment());
        } catch (InvalidConnectionException | ClassCastException e) {
            speaker().speak("Проблемы с соединением...");
        }
    }
}

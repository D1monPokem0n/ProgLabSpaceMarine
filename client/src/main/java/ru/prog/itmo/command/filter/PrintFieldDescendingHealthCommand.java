package ru.prog.itmo.command.filter;

import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.server.ConnectionModule;
import ru.prog.itmo.server.InvalidConnectionException;
import ru.prog.itmo.speaker.Speaker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.Collection;

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
            ObjectInputStream stream = getDeserializedInputStream(fromServer);
            @SuppressWarnings("unchecked")
            Collection<String> collection = (Collection<String>) stream.readObject();
            for (String output: collection){
                speaker().speak(output);
            }
        } catch (IOException | ClassNotFoundException | InvalidConnectionException e){
            speaker().speak("Проблемы с соединением...");
        }
    }
}

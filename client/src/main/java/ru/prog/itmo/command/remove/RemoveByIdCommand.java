package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ServerOCommand;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.control.ConsoleArgument;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.speaker.Speaker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

public class RemoveByIdCommand extends ServerOCommand {
    private final ConsoleArgument argument;

    public RemoveByIdCommand(ConnectionModule connectionModule, Speaker speaker, ConsoleArgument argument) {
        super("remove_by_id", connectionModule, speaker);
        this.argument = argument;
    }

    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по его id";
    }

    @Override
    public void execute() {
        super.execute();
        try {
            int id = Integer.parseInt(argument.getValue());
            Request<Integer> request = new Request<>(COMMAND_TYPE, id);
            ByteBuffer toServer = serializeRequest(request);
            connectionModule().sendRequest(toServer);
            ByteBuffer fromServer = connectionModule().receiveResponse();
            ObjectInputStream inputStream = getDeserializedInputStream(fromServer);
            @SuppressWarnings("unchecked")
            Response<String> response = (Response<String>) inputStream.readObject();
            if (response.getData() != null)
                speaker().speak(response.getData());
            else speaker().speak(response.getComment());
        } catch (NumberFormatException e) {
            speaker().speak("Вы должны вводить целое число...");
        } catch (IOException | ClassNotFoundException | InvalidConnectionException e){
            speaker().speak("Проблемы с соединением...");
        }
    }
}

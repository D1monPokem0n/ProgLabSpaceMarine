package ru.prog.itmo.command.info;

import ru.prog.itmo.StorageInfo;
import ru.prog.itmo.command.ServerOCommand;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.speaker.Speaker;

import java.nio.ByteBuffer;

public class InfoCommand extends ServerOCommand {

    public InfoCommand(ConnectionModule connectionModule, Speaker speaker) {
        super("info", connectionModule, speaker);
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
            StorageInfo info = (StorageInfo) response.getData();
            speaker().speak("Количество элементов в коллекции: " + info.getElementsCount() +
                    "\nДата инициализации коллекции: " + info.getCreationDate() +
                    "\nТип коллекции: " + info.getCollectionType() +
                    "\nТип файла коллекции: " + info.getFileType());
        } catch (InvalidConnectionException | ClassCastException e){
            speaker().speak("Проблемы с соединением...");
        }
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }
}

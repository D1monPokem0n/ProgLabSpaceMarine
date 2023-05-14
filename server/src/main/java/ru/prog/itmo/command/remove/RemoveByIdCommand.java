package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ClientOCommand;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public class RemoveByIdCommand extends ClientOCommand {
    public RemoveByIdCommand(Storage storage, ConnectionModule connectionModule, Speaker speaker) {
        super(storage, connectionModule, speaker);
    }

    @Override
    public void execute() {
        super.execute();
        Response<String> response = new Response<>();
        try {
            Request<?> request = connectionModule().getRequest();
            long id = (long) request.getData();
            if (storage().getStream().anyMatch(marine -> marine.getId() == id)) {
                SpaceMarine marineToDelete = storage().getById(id);
                storage().remove(marineToDelete);
                response.setData("Из коллекции удалён десантник: " + marineToDelete);
            } else {
                response.setComment("В коллекции нет десантика из данной части");
            }
            connectionModule().sendResponse(response);
        } catch (ClassCastException e){
            response.setComment("Некорректный запрос.");
        }
        connectionModule().sendResponse(response);
    }



    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по его id";
    }
}

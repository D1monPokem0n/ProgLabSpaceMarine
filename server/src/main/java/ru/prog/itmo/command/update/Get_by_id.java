package ru.prog.itmo.command.update;

import ru.prog.itmo.command.ClientCommand;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.storage.Storage;

public class Get_by_id extends ClientCommand {
    public Get_by_id(Storage storage, ConnectionModule connectionModule) {
        super(storage, connectionModule);
    }

    @Override
    public void execute() {
        super.execute();
        Response<SpaceMarine> response = new Response<>();
        try {
            Request<?> request = connectionModule().getRequest();
            Long id = (Long) request.getData();
            SpaceMarine marineToSend = storage().getById(id);
            response.setData(marineToSend);
            if (marineToSend == null)
                response.setComment("В коллекции нет десантника с данным id.");
        } catch (ClassCastException e){
            response.setComment("Некорректный запрос");
        }
        connectionModule().sendResponse(response);
    }

    @Override
    public String getDescription() {
        return "вернуть клиенту десантника с данными id";
    }
}

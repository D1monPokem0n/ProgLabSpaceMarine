package ru.prog.itmo.command.update;

import ru.prog.itmo.command.ClientCommand;
import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.storage.Storage;

import java.net.SocketAddress;

public class Get_by_id extends ClientCommand {
    public Get_by_id(Storage storage, ConnectionManager connectionManager) {
        super(storage, connectionManager);
    }

    @Override
    public void execute(SocketAddress address) {
        super.execute(address);
        Response<SpaceMarine> response = new Response<>();
        try {
            var marineToSend = getMarineToSend(address);
            if (marineToSend == null)
                response.setComment("В коллекции нет десантника с данным id.");
            else
                response.setData(marineToSend);
        } catch (ClassCastException e) {
            response.setComment("Некорректный запрос");
        } finally {
            connectionManager().putResponse(address, response);
        }
    }

    private SpaceMarine getMarineToSend(SocketAddress address){
        Request<?> request = connectionManager().getRequestByAddress(address);
        Long id = (Long) request.getData();
        return storage().getById(id);
    }

    @Override
    public String getDescription() {
        return "вернуть клиенту десантника с данными id";
    }
}

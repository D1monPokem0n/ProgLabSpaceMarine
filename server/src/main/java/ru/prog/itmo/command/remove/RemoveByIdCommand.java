package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ClientCommand;
import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.storage.StorageDBException;

import java.net.SocketAddress;

public class RemoveByIdCommand extends ClientCommand {
    public RemoveByIdCommand(Storage storage, ConnectionManager connectionManager) {
        super(storage, connectionManager);
    }

    @Override
    public void execute(SocketAddress address) {
        super.execute(address);
        Response<String> response = new Response<>();
        try {
            var id = getCurrentId(address);
            executeRemove(response, id, address);
        } catch (ClassCastException e){
            response.setComment("Некорректный запрос.");
        } catch (StorageDBException e) {
            response.setComment(e.getMessage());
        } finally {
            connectionManager().putResponse(address, response);
        }
    }

    private void executeRemove(Response<String> response, long id, SocketAddress address){
        if (storage().getStream().anyMatch(marine -> marine.getId() == id)) {
            SpaceMarine marineToDelete = storage().getById(id);
            removeIfUsersEqual(marineToDelete, response, address);
        } else {
            response.setComment("В коллекции нет десантика из данной части");
        }
    }

    private void removeIfUsersEqual(SpaceMarine marineToDelete, Response<String> response, SocketAddress address){
        if (connectionManager()
                .getUserNameByAddress(address)
                .equals(marineToDelete.getOwnerUser())) {
            storage().remove(marineToDelete);
            response.setData("Из коллекции удалён десантник: " + marineToDelete);
        } else response.setData("Десантник не принадлежит вам.");
    }

    private long getCurrentId(SocketAddress address){
        Request<?> request = connectionManager().getRequestByAddress(address);
        return  (long) request.getData();
    }

    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по его id";
    }
}

package ru.prog.itmo.command.update;

import ru.prog.itmo.command.ClientCommand;
import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.storage.StorageDBException;

import java.net.SocketAddress;

public class UpdateCommand extends ClientCommand {

    public UpdateCommand(Storage storage, ConnectionManager connectionManager) {
        super(storage, connectionManager);
    }

    @Override
    public void execute(SocketAddress address) {
        super.execute(address);
        Response<String> response = new Response<>();
        try {
            var updatedMarine = getUpdatedMarine(address);
            var oldMarine = getOldMarine(updatedMarine.getId());
           updateIfUsersEqual(oldMarine, updatedMarine,address, response);
        } catch (ClassCastException | UpdatingCancelledException e) {
            response.setComment("Некорректный запрос.");
        } catch (StorageDBException e) {
            response.setComment(e.getMessage());
        } finally {
            connectionManager().putResponse(address, response);
        }
    }

    private void updateIfUsersEqual(SpaceMarine oldMarine,
                                    SpaceMarine updatedMarine,
                                    SocketAddress address,
                                    Response<String> response){
        if (connectionManager().getUserNameByAddress(address).equals(oldMarine.getOwnerUser())) {
            storage().updateMarine(oldMarine, updatedMarine);
            response.setData("Данные о десантнике успешно обновлены");
        } else response.setData("Данный десантник не принадлежит вам.");
    }

    private SpaceMarine getOldMarine(long id) {
        SpaceMarine oldMarine = storage().getById(id);
        if (oldMarine == null)
            throw new UpdatingCancelledException();
        return oldMarine;
    }

    private SpaceMarine getUpdatedMarine(SocketAddress address) {
        Request<?> request = connectionManager().getRequestByAddress(address);
        return (SpaceMarine) request.getData();
    }

    @Override
    public String getDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному.";
    }
}

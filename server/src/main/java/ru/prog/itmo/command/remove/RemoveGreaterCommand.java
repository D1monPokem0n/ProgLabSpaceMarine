package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ClientCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.storage.StorageDBException;

import java.net.SocketAddress;
import java.util.List;

public class RemoveGreaterCommand extends ClientCommand implements UserAsking {
    public RemoveGreaterCommand(Storage storage, ConnectionManager connectionManager) {
        super(storage, connectionManager);
    }

    @Override
    public void execute(SocketAddress address) {
        super.execute(address);
        Response<String> response = new Response<>();
        try {
            var maxMarine = getMaxMarine(address);
            var userName = connectionManager().getUserNameByAddress(address);
            executeRemove(response, maxMarine, userName);
        } catch (ClassCastException e) {
            response.setComment("Некорректный запрос.");
        } catch (StorageDBException e) {
            response.setComment(e.getMessage());
        } finally {
            connectionManager().putResponse(address, response);
        }
    }

    private void executeRemove(Response<String> response, SpaceMarine maxMarine, String userName) {
        List<SpaceMarine> marinesToDelete = storage().getStream()
                .filter(marine -> marine.compareTo(maxMarine) > 0)
                .toList();
        if (marinesToDelete.isEmpty()) {
            response.setComment("Нет десантников выше заданного.");
        } else {
            int removedCount = storage().removeAll(marinesToDelete, userName);
            response.setData("Из коллекции удалено " + removedCount + " десантников.");
        }
    }

    private SpaceMarine getMaxMarine(SocketAddress address) {
        Request<?> request = connectionManager().getRequestByAddress(address);
        return (SpaceMarine) request.getData();
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, превышающие заданный";
    }
}

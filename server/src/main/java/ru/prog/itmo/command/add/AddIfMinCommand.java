package ru.prog.itmo.command.add;

import ru.prog.itmo.command.ClientCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.storage.StorageDBException;

import java.net.SocketAddress;

public class AddIfMinCommand extends ClientCommand implements UserAsking {
    public AddIfMinCommand(Storage storage, ConnectionManager connectionManager) {
        super(storage, connectionManager);
    }

    @Override
    public void execute(SocketAddress address) {
        super.execute(address);
        Response<String> response = new Response<>();
        try {
            Request<?> request = connectionManager().getRequestByAddress(address);
            SpaceMarine marineToAdd = (SpaceMarine) request.getData();
            if (!storageContainsMarine(response, marineToAdd)) {
                SpaceMarine minMarine = storage().sort()[0];
                addIfMinMarine(response, marineToAdd, minMarine);
            }
        } catch (ClassCastException e) {
            response.setComment("Некорректный запрос.");
        } catch (StorageDBException e) {
            response.setComment(e.getMessage());
        } finally {
            connectionManager().putResponse(address, response);
        }
    }

    private void addIfMinMarine(Response<String> response, SpaceMarine marineToAdd, SpaceMarine minMarine) {
        if (marineToAdd.compareTo(minMarine) > 0) {
            response.setData("Солдат не является минимальным.");
        } else if (marineToAdd.compareTo(minMarine) == 0) {
            response.setData("Солдат совпадает с минимальным, поэтому добавлен");
        } else {
            storage().add(marineToAdd);
            response.setData("Солдат успешно добавлен.\n" + marineToAdd);
        }
    }

    private boolean storageContainsMarine(Response<String> response, SpaceMarine marineToAdd) {
        if (storage().contains(marineToAdd)) {
            response.setData("Солдат уже содержится в коллекции");
            return true;
        }
        if (storage().getHashSet().size() == 0) {
            response.setData("В коллекции нет элементов, поэтому солдат добавлен.");
            storage().add(marineToAdd);
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
    }
}

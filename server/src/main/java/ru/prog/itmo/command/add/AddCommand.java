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

public class AddCommand extends ClientCommand implements UserAsking {
    public AddCommand(Storage storage, ConnectionManager connectionManager) {
        super(storage, connectionManager);
    }

    @Override
    public void execute(SocketAddress address) {
        super.execute(address);
        Response<String> response = new Response<>();
        try {
            Request<?> request = connectionManager().getRequestByAddress(address);
            SpaceMarine marineToAdd = (SpaceMarine) request.getData();
            if (storage().contains(marineToAdd)){
                response.setData("В хранилище уже есть такой десантник.");
            } else {
                storage().add(marineToAdd);
                response.setData("Десантник успешно добавлен\n" + marineToAdd);
            }
        } catch (ClassCastException e){
            response.setComment("Некорректный запрос.");
        } catch (StorageDBException e){
            response.setComment(e.getMessage());
        } finally {
            connectionManager().putResponse(address, response);
        }
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию";
    }
}

package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ClientCommand;
import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.storage.StorageDBException;

import java.net.SocketAddress;

public class ClearCommand extends ClientCommand {
    public ClearCommand(Storage storage, ConnectionManager connectionManager) {
        super(storage, connectionManager);
    }

    @Override
    public void execute(SocketAddress address) {
        super.execute(address);
        Response<String> response = new Response<>();
        try {
            storage().clear(connectionManager().getRequestByAddress(address).getUser().getLogin());
            response.setData("Из коллекции удалены все принадлежащие вам десантники.");
        } catch (StorageDBException e){
            response.setData("Не удалось удалить десантников из базы данных:\n" + e.getMessage());
        } finally {
            connectionManager().putResponse(address, response);
        }
    }

    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }
}

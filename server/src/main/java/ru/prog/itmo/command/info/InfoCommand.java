package ru.prog.itmo.command.info;

import ru.prog.itmo.StorageInfo;
import ru.prog.itmo.command.ClientCommand;
import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.storage.Storage;

import java.net.SocketAddress;

public class InfoCommand extends ClientCommand {

    public InfoCommand(Storage storage, ConnectionManager connectionManager) {
        super(storage, connectionManager);
    }

    @Override
    public void execute(SocketAddress address) {
        super.execute(address);
        Response<StorageInfo> response = new Response<>(storage().getInfo());
        connectionManager().putResponse(address, response);
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }
}

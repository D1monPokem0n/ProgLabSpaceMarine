package ru.prog.itmo.command.info;

import ru.prog.itmo.command.ClientCommand;
import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.storage.Storage;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ShowCommand extends ClientCommand {
    public ShowCommand(Storage storage, ConnectionManager connectionManager) {
        super(storage, connectionManager);
    }

    @Override
    public void execute(SocketAddress address) {
        super.execute(address);
        Response<ArrayList<SpaceMarine>> response = new Response<>();
        if (storage().getHashSet().size() != 0)
            response.setData(new ArrayList<>(List.of(storage().sort())));
        else
            response.setComment("Хранилище пустое.");
        connectionManager().putResponse(address, response);
    }


    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении.";
    }
}

package ru.prog.itmo.command.filter;

import ru.prog.itmo.command.ClientCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.storage.Storage;

import java.net.SocketAddress;
import java.util.Comparator;


public class MaxByMeleeWeaponCommand extends ClientCommand implements UserAsking {
    public MaxByMeleeWeaponCommand(Storage storage, ConnectionManager connectionManager) {
        super(storage, connectionManager);
    }

    @Override
    public void execute(SocketAddress address) {
        super.execute(address);
        Response<SpaceMarine> response = new Response<>();
        if (storage().getHashSet().size() != 0) {
            SpaceMarine marineToSend = storage()
                    .getStream()
                    .min(Comparator.comparing(SpaceMarine::getMeleeWeapon))
                    .orElse(null);
            response.setData(marineToSend);
        } else {
            response.setComment("В коллекции нет элементов");
        }
        connectionManager().putResponse(address, response);
    }

    @Override
    public String getDescription() {
        return "вывести любой объект из коллекции, значение поля meleeWeapon которого является максимальным";
    }
}

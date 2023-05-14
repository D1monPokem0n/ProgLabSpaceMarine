package ru.prog.itmo.command.filter;

import ru.prog.itmo.command.ClientIOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

import java.util.Comparator;


public class MaxByMeleeWeaponCommand extends ClientIOCommand implements UserAsking {
    public MaxByMeleeWeaponCommand(Storage storage, ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        super(storage, connectionModule, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        Response<SpaceMarine> response = new Response<>();
        if (storage().getHashSet().size() != 0) {
            SpaceMarine marineToSend = storage().getStream()
                    .min(Comparator.comparing(SpaceMarine::getMeleeWeapon))
                    .orElse(null);
            response.setData(marineToSend);
        } else {
            response.setComment("В коллекции нет элементов");
        }
        connectionModule().sendResponse(response);
    }

    @Override
    public String getDescription() {
        return "вывести любой объект из коллекции, значение поля meleeWeapon которого является максимальным";
    }
}

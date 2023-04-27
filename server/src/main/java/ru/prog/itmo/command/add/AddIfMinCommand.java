package ru.prog.itmo.command.add;

import ru.prog.itmo.command.ClientIOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public class AddIfMinCommand extends ClientIOCommand implements UserAsking {
    public AddIfMinCommand(Storage storage, ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        super(storage, connectionModule, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        Response<String> response = new Response<>();
        try {
            @SuppressWarnings("unchecked")
            Request<SpaceMarine> request = (Request<SpaceMarine>) connectionModule().getRequest();
            SpaceMarine marineToAdd = request.getData();
            SpaceMarine minMarine = storage().sort()[0];
            if (storage().contains(marineToAdd)) {
                response.setData("Солдат уже содержится в коллекции");
            } else if (storage().getHashSet().size() == 0){
                response.setData("В коллекции нет элементов");
            } else if (marineToAdd.compareTo(minMarine) > 0) {
                response.setData("Солдат не является минимальным.");
            } else if (marineToAdd.compareTo(minMarine) == 0) {
                response.setData("Солдат совпадает с минимальным, поэтому добавлен");
            } else {
                response.setData("Солдат успешно добавлен.");
                storage().add(marineToAdd);
            }
        } catch (ClassCastException e){
            response.setData("Некорректный запрос.");
        }
        connectionModule().sendResponse(response);
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
    }
}

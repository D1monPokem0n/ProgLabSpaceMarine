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

public class AddCommand extends ClientIOCommand implements UserAsking {
    public AddCommand(Storage storage, ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        super(storage, connectionModule, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        Response<String> response = new Response<>();
        try {
            Request<?> request = connectionModule().getRequest();
            SpaceMarine marineToAdd = (SpaceMarine) request.getData();
            if (storage().contains(marineToAdd)){
                response.setData("В хранилище уже есть такой десантник.");
            } else {
                response.setData("Десантник успешно добавлен\n" + marineToAdd);
                long id = SpaceMarine.getUniqueId();
                marineToAdd.setId(id);
                storage().add(marineToAdd);
            }
            connectionModule().sendResponse(response);
        } catch (ClassCastException e){
            response.setComment("Некорректный запрос.");
            connectionModule().sendResponse(response);
        }
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию";
    }
}

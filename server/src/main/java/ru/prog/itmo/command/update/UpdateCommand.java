package ru.prog.itmo.command.update;

import ru.prog.itmo.command.ClientIOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public class UpdateCommand extends ClientIOCommand implements UserAsking {

    public UpdateCommand(Storage storage, ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        super(storage, connectionModule, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        Response<String> response = new Response<>();
        try {
            Request<?> request = connectionModule().getRequest();
            SpaceMarine updatedMarine = (SpaceMarine) request.getData();
            long id = updatedMarine.getId();
            SpaceMarine oldMarine = storage().getById(id);
            if (oldMarine == null)
                throw new UpdatingCancelledException();
            storage().remove(oldMarine);
            storage().add(updatedMarine);
            response.setData("Данные о десантнике успешно обновлены");
        } catch (ClassCastException | UpdatingCancelledException e) {
            response.setComment("Некорректный запрос.");
        }
        connectionModule().sendResponse(response);
    }

    @Override
    public String getDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному.";
    }
}

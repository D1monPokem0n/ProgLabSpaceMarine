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
        Response<SpaceMarine> response1 = new Response<>();
        try {
            Request<Long> request1;
            try {
                request1 = (Request<Long>) connectionModule().getRequest();
            } catch (ClassCastException e) {
                response1.setComment("Некорректный запрос.");
                connectionModule().sendResponse(response1);
                throw new UpdatingCancelledException();
            }
            long id = request1.getData();
            SpaceMarine marineToUpdate = null;
            if (storage().getStream().anyMatch(marine -> marine.getId() == id)) {
                marineToUpdate = storage().getById(id);
                response1.setData(marineToUpdate);
            } else {
                response1.setComment("В коллекции нет десантика с данным id.");
            }
            connectionModule().sendResponse(response1);
            if (marineToUpdate != null) {
                Response<String> response2 = new Response<>();
                Request<SpaceMarine> request2;
                try {
                    request2 = (Request<SpaceMarine>) connectionModule().getRequest();
                } catch (ClassCastException e){
                    response2.setComment("Некорректный запрос.");
                    connectionModule().sendResponse(response2);
                    throw new UpdatingCancelledException();
                }
                SpaceMarine updatedMarine = request2.getData();
                if (storage().contains(updatedMarine)){
                    response2.setComment("Обновлённые значения совпадают с уже существубщим десантником." +
                            "Обновление отменено.");
                } else {
                    storage().remove(marineToUpdate);
                    storage().add(updatedMarine);
                    response2.setData("Данные о десантнике обновлены успешно: \n" + updatedMarine);
                }
                connectionModule().sendResponse(response2);
            }
        } catch (UpdatingCancelledException e){
            //
        }
    }

    @Override
    public String getDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному.";
    }
}

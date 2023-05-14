package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ClientIOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

import java.util.List;

public class RemoveGreaterCommand extends ClientIOCommand implements UserAsking {
    public RemoveGreaterCommand(Storage storage, ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        super(storage, connectionModule, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        Response<String> response = new Response<>();
        try {
            Request<?> request = connectionModule().getRequest();
            SpaceMarine maxMarine = (SpaceMarine) request.getData();
            List<SpaceMarine> marinesToDelete = storage().getStream()
                    .filter(marine -> marine.compareTo(maxMarine)> 0)
                    .toList();
            if (marinesToDelete.isEmpty()){
                response.setComment("Нет десантников, находящихся в части старше данной.");
            } else {
                response.setData("Из коллекции удалено " + marinesToDelete.size() + " десантников.");
                storage().removeAll(marinesToDelete);
            }
        } catch (ClassCastException e){
            response.setComment("Некорректный запрос.");
        }
        connectionModule().sendResponse(response);
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, превышающие заданный";
    }
}

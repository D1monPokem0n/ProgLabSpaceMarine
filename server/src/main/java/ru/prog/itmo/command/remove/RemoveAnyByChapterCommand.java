package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ClientIOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public class RemoveAnyByChapterCommand extends ClientIOCommand implements UserAsking {
    public RemoveAnyByChapterCommand(Storage storage, ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        super(storage, connectionModule, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        Response<String> response = new Response<>();
        try {
            @SuppressWarnings("unchecked")
            Request<Chapter> request = (Request<Chapter>) connectionModule().getRequest();
            Chapter currentChapter = request.getData();
            SpaceMarine marineToDelete = storage()
                    .getStream()
                    .filter(marine -> marine.getChapter().equals(currentChapter))
                    .findAny()
                    .orElse(null);
            if (marineToDelete == null)
                response.setComment("Нет десантника с такой частью");
            else response.setData("Из коллекции удалён десантник: " + marineToDelete);
        } catch (ClassCastException e){
            response.setComment("Некорректный запрос");
        }
        connectionModule().sendResponse(response);
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции один элемент, значение поля chapter которого эквивалентно заданному";
    }
}

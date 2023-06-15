package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ClientCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.spacemarine.chapter.ChapterComparator;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.storage.StorageDBException;

import java.net.SocketAddress;

public class RemoveAnyByChapterCommand extends ClientCommand implements UserAsking {
    public RemoveAnyByChapterCommand(Storage storage, ConnectionManager connectionManager) {
        super(storage, connectionManager);
    }

    @Override
    public void execute(SocketAddress address) {
        super.execute(address);
        Response<String> response = new Response<>();
        try {
            var currentChapter = getCurrentChapter(address);
            var marineToDelete = getMarineToDelete(currentChapter, address);
            executeRemove(response, marineToDelete);
        } catch (ClassCastException e) {
            response.setComment("Некорректный запрос");
        } catch (StorageDBException e) {
            response.setComment(e.getMessage());
        } finally {
            connectionManager().putResponse(address, response);
        }
    }

    private void executeRemove(Response<String> response, SpaceMarine marineToDelete){
        if (marineToDelete == null)
            response.setComment("Нет вашего десантника с такой частью");
        else {
            storage().remove(marineToDelete);
            response.setData("Из коллекции удалён десантник: " + marineToDelete);
        }
    }

    private Chapter getCurrentChapter(SocketAddress address) {
        @SuppressWarnings("unchecked")
        Request<Chapter> request = (Request<Chapter>) connectionManager().getRequestByAddress(address);
        return request.getData();
    }

    private SpaceMarine getMarineToDelete(Chapter currentChapter, SocketAddress address) {
        ChapterComparator chapterComparator = new ChapterComparator();
        return storage().getStream()
                .filter(marine -> chapterComparator.compare(marine.getChapter(), currentChapter) == 0)
                .filter(marine -> connectionManager().
                        getRequestByAddress(address).
                        getUser().getLogin().equals(marine.getOwnerUser()))
                .findAny().orElse(null);
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции один элемент, значение поля chapter которого эквивалентно заданному";
    }
}

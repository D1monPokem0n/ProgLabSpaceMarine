package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.command.StorageIOCommand;
import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.spacemarine.builder.user.SpaceMarineUserCreator;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.CreateCancelledException;
import ru.prog.itmo.speaker.Speaker;

import java.util.HashSet;

public class RemoveGreaterCommand extends StorageIOCommand implements UserAsking {
    public RemoveGreaterCommand(Storage storage, Speaker speaker, SpaceMarineReader reader) {
        super(storage, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        SpaceMarineUserCreator creator = new SpaceMarineUserCreator(getSpeaker(), getReader());
        HashSet<SpaceMarine> marinesToDelete = new HashSet<>();
        getSpeaker().speak("Задайтие десантника, который доллжен стать максимальным в базе. \nВсе, кто выше будут удалены.");
        try {
            SpaceMarine maxMarine = creator.create();
            for (SpaceMarine marine : getStorage().getHashSet()) {
                if (marine.compareTo(maxMarine) > 0) {
                    marinesToDelete.add(marine);
                }
            }
            getStorage().removeAll(marinesToDelete);
            getSpeaker().speak("Из коллекции удалено " + marinesToDelete.size() + " десантников");
        } catch (CreateCancelledException e) {
            getSpeaker().speak("Вам не удалось задать максимального десантника. \nУдаление отменено.");
        } catch (NullPointerException e) {
            getSpeaker().speak("В коллекции нет десантников, выше заданного Вами.");
        }
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, превышающие заданный";
    }
}

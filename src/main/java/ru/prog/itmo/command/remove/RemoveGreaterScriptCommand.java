package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.command.StorageIOCommand;
import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.script.SpaceMarineScriptCreator;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

import java.util.HashSet;

public class RemoveGreaterScriptCommand extends StorageIOCommand implements ScriptExecutable {
    public RemoveGreaterScriptCommand(Storage storage, Speaker speaker, SpaceMarineReader reader) {
        super(storage, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        SpaceMarineScriptCreator creator = new SpaceMarineScriptCreator(getReader());
        HashSet<SpaceMarine> marinesToDelete = new HashSet<>();
        try {
            SpaceMarine maxMarine = creator.create();
            for (SpaceMarine marine : getStorage().getHashSet()) {
                if (marine.compareTo(maxMarine) > 0) {
                    marinesToDelete.add(marine);
                }
            }
            getStorage().removeAll(marinesToDelete);
            getSpeaker().speak("Из коллекции удалено " + marinesToDelete.size() + " десантников");
        } catch (NullPointerException e){
            getSpeaker().speak("В коллекции нет десантников, выше заданного Вами.");
        }
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, превышающие заданный";
    }
}

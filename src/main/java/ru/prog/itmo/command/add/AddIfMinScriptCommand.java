package ru.prog.itmo.command.add;

import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.command.StorageIOCommand;
import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.CreateCancelledException;
import ru.prog.itmo.spacemarine.builder.script.SpaceMarineScriptCreator;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public class AddIfMinScriptCommand extends StorageIOCommand implements ScriptExecutable {
    public AddIfMinScriptCommand(Storage storage, Speaker speaker, SpaceMarineReader reader) {
        super(storage, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        SpaceMarine minSoldier = getStorage().sort()[0];
        try {
            SpaceMarineScriptCreator creator = new SpaceMarineScriptCreator(getReader());
            SpaceMarine marine = creator.create();
            if (marine.compareTo(minSoldier) < 0) {
                getStorage().add(marine);
                getSpeaker().speak("Морпех успешно добавлен.");
            } else {
                throw new CreateCancelledException("Морпех не меньше минимального морпеха.");
            }
        } catch (CreateCancelledException e) {
            getSpeaker().speak(e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
    }
}

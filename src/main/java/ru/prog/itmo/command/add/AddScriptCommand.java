package ru.prog.itmo.command.add;

import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.command.StorageIOCommand;
import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.CreateCancelledException;
import ru.prog.itmo.spacemarine.builder.script.SpaceMarineScriptCreator;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public class AddScriptCommand extends StorageIOCommand implements ScriptExecutable {
    public AddScriptCommand(Storage storage, Speaker speaker, SpaceMarineReader reader) {
        super(storage, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        SpaceMarineScriptCreator creator = new SpaceMarineScriptCreator(getReader());
        try {
            SpaceMarine marine = creator.create();
            getStorage().add(marine);
            getSpeaker().speak("Десантник добавлен:\n"+marine);
        } catch (CreateCancelledException e) {
            getSpeaker().speak(e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию";
    }
}
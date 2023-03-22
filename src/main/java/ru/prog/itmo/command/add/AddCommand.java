package ru.prog.itmo.command.add;

import ru.prog.itmo.Storage;
import ru.prog.itmo.command.StorageCommand;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.CreateCancelledException;
import ru.prog.itmo.speaker.ConsoleSpeaker;
import ru.prog.itmo.speaker.Speaker;

public class AddCommand extends StorageCommand {
    public AddCommand(Storage storage) {
        super(storage);
    }

    @Override
    public void execute() {
        super.execute();
        SpaceMarineAddCreator creator = new SpaceMarineAddCreator();
        try {
            SpaceMarine marine = creator.create();
            getStorage().add(marine);
        } catch (CreateCancelledException e) {
            Speaker speaker = new ConsoleSpeaker();
            speaker.speak(e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию";
    }
}

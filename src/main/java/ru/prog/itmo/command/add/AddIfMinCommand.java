package ru.prog.itmo.command.add;

import ru.prog.itmo.Storage;
import ru.prog.itmo.command.StorageCommand;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.CreateCancelledException;
import ru.prog.itmo.speaker.ConsoleSpeaker;
import ru.prog.itmo.speaker.Speaker;

public class AddIfMinCommand extends StorageCommand {
    public AddIfMinCommand(Storage storage) {
        super(storage);
    }

    @Override
    public void execute() {
        super.execute();
        SpaceMarine minSoldier = getStorage().sort()[0];
        try {
            SpaceMarineAddCreator creator = new SpaceMarineAddCreator();
            SpaceMarine marine = creator.create();
            if (marine.compareTo(minSoldier) < 0) {
                getStorage().add(marine);
            } else {
                throw new CreateCancelledException("Морпех не меньше минимального морпеха.");
            }
        } catch (CreateCancelledException e) {
            Speaker speaker = new ConsoleSpeaker();
            speaker.speak(e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return null;
    }
}

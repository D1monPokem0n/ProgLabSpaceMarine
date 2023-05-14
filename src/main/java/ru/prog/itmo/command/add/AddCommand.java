package ru.prog.itmo.command.add;

import ru.prog.itmo.command.StorageIOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.CreateCancelledException;
import ru.prog.itmo.spacemarine.builder.client.SpaceMarineClientCreator;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public class AddCommand extends StorageIOCommand implements UserAsking {
    public AddCommand(Storage storage, Speaker speaker, SpaceMarineReader reader) {
        super(storage, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        SpaceMarineClientCreator creator = new SpaceMarineClientCreator(getSpeaker(), getReader());
        try {
            SpaceMarine marine = creator.create();
            getStorage().add(marine);
            getSpeaker().speak("Вы успешно добавили десантника:\n"+marine);
        } catch (CreateCancelledException e) {
            getSpeaker().speak(e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию";
    }
}

package ru.prog.itmo.command.info;

import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.command.StorageOCommand;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;

public class ShowCommand extends StorageOCommand {
    public ShowCommand(Storage storage, Speaker speaker) {
        super(storage, speaker);
    }

    @Override
    public void execute() {
        super.execute();
        if (getStorage().isEmpty()) {
            getSpeaker().speak("В хранилище нет информации о космодесантниках.");
        } else {
            for (SpaceMarine marine : getStorage().getHashSet()) {
                getSpeaker().speak(marine.toString());
            }
        }
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении.";
    }
}

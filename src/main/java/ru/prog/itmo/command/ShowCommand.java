package ru.prog.itmo.command;

import ru.prog.itmo.Storage;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.ConsoleSpeaker;
import ru.prog.itmo.speaker.Speaker;

public class ShowCommand extends StorageCommand{
    public ShowCommand(Storage storage) {
        super(storage);
    }

    @Override
    public void execute() {
        super.execute();
        Speaker speaker = new ConsoleSpeaker();
        if (getStorage().getHashSet().isEmpty()) {
            speaker.speak("В хранилище нет информации о космодесантниках.");
        } else {
            for (SpaceMarine marine : getStorage().getHashSet()) {
                speaker.speak(marine.toString());
            }
        }
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении.";
    }
}

package ru.prog.itmo.command.filter;

import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.command.StorageIOCommand;
import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;

import java.util.Arrays;
import java.util.Comparator;

public class PrintFieldDescendingHealthCommand extends StorageIOCommand {
    public PrintFieldDescendingHealthCommand(Storage storage, Speaker speaker, SpaceMarineReader reader) {
        super(storage, speaker, reader);
    }

    @Override
    public String getDescription() {
        return "вывести значения поля health всех элементов в порядке убывания.";
    }

    @Override
    public void execute() {
        super.execute();
        SpaceMarine[] array = getStorage().getHashSet().toArray(new SpaceMarine[0]);
        if (array.length == 0) getSpeaker().speak("Коллекция пуста.");
        else {
            Arrays.sort(array, (Comparator.comparingInt(SpaceMarine::getHealth)));
            for (int i = array.length; i > 0; i--) {
                getSpeaker().speak("Показатель здоровья космодесантника "
                        + array[i - 1].getName()
                        + " под номером "
                        + array[i - 1].getId()
                        + " равен "
                        + array[i - 1].getHealth()
                        + ".");
            }
        }
    }
}

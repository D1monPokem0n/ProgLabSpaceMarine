package ru.prog.itmo.command.filter;

import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.command.StorageOCommand;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

import java.util.Arrays;
import java.util.Comparator;

public class MaxByMeleeWeaponScriptCommand extends StorageOCommand implements ScriptExecutable {
    public MaxByMeleeWeaponScriptCommand(Storage storage, Speaker speaker) {
        super(storage, speaker);
    }

    @Override
    public String getDescription() {
        return "вывести любой объект из коллекции, значение поля meleeWeapon которого является максимальным";
    }

    @Override
    public void execute() {
        super.execute();
        SpaceMarine[] array = getStorage().getHashSet().toArray(new SpaceMarine[0]);
        Arrays.sort(array, Comparator.comparing(SpaceMarine::getMeleeWeapon));
        if (array.length == 0) getSpeaker().speak("Нет элементов в коллекции");
        else {
            for (SpaceMarine marine : array) {
                getSpeaker().speak(marine.toString());
            }
        }
    }
}

package ru.prog.itmo.command.filter;

import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.command.StorageIOCommand;
import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;

import java.util.Arrays;
import java.util.Comparator;


public class MaxByMeleeWeaponCommand extends StorageIOCommand implements UserAsking {
    public MaxByMeleeWeaponCommand(Storage storage, Speaker speaker, SpaceMarineReader reader) {
        super(storage, speaker, reader);
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
        String answer;
        if (array.length == 0) {
            answer = "arrayIsNull";
        } else {
            getSpeaker().speak("Вы хотите вывести все элементы отсортированные по оружию?(Y/N)\nЕсли нет, то мы выведем один элемент с максимальным оружием.");
            String value = getReader().read();
            answer = value == null ? "" : value;
        }
        switch (answer) {
            case "Y","y" -> {
                for (SpaceMarine marine : array) {
                    getSpeaker().speak(marine.toString());
                }
            }
            case "N","n" -> getSpeaker().speak("Ну нет, так нет. Держите:\n" + array[0].toString());
            case "arrayIsNull" -> getSpeaker().speak("Коллекция пустая.");
            default -> getSpeaker().speak("Вы ввели какую-то фигню, повторите с начала команды.");
        }
    }
}

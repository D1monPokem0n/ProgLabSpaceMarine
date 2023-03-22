package ru.prog.itmo;

import ru.prog.itmo.spacemarine.SpaceMarine;

import java.util.Arrays;
import java.util.HashSet;

public class Storage {
    private final HashSet<SpaceMarine> hashSet;
    private int elementsCount;

    public Storage() {
        hashSet = new HashSet<>();
    }

    public HashSet<SpaceMarine> getHashSet() {
        return hashSet;
    }

    public void add(SpaceMarine spaceMarine) {
        hashSet.add(spaceMarine);
        elementsCount++;
    }

    public int getElementsCount() {
        return elementsCount;
    }

    public SpaceMarine[] sort() {
        SpaceMarine[] array = hashSet.toArray(new SpaceMarine[0]);
        Arrays.sort(array);
        return array;
    }
}

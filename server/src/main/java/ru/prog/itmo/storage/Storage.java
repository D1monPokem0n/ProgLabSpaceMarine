package ru.prog.itmo.storage;

import ru.prog.itmo.StorageInfo;
import ru.prog.itmo.spacemarine.SpaceMarine;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class Storage {
    private final HashSet<SpaceMarine> hashSet;
    private final StorageInfo info;
    private final StorageFile file;

    public Storage() {
        file = new StorageFile();
        hashSet = new HashSet<>();
        if (file.isExist()) {
            info = file.getStorageInfo();
            if (!Objects.equals(info.getCollectionType(), hashSet.getClass().getName()))
                throw new WrongStorageFileException("Не синхронизирована информация о типе коллекции");
            hashSet.addAll(file.getMarines());
            for (SpaceMarine marine : file.getMarines()) {
                SpaceMarine.addId(marine.getId());
            }
            if (hashSet.size() != info.getElementsCount())
                throw new WrongStorageFileException("Не синхронизирована информация о количестве элементов в коллекции");
        } else {
            info = new StorageInfo();
        }
    }

    public HashSet<SpaceMarine> getHashSet() {
        return hashSet;
    }

    public boolean add(SpaceMarine spaceMarine) {
        hashSet.add(spaceMarine);
        info.incrementElementsCount();
        return hashSet.contains(spaceMarine);
    }

    public StorageInfo getInfo() {
        return info;
    }

    public SpaceMarine[] sort() {
        SpaceMarine[] array = hashSet.toArray(new SpaceMarine[0]);
        Arrays.sort(array);
        return array;
    }

    public void removeAll(Collection<SpaceMarine> col) {
        info.reduceElementsCount(col.size());
        hashSet.removeAll(col);
    }

    public void clear() {
        info.reduceElementsCount(hashSet.size());
        hashSet.clear();
    }

    public boolean isEmpty() {
        return hashSet.isEmpty();
    }

    public boolean remove(SpaceMarine marine) {
        boolean haveDeleted = hashSet.remove(marine);
        if (haveDeleted) info.reduceElementsCount(1);
        return haveDeleted;
    }

    public SpaceMarine getById(long id) {
        SpaceMarine searchableMarine = null;
        for (SpaceMarine marine : hashSet) {
            if (marine.getId() == id) {
                searchableMarine = marine;
                break;
            }
        }
        return searchableMarine;
    }

    public StorageFile getFile() {
        return file;
    }
}

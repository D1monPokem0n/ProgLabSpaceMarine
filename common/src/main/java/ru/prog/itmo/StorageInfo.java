package ru.prog.itmo;

import ru.prog.itmo.spacemarine.SpaceMarine;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Класс, хранящий информацию о классе Storage
 * Хранит кол-во элементов, дату инициализацию, тип коллекции, тип файла с данными
 */
public class StorageInfo implements Serializable {
    private int elementsCount;
    private final String collectionType;
    private final String dataBaseName;
    private final static int fieldsCount = 4;


    public StorageInfo(HashSet<SpaceMarine> hashSet)  {
        this.elementsCount = hashSet.size();
        this.collectionType = hashSet.getClass().getTypeName();
        this.dataBaseName = "PostgreSQL";
    }

    /**
     * Конструктор класса, без параметров.
     * Используется, когда исходный файл создаётся с 0.
     */
    public StorageInfo(){
        elementsCount = 0;
        collectionType = HashSet.class.getTypeName();
        dataBaseName = "PostgreSQL";
    }


    /**
     * Метод, возвращающий число элементов.
     * @return elementsCount - число элементов
     */
    public int getElementsCount() {
        return elementsCount;
    }

    /**
     * Метод, инкрементирующий число элементов в коллекции.
     * Используется при добавлении в коллекцию нового элемента.
     */
    public void incrementElementsCount(){
        elementsCount++;
    }

    /**
     * Метод, возвращающий тип коллекции.
     * @return collectionType - тип коллекции
     */
    public String getCollectionType() {
        return collectionType;
    }

    /**
     * Метод, возвращающий тип файла с данными.
     * @return fileType - тип файла.
     */
    public String getDataBaseName() {
        return dataBaseName;
    }

    /**
     * Возвращает целое число, равное количеству необходимых для заполнения полей.
     * @return fieldsCount - количество полей в классе StorageInfo.
     */
    public static int getFieldsCount(){
        return fieldsCount;
    }

    /**
     * Метод, уменьшаеющий кол-во элементов в коле
     * @param deletedMarinesCount - число удалённых солдат.
     */
    public void reduceElementsCount(int deletedMarinesCount){
        elementsCount = elementsCount - deletedMarinesCount;
    }
}

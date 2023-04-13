package ru.prog.itmo.storage;

import java.time.LocalDateTime;
import java.util.HashSet;

/**
 * Класс, хранящий информацию о классе Storage
 * Хранит кол-во элементов, дату инициализацию, тип коллекции, тип файла с данными
 */
public class StorageInfo {
    private int elementsCount;
    private LocalDateTime creationDate;
    private final String collectionType;
    private final String fileType;
    private static int fieldsCount = 4;

    /**
     * Конструктор класса, устанавливающий все поля.
     * Используется, при загрузке данных о коллекции из файла с данными.
     * @param elementsCount - число элементов
     * @param creationDate - дата инициализации коллекции
     * @param collectionType - тип коллекции
     * @param fileType - тип файла с данными
     */
    public StorageInfo(int elementsCount, LocalDateTime creationDate, String collectionType, String fileType){
        this.elementsCount = elementsCount;
        this.creationDate = creationDate;
        this.collectionType = collectionType;
        this.fileType = fileType;
    }

    /**
     * Конструктор класса, без параметров.
     * Используется, когда исходный файл создаётся с 0.
     */
    public StorageInfo(){
        elementsCount = 0;
        creationDate = LocalDateTime.now();
        collectionType = HashSet.class.getTypeName();
        fileType = "csv";
    }

    /**
     * Метод, возвращающий дату инициализации.
     * @return creationDate - дата инициализации.
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
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
    public String getFileType() {
        return fileType;
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

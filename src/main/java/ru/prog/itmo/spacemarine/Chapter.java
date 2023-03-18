package ru.prog.itmo.spacemarine;

public class Chapter {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private String parentLegion;
    private Long marinesCount; //Поле может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 1000
    private String world; //Поле не может быть null

    @Override
    public String toString() {
        return "Chapter_name:"+name+",parentLegion:"+parentLegion+",marinesCount:"+marinesCount+",world:"+world;
    }
}
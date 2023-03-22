package ru.prog.itmo.spacemarine;

import java.util.Objects;

public class Chapter implements Comparable<Chapter> {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private String parentLegion;
    private Long marinesCount; //Поле может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 1000
    private String world; //Поле не может быть null

    public Chapter(String name, String parentLegion, Long marinesCount, String world){
        this.name = name;
        this.parentLegion = parentLegion;
        this.marinesCount = marinesCount;
        this.world = world;
    }

    @Override
    public String toString() {
        return "Chapter_name:" + name + ",parentLegion:" + parentLegion + ",marinesCount:" + marinesCount + ",world:" + world;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;
        Chapter other = (Chapter) obj;
        if (this.name.equals(other.name)
         && this.marinesCount == other.marinesCount) return true;
        return false;
    }

    @Override
    public int compareTo(Chapter o) {
        if (name.compareTo(o.name) != 0) return name.compareTo(o.name);
        return marinesCount.compareTo(o.marinesCount);
    }

    @Override
    public int hashCode() {
        return super.hashCode() * Objects.hash(name, marinesCount);
    }
}
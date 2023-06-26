package ru.prog.itmo.spacemarine.chapter;

import java.io.Serializable;
import java.util.Objects;

public class Chapter implements Comparable<Chapter>, Serializable {
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
    public Chapter(){
        name = "UltraMarines";
        parentLegion = "RomeLegion";
        marinesCount = 1000L;
        world = "Earth";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMarinesCount() {
        return marinesCount;
    }

    public void setMarinesCount(Long marinesCount) {
        this.marinesCount = marinesCount;
    }

    public String getParentLegion() {
        return parentLegion;
    }

    public void setParentLegion(String parentLegion) {
        this.parentLegion = parentLegion;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    @Override
    public int compareTo(Chapter o) {
        if (name.compareTo(o.name) != 0) return name.compareTo(o.name);
        return marinesCount.compareTo(o.marinesCount);
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
        return name.equals(other.name)
               && Objects.equals(marinesCount, other.marinesCount);
    }

    @Override
    public int hashCode() {
        return super.hashCode() * Objects.hash(name, marinesCount);
    }
}
package ru.prog.itmo.spacemarine;

import java.util.Objects;

public class SpaceMarine {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int health; //Значение поля должно быть больше 0
    private long heartCount; //Значение поля должно быть больше 0, Максимальное значение поля: 3
    private AstartesCategory category; //Поле может быть null
    private MeleeWeapon meleeWeapon; //Поле не может быть null
    private Chapter chapter; //Поле может быть null
    private static long lastId;

    public SpaceMarine(String name,
                       Coordinates coordinates,
                       int health,
                       long heartCount,
                       AstartesCategory category,
                       MeleeWeapon meleeWeapon,
                       Chapter chapter) {
        lastId++;
        this.id = lastId;
        this.name = name;
        this.coordinates = coordinates;
        creationDate = java.time.LocalDateTime.now();
        this.health = health;
        this.heartCount = heartCount;
        this.category = category;
        this.meleeWeapon = meleeWeapon;
        this.chapter = chapter;
    }

    public static void setLastId(long id) {
        lastId = id;
    }

    @Override
    public int hashCode() {
        return 7 * super.hashCode() + 13 * Objects.hash(id, name, category, meleeWeapon, chapter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpaceMarine other = (SpaceMarine) o;
        if (other.id == this.id) return true;
        return Objects.equals(name, other.name)
                && category == other.category
                && meleeWeapon == other.meleeWeapon
                && Objects.equals(chapter, other.chapter);
    }

    @Override
    public String toString() {
        return super.toString()
                +"[Id:" + id
                + ",name:" + name
                + ",coordinates:[" + coordinates.toString() + "]"
                + ",CreationDate:"+creationDate.toString()
                + ",health:" + health
                + ",heartCount:" + heartCount
                + ",category:" + category.getName()
                + ",meleeWeapon:" + meleeWeapon.getName()
                + ",chapter:[" + chapter.toString() + "]" + "]";
    }
}
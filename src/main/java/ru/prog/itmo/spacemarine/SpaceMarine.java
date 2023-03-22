package ru.prog.itmo.spacemarine;

import ru.prog.itmo.spacemarine.builder.InvalidSpaceMarineValueException;
import java.time.LocalDateTime;
import java.util.Objects;

public class SpaceMarine implements Comparable<SpaceMarine> {
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

    public SpaceMarine(long id,
                       String name,
                       Coordinates coordinates,
                       java.time.LocalDateTime creationDate,
                       int health,
                       long heartCount,
                       AstartesCategory category,
                       MeleeWeapon meleeWeapon,
                       Chapter chapter) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.health = health;
        this.heartCount = heartCount;
        this.category = category;
        this.meleeWeapon = meleeWeapon;
        this.chapter = chapter;
    }

    public static long getLastId() {
        return lastId;
    }

    public static void setLastId(long id) {
        lastId = id;
    }

    public static void incrementLastId() throws InvalidSpaceMarineValueException {
        if (lastId == Long.MAX_VALUE) throw new InvalidSpaceMarineValueException("Вы превысили размеры long.");
        lastId++;
    }

    public String getName() {
        return name;
    }

    public AstartesCategory getCategory() {
        return category;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public int getHealth() {
        return health;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public long getHeartCount() {
        return heartCount;
    }

    public long getId() {
        return id;
    }

    public MeleeWeapon getMeleeWeapon() {
        return meleeWeapon;
    }

    @Override
    public int hashCode() {
        return 7 * super.hashCode() + 13 * Objects.hash(name, heartCount, category, meleeWeapon, chapter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpaceMarine other = (SpaceMarine) o;
        return Objects.equals(name, other.name)
                && Objects.equals(heartCount, other.heartCount)
                && meleeWeapon == other.meleeWeapon
                && category == other.category
                && Objects.equals(chapter, other.chapter);
    }

    @Override
    public String toString() {
        return super.toString()
                + "[Id:" + id
                + ",name:" + name
                + ",coordinates:[" + coordinates.toString() + "]"
                + ",CreationDate:" + creationDate.toString()
                + ",health:" + health
                + ",heartCount:" + heartCount
                + ",category:" + category.getName()
                + ",meleeWeapon:" + meleeWeapon.getName()
                + ",chapter:[" + chapter.toString() + "]" + "]";
    }

    @Override
    public int compareTo(SpaceMarine o) {
        if (chapter.compareTo(o.chapter) != 0)
            return chapter.compareTo(o.chapter);
        if (category.compareTo(o.category) != 0)
            return category.compareTo(o.category);
        if (meleeWeapon.compareTo(o.meleeWeapon) != 0)
            return meleeWeapon.compareTo(o.meleeWeapon);
        if (heartCount != o.heartCount)
            return heartCount > o.heartCount ? 1 : -1;
        return name.compareTo(o.name);
    }
}
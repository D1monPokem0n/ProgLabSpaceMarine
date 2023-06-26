package ru.prog.itmo.spacemarine;

import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.spacemarine.chapter.ChapterComparator;
import ru.prog.itmo.spacemarine.coordinates.Coordinates;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class SpaceMarine implements Comparable<SpaceMarine>, Serializable {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private final java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int health; //Значение поля должно быть больше 0
    private long heartCount; //Значение поля должно быть больше 0, Максимальное значение поля: 3
    private AstartesCategory category; //Поле может быть null
    private MeleeWeapon meleeWeapon; //Поле не может быть null
    private Chapter chapter; //Поле может быть null
    private String ownerUser;

    public SpaceMarine(long id,
                       String name,
                       Coordinates coordinates,
                       java.time.LocalDateTime creationDate,
                       int health,
                       long heartCount,
                       AstartesCategory category,
                       MeleeWeapon meleeWeapon,
                       Chapter chapter,
                       String ownerUser) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.health = health;
        this.heartCount = heartCount;
        this.category = category;
        this.meleeWeapon = meleeWeapon;
        this.chapter = chapter;
        this.ownerUser = ownerUser;
    }

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

    public static SpaceMarine getTMPSpaceMarine() {
        return new SpaceMarine(
                0,
                "Oh, great soup",
                new Coordinates(0f, 0.0),
                LocalDateTime.now(),
                0,
                0,
                AstartesCategory.SCOUT,
                MeleeWeapon.POWER_FIST,
                new Chapter("Soup cookers", "", 0L, "Earth"),
                "Emperor"
        );
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AstartesCategory getCategory() {
        return category;
    }

    public void setCategory(AstartesCategory category) {
        this.category = category;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public float getCoordinatesX() {
        return coordinates.getX();
    }

    public double getCoordinatesY() {
        return coordinates.getY();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public long getHeartCount() {
        return heartCount;
    }

    public void setHeartCount(long heartCount) {
        this.heartCount = heartCount;
    }

    public long getId() {
        return id;
    }

    public MeleeWeapon getMeleeWeapon() {
        return meleeWeapon;
    }

    public void setMeleeWeapon(MeleeWeapon meleeWeapon) {
        this.meleeWeapon = meleeWeapon;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public String getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(String ownerUser) {
        this.ownerUser = ownerUser;
    }

    public void setAllFields(SpaceMarine other) { //Сделано для команды update, поэтому id и creationDate не меняются.
        this.name = other.name;
        this.getCoordinates().setX(other.getCoordinates().getX());
        this.getCoordinates().setY(other.getCoordinates().getY());
        this.health = other.health;
        this.heartCount = other.heartCount;
        this.category = other.category;
        this.meleeWeapon = other.meleeWeapon;
        if (other.getChapter() != null) {
            if (this.getChapter() == null) setChapter(new Chapter());
            this.getChapter().setName(other.getChapter().getName());
            this.getChapter().setParentLegion(other.getChapter().getParentLegion());
            this.getChapter().setMarinesCount(other.getChapter().getMarinesCount());
            this.getChapter().setWorld(other.getChapter().getWorld());
        } else {
            this.chapter = null;
        }
    }

    public static int getFieldsCount() {
        return 14;
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
        if (id == other.id) return true;
        return Objects.equals(name, other.name)
               && Objects.equals(heartCount, other.heartCount)
               && meleeWeapon == other.meleeWeapon
               && category == other.category
               && Objects.equals(chapter, other.chapter);
    }

    public String toLineString(){
        String chapterString = chapter == null ? null : chapter.toString();
        String categoryString = category == null ? null : category.getName();
        return "[Id: " + id
               + ",name: " + name
               + ",coordinates: [" + coordinates.toString() + "]"
               + ",CreationDate: " + creationDate.toString()
               + ",health: " + health
               + ",heartCount: " + heartCount
               + ",category: " + categoryString
               + ",meleeWeapon:" + meleeWeapon.getName()
               + ",chapter:[" + chapterString + "]"
               + ",ownerUser:" + ownerUser + "]";
    }

    @Override
    public String toString() {
        String chapterString = chapter == null ? null : chapter.toString();
        String categoryString = category == null ? null : category.getName();
        return "[Id: " + id
               + ",\n name: " + name
               + ",\n coordinates: [" + coordinates.toString() + "]"
               + ",\n CreationDate: " + creationDate.toString()
               + ",\n health: " + health
               + ",\n heartCount: " + heartCount
               + ",\n category: " + categoryString
               + ",\n meleeWeapon:" + meleeWeapon.getName()
               + ",\n chapter:[" + chapterString + "]"
               + ",\n ownerUser:" + ownerUser + "]";
    }

    @Override
    public int compareTo(SpaceMarine o) {
        ChapterComparator chapterComparator = new ChapterComparator();
        AstartesCategoryComparator categoryComparator = new AstartesCategoryComparator();
        if (chapterComparator.compare(chapter, o.chapter) != 0)
            return chapterComparator.compare(chapter, o.chapter);
        if (categoryComparator.compare(category, o.category) != 0)
            return categoryComparator.compare(category, o.category);
        if (meleeWeapon.compareTo(o.meleeWeapon) != 0)
            return meleeWeapon.compareTo(o.meleeWeapon);
        if (heartCount != o.heartCount)
            return Long.compare(heartCount, o.heartCount);
        return name.compareTo(o.name);
    }
}
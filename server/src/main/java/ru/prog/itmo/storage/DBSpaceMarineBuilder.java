package ru.prog.itmo.storage;

import ru.prog.itmo.spacemarine.AstartesCategory;
import ru.prog.itmo.spacemarine.InvalidSpaceMarineValueException;
import ru.prog.itmo.spacemarine.MeleeWeapon;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.SpaceMarineBuilder;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.spacemarine.coordinates.Coordinates;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBSpaceMarineBuilder implements SpaceMarineBuilder {
    private final ResultSet set;
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int health; //Значение поля должно быть больше 0
    private long heartCount; //Значение поля должно быть больше 0, Максимальное значение поля: 3
    private AstartesCategory category; //Поле может быть null
    private MeleeWeapon meleeWeapon; //Поле не может быть null
    private Chapter chapter; //Поле может быть null
    private String ownerUser;

    public DBSpaceMarineBuilder(ResultSet set) {
        this.set = set;
    }

    /*
    SELECT (1)SPACE_MARINE.ID, (2)SPACE_MARINE.NAME, (3)X, (4)Y, (5)CREATION_DATE,
                        (6)HEALTH, (7)HEART_COUNT, (8)CATEGORY, (9)WEAPON,
                        (10)CHAPTER.NAME, (11)PARENT_LEGION, (12)MARINES_COUNT, (13)WORLD, (14)USERS.NAME AS USER_OWNER FROM SPACE_MARINE
                        JOIN COORDINATES ON SPACE_MARINE.ID = COORDINATES.SPACE_MARINE_ID
                        LEFT JOIN CHAPTER ON CHAPTER.SPACE_MARINE_ID = SPACE_MARINE.ID
                        JOIN USERS ON SPACE_MARINE.USER_OWNER_ID = USERS.ID;
     */

    @Override
    public boolean setId() {
        try {
            id = set.getLong(1);
        } catch (SQLException e) {
            throw new InvalidSpaceMarineValueException(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean setName() {
        try {
            name = set.getString(2);
        } catch (SQLException e) {
            throw new InvalidSpaceMarineValueException(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean setCoordinates() {
        float x;
        double y;
        try {
            x = set.getFloat(3);
            y = set.getDouble(4);
        } catch (SQLException e) {
            throw new InvalidSpaceMarineValueException(e.getMessage());
        }
        coordinates = new Coordinates(x, y);
        return true;
    }

    @Override
    public boolean setCreationDate() {
        try {
            creationDate = set.getTimestamp(5).toLocalDateTime();
        } catch (SQLException e) {
            throw new InvalidSpaceMarineValueException(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean setHealth() {
        try {
            health = set.getInt(6);
        } catch (SQLException e) {
            throw new InvalidSpaceMarineValueException(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean setHeartCount() {
        try {
            heartCount = set.getLong(7);
        } catch (SQLException e) {
            throw new InvalidSpaceMarineValueException(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean setCategory() {
        try {
            var dbValue = set.getString(8);
            category = dbValue == null ? null : AstartesCategory.valueOf(dbValue);
        } catch (SQLException e) {
            throw new InvalidSpaceMarineValueException(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean setMeleeWeapon() {
        try {
            meleeWeapon = MeleeWeapon.valueOf(set.getString(9));
        } catch (SQLException e) {
            throw new InvalidSpaceMarineValueException(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean setChapter() {
        try {
            String name = set.getString(10);
            if (set.wasNull())
                chapter = null;
            else {
                String parentLegion = set.getString(11);
                long marinesCount = set.getLong(12);
                String world = set.getString(13);
                chapter = new Chapter(name, parentLegion, marinesCount, world);
            }
        } catch (SQLException e) {
            throw new InvalidSpaceMarineValueException(e.getMessage());
        }
        return true;
    }

    public boolean setUser() {
        try {
            ownerUser = set.getString(14);
        } catch (SQLException e) {
            throw new InvalidSpaceMarineValueException(e.getMessage());
        }
        return true;
    }

    @Override

    public SpaceMarine build() {
        return new SpaceMarine(
                id,
                name,
                coordinates,
                creationDate,
                health,
                heartCount,
                category,
                meleeWeapon,
                chapter,
                ownerUser
        );
    }

    public boolean nextRow() throws SQLException {
        return set.next();
    }
}

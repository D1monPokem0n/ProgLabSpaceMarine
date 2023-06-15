package ru.prog.itmo.spacemarine.builder.script;

import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.AstartesCategory;
import ru.prog.itmo.spacemarine.MeleeWeapon;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.SpaceMarineBuilder;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.spacemarine.chapter.builder.script.ChapterScriptCreator;
import ru.prog.itmo.spacemarine.coordinates.Coordinates;
import ru.prog.itmo.spacemarine.coordinates.builder.script.CoordinatesScriptCreator;

import static java.time.LocalDateTime.now;

public class SpaceMarineScriptBuilder implements SpaceMarineBuilder {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int health; //Значение поля должно быть больше 0
    private long heartCount; //Значение поля должно быть больше 0, Максимальное значение поля: 3
    private AstartesCategory category; //Поле может быть null
    private MeleeWeapon meleeWeapon; //Поле не может быть null
    private Chapter chapter; //Поле может быть null
    private final Reader reader;

    public SpaceMarineScriptBuilder(Reader reader) {
        this.reader = reader;
    }

    public boolean setId() {
        id = 0;
        return true;
    }

    public boolean setName() {
        String value = reader.read();
        if (value == null) throw new InvalidScriptException("Имя не может быть пустой строкой.");
        name = value;
        return true;
    }

    public boolean setCoordinates() {
        CoordinatesScriptCreator creator = new CoordinatesScriptCreator(reader);
        coordinates = creator.create();
        return true;
    }

    public boolean setCreationDate() {
        creationDate = now();
        return true;
    }

    public boolean setHealth() {
        String value = reader.read();
        if (value == null) throw new InvalidScriptException("Вы ввели пустую строку");
        try {
            int checkedValue = Integer.parseInt(value);
            if (checkedValue <= 0)
                throw new InvalidScriptException("Количество здоровья должно быть положительным числом, иначе космодесант press F.");
            if (checkedValue > Integer.MAX_VALUE)
                throw new InvalidScriptException("Вы вышли за пределы Java");
            health = checkedValue;
        } catch (NumberFormatException e) {
            throw new InvalidScriptException("Некорректный ввод.");
        }
        return true;
    }

    public boolean setHeartCount() {
        String value = reader.read();
        if (value == null) throw new InvalidScriptException("Не вводите пустую строку");
        try {
            heartCount = Long.parseLong(value);
            if (heartCount < 1 || heartCount > 3)
                throw new InvalidScriptException("Количество сердец космодесантника - от 1 до 3");
        } catch (NumberFormatException e) {
            throw new InvalidScriptException("Некорректный ввод.");
        }
        return true;
    }

    public boolean setCategory() {
        String value = reader.read();
        if (value == null) {
            category = null;
            return true;
        }
        try {
            category = AstartesCategory.getCategory(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidScriptException("То, что вы написали не является категорией космодесантника");
        }
        return true;
    }

    public boolean setMeleeWeapon() {
        String value = reader.read();
        if (value == null) throw new InvalidScriptException("Вы ввели пустую строку");
        try {
            meleeWeapon = MeleeWeapon.getMeleeWeapon(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidScriptException("То, что вы написали не является оружием космодесантника");
        }
        return true;
    }

    public boolean setChapter() {
        ChapterScriptCreator creator = new ChapterScriptCreator(reader);
        chapter = creator.create();
        return true;
    }

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
                chapter
        );
    }
}

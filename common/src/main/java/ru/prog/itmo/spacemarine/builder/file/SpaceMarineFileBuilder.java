package ru.prog.itmo.spacemarine.builder.file;

import ru.prog.itmo.spacemarine.*;
import ru.prog.itmo.spacemarine.builder.SpaceMarineBuilder;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.spacemarine.coordinates.Coordinates;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class SpaceMarineFileBuilder implements SpaceMarineBuilder {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int health; //Значение поля должно быть больше 0
    private long heartCount; //Значение поля должно быть больше 0, Максимальное значение поля: 3
    private AstartesCategory category; //Поле может быть null
    private MeleeWeapon meleeWeapon; //Поле не может быть null
    private Chapter chapter; //Поле может быть null
    private String[] values;

    public void setNextValues(String[] nextValues) {
        if (!(nextValues.length == 10 | nextValues.length == 13))
            throw new CreateCancelledException("Некорректное число данных в данной строке: " + Arrays.toString(nextValues));
        values = nextValues;
    }

    @Override
    public boolean setId() {
        try {
            id = Long.parseLong(values[0]);
            if (id <= 0)
                throw new InvalidSpaceMarineValueException("Некорректное значение id у десантника: \"" + values[0] + "\"");
        } catch (NumberFormatException e) {
            throw new InvalidSpaceMarineValueException("Некорректное значение id у десантника: \"" + values[0] + "\"");
        }
        return true;
    }

    @Override
    public boolean setName() {
        if (values[1] == null)
            throw new InvalidSpaceMarineValueException(
                    "Неккоректное значение name у десантника с id "
                            + id + ". Поле не может быть null");
        if (values[1].trim().equals(""))
            throw new InvalidSpaceMarineValueException(
                    "Неккоректное значение name у десантника с id "
                            + id + ". Поле не может быть пустой строкой");
        name = values[1];
        return true;
    }

    @Override
    public boolean setCoordinates() {
        float x;
        double y;
        if (values[2] == null | values[3] == null)
            throw new InvalidSpaceMarineValueException(
                    "Неккоректное значение координат у десантника с id "
                            + id + ". Поле не может быть null");
        try {
            x = Float.parseFloat(values[2]);
            y = Double.parseDouble(values[3]);
            if (y > 431)
                throw new InvalidSpaceMarineValueException(
                        "Неккоректное значение координаты Y десантника с id "
                                + id + ". Поле не может быть больше 431: " + y);
            coordinates = new Coordinates(x, y);
        } catch (NumberFormatException e) {
            throw new InvalidSpaceMarineValueException(
                    "Неккоректное значение координат y десантника с id "
                            + id + ". Оба значения должны быть числами: " + values[2] + ", " + values[3]);
        }
        return true;
    }

    @Override
    public boolean setCreationDate() {
        try {
            creationDate = LocalDateTime.parse(values[4]);
        } catch (DateTimeParseException e) {
            throw new InvalidSpaceMarineValueException(
                    "Неккоректное значение даты создания y десантника с id "
                            + id + ". Неверный формат: " + values[4]);
        }
        return true;
    }

    @Override
    public boolean setHealth() {
        try {
            health = Integer.parseInt(values[5]);
            if (health <= 0)
                throw new InvalidSpaceMarineValueException(
                        "Неккоректное значение здоровья y десантника с id "
                                + id + ". Здоровье должно быть положительным числом: " + values[5]);
        } catch (NumberFormatException e) {
            throw new InvalidSpaceMarineValueException(
                    "Неккоректное значение здоровья y десантника с id "
                            + id + ". Неверный формат: " + values[5]);
        }
        return true;
    }

    @Override
    public boolean setHeartCount() {
        try {
            heartCount = Long.parseLong(values[6]);
            if (heartCount < 1 | heartCount > 3)
                throw new InvalidSpaceMarineValueException(
                        "Неккоректное значение количества сердец y десантника с id "
                                + id + ". Количества сердец может быть от 1 до 3: " + heartCount);
        } catch (NumberFormatException e) {
            throw new InvalidSpaceMarineValueException(
                    "Неккоректное значение количества сердец y десантника с id "
                            + id + ". Неверный формат: " + values[6]);
        }
        return true;
    }

    @Override
    public boolean setCategory() {
        try {
            if (values[7] == null) {
                category = null;
            } else if (values[7].equals("null")) {
                category = null;
            } else {
                category = AstartesCategory.valueOf(values[7]);
            }
        } catch (IllegalArgumentException e) {
            throw new InvalidSpaceMarineValueException(
                    "Неккоректная категория y десантника с id "
                            + id + ". Существует всего 4 категории CHAPLAIN, LIBRARIAN, APOTHECARY, SCOUT: " + values[7]);
        }
        return true;
    }

    @Override
    public boolean setMeleeWeapon() {
        try {
            if (values[8] == null)
                throw new InvalidSpaceMarineValueException(
                        "Неккоректное оружие y десантника с id "
                                + id + ". Оружие не может быть null");
            meleeWeapon = MeleeWeapon.valueOf(values[8]);
        } catch (IllegalArgumentException e) {
            throw new InvalidSpaceMarineValueException(
                    "Неккоректное значение категории y десантника с id "
                            + id + ". Существует всего 4 оружия MANREAPER, LIGHTING_CLAW, POWER_FIST: " + values[8]);
        }
        return true;
    }

    @Override
    public boolean setChapter() {
        try {
            if (values.length == 10) {
                chapter = null;
            } else {
                if (values[9] == null)
                    throw new InvalidSpaceMarineValueException(
                            "Неккоректное название части y десантника с id "
                                    + id + ". Часть не должна быть null");
                String chapterName = values[9];
                if (chapterName.trim().equals(""))
                    throw new InvalidSpaceMarineValueException(
                            "Неккоректное название части y десантника с id "
                                    + id + ". Название не может быть пустой строкой");

                String parentLegion = values[10];

                Long marinesCount;
                if (values[11] == null) {
                    marinesCount = null;
                } else {
                    marinesCount = Long.parseLong(values[11]);
                    if (marinesCount > 1000 | marinesCount <= 0)
                        throw new InvalidSpaceMarineValueException(
                                "Неккоректное значение числа солдат в части y десантника с id "
                                        + id + ". Число десантников в части от 1 до 1000: " + marinesCount);
                }

                String world = values[12];
                if (world == null)
                    throw new InvalidSpaceMarineValueException(
                            "Неккоректное значение название мира в части y десантника с id "
                                    + id + ". Мир не может быть null");

                chapter = new Chapter(chapterName, parentLegion, marinesCount, world);
            }
        } catch (NumberFormatException e) {
            throw new InvalidSpaceMarineValueException(
                    "Неккоректное значение числа солдат в части y десантника с id "
                            + id + ". Должно быть введено число: " + values[11]);
        }
        return true;
    }

    @Override
    public SpaceMarine build() {
        return new SpaceMarine
                (
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

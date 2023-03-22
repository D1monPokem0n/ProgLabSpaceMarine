package ru.prog.itmo.command.add;

import ru.prog.itmo.reader.ConsoleReader;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.*;
import ru.prog.itmo.spacemarine.builder.InvalidSpaceMarineValueException;
import ru.prog.itmo.spacemarine.builder.SpaceMarineBuilder;
import ru.prog.itmo.speaker.ConsoleSpeaker;
import ru.prog.itmo.speaker.Speaker;

import static java.time.LocalDateTime.now;

public class SpaceMarineAddBuilder implements SpaceMarineBuilder {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int health; //Значение поля должно быть больше 0
    private long heartCount; //Значение поля должно быть больше 0, Максимальное значение поля: 3
    private AstartesCategory category; //Поле может быть null
    private MeleeWeapon meleeWeapon; //Поле не может быть null
    private Chapter chapter; //Поле может быть null
    Reader reader;
    Speaker speaker;

    public SpaceMarineAddBuilder() {
        reader = new ConsoleReader();
        speaker = new ConsoleSpeaker();
    }

    public boolean setId() throws InvalidSpaceMarineValueException {
        id = SpaceMarine.getLastId();
        SpaceMarine.incrementLastId();
        return true;
    }

    public boolean setName() throws InvalidSpaceMarineValueException {
        speaker.speak("Введите имя космодесантника: ");
        String value = reader.read();
        if (value == null) {
            throw new InvalidSpaceMarineValueException("Имя не может быть пустой строкой.");
        } else {
            name = value;
            return true;
        }
    }

    public boolean setCoordinates() throws InvalidSpaceMarineValueException {
        speaker.speak("Введите местоположение десантника по оси X: ");
        String valueX = reader.read();
        if (valueX == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку.");
        try {
            float coordinateX = Float.parseFloat(valueX);
            if (coordinateX > Float.MAX_VALUE || coordinateX < Float.MIN_VALUE)
                throw new InvalidSpaceMarineValueException("Значение координаты вышло за пределы Java");
            String valueY = reader.read();
            double coordinateY = Double.parseDouble(valueY);
            if (coordinateY > Double.MAX_VALUE || coordinateY < Double.MIN_VALUE)
                throw new InvalidSpaceMarineValueException("Значение координаты вышло за пределы Java");
            if (coordinateY > 431)
                throw new InvalidSpaceMarineValueException("Космодесантник не может находится выше 431 метра по оси Y.");
            coordinates = new Coordinates(coordinateX, coordinateY);
        } catch (NumberFormatException e) {
            throw new InvalidSpaceMarineValueException("Введите число, сударь.");
        }
        return true;
    }

    public boolean setCreationDate() {
        creationDate = now();
        return true;
    }

    public boolean setHealth() throws InvalidSpaceMarineValueException {
        speaker.speak("Введите количество здоровья космодесантника: ");
        String value = reader.read();
        try {
            int checkedValue = Integer.parseInt(value);
            if (checkedValue <= 0)
                throw new InvalidSpaceMarineValueException("Количество здоровья должно быть положительным числом, иначе космодесант press F.");
            health = checkedValue;
        } catch (NumberFormatException e) {
            throw new InvalidSpaceMarineValueException("Введите число, сударь.");
        }
        return true;
    }

    public boolean setHeartCount() throws InvalidSpaceMarineValueException {
        speaker.speak("Введите количество сердец Вашего воина - от 1 до 3.");
        String value = reader.read();
        try {
            heartCount = Long.parseLong(value);
            if (heartCount < 1 || heartCount > 3)
                throw new InvalidSpaceMarineValueException("Количество сердец - от 1 до 3");
        } catch (NumberFormatException e) {
            throw new InvalidSpaceMarineValueException("Введите число, сударь.");
        }
        return true;
    }

    public boolean setCategory() throws InvalidSpaceMarineValueException {
        speaker.speak("""
                Существует 4 категории космодесантников:
                Chaplain, Librarian, Apothecary, Scout
                Введите категорию вашего солдата:\s""");
        String value = reader.read();
        try {
            category = AstartesCategory.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidSpaceMarineValueException("То, что вы написали не является категорией космодесантника");
        }
        return true;
    }

    public boolean setMeleeWeapon() throws InvalidSpaceMarineValueException {
        speaker.speak("""
                Существует 3 вида оружия космодесантников:
                Manreaper, Lighting claw, Power fist
                Введите категорию вашего солдата:\s""");
        String value = reader.read();
        try {
            meleeWeapon = MeleeWeapon.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidSpaceMarineValueException("То, что вы написали не является оружием космодесантника");
        }
        return true;
    }

    public boolean setChapter() throws InvalidSpaceMarineValueException {
        speaker.speak("Создайте часть, к которой относится ваш солдат:");
        String name;
        String parentLegion;
        long marinesCount;
        String world;
        speaker.speak("Введите название части, к которой принадлежит ваш солдат:");
        name = reader.read();
        if (name == null) {
            throw new InvalidSpaceMarineValueException("Имя не может быть пустой строчкой...");
        }
        speaker.speak("Введите название Легиона-родителя: ");
        parentLegion = reader.read();
        speaker.speak("Введите количество солдат в части(макс. 1000): ");
        String value = reader.read();
        try {
            marinesCount = Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new InvalidSpaceMarineValueException("Число вводить надо было...");
        }
        if (marinesCount > 1000) throw new InvalidSpaceMarineValueException("В части должно быть меньше 1000...");
        speaker.speak("Введите название мира, в котором находится ваша часть: ");
        world = reader.read();
        if (world == null) throw new InvalidSpaceMarineValueException("Не бывает мира с никаким названием...");
        chapter = new Chapter(name, parentLegion, marinesCount, world);
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
                chapter);
    }
}

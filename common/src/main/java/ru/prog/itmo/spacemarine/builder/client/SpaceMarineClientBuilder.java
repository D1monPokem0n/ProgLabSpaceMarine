package ru.prog.itmo.spacemarine.builder.client;

import ru.prog.itmo.spacemarine.CreateCancelledException;
import ru.prog.itmo.spacemarine.InvalidSpaceMarineValueException;
import ru.prog.itmo.spacemarine.builder.SpaceMarineBuilder;
import ru.prog.itmo.spacemarine.chapter.builder.ChapterCreator;
import ru.prog.itmo.spacemarine.chapter.builder.client.ChapterClientCreator;
import ru.prog.itmo.spacemarine.coordinates.builder.client.CoordinatesUserCreator;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.AstartesCategory;
import ru.prog.itmo.spacemarine.MeleeWeapon;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.spacemarine.coordinates.Coordinates;
import ru.prog.itmo.speaker.Speaker;

import static java.time.LocalDateTime.now;

public class SpaceMarineClientBuilder implements SpaceMarineBuilder {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int health; //Значение поля должно быть больше 0
    private long heartCount; //Значение поля должно быть больше 0, Максимальное значение поля: 3
    private AstartesCategory category; //Поле может быть null
    private MeleeWeapon meleeWeapon; //Поле не может быть null
    private Chapter chapter; //Поле может быть null
    private Reader reader;
    private Speaker speaker;

    public SpaceMarineClientBuilder(Speaker speaker, Reader reader) {
        this.reader = reader;
        this.speaker = speaker;
    }

    public boolean setId() {
        id = SpaceMarine.getUniqueId();
        if (id < 0)
            throw new CreateCancelledException("Возникли проблемы с id. Возможно вы вышли за пределы Java.");
        return true;
    }

    public boolean setName() {
        speaker.speak("Введите имя космодесантника: ");
        String value = reader.read();
        if (value == null) throw new InvalidSpaceMarineValueException("Имя не может быть пустой строкой.");
        if (value.equals("cancel")) throw new CreateCancelledException();
        name = value;
        return true;
    }

    public boolean setCoordinates() {
        speaker.speak("Введите координаты местоположение вашего космодесантника: ");
        CoordinatesUserCreator creator = new CoordinatesUserCreator(speaker, reader);
        coordinates = creator.create();
        return true;
    }

    public boolean setCreationDate() {
        creationDate = now();
        return true;
    }

    public boolean setHealth() {
        speaker.speak("Введите количество здоровья космодесантника: ");
        String value = reader.read();
        if (value == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        if (value.equals("cancel")) throw new CreateCancelledException();
        try {
            int checkedValue = Integer.parseInt(value);
            if (checkedValue <= 0)
                throw new InvalidSpaceMarineValueException("Количество здоровья должно быть положительным числом, иначе космодесант press F.");
            if (checkedValue > Integer.MAX_VALUE)
                throw new InvalidSpaceMarineValueException("Вы вышли за пределы Java");
            health = checkedValue;
        } catch (NumberFormatException e) {
            throw new InvalidSpaceMarineValueException("Некорректный ввод.");
        }
        return true;
    }

    public boolean setHeartCount() {
        speaker.speak("Введите количество сердец Вашего воина - от 1 до 3.");
        String value = reader.read();
        if (value == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        if (value.equals("cancel")) throw new CreateCancelledException();
        try {
            heartCount = Long.parseLong(value);
            if (heartCount < 1 || heartCount > 3)
                throw new InvalidSpaceMarineValueException("Количество сердец - от 1 до 3");
        } catch (NumberFormatException e) {
            throw new InvalidSpaceMarineValueException("Некорректный ввод.");
        }
        return true;
    }

    public boolean setCategory() {
        speaker.speak("""
                Существует 4 категории космодесантников:
                Chaplain, Librarian, Apothecary, Scout
                Если ваш солдат ещё не получил категорию - введите пустую строку.
                Введите категорию вашего солдата:\s""");
        String value = reader.read();
        if (value == null) {
            category = null;
            return true;
        }
        if (value.equals("cancel")) throw new CreateCancelledException();
        try {
            category = AstartesCategory.getCategory(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidSpaceMarineValueException("То, что вы написали не является категорией космодесантника");
        }
        return true;
    }

    public boolean setMeleeWeapon() {
        speaker.speak("""
                Существует 3 вида оружия космодесантников:
                Manreaper, Lighting claw, Power fist
                Введите категорию вашего солдата:\s""");
        String value = reader.read();
        if (value == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        if (value.equals("cancel")) throw new CreateCancelledException();
        try {
            meleeWeapon = MeleeWeapon.getMeleeWeapon(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidSpaceMarineValueException("То, что вы написали не является оружием космодесантника");
        }
        return true;
    }

    public boolean setChapter() {
        speaker.speak("Относится ли ваш десантник к какой-то части?(Y/N)");
        String answer = reader.read();
        answer = answer == null ? "null" : answer;
        switch (answer) {
            case "Y", "y" -> {
                ChapterCreator creator = new ChapterClientCreator(speaker, reader);
                chapter = creator.create();
                return true;
            }
            case "N", "n" -> {
                chapter = null;
                return true;
            }
            default -> throw new InvalidSpaceMarineValueException("Введите либо Y, либо N, для отмены вводиться cancel");

        }
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

package ru.prog.itmo.spacemarine.chapter.builder.user;

import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.spacemarine.builder.CreateCancelledException;
import ru.prog.itmo.spacemarine.builder.InvalidSpaceMarineValueException;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.spacemarine.chapter.builder.ChapterBuilder;
import ru.prog.itmo.speaker.Speaker;

public class ChapterUserBuilder implements ChapterBuilder {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private String parentLegion;
    private Long marinesCount; //Поле может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 1000
    private String world; //Поле не может быть null
    private final Speaker speaker;
    private final SpaceMarineReader reader;

    public ChapterUserBuilder(Speaker speaker, SpaceMarineReader reader) {
        this.speaker = speaker;
        this.reader = reader;
    }

    @Override
    public boolean setName() {
        speaker.speak("Введите название части, к которой принадлежит ваш солдат:");
        name = reader.read();
        if (name == null) throw new InvalidSpaceMarineValueException("Имя не может быть пустой строчкой...");
        if (name.equals("cancel")) throw new CreateCancelledException();
        return true;
    }

    @Override
    public boolean setParentLegion() {
        speaker.speak("Введите название Легиона-родителя: ");
        String value = reader.read();
        if (value.equals("cancel")) throw new CreateCancelledException();
        parentLegion = value;
        return true;
    }

    @Override
    public boolean setMarinesCount() {
        speaker.speak("Введите количество солдат в части(макс. 1000): ");
        String value = reader.read();
        if (value == null) {
            marinesCount = null;
            return true;
        }
        if (value.equals("cancel")) throw new CreateCancelledException();
        try {
            marinesCount = Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new InvalidSpaceMarineValueException("Число вводить надо было...");
        }
        if (marinesCount > 1000) throw new InvalidSpaceMarineValueException("В части должно быть максимум 1000...");
        return true;
    }

    @Override
    public boolean setWorld() {
        speaker.speak("Введите название мира, в котором находится ваша часть: ");
        String value = reader.read();
        if (value == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        if (value.equals("")) throw new InvalidSpaceMarineValueException("Не бывает мира с никаким названием...");
        if (value.equals("cancel")) throw new CreateCancelledException();
        world = value;
        return true;
    }

    @Override
    public Chapter build() {
        return new Chapter(
                name,
                parentLegion,
                marinesCount,
                world
        );
    }
}

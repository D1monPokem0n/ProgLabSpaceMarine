package ru.prog.itmo.spacemarine.chapter.builder.script;

import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.builder.script.InvalidScriptException;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.spacemarine.chapter.builder.ChapterBuilder;

public class ChapterScriptBuilder implements ChapterBuilder {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private String parentLegion;
    private Long marinesCount; //Поле может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 1000
    private String world; //Поле не может быть null
    private final Reader reader;

    public ChapterScriptBuilder(Reader reader) {
        this.reader = reader;
    }

    public boolean needToCreate() {
        String answer = reader.read();
        answer = answer == null ? "null" : answer;
        if (answer.equals("y") | answer.equals("Y")) return true;
        if (answer.equals("n") | answer.equals("N")) return false;
        throw new InvalidScriptException("Некорректный ввод");
    }

    @Override
    public boolean setName() {
        name = reader.read();
        if (name == null) throw new InvalidScriptException("Имя не может быть пустой строчкой...");
        return true;
    }

    @Override
    public boolean setParentLegion() {
        parentLegion = reader.read();
        return true;
    }

    @Override
    public boolean setMarinesCount() {
        String value = reader.read();
        if (value == null) {
            marinesCount = null;
            return true;
        }
        try {
            marinesCount = Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new InvalidScriptException("Число вводить надо было...");
        }
        if (marinesCount > 1000) throw new InvalidScriptException("В части должно быть максимум 1000...");
        return true;
    }

    @Override
    public boolean setWorld() {
        String value = reader.read();
        if (value == null) throw new InvalidScriptException("Не вводите пустую строку");
        if (value.equals("")) throw new InvalidScriptException("Не бывает мира с никаким названием...");
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

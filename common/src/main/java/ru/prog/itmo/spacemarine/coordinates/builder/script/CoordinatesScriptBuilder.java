package ru.prog.itmo.spacemarine.coordinates.builder.script;

import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.builder.script.InvalidScriptException;
import ru.prog.itmo.spacemarine.coordinates.Coordinates;
import ru.prog.itmo.spacemarine.coordinates.builder.CoordinatesBuilder;

public class CoordinatesScriptBuilder implements CoordinatesBuilder {
    private Float x; //Поле не может быть null
    private Double y; //Максимальное значение поля: 431, Поле не может быть null
    private final Reader reader;

    public CoordinatesScriptBuilder(Reader reader) {
        this.reader = reader;
    }

    @Override
    public boolean setX() {
        String value = reader.read();
        if (value == null) throw new InvalidScriptException("Вы ввели пустую строку.");
        try {
            x = Float.parseFloat(value);
            if (x == Float.NEGATIVE_INFINITY | x == Float.POSITIVE_INFINITY)
                throw new InvalidScriptException("Значение координаты x вышло за пределы Java");
        } catch (NumberFormatException | NullPointerException e) {
            throw new InvalidScriptException("Введите число, сударь.");
        }
        return true;
    }

    @Override
    public boolean setY() {
        String value = reader.read();
        if (value == null) throw new InvalidScriptException("Вы ввели пустую строку.");
        try {
            y = Double.parseDouble(value);
            if (y == Double.POSITIVE_INFINITY | y == Double.NEGATIVE_INFINITY)
                throw new InvalidScriptException("Значение координаты вышло за пределы Java");
            if (y > 431)
                throw new InvalidScriptException("Космодесантник не может находится выше 431 метра по оси Y.");
        } catch (NumberFormatException e) {
            throw new InvalidScriptException("Введите число, сударь.");
        }
        return true;
    }

    @Override
    public Coordinates build() {
        return new Coordinates(x, y);
    }
}

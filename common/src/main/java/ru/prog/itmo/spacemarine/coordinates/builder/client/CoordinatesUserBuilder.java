package ru.prog.itmo.spacemarine.coordinates.builder.client;

import ru.prog.itmo.spacemarine.CreateCancelledException;
import ru.prog.itmo.spacemarine.InvalidSpaceMarineValueException;
import ru.prog.itmo.spacemarine.coordinates.builder.CoordinatesBuilder;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.coordinates.Coordinates;
import ru.prog.itmo.speaker.Speaker;

public class CoordinatesUserBuilder implements CoordinatesBuilder {
    private Float x; //Поле не может быть null
    private Double y; //Максимальное значение поля: 431, Поле не может быть null
    private final Speaker speaker;
    private final Reader reader;

    public CoordinatesUserBuilder(Speaker speaker, Reader reader) {
        this.speaker = speaker;
        this.reader = reader;
    }

    @Override
    public boolean setX() {
        speaker.speak("Введите местоположение десантника по оси X: ");
        String value = reader.read();
        if (value == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку.");
        if (value.equals("cancel")) throw new CreateCancelledException();
        try {
            x = Float.parseFloat(value);
            if (x == Float.NEGATIVE_INFINITY | x == Float.POSITIVE_INFINITY)
                throw new InvalidSpaceMarineValueException("Значение координаты x вышло за пределы Java");
        } catch (NumberFormatException | NullPointerException e) {
            throw new InvalidSpaceMarineValueException("Введите число, сударь.");
        }
        return true;
    }

    @Override
    public boolean setY() {
        speaker.speak("Введите местоположение десантника по оси Y(макс. 431): ");
        String value = reader.read();
        if (value == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку.");
        if (value.equals("cancel")) throw new CreateCancelledException();
        try {
            y = Double.parseDouble(value);
            if (y == Double.POSITIVE_INFINITY | y == Double.NEGATIVE_INFINITY)
                throw new InvalidSpaceMarineValueException("Значение координаты вышло за пределы Java");
            if (y > 431)
                throw new InvalidSpaceMarineValueException("Космодесантник не может находится выше 431 метра по оси Y.");
        } catch (NumberFormatException e) {
            throw new InvalidSpaceMarineValueException("Введите число, сударь.");
        }
        return true;
    }

    @Override
    public Coordinates build() {
        return new Coordinates(x, y);
    }
}

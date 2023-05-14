package ru.prog.itmo.spacemarine.coordinates.builder.client;

import ru.prog.itmo.spacemarine.builder.SpaceMarineCheckSettable;
import ru.prog.itmo.spacemarine.builder.client.SpaceMarineClientCreator;
import ru.prog.itmo.spacemarine.coordinates.builder.CoordinatesBuilder;
import ru.prog.itmo.spacemarine.coordinates.builder.CoordinatesCreator;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.coordinates.Coordinates;
import ru.prog.itmo.speaker.Speaker;

import java.util.ArrayList;

public class CoordinatesUserCreator implements CoordinatesCreator {
    private final CoordinatesBuilder builder;
    private final ArrayList<SpaceMarineCheckSettable> setters;
    private final Speaker speaker;

    public CoordinatesUserCreator(Speaker speaker, Reader reader) {
        builder = new CoordinatesUserBuilder(speaker, reader);
        this.speaker = speaker;
        setters = new ArrayList<>();
        setters.add(builder::setX);
        setters.add(builder::setY);
    }

    @Override
    public Coordinates create() {
        speaker.speak("Вы начали задавать местоположение десантника относительно плоскости XOY.");
        SpaceMarineClientCreator.setAll(setters, speaker);
        return builder.build();
    }
}

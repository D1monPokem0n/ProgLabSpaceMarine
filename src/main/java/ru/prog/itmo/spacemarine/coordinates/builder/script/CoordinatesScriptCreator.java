package ru.prog.itmo.spacemarine.coordinates.builder.script;

import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.spacemarine.builder.CreateCancelledException;
import ru.prog.itmo.spacemarine.coordinates.Coordinates;
import ru.prog.itmo.spacemarine.coordinates.builder.CoordinatesCreator;

public class CoordinatesScriptCreator implements CoordinatesCreator {
    private final CoordinatesScriptBuilder builder;
    public CoordinatesScriptCreator(SpaceMarineReader reader){
        builder = new CoordinatesScriptBuilder(reader);
    }

    @Override
    public Coordinates create() throws CreateCancelledException {
        builder.setX();
        builder.setY();
        return builder.build();
    }
}

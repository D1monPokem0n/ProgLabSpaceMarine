package ru.prog.itmo.spacemarine.coordinates.builder.script;

import ru.prog.itmo.spacemarine.coordinates.builder.CoordinatesCreator;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.coordinates.Coordinates;

public class CoordinatesScriptCreator implements CoordinatesCreator {
    private final CoordinatesScriptBuilder builder;
    public CoordinatesScriptCreator(Reader reader){
        builder = new CoordinatesScriptBuilder(reader);
    }

    @Override
    public Coordinates create() {
        builder.setX();
        builder.setY();
        return builder.build();
    }
}

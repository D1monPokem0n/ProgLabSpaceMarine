package ru.prog.itmo.spacemarine.coordinates.builder;

import ru.prog.itmo.spacemarine.CreateCancelledException;
import ru.prog.itmo.spacemarine.coordinates.Coordinates;

public interface CoordinatesCreator {
    Coordinates create() throws CreateCancelledException;
}

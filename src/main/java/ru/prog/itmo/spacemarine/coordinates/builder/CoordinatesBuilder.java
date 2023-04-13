package ru.prog.itmo.spacemarine.coordinates.builder;

import ru.prog.itmo.spacemarine.coordinates.Coordinates;

public interface CoordinatesBuilder {
    boolean setX();
    boolean setY();
    Coordinates build();
}

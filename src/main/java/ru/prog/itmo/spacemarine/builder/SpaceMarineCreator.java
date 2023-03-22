package ru.prog.itmo.spacemarine.builder;

import ru.prog.itmo.spacemarine.SpaceMarine;

public interface SpaceMarineCreator {
    SpaceMarine create() throws CreateCancelledException;
}

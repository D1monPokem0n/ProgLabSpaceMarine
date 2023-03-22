package ru.prog.itmo.spacemarine.builder;

import ru.prog.itmo.spacemarine.SpaceMarine;

public interface SpaceMarineBuilder {
    boolean setId() throws InvalidSpaceMarineValueException, CreateCancelledException;
    boolean setName() throws InvalidSpaceMarineValueException, CreateCancelledException;
    boolean setCoordinates() throws InvalidSpaceMarineValueException, CreateCancelledException;
    boolean setCreationDate() throws InvalidSpaceMarineValueException, CreateCancelledException;
    boolean setHealth() throws InvalidSpaceMarineValueException, CreateCancelledException;
    boolean setHeartCount() throws InvalidSpaceMarineValueException, CreateCancelledException;
    boolean setCategory() throws InvalidSpaceMarineValueException, CreateCancelledException;
    boolean setMeleeWeapon() throws InvalidSpaceMarineValueException, CreateCancelledException;
    boolean setChapter() throws InvalidSpaceMarineValueException, CreateCancelledException;
    SpaceMarine build();
}

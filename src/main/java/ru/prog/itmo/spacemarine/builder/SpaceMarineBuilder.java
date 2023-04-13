package ru.prog.itmo.spacemarine.builder;

import ru.prog.itmo.spacemarine.SpaceMarine;

public interface SpaceMarineBuilder {
    boolean setId();
    boolean setName();
    boolean setCoordinates();
    boolean setCreationDate();
    boolean setHealth();
    boolean setHeartCount();
    boolean setCategory();
    boolean setMeleeWeapon();
    boolean setChapter();
    SpaceMarine build();
}

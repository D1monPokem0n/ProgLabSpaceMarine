package ru.prog.itmo.command.update;

import ru.prog.itmo.spacemarine.SpaceMarine;

@FunctionalInterface
public interface SpaceMarineUpdatable {
    boolean update(SpaceMarine marine);
}

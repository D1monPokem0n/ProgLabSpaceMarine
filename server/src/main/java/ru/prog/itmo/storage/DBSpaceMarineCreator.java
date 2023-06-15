package ru.prog.itmo.storage;

import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.SpaceMarineCreator;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBSpaceMarineCreator implements SpaceMarineCreator {
    private final DBSpaceMarineBuilder builder;

    public DBSpaceMarineCreator(ResultSet set) {
        builder = new DBSpaceMarineBuilder(set);
    }

    @Override
    public SpaceMarine create() {
        builder.setId();
        builder.setName();
        builder.setCoordinates();
        builder.setCreationDate();
        builder.setHealth();
        builder.setHeartCount();
        builder.setCategory();
        builder.setMeleeWeapon();
        builder.setChapter();
        builder.setUser();
        return builder.build();
    }

    public boolean nextRow() throws SQLException{
        return builder.nextRow();
    }
}

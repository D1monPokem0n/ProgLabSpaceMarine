package ru.prog.itmo.spacemarine.builder.script;

import ru.prog.itmo.spacemarine.builder.SpaceMarineCreator;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.SpaceMarine;

public class SpaceMarineScriptCreator implements SpaceMarineCreator {
    private final SpaceMarineScriptBuilder builder;
    public SpaceMarineScriptCreator(Reader reader){
        builder = new SpaceMarineScriptBuilder(reader);
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
        return builder.build();
    }
}

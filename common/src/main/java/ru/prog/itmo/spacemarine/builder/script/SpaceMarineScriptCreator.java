package ru.prog.itmo.spacemarine.builder.script;

import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.SpaceMarineCreator;

public class SpaceMarineScriptCreator implements SpaceMarineCreator {
    private final SpaceMarineScriptBuilder builder;
    public SpaceMarineScriptCreator(SpaceMarineReader reader){
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

package ru.prog.itmo.spacemarine.builder.file;

import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.CreateCancelledException;
import ru.prog.itmo.spacemarine.builder.SpaceMarineCreator;

import java.util.Scanner;

public class SpaceMarineFileCreator implements SpaceMarineCreator {
    private SpaceMarineFileBuilder builder;
    private Scanner fileReader;

    public SpaceMarineFileCreator(Scanner scanner){
        fileReader = scanner;
        builder = new SpaceMarineFileBuilder();
    }

    public void setNextValues(){
        if (!fileReader.hasNextLine()) throw new CreateCancelledException("Файл закончился");
        String[] values = fileReader.nextLine().split(",");
        builder.setNextValues(values);
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

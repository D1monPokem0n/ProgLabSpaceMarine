package ru.prog.itmo.spacemarine.builder.user;

import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.InvalidSpaceMarineValueException;
import ru.prog.itmo.spacemarine.builder.SpaceMarineBuilder;
import ru.prog.itmo.spacemarine.builder.SpaceMarineCheckSettable;
import ru.prog.itmo.spacemarine.builder.SpaceMarineCreator;
import ru.prog.itmo.spacemarine.builder.user.SpaceMarineUserBuilder;
import ru.prog.itmo.speaker.Speaker;

import java.util.ArrayList;

public class SpaceMarineUserCreator implements SpaceMarineCreator {
    private final SpaceMarineBuilder builder;
    private ArrayList<SpaceMarineCheckSettable> setters;
    private Speaker speaker;

    public SpaceMarineUserCreator(Speaker speaker, SpaceMarineReader reader) {
        builder = new SpaceMarineUserBuilder(speaker, reader);
        this.speaker = speaker;
        settersInit();
    }

    private void settersInit() {
        setters = new ArrayList<>();
        setters.add(builder::setId);
        setters.add(builder::setName);
        setters.add(builder::setCoordinates);
        setters.add(builder::setCreationDate);
        setters.add(builder::setHealth);
        setters.add(builder::setHeartCount);
        setters.add(builder::setCategory);
        setters.add(builder::setMeleeWeapon);
        setters.add(builder::setChapter);
    }

    public static void setAll(ArrayList<SpaceMarineCheckSettable> setters, Speaker speaker) {
        for (SpaceMarineCheckSettable setter : setters) {
            boolean isSettingDone = false;
            while (!isSettingDone) {
                try {
                    isSettingDone = setter.checkSet();
                } catch (InvalidSpaceMarineValueException e) {
                    speaker.speak(e.getMessage());
                }
            }
        }
    }

    public SpaceMarine create(){
        speaker.speak("Вы начали процесс создания нового кибернетического организма.\nДля отмены введите команду: cancel");
        setAll(setters, speaker);
        return builder.build();
    }
}

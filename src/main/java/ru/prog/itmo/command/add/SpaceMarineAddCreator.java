package ru.prog.itmo.command.add;

import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.*;
import ru.prog.itmo.speaker.ConsoleSpeaker;
import ru.prog.itmo.speaker.Speaker;

import java.util.ArrayList;

public class SpaceMarineAddCreator implements SpaceMarineCreator {
    private final SpaceMarineBuilder builder;
    private ArrayList<SpaceMarineCheckSettable> setters;

    public SpaceMarineAddCreator() {
        builder = new SpaceMarineAddBuilder();
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

    public SpaceMarine create() throws CreateCancelledException {
        Speaker speaker = new ConsoleSpeaker();
        speaker.speak("Вы начали процесс создания нового кибернетического организма.\nДля отмены введите команду: cancel");
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
        return builder.build();
    }
}

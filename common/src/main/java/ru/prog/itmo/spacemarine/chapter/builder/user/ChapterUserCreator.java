package ru.prog.itmo.spacemarine.chapter.builder.user;

import ru.prog.itmo.spacemarine.builder.SpaceMarineCheckSettable;
import ru.prog.itmo.spacemarine.builder.user.SpaceMarineUserCreator;
import ru.prog.itmo.spacemarine.chapter.builder.ChapterBuilder;
import ru.prog.itmo.spacemarine.chapter.builder.ChapterCreator;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.speaker.ConsoleSpeaker;
import ru.prog.itmo.speaker.Speaker;

import java.util.ArrayList;

public class ChapterUserCreator implements ChapterCreator {
    private final ChapterBuilder builder;
    private final ArrayList<SpaceMarineCheckSettable> setters;

    public ChapterUserCreator(Speaker speaker, Reader reader){
        builder = new ChapterUserBuilder(speaker, reader);
        setters = new ArrayList<>();
        setters.add(builder::setName);
        setters.add(builder::setParentLegion);
        setters.add(builder::setMarinesCount);
        setters.add(builder::setWorld);
    }

    @Override
    public Chapter create() {
        Speaker speaker = new ConsoleSpeaker();
        speaker.speak("Вы начали задавать данные о части легиона Космического Десанта");
        SpaceMarineUserCreator.setAll(setters, speaker);
        return builder.build();
    }
}

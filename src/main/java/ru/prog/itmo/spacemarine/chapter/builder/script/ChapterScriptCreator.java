package ru.prog.itmo.spacemarine.chapter.builder.script;

import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.spacemarine.chapter.builder.ChapterCreator;

public class ChapterScriptCreator implements ChapterCreator {
    private final ChapterScriptBuilder builder;
    public ChapterScriptCreator(SpaceMarineReader reader){
        builder = new ChapterScriptBuilder(reader);
    }
    @Override
    public Chapter create() {
        if (builder.needToCreate()) {
            builder.setName();
            builder.setParentLegion();
            builder.setMarinesCount();
            builder.setWorld();
            return builder.build();
        }
        return null;
    }
}

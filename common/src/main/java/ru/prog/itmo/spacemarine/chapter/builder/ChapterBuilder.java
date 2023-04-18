package ru.prog.itmo.spacemarine.chapter.builder;

import ru.prog.itmo.spacemarine.chapter.Chapter;

public interface ChapterBuilder {
    boolean setName();
    boolean setParentLegion();
    boolean setMarinesCount();
    boolean setWorld();
    Chapter build();
}

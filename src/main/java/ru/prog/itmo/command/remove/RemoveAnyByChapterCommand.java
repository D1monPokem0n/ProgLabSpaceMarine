package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.command.StorageIOCommand;
import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.CreateCancelledException;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.spacemarine.chapter.builder.ChapterCreator;
import ru.prog.itmo.spacemarine.chapter.builder.client.ChapterClientCreator;
import ru.prog.itmo.speaker.Speaker;

public class RemoveAnyByChapterCommand extends StorageIOCommand implements UserAsking {
    public RemoveAnyByChapterCommand(Storage storage, Speaker speaker, SpaceMarineReader reader) {
        super(storage, speaker, reader);
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции один элемент, значение поля chapter которого эквивалентно заданному";
    }

    @Override
    public void execute() {
        super.execute();
        ChapterCreator creator = new ChapterClientCreator(getSpeaker(), getReader());
        getSpeaker().speak("Задайте данные о части, из которой вы хотите удалить космодесантника");
        try {
            Chapter chapter = creator.create();
            SpaceMarine searchableMarine = null;
            for (SpaceMarine marine : getStorage().getHashSet()) {
                if (marine.getChapter().equals(chapter)) {
                    searchableMarine = marine;
                    break;
                }
            }
            if (getStorage().remove(searchableMarine)) {
                getSpeaker().speak("Из части удалён десантник:\n" + searchableMarine);
            } else {
                getSpeaker().speak("Нет десантников в данной части");
            }
        } catch (CreateCancelledException e) {
            getSpeaker().speak("Вам не удалось задать часть.\nУдаление отменено.");
        }
    }
}

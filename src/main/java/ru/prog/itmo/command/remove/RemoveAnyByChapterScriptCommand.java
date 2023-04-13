package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.command.StorageIOCommand;
import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.spacemarine.chapter.builder.script.ChapterScriptCreator;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public class RemoveAnyByChapterScriptCommand extends StorageIOCommand implements ScriptExecutable {
    public RemoveAnyByChapterScriptCommand(Storage storage, Speaker speaker, SpaceMarineReader reader) {
        super(storage, speaker, reader);
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции один элемент, значение поля chapter которого эквивалентно заданному";
    }

    @Override
    public void execute() {
        super.execute();
        ChapterScriptCreator creator = new ChapterScriptCreator(getReader());
        Chapter chapter = creator.create();
        boolean haveDeleted = false;
        for (SpaceMarine marine : getStorage().getHashSet()) {
            if (marine.getChapter().equals(chapter)) {
                getStorage().remove(marine);
                getSpeaker().speak("Из части удалён десантник:\n" + marine);
                haveDeleted = true;
                break;
            }
        }
        if (!haveDeleted) getSpeaker().speak("Нет десантников в данной части");
    }
}

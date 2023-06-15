package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.connection.*;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.builder.script.InvalidScriptException;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.spacemarine.chapter.builder.script.ChapterScriptCreator;
import ru.prog.itmo.speaker.Speaker;

public class RemoveAnyByChapterScriptCommand extends ServerIOCommand implements ScriptExecutable {
    public RemoveAnyByChapterScriptCommand(SendModule sendModule,
                                           ReceiveModule receiveModule,
                                           Speaker speaker,
                                           Reader reader) {
        super("remove_any_by_chapter", sendModule, receiveModule, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        try {
            var chapter = createChapter();
            executeRemoveRequest(chapter);
        } catch (InvalidConnectionException e) {
            throw new InvalidScriptException("Проблемы с соединением...");
        }
    }

    private void executeRemoveRequest(Chapter chapter){
        sendModule().submitSending(new Request<>(COMMAND_TYPE, chapter, true));
        Response<?> response = receiveModule().getResponse();
        if (response.getData() != null)
            speaker().speak((String) response.getData());
        else speaker().speak(response.getComment());
    }

    private Chapter createChapter(){
        ChapterScriptCreator creator = new ChapterScriptCreator(reader());
        return creator.create();
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции один элемент, значение поля chapter которого эквивалентно заданному";
    }
}

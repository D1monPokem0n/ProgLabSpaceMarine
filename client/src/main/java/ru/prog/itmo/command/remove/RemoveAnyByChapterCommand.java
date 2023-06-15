package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.*;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.CreateCancelledException;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.spacemarine.chapter.builder.ChapterCreator;
import ru.prog.itmo.spacemarine.chapter.builder.client.ChapterClientCreator;
import ru.prog.itmo.speaker.Speaker;

public class RemoveAnyByChapterCommand extends ServerIOCommand implements UserAsking {
    public RemoveAnyByChapterCommand(SendModule sendModule,
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
        } catch (CreateCancelledException e) {
            speaker().speak("Вам не удалось задать часть.\nУдаление отменено.");
        } catch (InvalidConnectionException e){
            speaker().speak("Проблемы с соединением...");
        }
    }

    private void executeRemoveRequest(Chapter chapter){
        sendModule().submitSending(new Request<>(COMMAND_TYPE, chapter));
        Response<?> response = receiveModule().getResponse();
        if (response.getData() != null)
            speaker().speak((String) response.getData());
        else speaker().speak(response.getComment());
    }

    private Chapter createChapter(){
        ChapterCreator creator = new ChapterClientCreator(speaker(), reader());
        speaker().speak("Задайте данные о части, из которой вы хотите удалить космодесантника");
        return creator.create();
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции один элемент, значение поля chapter которого эквивалентно заданному";
    }
}

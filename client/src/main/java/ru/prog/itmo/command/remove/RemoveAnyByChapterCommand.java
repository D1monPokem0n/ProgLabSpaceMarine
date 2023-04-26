package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.server.ConnectionModule;
import ru.prog.itmo.server.InvalidConnectionException;
import ru.prog.itmo.spacemarine.CreateCancelledException;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.spacemarine.chapter.builder.ChapterCreator;
import ru.prog.itmo.spacemarine.chapter.builder.user.ChapterUserCreator;
import ru.prog.itmo.speaker.Speaker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

public class RemoveAnyByChapterCommand extends ServerIOCommand implements UserAsking {
    public RemoveAnyByChapterCommand(ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        super("remove_any_by_chapter", connectionModule, speaker, reader);
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции один элемент, значение поля chapter которого эквивалентно заданному";
    }

    @Override
    public void execute() {
        super.execute();
        ChapterCreator creator = new ChapterUserCreator(speaker(), reader());
        speaker().speak("Задайте данные о части, из которой вы хотите удалить космодесантника");
        try {
            Chapter chapter = creator.create();
            Request<Chapter> request = new Request<>(COMMAND_TYPE, chapter);
            ByteBuffer toServer = serializeRequest(request);
            connectionModule().sendRequest(toServer);
            ByteBuffer fromServer = connectionModule().receiveResponse();
            ObjectInputStream inputStream = getDeserializedInputStream(fromServer);
            @SuppressWarnings("unchecked")
            Response<String> response = (Response<String>) inputStream.readObject();
            speaker().speak(response.getData());
        } catch (CreateCancelledException e) {
            speaker().speak("Вам не удалось задать часть.\nУдаление отменено.");
        } catch (IOException | ClassNotFoundException | InvalidConnectionException e){
            speaker().speak("Проблемы с соединением...");
        }
    }
}

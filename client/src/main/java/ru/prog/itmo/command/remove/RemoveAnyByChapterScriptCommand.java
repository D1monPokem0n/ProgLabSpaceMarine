package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.builder.script.InvalidScriptException;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.spacemarine.chapter.builder.script.ChapterScriptCreator;
import ru.prog.itmo.speaker.Speaker;

import java.nio.ByteBuffer;

public class RemoveAnyByChapterScriptCommand extends ServerIOCommand implements ScriptExecutable {
    public RemoveAnyByChapterScriptCommand(ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        super("remove_any_by_chapter", connectionModule, speaker, reader);
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции один элемент, значение поля chapter которого эквивалентно заданному";
    }

    @Override
    public void execute() {
        super.execute();
        ChapterScriptCreator creator = new ChapterScriptCreator(reader());
        Chapter chapter = creator.create();
        try {
            Request<Chapter> request = new Request<>(COMMAND_TYPE, chapter, true);
            ByteBuffer toServer = serializeRequest(request);
            connectionModule().sendRequest(toServer);
            ByteBuffer fromServer = connectionModule().receiveResponse();
            Response<?> response = getDeserializedResponse(fromServer);
            if (response.getData() != null)
                speaker().speak((String) response.getData());
            else speaker().speak(response.getComment());
        } catch (InvalidConnectionException e) {
            throw new InvalidScriptException("Проблемы с соединением...");
        }
    }
}

package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.command.StorageIOCommand;
import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.speaker.Speaker;

public class ClearCommand extends StorageIOCommand implements UserAsking {
    public ClearCommand(Storage storage, Speaker speaker, SpaceMarineReader reader) {
        super(storage, speaker, reader);
    }

    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }

    @Override
    public void execute() {
        super.execute();
        getSpeaker().speak("Вы уверены, что хотите удалить?) Y/N");
        String answer = getReader().read();
        answer = answer == null ? "null" : answer;
        if (answer.equals("Y")) {
            getStorage().clear();
        } else if (answer.equals("N")) {
            getSpeaker().speak("Ну нет, так нет.");
        } else {
            getSpeaker().speak("Вы ввели какую-то фигню, мы не уверены в вашей компетенции по удалению солдат Космического Десанта.");
        }
    }
}

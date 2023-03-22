package ru.prog.itmo.command;

import ru.prog.itmo.Storage;
import ru.prog.itmo.reader.ConsoleReader;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.ConsoleSpeaker;
import ru.prog.itmo.speaker.Speaker;

public class ClearCommand extends StorageCommand{
    public ClearCommand(Storage storage) {
        super(storage);
    }

    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }

    @Override
    public void execute() {
        super.execute();
        Speaker speaker = new ConsoleSpeaker();
        Reader reader = new ConsoleReader();
        speaker.speak("Вы уверены, что хотите удалить?) Да/Нет");
        String answer = reader.read();
        if (answer.equals("Да")){
            getStorage().getHashSet().clear();
        } else if(answer.equals("Нет")){
            speaker.speak("Ну нет, так нет.");
        } else {
            speaker.speak("Вы ввели какую-то фигню, мы не уверены в вашей компетенции по удалению солдат Космического Десанта.");
        }
    }
}

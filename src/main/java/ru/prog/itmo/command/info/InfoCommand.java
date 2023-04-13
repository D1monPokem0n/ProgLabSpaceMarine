package ru.prog.itmo.command.info;

import ru.prog.itmo.command.StorageOCommand;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public class InfoCommand extends StorageOCommand {

    public InfoCommand(Storage storage, Speaker speaker) {
        super(storage, speaker);
    }

    @Override
    public void execute() {
        super.execute();
        getSpeaker().speak("Количество элементов в коллекции: " + getStorage().getInfo().getElementsCount() +
                "\nДата инициализации коллекции: " + getStorage().getInfo().getCreationDate() +
                "\nТип коллекции: " + getStorage().getInfo().getCollectionType() +
                "\nТип файла коллекции: " + getStorage().getInfo().getFileType());
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }
}

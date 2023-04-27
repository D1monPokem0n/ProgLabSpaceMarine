package ru.prog.itmo.command;

import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.NotWritableFileException;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.storage.WrongStorageFileException;


public class SaveCommand extends StorageIOCommand {
    public SaveCommand(Storage storage, Speaker speaker, Reader reader) {
        super(storage, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        try {
            storage().getFile().save(storage());
            getSpeaker().speak("Файл успешно записан.");
        } catch (NotWritableFileException | WrongStorageFileException e){
            getSpeaker().speak(e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "сохранить коллекцию в файл.";
    }
}

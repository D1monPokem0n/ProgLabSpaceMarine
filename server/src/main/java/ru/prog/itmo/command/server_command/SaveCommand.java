package ru.prog.itmo.command.server_command;

import ru.prog.itmo.command.Command;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.storage.WrongDataBaseException;


public class SaveCommand implements Command {
    private final Storage storage;
    private final Speaker speaker;
    public SaveCommand(Storage storage, Speaker speaker) {
        this.storage = storage;
        this.speaker = speaker;
    }

    @Override
    public void execute() {
       try {
            storage.save();
            speaker.speak("Все изменения сохранены.\n");
        } catch (WrongDataBaseException e){
            speaker.speak(e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "сохранить коллекцию в БД.";
    }
}

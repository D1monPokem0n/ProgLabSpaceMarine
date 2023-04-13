package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.command.StorageOCommand;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public class ClearScriptCommand extends StorageOCommand implements ScriptExecutable {
    public ClearScriptCommand(Storage storage, Speaker speaker) {
        super(storage, speaker);
    }

    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }

    @Override
    public void execute() {
        super.execute();
        getStorage().clear();
    }
}

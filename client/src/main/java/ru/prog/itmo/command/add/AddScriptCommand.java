package ru.prog.itmo.command.add;

import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.server.ConnectionModule;
import ru.prog.itmo.speaker.Speaker;

public class AddScriptCommand extends AbstractAddScriptCommand implements ScriptExecutable {
    public AddScriptCommand(ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        super("add", connectionModule, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию";
    }
}
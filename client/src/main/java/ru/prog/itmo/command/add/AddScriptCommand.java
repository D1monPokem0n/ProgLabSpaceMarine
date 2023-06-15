package ru.prog.itmo.command.add;

import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.connection.ReceiveModule;
import ru.prog.itmo.connection.SendModule;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;

public class AddScriptCommand extends AbstractAddScriptCommand implements ScriptExecutable {
    public AddScriptCommand(SendModule sendModule,
                      ReceiveModule receiveModule,
                      Speaker speaker,
                      Reader reader) {
        super("add", sendModule, receiveModule, speaker, reader);
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
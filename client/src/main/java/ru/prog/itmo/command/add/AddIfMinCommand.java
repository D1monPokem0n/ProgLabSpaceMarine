package ru.prog.itmo.command.add;

import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.speaker.Speaker;

public class AddIfMinCommand extends AbstractAddCommand implements UserAsking {
    public AddIfMinCommand(ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        super("add_if_min", connectionModule, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
    }
}

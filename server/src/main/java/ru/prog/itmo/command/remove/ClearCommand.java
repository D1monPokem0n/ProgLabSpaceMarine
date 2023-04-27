package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ClientOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public class ClearCommand extends ClientOCommand implements UserAsking {
    public ClearCommand(Storage storage, ConnectionModule connectionModule, Speaker speaker) {
        super(storage, connectionModule, speaker);
    }

    @Override
    public void execute() {
        super.execute();
        Response<String> response = new Response<>();
        storage().clear();
        response.setData("Коллекция очищена");
        connectionModule().sendResponse(response);
    }

    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }
}

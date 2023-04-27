package ru.prog.itmo.command.info;

import ru.prog.itmo.StorageInfo;
import ru.prog.itmo.command.ClientOCommand;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public class InfoCommand extends ClientOCommand {

    public InfoCommand(Storage storage, ConnectionModule connectionModule, Speaker speaker) {
        super(storage, connectionModule, speaker);
    }

    @Override
    public void execute() {
        super.execute();
        Response<StorageInfo> response = new Response<>(storage().getInfo());
        connectionModule().sendResponse(response);
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }
}

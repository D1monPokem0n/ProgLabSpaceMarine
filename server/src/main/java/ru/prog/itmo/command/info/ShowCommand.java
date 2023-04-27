package ru.prog.itmo.command.info;

import ru.prog.itmo.command.ClientOCommand;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

import java.util.List;

public class ShowCommand extends ClientOCommand {
    public ShowCommand(Storage storage, ConnectionModule connectionModule, Speaker speaker) {
        super(storage, connectionModule, speaker);
    }

    @Override
    public void execute() {
        super.execute();
        Response<List<SpaceMarine>> response = new Response<>();
        List<SpaceMarine> marinesList;
        marinesList = storage().getStream().sorted().toList();
        if (marinesList.size() == 0)
            response.setComment("В коллекции нет элементов");
        response.setData(marinesList);
        connectionModule().sendResponse(response);
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении.";
    }
}

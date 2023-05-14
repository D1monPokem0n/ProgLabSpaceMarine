package ru.prog.itmo.command.info;

import ru.prog.itmo.command.ClientOCommand;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

public class ShowCommand extends ClientOCommand {
    public ShowCommand(Storage storage, ConnectionModule connectionModule, Speaker speaker) {
        super(storage, connectionModule, speaker);
    }

    @Override
    public void execute() {
        super.execute();
        try {
            /*Response<String> response = new Response<>();
            StringBuilder builder = new StringBuilder();
            for (SpaceMarine marine : storage().sort()){
                builder.append(marine.toString());
                builder.append("\n");
            }
            response.setData(builder.toString());*/
            Response<Integer> response = new Response<>();
            if (storage().getHashSet().size() != 0) {
                response.setData(storage().getHashSet().size());
                connectionModule().sendResponse(response);
                for (SpaceMarine currentMarine : storage().getHashSet()) {
                    Response<SpaceMarine> marineResponse = new Response<>();
                    marineResponse.setData(currentMarine);
                    connectionModule().sendResponse(marineResponse);
                }
            } else {
                response.setComment("Хранилище пустое.");
                connectionModule().sendResponse(response);
            }
        } catch (InvalidConnectionException e) {
            speaker().speak("Не удалось отправить ответ клиенту.");
        }
    }


    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении.";
    }
}

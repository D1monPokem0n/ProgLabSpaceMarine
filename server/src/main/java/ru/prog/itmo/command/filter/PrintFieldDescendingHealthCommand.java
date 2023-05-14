package ru.prog.itmo.command.filter;

import ru.prog.itmo.command.ClientIOCommand;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

import java.util.Comparator;
import java.util.List;

public class PrintFieldDescendingHealthCommand extends ClientIOCommand {
    public PrintFieldDescendingHealthCommand(Storage storage, ConnectionModule connectionModule, Speaker speaker, Reader reader) {
        super(storage, connectionModule, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        Response<String> response = new Response<>();
        List<String> collectionToSend;
        collectionToSend = storage().getStream()
                .sorted(Comparator.comparing(SpaceMarine::getHealth).reversed())
                .map(marine -> "Показатель здоровья десантника "
                        + marine.getName()
                        + " под номером "
                        + marine.getId()
                        + " равен: "
                        + marine.getHealth()

                )
                .toList();
        StringBuilder stringBuilder = new StringBuilder();
        for (String token : collectionToSend) {
            stringBuilder.append(token);
            stringBuilder.append('\n');
        }
        response.setData(stringBuilder.toString());
        if (collectionToSend.size() == 0) {
            response.setComment("В коллекции нет элементов");
        }
        connectionModule().sendResponse(response);
    }

    @Override
    public String getDescription() {
        return "вывести значения поля health всех элементов в порядке убывания.";
    }
}

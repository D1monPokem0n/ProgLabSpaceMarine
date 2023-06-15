package ru.prog.itmo.command.filter;

import ru.prog.itmo.command.ClientCommand;
import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.storage.Storage;

import java.net.SocketAddress;
import java.util.Comparator;
import java.util.List;

public class PrintFieldDescendingHealthCommand extends ClientCommand {
    public PrintFieldDescendingHealthCommand(Storage storage, ConnectionManager connectionManager) {
        super(storage, connectionManager);
    }

    @Override
    public void execute(SocketAddress address) {
        super.execute(address);
        Response<String> response = new Response<>();
        if (storage().getHashSet().size() != 0) {
            var collectionToSend = getCollectionToSend();
            var stringToSend = getStringToSend(collectionToSend);
            response.setData(stringToSend);
        } else response.setComment("В коллекции нет элементов");
        connectionManager().putResponse(address, response);
    }

    private String getStringToSend(List<String> collectionToSend) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String token : collectionToSend) {
            stringBuilder.append(token);
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    private List<String> getCollectionToSend() {
        StringBuilder stringBuilder = new StringBuilder();
        return storage().getStream()
                .sorted(Comparator.comparing(SpaceMarine::getHealth)
                        .reversed())
                .map(marine -> getFormatedString(stringBuilder, marine))
                .toList();
    }

    private String getFormatedString(StringBuilder stringBuilder, SpaceMarine marine) {
        stringBuilder.append("Показатель здоровья десантника ");
        stringBuilder.append(marine.getName());
        stringBuilder.append(" под номером ");
        stringBuilder.append(marine.getId());
        stringBuilder.append(" равен: ");
        stringBuilder.append(marine.getHealth());
        return stringBuilder.toString();
    }

    @Override
    public String getDescription() {
        return "вывести значения поля health всех элементов в порядке убывания.";
    }
}

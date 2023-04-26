package ru.prog.itmo.command.filter;

import ru.prog.itmo.command.ServerOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.server.ConnectionModule;
import ru.prog.itmo.server.InvalidConnectionException;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;


public class MaxByMeleeWeaponCommand extends ServerOCommand implements UserAsking {
    public MaxByMeleeWeaponCommand(ConnectionModule connectionModule, Speaker speaker) {
        super("max_by_melee_weapon", connectionModule, speaker);
    }

    @Override
    public String getDescription() {
        return "вывести любой объект из коллекции, значение поля meleeWeapon которого является максимальным";
    }

    @Override
    public void execute() {
        super.execute();
        try {
            Request<Object> request = new Request<>(COMMAND_TYPE, null);
            ByteBuffer toServer = serializeRequest(request);
            connectionModule().sendRequest(toServer);
            ByteBuffer fromServer = connectionModule().receiveResponse();
            ObjectInputStream inputStream = getDeserializedInputStream(fromServer);
            @SuppressWarnings("unchecked")
            Response<SpaceMarine> response = (Response<SpaceMarine>) inputStream.readObject();
            speaker().speak("Минимальный десантник в коллекции:\n"+response.getData());
        } catch (IOException | ClassNotFoundException | InvalidConnectionException e){
            speaker().speak("Проблемы с соединением...");
        }
    }
}

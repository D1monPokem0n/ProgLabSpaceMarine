package ru.prog.itmo.command.filter;

import ru.prog.itmo.command.ServerOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;

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
            Response<?> response = getDeserializedResponse(fromServer);
            if (response.getData() != null) {
                SpaceMarine marine = (SpaceMarine) response.getData();
                speaker().speak("Максимальный по оружию десантник в коллекции:\n" + marine);
            } else {
                speaker().speak(response.getComment());
            }
        } catch (InvalidConnectionException e){
            speaker().speak("Проблемы с соединением...");
        }
    }
}

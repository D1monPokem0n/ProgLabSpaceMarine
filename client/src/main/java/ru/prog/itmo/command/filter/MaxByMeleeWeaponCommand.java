package ru.prog.itmo.command.filter;

import ru.prog.itmo.command.ServerOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.ReceiveModule;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.SendModule;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;


public class MaxByMeleeWeaponCommand extends ServerOCommand implements UserAsking {
    public MaxByMeleeWeaponCommand(SendModule sendModule,
                                   ReceiveModule receiveModule,
                                   Speaker speaker) {
        super("max_by_melee_weapon", sendModule, receiveModule, speaker);
    }

    @Override
    public void execute() {
        super.execute();
        sendModule().submitSending(new Request<>(COMMAND_TYPE, null));
        var response = receiveModule().getResponse();
        if (response.getData() != null) {
            SpaceMarine marine = (SpaceMarine) response.getData();
            speaker().speak("Максимальный по оружию десантник в коллекции:\n" + marine);
        } else
            speaker().speak(response.getComment());

    }

    @Override
    public String getDescription() {
        return "вывести любой объект из коллекции, значение поля meleeWeapon которого является максимальным";
    }
}

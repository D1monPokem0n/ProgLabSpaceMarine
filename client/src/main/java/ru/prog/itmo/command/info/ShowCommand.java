package ru.prog.itmo.command.info;

import ru.prog.itmo.command.ServerOCommand;
import ru.prog.itmo.connection.*;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.speaker.Speaker;

import java.util.ArrayList;

public class ShowCommand extends ServerOCommand {
    public ShowCommand(SendModule sendModule,
                       ReceiveModule receiveModule,
                       Speaker speaker) {
        super("show", sendModule, receiveModule, speaker);
    }

    @Override
    public void execute() {
        super.execute();
        try {
            sendModule().submitSending(new Request<>(COMMAND_TYPE, null));
            Response<?> response = receiveModule().getResponse();
            if (response.getData() != null)
                showMarines(response.getData());
            else
                speaker().speak(response.getComment());
        } catch (InvalidConnectionException | ClassCastException e) {
            speaker().speak("Проблемы с соединением...");
        }
    }

    private void showMarines(Object receivedData) {
        @SuppressWarnings("unchecked")
        ArrayList<SpaceMarine> marines = (ArrayList<SpaceMarine>) receivedData;
        for (SpaceMarine marine : marines) {
            speaker().speak(marine.toString());
        }
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении.";
    }
}

package ru.prog.itmo.command.info;

import ru.prog.itmo.StorageInfo;
import ru.prog.itmo.command.ServerOCommand;
import ru.prog.itmo.connection.*;
import ru.prog.itmo.speaker.Speaker;

public class InfoCommand extends ServerOCommand {

    public InfoCommand(SendModule sendModule,
                       ReceiveModule receiveModule,
                       Speaker speaker) {
        super("info", sendModule, receiveModule, speaker);
    }

    @Override
    public void execute() {
        super.execute();
        try {
            sendModule().submitSending(new Request<>(COMMAND_TYPE, null));
            Response<?> response = receiveModule().getResponse();
            StorageInfo info = (StorageInfo) response.getData();
            speaker().speak("Количество элементов в коллекции: " + info.getElementsCount() +
                    "\nТип коллекции: " + info.getCollectionType() +
                    "\nТип файла коллекции: " + info.getDataBaseName());
        } catch (InvalidConnectionException | ClassCastException e){
            speaker().speak("Проблемы с соединением...");
        }
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }
}

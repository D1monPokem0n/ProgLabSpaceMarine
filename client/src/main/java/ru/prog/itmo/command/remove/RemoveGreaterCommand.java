package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.*;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.CreateCancelledException;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.client.SpaceMarineClientCreator;
import ru.prog.itmo.speaker.Speaker;

public class RemoveGreaterCommand extends ServerIOCommand implements UserAsking {
    public RemoveGreaterCommand(SendModule sendModule,
                                ReceiveModule receiveModule,
                                Speaker speaker,
                                Reader reader) {
        super("remove_greater", sendModule, receiveModule, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        try {
            var maxMarine = createMaxMarine();
            executeRemoveRequest(maxMarine);
        } catch (CreateCancelledException e) {
            speaker().speak("Вам не удалось задать максимального десантника. \nУдаление отменено.");
        } catch (InvalidConnectionException e) {
            speaker().speak("Проблемы с соединением...");
        }
    }

    private void executeRemoveRequest(SpaceMarine maxMarine){
        sendModule().submitSending(new Request<>(COMMAND_TYPE, maxMarine));
        Response<?> response = receiveModule().getResponse();
        if (response.getData() != null)
            speaker().speak((String) response.getData());
        else speaker().speak(response.getComment());
    }

    private SpaceMarine createMaxMarine(){
        SpaceMarineClientCreator creator = new SpaceMarineClientCreator(speaker(), reader());
        speaker().speak("Задайтие десантника, который доллжен стать максимальным в базе. \nВсе, кто выше будут удалены.");
        return creator.create();
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, превышающие заданный";
    }
}

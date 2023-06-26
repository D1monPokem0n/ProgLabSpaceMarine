package ru.prog.itmo.command.add;

import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.connection.*;
import ru.prog.itmo.control.Controller;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.CreateCancelledException;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.client.SpaceMarineClientCreator;
import ru.prog.itmo.speaker.Speaker;

public abstract class AbstractAddCommand extends ServerIOCommand {
    public AbstractAddCommand(String commandType,
                              SendModule sendModule,
                              ReceiveModule receiveModule,
                              Speaker speaker,
                              Reader reader) {
        super(commandType, sendModule, receiveModule, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        try {
            SpaceMarineClientCreator creator = new SpaceMarineClientCreator(speaker(), reader());
            SpaceMarine marine = creator.create();
            marine.setOwnerUser(Controller.getUser().getLogin());
            sendModule().submitSending(new Request<>(COMMAND_TYPE, marine));
            Response<?> response = receiveModule().getResponse();
            if (response.getData() != null)
                speaker().speak((String) response.getData());
            else throw new InvalidConnectionException(response.getComment());
        } catch (CreateCancelledException | InvalidConnectionException  e) {
            speaker().speak("Проблемы с соединением...");
        }
    }
}

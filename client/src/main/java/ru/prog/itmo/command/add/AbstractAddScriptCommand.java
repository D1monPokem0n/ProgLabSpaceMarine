package ru.prog.itmo.command.add;

import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.connection.*;
import ru.prog.itmo.control.Controller;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.CreateCancelledException;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.script.InvalidScriptException;
import ru.prog.itmo.spacemarine.builder.script.SpaceMarineScriptCreator;
import ru.prog.itmo.speaker.Speaker;

public abstract class AbstractAddScriptCommand extends ServerIOCommand {
    public AbstractAddScriptCommand(String commandType,
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
            SpaceMarineScriptCreator creator = new SpaceMarineScriptCreator(reader());
            SpaceMarine marine = creator.create();
            marine.setOwnerUser(Controller.getUser().getLogin());
            sendModule().submitSending(new Request<>(COMMAND_TYPE, marine, true));
            var response = receiveModule().getResponse();
            if (response.getData() != null)
                speaker().speak((String) response.getData());
            else throw new InvalidConnectionException(response.getComment());
        } catch (CreateCancelledException e) {
            throw new InvalidScriptException("Проблемы с соединением...");
        } catch (InvalidConnectionException e) {
            throw new InvalidScriptException(e.getMessage());
        }
    }
}

package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.command.ServerOCommand;
import ru.prog.itmo.connection.*;
import ru.prog.itmo.spacemarine.builder.script.InvalidScriptException;
import ru.prog.itmo.speaker.Speaker;

public class ClearScriptCommand extends ServerOCommand implements ScriptExecutable {
    public ClearScriptCommand(SendModule sendModule, ReceiveModule receiveModule, Speaker speaker) {
        super("clear", sendModule, receiveModule, speaker);
    }

    @Override
    public void execute() {
        super.execute();
        try {
            sendModule().submitSending(new Request<>(COMMAND_TYPE, null, true));
            Response<?> response = receiveModule().getResponse();
            speaker().speak((String) response.getData());
        } catch (InvalidConnectionException e){
            throw new InvalidScriptException("Проблемы с соединением");
        }
    }

    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }
}

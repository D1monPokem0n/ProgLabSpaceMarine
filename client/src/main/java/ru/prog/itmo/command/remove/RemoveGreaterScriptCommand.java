package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.connection.*;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.script.InvalidScriptException;
import ru.prog.itmo.spacemarine.builder.script.SpaceMarineScriptCreator;
import ru.prog.itmo.speaker.Speaker;

public class RemoveGreaterScriptCommand extends ServerIOCommand implements ScriptExecutable {
    public RemoveGreaterScriptCommand(SendModule sendModule,
                                      ReceiveModule receiveModule,
                                      Speaker speaker,
                                      Reader reader) {
        super("remove_greater", sendModule, receiveModule, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        try {
            SpaceMarine maxMarine = createMaxMarine();
            executeRemoveRequest(maxMarine);
        } catch (NullPointerException e){
            speaker().speak("В коллекции нет десантников, выше заданного Вами.");
        } catch (InvalidConnectionException e){
            throw new InvalidScriptException("Проблемы с соединением...");
        }
    }

    private void executeRemoveRequest(SpaceMarine maxMarine){
        sendModule().submitSending(new Request<>(COMMAND_TYPE, maxMarine, true));
        Response<?> response = receiveModule().getResponse();
        if (response.getData() != null)
            speaker().speak((String) response.getData());
        else speaker().speak(response.getComment());
    }

    private SpaceMarine createMaxMarine(){
        SpaceMarineScriptCreator creator = new SpaceMarineScriptCreator(reader());
        return creator.create();
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, превышающие заданный";
    }
}

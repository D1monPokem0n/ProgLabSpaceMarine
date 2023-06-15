package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.*;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;

public class ClearCommand extends ServerIOCommand implements UserAsking {
    public ClearCommand(SendModule sendModule,
                        ReceiveModule receiveModule,
                        Speaker speaker,
                        Reader reader) {
        super("clear", sendModule, receiveModule, speaker, reader);
    }

    @Override
    public void execute() {
        super.execute();
        var answer = askUserIfHeSure();
        if (userSure(answer))
            executeClearRequest();
        if (userNotSure(answer))
            speaker().speak("Ну нет, так нет.");
        if (userDontUnderstandUs(answer))
            speaker().speak("Вы ввели какую-то фигню, мы не уверены в вашей компетенции по удалению солдат Космического Десанта.");
    }

    private void executeClearRequest(){
        try {
            sendModule().submitSending(new Request<>(COMMAND_TYPE, null));
            Response<?> response = receiveModule().getResponse();
            speaker().speak((String) response.getData());
        } catch (InvalidConnectionException e) {
            speaker().speak("Проблемы с соединением");
        }
    }

    private String askUserIfHeSure() {
        speaker().speak("Вы уверены, что хотите удалить?) (Y/N)");
        var answer = reader().read();
        return answer == null ? "null" : answer;
    }

    private boolean userSure(String answer) {
        return answer.equals("Y");
    }

    private boolean userNotSure(String answer) {
        return answer.equals("N");
    }

    private boolean userDontUnderstandUs(String answer) {
        return !answer.equals("Y") && !answer.equals("N");
    }

    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }
}

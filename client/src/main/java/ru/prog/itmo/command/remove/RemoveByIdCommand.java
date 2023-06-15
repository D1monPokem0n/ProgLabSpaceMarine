package ru.prog.itmo.command.remove;

import ru.prog.itmo.command.ServerOCommand;
import ru.prog.itmo.connection.*;
import ru.prog.itmo.control.ConsoleArgument;
import ru.prog.itmo.speaker.Speaker;

public class RemoveByIdCommand extends ServerOCommand {
    private final ConsoleArgument argument;

    public RemoveByIdCommand(SendModule sendModule,
                             ReceiveModule receiveModule,
                             Speaker speaker,
                             ConsoleArgument argument) {
        super("remove_by_id", sendModule, receiveModule, speaker);
        this.argument = argument;
    }

    @Override
    public void execute() {
        super.execute();
        try {
            long id = Long.parseLong(argument.getValue());
            executeRemoveRequest(id);
        } catch (NumberFormatException e) {
            speaker().speak("Вы должны вводить целое число...");
        } catch (InvalidConnectionException e) {
            speaker().speak("Проблемы с соединением...");
        }
    }

    private void executeRemoveRequest(long id){
        sendModule().submitSending(new Request<>(COMMAND_TYPE, id));
        Response<?> response = receiveModule().getResponse();
        if (response.getData() != null)
            speaker().speak((String) response.getData());
        else speaker().speak(response.getComment());
    }

    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по его id";
    }
}

package ru.prog.itmo.command.filter;

import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.connection.*;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;

public class PrintFieldDescendingHealthCommand extends ServerIOCommand {
    public PrintFieldDescendingHealthCommand(SendModule sendModule,
                                             ReceiveModule receiveModule,
                                             Speaker speaker,
                                             Reader reader) {
        super("print_field_descending_health", sendModule, receiveModule, speaker, reader);
    }

    @Override
    public String getDescription() {
        return "вывести значения поля health всех элементов в порядке убывания.";
    }

    @Override
    public void execute() {
        super.execute();
        try {
            sendModule().submitSending(new Request<>(COMMAND_TYPE, null));
            Response<?> response = receiveModule().getResponse();
            if (response.getData() != null)
                speaker().speak((String) response.getData());
            else speaker().speak(response.getComment());
        } catch (InvalidConnectionException | ClassCastException e) {
            speaker().speak("Проблемы с соединением...");
        }
    }
}

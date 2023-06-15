package ru.prog.itmo.command.server_command;

import ru.prog.itmo.command.Command;
import ru.prog.itmo.connection.TokenCreator;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;

public class ChangeAccessTokenTime implements Command {
    private final Speaker speaker;
    private final Reader reader;

    public ChangeAccessTokenTime(Speaker speaker, Reader reader) {
        this.speaker = speaker;
        this.reader = reader;
    }

    @Override
    public void execute() {
        speaker.speak("Нынешнее время жизни access-token'a: " + TokenCreator.getAccessTokenLifeTime());
        speaker.speak("Задайте новое время жизни в секундах или введите cancel для отмены.");
        var answer = reader.read();
        answer = answer == null ? "null" : answer;
        if (!answer.equals("cancel")) {
            setNewValue(answer);
        } else speaker.speak("Изменения не внесены.\n");
    }

    private void setNewValue(String answer) {
        try {
            var newValue = Integer.parseInt(answer);
            TokenCreator.setAccessTokenLifeTime(newValue);
            speaker.speak("Новое время жизни access-token'a: " + TokenCreator.getAccessTokenLifeTime() + "\n");
        } catch (NumberFormatException | NullPointerException e) {
            speaker.speak("Введено не число.");
        }
    }

    @Override
    public String getDescription() {
        return "изменить время валидность access-token'ов";
    }
}

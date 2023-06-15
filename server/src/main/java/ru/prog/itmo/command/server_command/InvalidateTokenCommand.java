package ru.prog.itmo.command.server_command;

import ru.prog.itmo.command.Command;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.storage.StorageDBException;

public class InvalidateTokenCommand implements Command {
    private final Storage storage;
    private final Speaker speaker;
    private final Reader reader;

    public InvalidateTokenCommand(Storage storage, Speaker speaker, Reader reader) {
        this.storage = storage;
        this.speaker = speaker;
        this.reader = reader;
    }

    @Override
    public void execute() {
        var login = getLoginToDelete();
        try {
            if (login.equals("all")) {
                storage.clearAllTokens();
            } else storage.clearToken(login);
            speaker.speak("Токен(ы) инвалидирован(ы).\n");
        } catch (StorageDBException e){
            speaker.speak(e.getMessage());
        }
    }

    private String getLoginToDelete(){
        speaker.speak("Введите имя пользователя, чей токен нужно инвалидировать(all для всех): ");
        String login = reader.read();
        login = login == null ? "null" : login;
        return login;
    }

    @Override
    public String getDescription() {
        return "сбросить refresh-token пользователя. Также можно сбросить токены всех пользователей";
    }
}

package ru.prog.itmo.command.authorization;

import ru.prog.itmo.command.ClientCommand;
import ru.prog.itmo.connection.*;
import ru.prog.itmo.storage.Storage;

import java.net.SocketAddress;

public class RegisterCommand extends ClientCommand {
    private final TokenCreator tokenCreator;

    public RegisterCommand(Storage storage, ConnectionManager connectionManager) {
        super(storage, connectionManager);
        this.tokenCreator = new TokenCreator();
    }

    @Override
    public void execute(SocketAddress address) {
        Request<?> request = connectionManager().getRequestByAddress(address);
        Response<String> response = new Response<>();
        User newUser = (User) request.getData();
        try {
            storage().addNewUser(newUser, getRandomSalt());
            setTokens(response, newUser);
        } catch (InvalidUserException e) {
            response.setComment(e.getMessage());
        } finally {
            connectionManager().putResponse(address, response);
        }
    }

    private void setTokens(Response<String> response, User newUser){
        response.setData("Данные успешно сохранены.");
        response.setAccessToken(tokenCreator.createAccessToken(newUser.getLogin()));
        var refreshToken = tokenCreator.createRefreshToken(newUser.getLogin());
        storage().updateRefreshToken(newUser.getLogin(), refreshToken);
        response.setRefreshToken(refreshToken);
    }

    private String getRandomSalt() {
        return java.util.UUID.randomUUID().toString();
    }

    @Override
    public String getDescription() {
        return "зарегистрировать нового пользователя";
    }
}

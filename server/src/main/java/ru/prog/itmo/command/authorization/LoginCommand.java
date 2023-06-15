package ru.prog.itmo.command.authorization;

import ru.prog.itmo.command.ClientCommand;
import ru.prog.itmo.connection.*;
import ru.prog.itmo.storage.Storage;

import java.net.SocketAddress;

public class LoginCommand extends ClientCommand {
    private final TokenCreator tokenCreator;

    public LoginCommand(Storage storage, ConnectionManager connectionManager) {
        super(storage, connectionManager);
        this.tokenCreator = new TokenCreator();
    }

    @Override
    public void execute(SocketAddress address) {
        Request<?> request = connectionManager().getRequestByAddress(address);
        Response<Boolean> response = new Response<>();
        User user = (User) request.getData();
        try {
            if (storage().checkUser(user)) {
                setTokens(response, user);
            } else response.setData(false);
        } catch (InvalidUserException e) {
            response.setComment(e.getMessage());
        } finally {
            connectionManager().putResponse(address, response);
        }
    }

    private void setTokens(Response<Boolean> response, User user){
        response.setData(true);
        response.setAccessToken(tokenCreator.createAccessToken(user.getLogin()));
        var refreshToken = tokenCreator.createRefreshToken(user.getLogin());
        storage().updateRefreshToken(user.getLogin(), refreshToken);
        response.setRefreshToken(refreshToken);
    }

    @Override
    public String getDescription() {
        return "пройти авторизацию";
    }
}

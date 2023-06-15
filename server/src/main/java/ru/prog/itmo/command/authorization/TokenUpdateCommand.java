package ru.prog.itmo.command.authorization;

import ru.prog.itmo.command.ClientCommand;
import ru.prog.itmo.connection.*;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.storage.StorageDBException;

import java.net.SocketAddress;

public class TokenUpdateCommand extends ClientCommand {
    private final TokenCreator tokenCreator;
    private final TokenValidator tokenValidator;

    public TokenUpdateCommand(Storage storage, ConnectionManager connectionManager) {
        super(storage, connectionManager);
        this.tokenCreator = new TokenCreator();
        this.tokenValidator = new TokenValidator();
    }

    @Override
    public void execute(SocketAddress address) {
        var refreshToken = getRefreshToken(address);
        Response<Boolean> response = new Response<>();
        String login = connectionManager().getUserNameByAddress(address);
        try {
            validateToken(refreshToken, login);
            createNewTokens(login, response);
        } catch (InvalidUserException | StorageDBException e) {
            response.setData(false);
        } finally {
            connectionManager().putResponse(address, response);
        }
    }

    private void validateToken(String currentToken, String login){
        var actualToken = storage().getRefreshToken(login);
        if (!currentToken.equals(actualToken))
            throw new InvalidUserException("");
        var isValid = tokenValidator.validate(currentToken, login);
        if (!isValid) throw new InvalidUserException();
    }

    private void createNewTokens(String login, Response<Boolean> response){
        response.setData(true);
        response.setAccessToken(tokenCreator.createAccessToken(login));
        var refreshToken = tokenCreator.createRefreshToken(login);
        storage().updateRefreshToken(login, refreshToken);
        response.setRefreshToken(refreshToken);
    }


    private String getRefreshToken(SocketAddress address) {
        Request<?> request = connectionManager().getRequestByAddress(address);
        return (String) request.getData();
    }

    @Override
    public String getDescription() {
        return "";
    }
}

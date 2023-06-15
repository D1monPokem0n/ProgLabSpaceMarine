package ru.prog.itmo.control;

import ru.prog.itmo.command.Command;
import ru.prog.itmo.command.server_command.*;
import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

import java.util.HashMap;

public class ServerCommandMap {
    private final HashMap<String, Command> commandMap;

    public ServerCommandMap(Storage storage,
                            ConnectionManager connectionManager,
                            ClientCommandsMap clientCommandsMap,
                            ServerState serverState,
                            Speaker speaker,
                            Reader reader) {
        commandMap = new HashMap<>();
        commandMap.put("exit", new ExitCommand(connectionManager, serverState, speaker));
        commandMap.put("save", new SaveCommand(storage, speaker));
        commandMap.put("help", new HelpCommand(this, clientCommandsMap, speaker));
        commandMap.put("change_access_token_time" , new ChangeAccessTokenTime(speaker, reader));
        commandMap.put("change_refresh_token_time" , new ChangeRefreshTokenTime(speaker, reader));
        commandMap.put("invalidate_token", new InvalidateTokenCommand(storage, speaker, reader));
    }

    public Command getCommand(String commandName) {
        return commandMap.get(commandName);
    }

    public HashMap<String, Command> getCommandMap() {
        return commandMap;
    }
}
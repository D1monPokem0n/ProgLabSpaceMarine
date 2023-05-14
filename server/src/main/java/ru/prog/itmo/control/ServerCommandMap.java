package ru.prog.itmo.control;

import ru.prog.itmo.command.Command;
import ru.prog.itmo.command.ExitCommand;
import ru.prog.itmo.command.SaveCommand;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

import java.util.HashMap;

public class ServerCommandMap {
    private final HashMap<String, Command> commandMap;

    public ServerCommandMap(Storage storage,
                             ConnectionModule connectionModule,
                             ServerState serverState,
                             Speaker speaker,
                             Reader reader) {
        commandMap = new HashMap<>();
        SaveCommand save = new SaveCommand(storage, speaker, reader);
        ExitCommand exit = new ExitCommand(connectionModule, serverState);
        commandMap.put("exit", exit);
        commandMap.put("save", save);
    }

    public Command getCommand(String commandName) {
        return commandMap.get(commandName);
    }
}
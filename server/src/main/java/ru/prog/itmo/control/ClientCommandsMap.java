package ru.prog.itmo.control;

import ru.prog.itmo.command.AbstractServerCommand;
import ru.prog.itmo.command.add.AddCommand;
import ru.prog.itmo.command.add.AddIfMinCommand;
import ru.prog.itmo.command.authorization.LoginCommand;
import ru.prog.itmo.command.authorization.RegisterCommand;
import ru.prog.itmo.command.authorization.TokenUpdateCommand;
import ru.prog.itmo.command.filter.MaxByMeleeWeaponCommand;
import ru.prog.itmo.command.filter.PrintFieldDescendingHealthCommand;
import ru.prog.itmo.command.info.InfoCommand;
import ru.prog.itmo.command.info.ShowCommand;
import ru.prog.itmo.command.remove.ClearCommand;
import ru.prog.itmo.command.remove.RemoveAnyByChapterCommand;
import ru.prog.itmo.command.remove.RemoveByIdCommand;
import ru.prog.itmo.command.remove.RemoveGreaterCommand;
import ru.prog.itmo.command.update.Get_by_id;
import ru.prog.itmo.command.update.UpdateCommand;
import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.storage.Storage;

import java.util.HashMap;

public class ClientCommandsMap {
    private final HashMap<String, AbstractServerCommand> commandMap;

    public ClientCommandsMap(Storage storage,
                             ConnectionManager connectionManager) {
        commandMap = new HashMap<>();
        commandMap.put("add", new AddCommand(storage, connectionManager));
        commandMap.put("add_if_min", new AddIfMinCommand(storage, connectionManager));
        commandMap.put("clear", new ClearCommand(storage, connectionManager));
        commandMap.put("remove_any_by_chapter", new RemoveAnyByChapterCommand(storage, connectionManager));
        commandMap.put("max_by_melee_weapon", new MaxByMeleeWeaponCommand(storage, connectionManager));
        commandMap.put("info", new InfoCommand(storage, connectionManager));
        commandMap.put("print_field_descending_health", new PrintFieldDescendingHealthCommand(storage, connectionManager));
        commandMap.put("remove_by_id", new RemoveByIdCommand(storage, connectionManager));
        commandMap.put("remove_greater", new RemoveGreaterCommand(storage, connectionManager));
        commandMap.put("show", new ShowCommand(storage, connectionManager));
        commandMap.put("update", new UpdateCommand(storage, connectionManager));
        commandMap.put("get_by_id", new Get_by_id(storage, connectionManager));
        commandMap.put("login", new LoginCommand(storage, connectionManager));
        commandMap.put("register", new RegisterCommand(storage, connectionManager));
        commandMap.put("token_update", new TokenUpdateCommand(storage, connectionManager));
    }

    public AbstractServerCommand getCommand(String commandName) {
        return commandMap.get(commandName);
    }

    public HashMap<String, AbstractServerCommand> getCommandMap() {
        return commandMap;
    }
}

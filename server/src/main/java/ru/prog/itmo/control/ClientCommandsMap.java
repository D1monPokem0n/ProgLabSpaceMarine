package ru.prog.itmo.control;

import ru.prog.itmo.command.Command;
import ru.prog.itmo.command.add.AddCommand;
import ru.prog.itmo.command.add.AddIfMinCommand;
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
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

import java.util.HashMap;

public class ClientCommandsMap {
    private final HashMap<String, Command> commandMap;

    public ClientCommandsMap(Storage storage,
                             ConnectionModule connectionModule,
                             ServerState serverState,
                             Speaker speaker,
                             Reader reader) {
        commandMap = new HashMap<>();
        commandMap.put("add", new AddCommand(storage, connectionModule, speaker, reader));
        commandMap.put("add_if_min", new AddIfMinCommand(storage, connectionModule, speaker, reader));
        commandMap.put("clear", new ClearCommand(storage, connectionModule, speaker));
        commandMap.put("remove_any_by_chapter", new RemoveAnyByChapterCommand(storage, connectionModule, speaker, reader));
        commandMap.put("max_by_melee_weapon", new MaxByMeleeWeaponCommand(storage, connectionModule, speaker, reader));
        commandMap.put("info", new InfoCommand(storage, connectionModule, speaker));
        commandMap.put("print_field_descending_health", new PrintFieldDescendingHealthCommand(storage, connectionModule, speaker, reader));
        commandMap.put("remove_by_id", new RemoveByIdCommand(storage, connectionModule, speaker));
        commandMap.put("remove_greater", new RemoveGreaterCommand(storage, connectionModule, speaker, reader));
        commandMap.put("show", new ShowCommand(storage, connectionModule, speaker));
        commandMap.put("update", new UpdateCommand(storage, connectionModule, speaker, reader));
        commandMap.put("get_by_id", new Get_by_id(storage, connectionModule));
    }

    public Command getCommand(String commandName) {
        return commandMap.get(commandName);
    }
}

package ru.prog.itmo;

import ru.prog.itmo.command.*;
import ru.prog.itmo.command.add.AddCommand;
import ru.prog.itmo.command.add.AddIfMinCommand;

import java.util.HashMap;

public class CommandMap {
    private final HashMap<String, Command> commandMap;
    public CommandMap(Storage storage, ProgramState programState, ConsoleArgument argument){
        commandMap = new HashMap<>();
        commandMap.put("add", new AddCommand(storage));
        commandMap.put("add_if_min", new AddIfMinCommand(storage));
        commandMap.put("clear", new ClearCommand(storage));
        commandMap.put("execute_script", new ExecuteScriptCommand());
        commandMap.put("exit", new ExitCommand(programState));
        commandMap.put("remove_any_by_chapter", new RemoveAnyByChapterCommand(storage));
        commandMap.put("max_by_melee_weapon", new MaxByMeleeWeaponCommand(storage));
        commandMap.put("help", new HelpCommand());
        commandMap.put("history", new HistoryCommand());
        commandMap.put("info", new InfoCommand(storage));
        commandMap.put("print_field_descending_health", new PrintFieldDescendingHealthCommand(storage));
        commandMap.put("remove_by_id", new RemoveByIdCommand(storage, argument));
        commandMap.put("remove_greater", new RemoveGreaterCommand(storage));
        commandMap.put("save", new SaveCommand(storage));
        commandMap.put("show", new ShowCommand(storage));
        commandMap.put("update", new UpdateCommand(storage));
    }

    public HashMap<String, Command> getCommandHashMap() {
        return commandMap;
    }
}

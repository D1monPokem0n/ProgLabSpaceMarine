package ru.prog.itmo;

import ru.prog.itmo.command.*;

import java.util.HashMap;

public class Controller {
    private ProgramState programState;
    private Storage storage;
    private final HashMap<String, Command> commandList;
    public Controller(Storage storage){
        commandList = new HashMap<>();
        commandList.put("add", new AddCommand(storage));
        commandList.put("add_if_min", new AddIfMinCommand(storage));
        commandList.put("clear", new ClearCommand(storage));
        commandList.put("execute_script", new ExecuteScriptCommand());
        commandList.put("exit", new ExitCommand());
        commandList.put("remove_any_by_chapter", new RemoveAnyByChapterCommand(storage));
        commandList.put("max_by_melee_weapon", new MaxByMeleeWeaponCommand(storage));
        commandList.put("help", new HelpCommand());
        commandList.put("history", new HistoryCommand());
        commandList.put("info", new InfoCommand(storage));
        commandList.put("print_field_descending_health", new PrintFieldDescendingHealthCommand(storage));
        commandList.put("remove_by_id", new RemoveByIdCommand(storage));
        commandList.put("remove_greater", new RemoveGreaterCommand(storage));
        commandList.put("save", new SaveCommand(storage));
        commandList.put("show", new ShowCommand(storage));
        commandList.put("update", new UpdateCommand(storage));
    }
    public void executeCommand(String commandName){
        commandList.get(commandName).execute();
    }
}

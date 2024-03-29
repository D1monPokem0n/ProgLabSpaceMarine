package ru.prog.itmo.control;

import ru.prog.itmo.command.Command;
import ru.prog.itmo.command.ExitCommand;
import ru.prog.itmo.command.SaveCommand;
import ru.prog.itmo.command.add.AddCommand;
import ru.prog.itmo.command.add.AddIfMinCommand;
import ru.prog.itmo.command.add.AddIfMinScriptCommand;
import ru.prog.itmo.command.add.AddScriptCommand;
import ru.prog.itmo.command.filter.MaxByMeleeWeaponCommand;
import ru.prog.itmo.command.filter.MaxByMeleeWeaponScriptCommand;
import ru.prog.itmo.command.filter.PrintFieldDescendingHealthCommand;
import ru.prog.itmo.command.info.HelpCommand;
import ru.prog.itmo.command.info.HistoryCommand;
import ru.prog.itmo.command.info.InfoCommand;
import ru.prog.itmo.command.info.ShowCommand;
import ru.prog.itmo.command.remove.*;
import ru.prog.itmo.command.script.ExecuteScriptCommand;
import ru.prog.itmo.command.update.UpdateCommand;
import ru.prog.itmo.command.update.UpdateScriptCommand;
import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public class CommandMap {
    private final HashMap<String, Command> commandMap;
    private final Map<String, List<Command>> commandsRealizationsMap;

    public CommandMap(Storage storage,
                      ProgramState programState,
                      ConsoleArgument argument,
                      LinkedList<String> lastCommands,
                      Speaker speaker,
                      SpaceMarineReader reader) {
        commandMap = new HashMap<>();
        commandMap.put("add", new AddCommand(storage, speaker, reader));
        commandMap.put("add_if_min", new AddIfMinCommand(storage, speaker, reader));
        commandMap.put("clear", new ClearCommand(storage, speaker, reader));
        commandMap.put("execute_script", new ExecuteScriptCommand(storage, speaker, argument));
        commandMap.put("exit", new ExitCommand(programState));
        commandMap.put("remove_any_by_chapter", new RemoveAnyByChapterCommand(storage, speaker, reader));
        commandMap.put("max_by_melee_weapon", new MaxByMeleeWeaponCommand(storage, speaker, reader));
        commandMap.put("help", new HelpCommand(speaker, this));
        commandMap.put("history", new HistoryCommand(speaker, lastCommands));
        commandMap.put("info", new InfoCommand(storage, speaker));
        commandMap.put("print_field_descending_health", new PrintFieldDescendingHealthCommand(storage, speaker, reader));
        commandMap.put("remove_by_id", new RemoveByIdCommand(storage, speaker, argument));
        commandMap.put("remove_greater", new RemoveGreaterCommand(storage, speaker, reader));
        commandMap.put("save", new SaveCommand(storage, speaker, reader));
        commandMap.put("show", new ShowCommand(storage, speaker));
        commandMap.put("update", new UpdateCommand(storage, argument, speaker, reader));

        commandsRealizationsMap = ofEntries(
                entry("add", List.of(
                        new AddCommand(storage, speaker, reader),
                        new AddScriptCommand(storage, speaker, reader)
                )),
                entry("add_if_min", List.of(
                        new AddIfMinCommand(storage, speaker, reader),
                        new AddIfMinScriptCommand(storage, speaker, reader)
                )),
                entry("clear", List.of(
                        new ClearCommand(storage, speaker, reader),
                        new ClearScriptCommand(storage, speaker)
                )),
                entry("execute_script", List.of(new ExecuteScriptCommand(storage, speaker, argument))),
                entry("exit", List.of(new ExitCommand(programState))),
                entry("remove_any_by_chapter", List.of(
                        new RemoveAnyByChapterCommand(storage, speaker, reader),
                        new RemoveAnyByChapterScriptCommand(storage, speaker, reader)
                )),
                entry("max_by_melee_weapon", List.of(
                        new MaxByMeleeWeaponCommand(storage, speaker, reader),
                        new MaxByMeleeWeaponScriptCommand(storage, speaker)
                )),
                entry("help", List.of(new HelpCommand(speaker, this))),
                entry("history", List.of(new HistoryCommand(speaker, lastCommands))),
                entry("info", List.of(new InfoCommand(storage, speaker))),
                entry("print_field_descending_health", List.of(new PrintFieldDescendingHealthCommand(storage, speaker, reader))),
                entry("remove_by_id", List.of(new RemoveByIdCommand(storage, speaker, argument))),
                entry("remove_greater", List.of(
                        new RemoveGreaterCommand(storage, speaker, reader),
                        new RemoveGreaterScriptCommand(storage, speaker, reader)
                )),
                entry("save", List.of(new SaveCommand(storage, speaker, reader))),
                entry("show", List.of(new ShowCommand(storage, speaker))),
                entry("update", List.of(
                        new UpdateCommand(storage, argument, speaker, reader),
                        new UpdateScriptCommand(storage, argument, speaker, reader)
                ))
        );
    }

    public HashMap<String, Command> getCommandHashMap() {
        return commandMap;
    }

    public Command getCommand(String commandName) {
        return commandMap.get(commandName);
    }

    public void put(String commandName, Command command){
        commandMap.put(commandName, command);
    }

    public Map<String, List<Command>> getCommandsRealizationsMap(){
        return commandsRealizationsMap;
    }
}

package ru.prog.itmo.control;

import ru.prog.itmo.command.Command;
import ru.prog.itmo.command.ExitCommand;
import ru.prog.itmo.command.add.AddCommand;
import ru.prog.itmo.command.add.AddIfMinCommand;
import ru.prog.itmo.command.add.AddIfMinScriptCommand;
import ru.prog.itmo.command.add.AddScriptCommand;
import ru.prog.itmo.command.filter.MaxByMeleeWeaponCommand;
import ru.prog.itmo.command.filter.PrintFieldDescendingHealthCommand;
import ru.prog.itmo.command.info.HelpCommand;
import ru.prog.itmo.command.info.HistoryCommand;
import ru.prog.itmo.command.info.InfoCommand;
import ru.prog.itmo.command.info.ShowCommand;
import ru.prog.itmo.command.remove.*;
import ru.prog.itmo.command.script.ExecuteScriptCommand;
import ru.prog.itmo.command.update.UpdateCommand;
import ru.prog.itmo.command.update.UpdateScriptCommand;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.speaker.Speaker;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public class CommandMap {
    private final HashMap<String, Command> commandMap;
    private final Map<String, List<Command>> commandsRealizationsMap;

    public CommandMap(ConnectionModule connectionModule,
                      ClientState clientState,
                      ConsoleArgument argument,
                      LinkedList<String> lastCommands,
                      Speaker speaker,
                      Reader reader) {
        commandMap = new HashMap<>();
        commandMap.put("add", new AddCommand(connectionModule, speaker, reader));
        commandMap.put("add_if_min", new AddIfMinCommand(connectionModule, speaker, reader));
        commandMap.put("clear", new ClearCommand(connectionModule, speaker, reader));
        commandMap.put("execute_script", new ExecuteScriptCommand(connectionModule, speaker, argument));
        commandMap.put("exit", new ExitCommand(clientState));
        commandMap.put("remove_any_by_chapter", new RemoveAnyByChapterCommand(connectionModule, speaker, reader));
        commandMap.put("max_by_melee_weapon", new MaxByMeleeWeaponCommand(connectionModule, speaker));
        commandMap.put("help", new HelpCommand(speaker, this));
        commandMap.put("history", new HistoryCommand(speaker, lastCommands));
        commandMap.put("info", new InfoCommand(connectionModule, speaker));
        commandMap.put("print_field_descending_health", new PrintFieldDescendingHealthCommand(connectionModule, speaker, reader));
        commandMap.put("remove_by_id", new RemoveByIdCommand(connectionModule, speaker, argument));
        commandMap.put("remove_greater", new RemoveGreaterCommand(connectionModule, speaker, reader));
        commandMap.put("show", new ShowCommand(connectionModule, speaker));
        commandMap.put("update", new UpdateCommand(connectionModule, argument, speaker, reader));

        commandsRealizationsMap = ofEntries(
                entry("add", List.of(
                        new AddCommand(connectionModule, speaker, reader),
                        new AddScriptCommand(connectionModule, speaker, reader)
                )),
                entry("add_if_min", List.of(
                        new AddIfMinCommand(connectionModule, speaker, reader),
                        new AddIfMinScriptCommand(connectionModule, speaker, reader)
                )),
                entry("clear", List.of(
                        new ClearCommand(connectionModule, speaker, reader),
                        new ClearScriptCommand(connectionModule, speaker)
                )),
                entry("execute_script", List.of(new ExecuteScriptCommand(connectionModule, speaker, argument))),
                entry("exit", List.of(new ExitCommand(clientState))),
                entry("remove_any_by_chapter", List.of(
                        new RemoveAnyByChapterCommand(connectionModule, speaker, reader),
                        new RemoveAnyByChapterScriptCommand(connectionModule, speaker, reader)
                )),
                entry("max_by_melee_weapon", List.of(new MaxByMeleeWeaponCommand(connectionModule, speaker))),
                entry("help", List.of(new HelpCommand(speaker, this))),
                entry("history", List.of(new HistoryCommand(speaker, lastCommands))),
                entry("info", List.of(new InfoCommand(connectionModule, speaker))),
                entry("print_field_descending_health", List.of(new PrintFieldDescendingHealthCommand(connectionModule, speaker, reader))),
                entry("remove_by_id", List.of(new RemoveByIdCommand(connectionModule, speaker, argument))),
                entry("remove_greater", List.of(
                        new RemoveGreaterCommand(connectionModule, speaker, reader),
                        new RemoveGreaterScriptCommand(connectionModule, speaker, reader)
                )),
                entry("show", List.of(new ShowCommand(connectionModule, speaker))),
                entry("update", List.of(
                        new UpdateCommand(connectionModule, argument, speaker, reader),
                        new UpdateScriptCommand(connectionModule, argument, speaker, reader)
                ))
        );
    }

    public HashMap<String, Command> getCommandHashMap() {
        return commandMap;
    }

    public Command getCommand(String commandName) {
        return commandMap.get(commandName);
    }

    public void put(String commandName, Command command) {
        commandMap.put(commandName, command);
    }

    public Map<String, List<Command>> getCommandsRealizationsMap() {
        return commandsRealizationsMap;
    }
}

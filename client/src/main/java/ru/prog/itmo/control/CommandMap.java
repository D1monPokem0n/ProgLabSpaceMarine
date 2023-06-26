package ru.prog.itmo.control;

import ru.prog.itmo.command.Command;
import ru.prog.itmo.command.authorization.LogOutCommand;
import ru.prog.itmo.command.exit.ExitCommand;
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
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.ReceiveModule;
import ru.prog.itmo.connection.SendModule;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;

import java.util.*;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public class CommandMap {
    private final HashMap<String, Command> commandMap;
    private final Map<String, List<Command>> commandsRealizationsMap;

    public CommandMap(/*Properties properties,*/
                      ConnectionModule connectionModule,
                      SendModule sendModule,
                      ReceiveModule receiveModule,
                      ClientState clientState,
                      ConsoleArgument argument,
                      LinkedList<String> lastCommands,
                      Speaker speaker,
                      Reader reader) {
        commandMap = new HashMap<>();
        commandMap.put("add", new AddCommand(sendModule, receiveModule, speaker, reader));
        commandMap.put("add_if_min", new AddIfMinCommand(sendModule, receiveModule, speaker, reader));
        commandMap.put("clear", new ClearCommand(sendModule, receiveModule, speaker, reader));
        commandMap.put("execute_script", new ExecuteScriptCommand(/*properties, */connectionModule,sendModule, receiveModule, speaker, argument));
        commandMap.put("exit", new ExitCommand(clientState));
        commandMap.put("remove_any_by_chapter", new RemoveAnyByChapterCommand(sendModule, receiveModule, speaker, reader));
        commandMap.put("max_by_melee_weapon", new MaxByMeleeWeaponCommand(sendModule, receiveModule, speaker));
        commandMap.put("help", new HelpCommand(speaker, this));
        commandMap.put("history", new HistoryCommand(speaker, lastCommands));
        commandMap.put("info", new InfoCommand(sendModule, receiveModule, speaker));
        commandMap.put("print_field_descending_health", new PrintFieldDescendingHealthCommand(sendModule, receiveModule, speaker, reader));
        commandMap.put("remove_by_id", new RemoveByIdCommand(sendModule, receiveModule, speaker, argument));
        commandMap.put("remove_greater", new RemoveGreaterCommand(sendModule, receiveModule, speaker, reader));
        commandMap.put("show", new ShowCommand(sendModule, receiveModule, speaker));
        commandMap.put("update", new UpdateCommand(sendModule, receiveModule, argument, speaker, reader));
        commandMap.put("log_out", new LogOutCommand(speaker, clientState));

        commandsRealizationsMap = ofEntries(
                entry("add", List.of(
                        new AddCommand(sendModule, receiveModule, speaker, reader),
                        new AddScriptCommand(sendModule, receiveModule, speaker, reader)
                )),
                entry("add_if_min", List.of(
                        new AddIfMinCommand(sendModule, receiveModule, speaker, reader),
                        new AddIfMinScriptCommand(sendModule, receiveModule, speaker, reader)
                )),
                entry("clear", List.of(
                        new ClearCommand(sendModule, receiveModule, speaker, reader),
                        new ClearScriptCommand(sendModule, receiveModule, speaker)
                )),
                entry("execute_script", List.of(new ExecuteScriptCommand(/*properties,*/ connectionModule, sendModule, receiveModule, speaker, argument))),
                entry("exit", List.of(new ExitCommand(clientState))),
                entry("remove_any_by_chapter", List.of(
                        new RemoveAnyByChapterCommand(sendModule, receiveModule, speaker, reader),
                        new RemoveAnyByChapterScriptCommand(sendModule, receiveModule, speaker, reader)
                )),
                entry("max_by_melee_weapon", List.of(new MaxByMeleeWeaponCommand(sendModule, receiveModule, speaker))),
                entry("help", List.of(new HelpCommand(speaker, this))),
                entry("history", List.of(new HistoryCommand(speaker, lastCommands))),
                entry("info", List.of(new InfoCommand(sendModule, receiveModule, speaker))),
                entry("print_field_descending_health", List.of(new PrintFieldDescendingHealthCommand(sendModule, receiveModule, speaker, reader))),
                entry("remove_by_id", List.of(new RemoveByIdCommand(sendModule, receiveModule, speaker, argument))),
                entry("remove_greater", List.of(
                        new RemoveGreaterCommand(sendModule, receiveModule, speaker, reader),
                        new RemoveGreaterScriptCommand(sendModule, receiveModule, speaker, reader)
                )),
                entry("show", List.of(new ShowCommand(sendModule, receiveModule, speaker))),
                entry("update", List.of(
                        new UpdateCommand(sendModule, receiveModule, argument, speaker, reader),
                        new UpdateScriptCommand(sendModule, receiveModule, argument, speaker, reader)
                )),
                entry("log_out", List.of(new LogOutCommand(speaker, clientState)))
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

package ru.prog.itmo.command.server_command;

import ru.prog.itmo.command.Command;
import ru.prog.itmo.control.ClientCommandsMap;
import ru.prog.itmo.control.ServerCommandMap;
import ru.prog.itmo.speaker.Speaker;

public class HelpCommand implements Command {
    private final ServerCommandMap serverCommandMap;
    private final ClientCommandsMap clientCommandsMap;
    private final Speaker speaker;

    public HelpCommand(ServerCommandMap serverCommandMap, ClientCommandsMap clientCommandsMap, Speaker speaker){
        this.serverCommandMap = serverCommandMap;
        this.clientCommandsMap = clientCommandsMap;
        this.speaker = speaker;
    }

    @Override
    public void execute() {
        speaker.speak("\nКОМАНДЫ ДОСТУПНЫЕ НА СЕРВЕРЕ:\n");
        for (String commandName : serverCommandMap.getCommandMap().keySet())
            speaker.speak(commandName + ": " + serverCommandMap.getCommand(commandName).getDescription());
        speaker.speak("\nКОМАНДЫ ДОСТУПНЫЕ ПОЛЬЗОВАТЕЛЮ:\n");
        for (String commandName : clientCommandsMap.getCommandMap().keySet())
            speaker.speak(commandName + ": " + clientCommandsMap.getCommand(commandName).getDescription());
        speaker.speak("\n");
    }

    @Override
    public String getDescription() {
        return "вывести информацию о серверных и пользовательских командах";
    }
}

package ru.prog.itmo.command.server_command;

import ru.prog.itmo.command.Command;
import ru.prog.itmo.connection.ConnectionManager;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.control.ServerState;
import ru.prog.itmo.speaker.Speaker;

public class ExitCommand implements Command {
    private final ServerState serverState;
    private final ConnectionManager connectionManager;
    private final Speaker speaker;

    public ExitCommand(ConnectionManager connectionManager, ServerState serverState, Speaker speaker) {
        this.serverState = serverState;
        this.connectionManager = connectionManager;
        this.speaker = speaker;
    }

    @Override
    public void execute() {
        try {
            connectionManager.disconnect();
            serverState.setWorkStatus(false);
            speaker.speak("Завершение работы сервера...\n");
        } catch (InvalidConnectionException e){
            speaker.speak(e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "завершить программу";
    }
}

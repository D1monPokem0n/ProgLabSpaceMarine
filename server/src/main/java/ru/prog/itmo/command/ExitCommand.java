package ru.prog.itmo.command;

import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.control.ServerState;
import ru.prog.itmo.speaker.ConsoleSpeaker;
import ru.prog.itmo.speaker.Speaker;

public class ExitCommand extends AbstractServerCommand {
    private final ServerState serverState;
    private ConnectionModule connectionModule;

    public ExitCommand(ConnectionModule connectionModule, ServerState serverState) {
        this.serverState = serverState;
        this.connectionModule = connectionModule;
    }

    @Override
    public void execute() {
        super.execute();
        try {
            connectionModule.disconnect();
            serverState.setWorkStatus(false);
        } catch (InvalidConnectionException e){
            Speaker speaker = new ConsoleSpeaker();
            speaker.speak(e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "завершить программу (без сохранения в файл)";
    }
}

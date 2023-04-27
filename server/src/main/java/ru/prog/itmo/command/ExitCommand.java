package ru.prog.itmo.command;

import ru.prog.itmo.control.ServerState;

public class ExitCommand extends AbstractServerCommand {
    private final ServerState serverState;

    public ExitCommand(ServerState serverState) {
        this.serverState = serverState;
    }

    @Override
    public void execute() {
        super.execute();
        serverState.setWorkStatus(false);
    }

    @Override
    public String getDescription() {
        return "завершить программу (без сохранения в файл)";
    }
}

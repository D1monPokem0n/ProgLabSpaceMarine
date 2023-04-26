package ru.prog.itmo.command;

import ru.prog.itmo.control.ClientState;

public class ExitCommand extends ConsoleCommand {
    private final ClientState clientState;

    public ExitCommand(ClientState clientState) {
        super("exit");
        this.clientState = clientState;
    }

    @Override
    public void execute() {
        super.execute();
        clientState.setWorkStatus(false);
    }

    @Override
    public String getDescription() {
        return "завершить программу (без сохранения в файл)";
    }
}

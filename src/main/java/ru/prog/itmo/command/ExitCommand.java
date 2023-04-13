package ru.prog.itmo.command;

import ru.prog.itmo.control.ProgramState;

public class ExitCommand extends ConsoleCommand {
    private final ProgramState programState;

    public ExitCommand(ProgramState programState) {
        this.programState = programState;
    }

    @Override
    public void execute() {
        super.execute();
        programState.setWorkStatus(false);
    }

    @Override
    public String getDescription() {
        return "завершить программу (без сохранения в файл)";
    }
}

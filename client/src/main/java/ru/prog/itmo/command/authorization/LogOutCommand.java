package ru.prog.itmo.command.authorization;

import ru.prog.itmo.command.ConsoleOCommand;
import ru.prog.itmo.control.ClientState;
import ru.prog.itmo.control.Controller;
import ru.prog.itmo.speaker.Speaker;

public class LogOutCommand extends ConsoleOCommand {
    private ClientState clientState;

    public LogOutCommand(Speaker speaker, ClientState clientState) {
        super("log_out", speaker);
    }

    public void execute(){
        Controller.clearUserInfo();
        clientState.setLogged(false);
        speaker().speak("Выход с аккаунта.");
        throw new LogOutException();
    }

    @Override
    public String getDescription() {
        return "разлогиниться";
    }
}

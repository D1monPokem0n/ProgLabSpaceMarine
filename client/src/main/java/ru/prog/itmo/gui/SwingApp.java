package ru.prog.itmo.gui;

import ru.prog.itmo.control.ClientState;
import ru.prog.itmo.control.CommandManager;
import ru.prog.itmo.speaker.Speaker;

import javax.swing.*;

public class SwingApp {
    private final CommandManager commandManager;
    private final Speaker speaker;
    private final ClientState clientState;
    private AuthorizationFrame authorizationFrame;
    private MainFrame mainFrame;


    public void initMainFrame() {
        mainFrame = new MainFrame(commandManager, speaker, clientState);
    }

    public SwingApp(CommandManager commandManager, Speaker speaker, ClientState clientState) {
        this.commandManager = commandManager;
        this.speaker = speaker;
        this.clientState = clientState;
        setupEnterActionForAllButtons();
        login();
    }

    private void setupEnterActionForAllButtons() {
        InputMap inputMap = (InputMap) UIManager.getDefaults().get("Button.focusInputMap");
        Object pressedAction = inputMap.get(KeyStroke.getKeyStroke("pressed SPACE"));
        Object releasedAction = inputMap.get(KeyStroke.getKeyStroke("released SPACE"));
        inputMap.put(KeyStroke.getKeyStroke("pressed ENTER"), pressedAction);
        inputMap.put(KeyStroke.getKeyStroke("released ENTER"), releasedAction);
    }

    public void setHasUpdates(){
        mainFrame.refresh();
    }

    private void login() {
        authorizationFrame = new AuthorizationFrame(commandManager, speaker, this, clientState);
        authorizationFrame.setTitle("Authorization Form");
        authorizationFrame.setBounds(500, 200, 300, 500);
        authorizationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        authorizationFrame.setResizable(false);
        authorizationFrame.setVisible(true);
    }
}
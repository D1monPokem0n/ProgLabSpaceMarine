package ru.prog.itmo.gui;

import javax.swing.*;

public class MainFrame extends JFrame {
    private AuthorizationPanel authorizationPanel;
    private WorkPanel workPanel;

    public MainFrame() {
        login();
    }

    private void login() {
        add(authorizationPanel);
    }

    private void work() {
        workPanel = new WorkPanel();
        add(workPanel);
    }
}

package ru.prog.itmo.gui;

import ru.prog.itmo.control.ClientState;
import ru.prog.itmo.control.CommandManager;
import ru.prog.itmo.speaker.Speaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AuthorizationFrame extends JFrame implements ActionListener {
    public static final int MIN_PASSWORD_LENGTH = 4;
    public static final int MAX_PASSWORD_LENGTH = 50;
    public static final int MIN_LOGIN_LENGTH = 4;
    public static final int MAX_LOGIN_LENGTH = 30;
    private LoginFrame loginFrame;
    private RegisterFrame registerFrame;
    private JButton login;
    private JButton register;
    private CommandManager commandManager;
    private Speaker speaker;
    private ClientState clientState;
    private SwingApp swingApp;

    public AuthorizationFrame(CommandManager commandManager,
                              Speaker speaker,
                              SwingApp swingApp,
                              ClientState clientState) {
        setComponents(commandManager, speaker, swingApp, clientState);
        setLocationsAndSizes();
        addComponents();
        addEvents();
        setLayout(null);
    }

    private void addEvents() {
        login.addActionListener(this);
        register.addActionListener(this);
    }

    private void addComponents() {
        add(login);
        add(register);
    }

    private void setLocationsAndSizes() {
        loginFrame.setLocation(500, 0);
        loginFrame.setSize(new Dimension(370, 600));
        registerFrame.setLocation(500, 0);
        registerFrame.setSize(new Dimension(370, 600));
        login.setBounds(100, 100, 100, 25);
        register.setBounds(100, 200, 100, 25);
    }

    private void setComponents(CommandManager commandManager,
                               Speaker speaker,
                               SwingApp swingApp,
                               ClientState clientState) {
        this.commandManager = commandManager;
        this.speaker = speaker;
        this.swingApp = swingApp;
        loginFrame = new LoginFrame(this, commandManager, speaker, clientState);
        registerFrame = new RegisterFrame(this, commandManager, speaker, clientState);
        login = new JButton(speaker.speak("LOGIN"));
        register = new JButton(speaker.speak("REGISTER"));
    }

    public void finish() {
        setVisible(false);
        swingApp.initMainFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login) {
            registerFrame.setVisible(false);
            setVisible(false);
            loginFrame.setVisible(true);
        }
        if (e.getSource() == register) {
            loginFrame.setVisible(false);
            setVisible(false);
            registerFrame.setVisible(true);
        }
    }
}

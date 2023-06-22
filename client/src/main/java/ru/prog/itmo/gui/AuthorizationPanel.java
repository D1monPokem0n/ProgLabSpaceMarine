package ru.prog.itmo.gui;

import ru.prog.itmo.connection.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AuthorizationPanel extends JPanel {
    private JTextField textField;
    private JPasswordField passwordField;
    private JPanel northPanel;
    private JPanel southPanel;
    private JButton loginButton;
    private SendModule sendModule;
    private ReceiveModule receiveModule;

    public AuthorizationPanel(SendModule sendModule, ReceiveModule receiveModule) {
        add(northPanel(), BorderLayout.CENTER);
        add(southPanel(), BorderLayout.SOUTH);
    }

    private JPanel northPanel() {
        northPanel = new JPanel();
        textField = new JTextField(20);
        passwordField = new JPasswordField(20);
        northPanel.add(new JLabel("User name(min. 4): ", SwingConstants.RIGHT));
        northPanel.add(textField);
        northPanel.add(new JLabel("Password(min. 4): ", SwingConstants.RIGHT));
        northPanel.add(passwordField);
        setLayout(new GridLayout(2, 2));
        northPanel.setMinimumSize(new Dimension(200, 200));
        northPanel.setMaximumSize(new Dimension(200, 200));
        return northPanel;
    }

    private JPanel southPanel() {
        southPanel = new JPanel();
        loginButton = new JButton("Login");
        southPanel.add(loginButton);
        loginButton.addActionListener(this::validateLogin);
        return southPanel;
    }

    private void validateLogin(ActionEvent event) {
        String login = textField.getText();
        String password = new String(passwordField.getPassword());
        var correct = false;
        if (login == null || login.length() < 4)
            JOptionPane.showMessageDialog(null, "Некорректный логин");
        else if (password == null || password.length() < 4)
            JOptionPane.showMessageDialog(null, "Некорректный пароль");

        this.setVisible(false);
    }


}



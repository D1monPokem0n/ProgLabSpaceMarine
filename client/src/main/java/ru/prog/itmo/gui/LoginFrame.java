package ru.prog.itmo.gui;

import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.connection.InvalidUserException;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.connection.User;
import ru.prog.itmo.control.ClientState;
import ru.prog.itmo.control.CommandManager;
import ru.prog.itmo.control.Controller;
import ru.prog.itmo.speaker.Speaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ru.prog.itmo.gui.AuthorizationFrame.*;

public class LoginFrame extends JFrame implements ActionListener {

    private final AuthorizationFrame authorizationFrame;
    private final CommandManager commandManager;
    private final Speaker speaker;
    private final ClientState clientState;
    private final Container container = getContentPane();
    private JLabel userLabel;
    private JLabel passwordLabel;
    private JTextField userTextField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private JCheckBox showPassword;
    private static final String PEPPER = "SAC=*yStn]0ZRJ0X";


    public LoginFrame(AuthorizationFrame authorizationFrame,
                      CommandManager commandManager,
                      Speaker speaker,
                      ClientState clientState) {
        this.speaker = speaker;
        this.authorizationFrame = authorizationFrame;
        this.commandManager = commandManager;
        this.clientState = clientState;
        setComponents();
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
    }

    private void setComponents() {
        userLabel = new JLabel(speaker.speak("USERNAME"));
        passwordLabel = new JLabel(speaker.speak("PASSWORD"));
        userTextField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton(speaker.speak("LOGIN"));
        cancelButton = new JButton(speaker.speak("CANCEL"));
        showPassword = new JCheckBox(speaker.speak("Show Password"));
    }

    private void setLayoutManager() {
        container.setLayout(null);
    }

    private void setLocationAndSize() {
        userLabel.setBounds(50, 150, 100, 30);
        passwordLabel.setBounds(50, 220, 100, 30);
        userTextField.setBounds(150, 150, 150, 30);
        passwordField.setBounds(150, 220, 150, 30);
        showPassword.setBounds(150, 250, 150, 30);
        loginButton.setBounds(50, 300, 100, 30);
        cancelButton.setBounds(200, 300, 100, 30);
    }

    private void addComponentsToContainer() {
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        passwordField.setEchoChar('*');
        container.add(showPassword);
        container.add(loginButton);
        container.add(cancelButton);
    }

    private void addActionEvent() {
        userTextField.addActionListener(this);
        passwordField.addActionListener(this);
        loginButton.addActionListener(this);
        cancelButton.addActionListener(this);
        showPassword.addActionListener(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == loginButton)
            login();
        if (event.getSource() == cancelButton)
            cancel();
        if (event.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        }
        if (event.getSource() == userTextField) {
            passwordField.requestFocus();
        }
        if (event.getSource() == passwordField) {
            loginButton.requestFocus();
        }
    }

    private void cancel() {
        userTextField.setText("");
        passwordField.setText("");
        setVisible(false);
        authorizationFrame.setVisible(true);
    }

    private void login() {
        var login = userTextField.getText();
        var password = new String(passwordField.getPassword());
        tryToLogin(login, password);
    }

    private void tryToLogin(String login, String password) {
        if (login.length() > MAX_LOGIN_LENGTH || login.length() < MIN_LOGIN_LENGTH)
            JOptionPane.showMessageDialog(this, speaker.speak("Incorrect_login_length"));
        else if (password.length() > MAX_PASSWORD_LENGTH || password.length() < MIN_PASSWORD_LENGTH)
            JOptionPane.showMessageDialog(this, speaker.speak("Incorrect_password_length"));
        else validateUser(login, password);
    }

    private void validateUser(String login, String password) {
        var user = new User(login, PEPPER + password);
        try {
            var response = (Response<?>) commandManager.executeLoginCommand(user);
            var isLogged = (boolean) response.getData();
            if (isLogged) {
                successfullyLogged(user, response);
            } else
                JOptionPane.showMessageDialog(this, speaker.speak("Invalid_username_or_password"));
        } catch (InvalidUserException | ClassCastException | NullPointerException | InvalidConnectionException e) {
            JOptionPane.showMessageDialog(this, speaker.speak(e.getMessage()));
        }
    }

    private void successfullyLogged(User user, Response<?> response) {
        JOptionPane.showMessageDialog(this, speaker.speak("Login_successful"));
        user.setToken(response.getAccessToken());
        Controller.setUser(user);
        Controller.setRefreshToken(response.getRefreshToken());
        clientState.setLogged(true);
        setVisible(false);
        authorizationFrame.finish();
    }
}


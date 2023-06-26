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

public class RegisterFrame extends JFrame implements ActionListener {

    private final AuthorizationFrame authorizationFrame;
    private final CommandManager commandManager;
    private final Speaker speaker;
    private final ClientState clientState;
    private final Container container = getContentPane();
    private final JLabel userLabel = new JLabel("USERNAME");
    private final JLabel passwordLabel = new JLabel("PASSWORD");
    private final JLabel repeatPasswordLabel = new JLabel("REPEAT PASSWORD");
    private final JTextField userTextField = new JTextField();
    private final JPasswordField passwordField1 = new JPasswordField();
    private final JPasswordField passwordField2 = new JPasswordField();
    private final JButton registerButton = new JButton("REGISTER");
    private final JButton cancelButton = new JButton("CANCEL");
    private final JCheckBox showPassword = new JCheckBox("Show Password");
    private static final String PEPPER = "SAC=*yStn]0ZRJ0X";


    public RegisterFrame(AuthorizationFrame authorizationFrame,
                         CommandManager commandManager,
                         Speaker speaker,
                         ClientState clientState) {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
        this.authorizationFrame = authorizationFrame;
        this.commandManager = commandManager;
        this.speaker = speaker;
        this.clientState = clientState;
    }

    private void setLayoutManager() {
        container.setLayout(null);
    }

    private void setLocationAndSize() {
        userLabel.setBounds(50, 150, 100, 30);
        passwordLabel.setBounds(50, 220, 100, 30);
        repeatPasswordLabel.setBounds(40, 290, 100, 30);
        userTextField.setBounds(150, 150, 150, 30);
        passwordField1.setBounds(150, 220, 150, 30);
        passwordField2.setBounds(150, 290, 150, 30);
        showPassword.setBounds(150, 250, 150, 30);
        registerButton.setBounds(50, 340, 100, 30);
        cancelButton.setBounds(200, 340, 100, 30);
    }

    private void addComponentsToContainer() {
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        passwordField1.setEchoChar('*');
        passwordField2.setEchoChar('*');
        container.add(passwordField1);
        container.add(passwordField2);
        container.add(repeatPasswordLabel);
        container.add(showPassword);
        container.add(registerButton);
        container.add(cancelButton);
    }

    private void addActionEvent() {
        userTextField.addActionListener(this);
        passwordField1.addActionListener(this);
        passwordField2.addActionListener(this);
        registerButton.addActionListener(this);
        cancelButton.addActionListener(this);
        showPassword.addActionListener(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == registerButton) {
            var login = userTextField.getText();
            var password1 = new String(passwordField1.getPassword());
            var password2 = new String(passwordField2.getPassword());
            tryToRegister(login, password1, password2);
        }
        if (event.getSource() == cancelButton) {
            userTextField.setText("");
            passwordField1.setText("");
            passwordField2.setText("");
            setVisible(false);
            authorizationFrame.setVisible(true);
        }
        if (event.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passwordField1.setEchoChar((char) 0);
                passwordField2.setEchoChar((char) 0);
            } else {
                passwordField1.setEchoChar('*');
                passwordField2.setEchoChar('*');
            }
        }
        if (event.getSource() == userTextField) {
            passwordField1.requestFocus();
        }
        if (event.getSource() == passwordField1) {
            passwordField2.requestFocus();
        }
         if (event.getSource() == passwordField2) {
             registerButton.requestFocus();
         }
    }

    private void tryToRegister(String login, String password1, String password2) {
        if (login.length() > MAX_LOGIN_LENGTH || login.length() < MIN_LOGIN_LENGTH)
            JOptionPane.showMessageDialog(this, speaker.speak("Incorrect_login_length"));
        else if (password1.length() > MAX_PASSWORD_LENGTH || password1.length() < MIN_PASSWORD_LENGTH)
            JOptionPane.showMessageDialog(this, speaker.speak("Incorrect_password_length"));
        else if (!password1.equals(password2))
            JOptionPane.showMessageDialog(this, speaker.speak("Different_passwords"));
        else validateUser(login, password1);
    }

    private void validateUser(String login, String password) {
        var user = new User(login, PEPPER + password);
        try {
            var response = (Response<?>) commandManager.executeRegisterCommand(user);
            var isLogged = response.getData() != null;
            if (isLogged) {
                successfulRegistered(user, response);
            } else
                JOptionPane.showMessageDialog(this, speaker.speak(response.getComment()));
        } catch (InvalidUserException | InvalidConnectionException e) {
            JOptionPane.showMessageDialog(this, speaker.speak(e.getMessage()));
        }
    }

    private void successfulRegistered(User user, Response<?> response) {
        JOptionPane.showMessageDialog(this, speaker.speak("Register_successful"));
        user.setToken(response.getAccessToken());
        Controller.setUser(user);
        Controller.setRefreshToken(response.getRefreshToken());
        clientState.setLogged(true);
        setVisible(false);
        authorizationFrame.finish();
    }
}

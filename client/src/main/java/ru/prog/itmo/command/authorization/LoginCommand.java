package ru.prog.itmo.command.authorization;

import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.connection.*;
import ru.prog.itmo.control.Controller;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;

import java.util.Objects;

public class LoginCommand extends ServerIOCommand {

    private static final String PEPPER = "SAC=*yStn]0ZRJ0X";
    private static final int MIN_PASSWORD_LENGTH = 4;
    private static final int MAX_PASSWORD_LENGTH = 50;
    private static final int MIN_LOGIN_LENGTH = 4;
    private static final int MAX_LOGIN_LENGTH = 30;


    public LoginCommand(SendModule sendModule, ReceiveModule receiveModule, Speaker speaker, Reader reader) {
        super("login", sendModule, receiveModule, speaker, reader);
    }

    @Override
    public void execute() {
        while (Controller.isNotLogged()) {
            speaker().speak("Войдите в аккаунт или создайте новый.(login/register)(Для выхода из приложения введите exit)");
            String answer = reader().read();
            answer = answer == null ? "null" : answer;
            switch (answer) {
                case "login" -> login();
                case "register" -> register();
                case "exit" -> throw new LoginCancelledException();
                default -> speaker().speak("Некорректный ввод");
            }
        }
    }

    private void login() {
        while (Controller.isNotLogged()) {
            speaker().speak("Введите логин:\n");
            var login = reader().read();
            if (isGoodLogin(login)) {
                tryToFindUser(login);
                if (Controller.isNotLogged()) {
                    inputPassword(login);
                }
            }
        }
    }

    private void tryToFindUser(String login) {
        var refreshToken = Controller.getProperties().get(login);
        refreshToken = refreshToken == null ? "null" : refreshToken;
        if (!refreshToken.equals("null")) {
            setLoggedIfTokenValid(login, (String) refreshToken);
        } else Controller.setIsLogged(false);
    }

    private void setLoggedIfTokenValid(String login, String refreshToken) {
        Request<String> request = new Request<>("token_update", refreshToken);
        Controller.setUser(new User(login, ""));
        sendModule().submitSending(request);
        var response = receiveModule().getResponse();
        if ((Boolean) response.getData())
            setLoggedUser(response);
        else Controller.setIsLogged(false);
    }

    private void setLoggedUser(Response<?> response) {
        var user = Controller.getUser();
        user.setToken(response.getAccessToken());
        Controller.setRefreshToken(response.getRefreshToken());
        Controller.setIsLogged(true);
    }

    private boolean isGoodLogin(String login) {
        boolean notNull = login != null;
        if (notNull) {
            if (login.equals("exit")) throw new LoginCancelledException();
            return checkLoginLength(login);
        }
        speaker().speak("Не вводите пустую строчку.");
        return false;
    }

    private boolean checkLoginLength(String login) {
        boolean isMoreMin = login.length() >= MIN_LOGIN_LENGTH;
        boolean isLessMax = login.length() <= MAX_LOGIN_LENGTH;
        if (!isMoreMin) speaker().speak("Минимальная длина логина - " + MIN_LOGIN_LENGTH);
        if (!isLessMax) speaker().speak("Максимальная длина пароля - " + MAX_LOGIN_LENGTH);
        return isLessMax & isMoreMin;
    }

    private boolean isGoodPassword(String password) {
        boolean notNull = password != null;
        if (notNull) {
            if (password.equals("exit")) throw new LoginCancelledException();
            return checkPasswordLength(password);
        }
        return false;
    }

    private boolean checkPasswordLength(String password) {
        boolean isMoreMin = password.length() >= MIN_PASSWORD_LENGTH;
        boolean isLessMax = password.length() <= MAX_PASSWORD_LENGTH;
        if (!isMoreMin) speaker().speak("Минимальная длина пароля - " + MIN_PASSWORD_LENGTH);
        if (!isLessMax) speaker().speak("Максимальная длина пароля - " + MAX_PASSWORD_LENGTH);
        return isLessMax & isMoreMin;
    }

    private void inputPassword(String login) {
        for (int i = 0; i < 5 && Controller.isNotLogged(); i++) {
            speaker().speak("Введите пароль:\n");
            var password = reader().read();
            if (isGoodPassword(password))
                if (!checkUserExist(login, password)) break;
        }
        if (Controller.isNotLogged()) speaker().speak("Авторизация провалена.");
    }

    private boolean checkUserExist(String login, String password) {
        User user = new User(login, PEPPER + password);
        Request<User> request = new Request<>("login", user);
        sendModule().submitSending(request);
        Response<?> response = receiveModule().getResponse();
        if (Objects.isNull(response.getData())) {
            speaker().speak(response.getComment());
            return false;
        } else {
            setUser(response, user);
            return true;
        }
    }

    private void setUser(Response<?> response, User user) {
        Controller.setIsLogged((Boolean) response.getData());
        if (Controller.isNotLogged()) {
            speaker().speak("Неверный пароль.");
        } else {
            user.setToken(response.getAccessToken());
            Controller.setUser(user);
            Controller.setRefreshToken(response.getRefreshToken());
            Controller.setIsLogged(true);
        }
    }

    private void register() {
        while (Controller.isNotLogged()) {
            try {
                speaker().speak("Введите логин:\n");
                var login = reader().read();
                if (isGoodLogin(login)) {
                    var password = newPassword();
                    String salt = getRandomSalt();
                    sendRegisterRequest(login, password, salt);
                }
            } catch (InvalidUserException e) {
                speaker().speak(e.getMessage());
            }
        }
    }

    private void sendRegisterRequest(String login, String password, String salt) {
        User user = new User(login, PEPPER + password);
        Request<User> request = new Request<>("register", user);
        request.setSalt(salt);
        sendModule().submitSending(request);
        var response = receiveModule().getResponse();
        checkRegisterResponse(response, user);
    }

    private void checkRegisterResponse(Response<?> response, User user){
        if (Objects.isNull(response.getData()))
            speaker().speak(response.getComment());
        else {
            speaker().speak((String) response.getData());
            user.setToken(response.getAccessToken());
            Controller.setUser(user);
            Controller.setRefreshToken(response.getRefreshToken());
            Controller.setIsLogged(true);
        }
    }

    private String newPassword() {
        for (int i = 0; i < 5; i++) {
            try {
                speaker().speak("Введите пароль:\n");
                String password = reader().read();
                return checkPassword(password);
            } catch (InvalidUserException e){
                speaker().speak(e.getMessage());
            }
        }
        throw new InvalidUserException("Не удалось задать пароль");
    }

    private String checkPassword(String password1){
        if (isGoodPassword(password1)) {
            speaker().speak("Повторите пароль:\n");
            String password2 = reader().read();
            if (Objects.isNull(password2)) throw new InvalidUserException("Не вводите пустую строку");
            if (!password1.equals(password2))
                throw new InvalidUserException("Пароли не совпадают.");
            return password1;
        }
        throw new InvalidUserException();
    }

    private String getRandomSalt() {
        return java.util.UUID.randomUUID().toString();
    }

    @Override
    public String getDescription() {
        return "";
    }
}

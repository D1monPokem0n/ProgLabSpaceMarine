package ru.prog.itmo.connection;

import java.io.Serializable;

public class User implements Serializable {
    private final String login;
    private String token;

    public User(String login, String token) {
        this.login = login;
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

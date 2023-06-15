package ru.prog.itmo.connection;

import java.io.Serializable;

public class Request<T> implements Serializable {
    private String commandType;
    private T data;
    private boolean isScriptCommand;
    private String salt;
    private User user;

    public Request(String commandType, T data){
        this.commandType = commandType;
        this.data = data;
        this.isScriptCommand = false;
        user = null;
    }

    public Request(String commandType, T data, boolean isScriptCommand){
        this.commandType = commandType;
        this.data = data;
        this.isScriptCommand = isScriptCommand;
        user = null;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public boolean isScriptCommand() {
        return isScriptCommand;
    }

    public void setScriptCommand(boolean scriptCommand) {
        isScriptCommand = scriptCommand;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}

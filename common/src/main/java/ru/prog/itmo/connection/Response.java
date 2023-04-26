package ru.prog.itmo.connection;

import java.io.Serializable;

public class Response<T> implements Serializable {
    private String commandType;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }
}

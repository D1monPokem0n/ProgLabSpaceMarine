package ru.prog.itmo.connection;

import java.io.Serializable;

public class Response<T> implements Serializable {
    private T data;
    String comment;

    public Response(T data) {
        this.data = data;
    }

    public Response() {
        data = null;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

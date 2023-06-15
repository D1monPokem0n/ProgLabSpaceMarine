package ru.prog.itmo.connection;

import java.io.Serializable;

public class Response<T> implements Serializable {
    private T data;
    private String comment;
    private boolean notAuthorized;
    private String accessToken;
    private String refreshToken;

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

    public void setNotAuthorized(boolean notAuthorized) {
        this.notAuthorized = notAuthorized;
    }

    public boolean isNotAuthorized() {
        return notAuthorized;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

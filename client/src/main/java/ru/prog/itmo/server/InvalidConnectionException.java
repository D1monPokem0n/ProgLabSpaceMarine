package ru.prog.itmo.server;

public class InvalidConnectionException extends RuntimeException{
    public InvalidConnectionException(String message){
        super(message);
    }
}

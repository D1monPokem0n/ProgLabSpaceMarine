package ru.prog.itmo.connection;

public class InvalidConnectionException extends RuntimeException{
    public InvalidConnectionException(String message){
        super(message);
    }
}

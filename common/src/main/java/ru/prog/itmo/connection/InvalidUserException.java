package ru.prog.itmo.connection;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException(String message) {
        super(message);
    }

    public InvalidUserException() {
    }
}

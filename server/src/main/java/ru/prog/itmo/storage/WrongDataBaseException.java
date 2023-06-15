package ru.prog.itmo.storage;

public class WrongDataBaseException extends RuntimeException{
    public WrongDataBaseException(String message){
        super(message);
    }
}

package ru.prog.itmo.storage;

public class NotWritableFileException extends Exception{
    public NotWritableFileException(String message){
        super(message);
    }
}

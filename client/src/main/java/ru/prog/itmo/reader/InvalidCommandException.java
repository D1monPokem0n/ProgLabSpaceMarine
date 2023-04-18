package ru.prog.itmo.reader;

public class InvalidCommandException extends Exception{
    public InvalidCommandException(String message){
        super(message);
    }
}

package ru.prog.itmo.storage;

public class WrongStorageFileException extends RuntimeException{
    public WrongStorageFileException(String message){
        super(message);
    }
}

package ru.prog.itmo.storage;


public class EmptyStorageException extends Exception {
    private final String message;
    public EmptyStorageException(String message){
        this.message = message;
    }
    public EmptyStorageException(){
        message = "Хранилище пустое.";
    }

    public String getMessage() {
        return message;
    }
}

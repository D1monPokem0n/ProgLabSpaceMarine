package ru.prog.itmo.storage;

public class StorageDBFatalError extends RuntimeException {
    public StorageDBFatalError (String message){
        super(message);
    }
}

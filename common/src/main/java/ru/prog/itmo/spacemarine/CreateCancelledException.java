package ru.prog.itmo.spacemarine;


public class CreateCancelledException extends RuntimeException {
    public CreateCancelledException() {
        super("Создание космодесантника отменено.");
    }

    public CreateCancelledException(String message) {
        super(message);}
    }

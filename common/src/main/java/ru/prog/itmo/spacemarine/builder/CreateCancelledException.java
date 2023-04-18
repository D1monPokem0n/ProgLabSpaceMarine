package ru.prog.itmo.spacemarine.builder;


public class CreateCancelledException extends RuntimeException {
    public CreateCancelledException() {
        super("Создание космодесантника отменено.");
    }

    public CreateCancelledException(String message) {
        super(message);}
    }

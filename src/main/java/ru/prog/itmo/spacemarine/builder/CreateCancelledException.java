package ru.prog.itmo.spacemarine.builder;


public class CreateCancelledException extends Exception {
    private final String message;

    public CreateCancelledException() {
        message = "Создание космодесантника отменено.";
    }

    public CreateCancelledException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

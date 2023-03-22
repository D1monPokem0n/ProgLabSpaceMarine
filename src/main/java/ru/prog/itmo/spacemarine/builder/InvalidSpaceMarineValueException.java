package ru.prog.itmo.spacemarine.builder;


public class InvalidSpaceMarineValueException extends Exception{
    private final String message;
    public InvalidSpaceMarineValueException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

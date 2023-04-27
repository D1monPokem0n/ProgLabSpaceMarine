package ru.prog.itmo.spacemarine.builder.script;

public class InvalidScriptException extends RuntimeException{
    public InvalidScriptException(String message){
        super(message);
    }
}

package ru.prog.itmo.command.script;

public class InvalidScriptException extends RuntimeException{
    public InvalidScriptException(String message){
        super(message);
    }
}

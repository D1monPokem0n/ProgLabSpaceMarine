package ru.prog.itmo.command.script;

public class EndOfScriptException extends RuntimeException{
    public EndOfScriptException(String message){
        super(message);
    }
    public EndOfScriptException(){
        super();
    }
}

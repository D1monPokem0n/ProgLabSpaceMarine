package ru.prog.itmo.speaker;

public class ConsoleSpeaker implements Speaker{
    public ConsoleSpeaker(){}
    public String speak(String message){
        System.out.println(message);
        return message;
    }
}

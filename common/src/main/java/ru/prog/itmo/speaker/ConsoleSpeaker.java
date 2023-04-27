package ru.prog.itmo.speaker;

public class ConsoleSpeaker implements Speaker{
    public ConsoleSpeaker(){}
    public void speak(String message){
        System.out.println(message);
    }
}

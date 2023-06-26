package ru.prog.itmo.control;

import ru.prog.itmo.speaker.Speaker;

public class SmartSpeaker implements Speaker {
    @Override
    public String speak(String message) {
        return message;
//        return Controller.messages().getString(message);
    }
}

package ru.prog.itmo.command;

import ru.prog.itmo.speaker.Speaker;

public abstract class ConsoleOCommand extends ConsoleCommand{
    private Speaker speaker;
    public ConsoleOCommand(Speaker speaker){
        this.speaker = speaker;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }
}

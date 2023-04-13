package ru.prog.itmo.command;

import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.speaker.Speaker;

public abstract class ConsoleIOCommand extends ConsoleOCommand {
    private SpaceMarineReader reader;
    public ConsoleIOCommand(Speaker speaker, SpaceMarineReader reader){
        super(speaker);
        this.reader = reader;
    }

    public void setReader(SpaceMarineReader reader) {
        this.reader = reader;
    }

    public SpaceMarineReader getReader() {
        return reader;
    }
}

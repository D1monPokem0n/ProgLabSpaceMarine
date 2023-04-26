package ru.prog.itmo.command;

import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.speaker.Speaker;

public abstract class ConsoleIOCommand extends ConsoleOCommand {
    private Reader reader;
    public ConsoleIOCommand(String commandType, Speaker speaker, Reader reader){
        super(commandType, speaker);
        this.reader = reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Reader getReader() {
        return reader;
    }
}

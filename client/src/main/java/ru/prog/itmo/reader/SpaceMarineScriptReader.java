package ru.prog.itmo.reader;

import ru.prog.itmo.command.script.EndOfScriptException;
import ru.prog.itmo.speaker.Speaker;

import java.io.InputStreamReader;
import java.util.Scanner;


public class SpaceMarineScriptReader implements SpaceMarineReader {
    private final Scanner scanner;
    private final Speaker speaker;

    public SpaceMarineScriptReader(Speaker speaker, InputStreamReader streamReader) {
        this.speaker = speaker;
        scanner = new Scanner(streamReader);
    }

    @Override
    public String read() {
        if (!scanner.hasNextLine()) {
            scanner.close();
            throw new EndOfScriptException();
        }
        String value = scanner.nextLine();
        speaker.speak(value);
        return value.equals("") ? null : value;
    }
}

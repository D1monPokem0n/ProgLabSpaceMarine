package ru.prog.itmo.reader;

import java.util.Scanner;

public class SpaceMarineConsoleReader implements SpaceMarineReader {
    public String read(){
        Scanner in = new Scanner(System.in);
        String inValue = in.nextLine();
        return inValue.equals("") ? null : inValue;
    }
}

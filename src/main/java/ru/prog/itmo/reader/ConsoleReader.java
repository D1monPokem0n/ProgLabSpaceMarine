package ru.prog.itmo.reader;

import java.util.Scanner;

public class ConsoleReader implements Reader {
    public String read(){
        Scanner in = new Scanner(System.in);
        String value = in.nextLine().trim();
        return value.equals("") ? null : value;
    }
}

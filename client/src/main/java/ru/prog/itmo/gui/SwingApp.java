package ru.prog.itmo.gui;

import javax.swing.*;
import java.awt.*;

public class SwingApp {
    public SwingApp() {
        var frame = new MainFrame();
        frame.setTitle("test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(240*3, 120*3);
        frame.setVisible(true);
    }

    public static void start() {
        EventQueue.invokeLater(SwingApp::new);
    }
}
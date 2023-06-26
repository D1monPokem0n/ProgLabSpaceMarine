package ru.prog.itmo.gui;

import ru.prog.itmo.control.CommandManager;
import ru.prog.itmo.speaker.Speaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.HashMap;

public class CommandsPanel extends JPanel implements ActionListener {
    private CommandManager commandManager;
    private Speaker speaker;
    private HashMap<String, JButton> buttons;
    private MarineFrame marineFrame;

    public CommandsPanel(CommandManager commandManager, Speaker speaker) {
        initFrame(commandManager, speaker);
        initComponents();
        setSizesAndAdd();
        addEvents();
    }

    private void initFrame(CommandManager commandManager, Speaker speaker) {
        this.commandManager = commandManager;
        this.speaker = speaker;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentY(JComponent.CENTER_ALIGNMENT);
    }

    private void initComponents() {
        buttons = getButtons();
        marineFrame = new MarineFrame(
                commandManager,
                speaker,
                buttons.get("add"),
                buttons.get("add_if_min"),
                buttons.get("remove_greater"),
                buttons.get("update")
        );
        marineFrame.setVisible(false);
        marineFrame.setLocation(100, 100);
    }

    private void setSizesAndAdd() {
        for (JButton button : buttons.values()) {
            var buttonSize = new Dimension(200, 25);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.setPreferredSize(buttonSize);
            add(button);
        }
    }

    private void addEvents() {
        for (JButton button : buttons.values())
            button.addActionListener(this);
    }

    private HashMap<String, JButton> getButtons() {
        var buttons = new HashMap<String, JButton>();
        buttons.put("add", new JButton(speaker.speak("add")));
        buttons.put("add_if_min", new JButton(speaker.speak("add_if_min")));
        buttons.put("help", new JButton(speaker.speak("help")));
        buttons.put("history", new JButton(speaker.speak("history")));
        buttons.put("info", new JButton(speaker.speak("info")));
        buttons.put("clear", new JButton(speaker.speak("clear")));
        buttons.put("update", new JButton(speaker.speak("remove_any_by_chapter")));
        buttons.put("remove_by_id", new JButton(speaker.speak("remove_by_id")));
        buttons.put("remove_greater", new JButton(speaker.speak("remove_greater")));
        buttons.put("execute_script", new JButton(speaker.speak("execute_script")));
        buttons.put("exit", new JButton(speaker.speak("exit")));
        return buttons;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttons.get("add") ||
            e.getSource() == buttons.get("add_if_min") ||
            e.getSource() == buttons.get("remove_greater"))
            marineFrame.setVisible(true);
        if (e.getSource() == buttons.get("exit"))
            System.exit(0);
        if (e.getSource() == buttons.get("help"))
            JOptionPane.showMessageDialog(this,
                    commandManager.executeHelpCommand());
        if (e.getSource() == buttons.get("history"))
            JOptionPane.showMessageDialog(this,
                    commandManager.executeHistoryCommand());
        if (e.getSource() == buttons.get("info"))
            JOptionPane.showMessageDialog(this,
                    commandManager.executeInfoCommand());
        if (e.getSource() == buttons.get("clear"))
            JOptionPane.showMessageDialog(this,
                    commandManager.executeClearCommand());
        if (e.getSource() == buttons.get("update"))
            executeUpdate();
        if (e.getSource() == buttons.get("remove_by_id"))
            executeRemoveById();
        if (e.getSource() == buttons.get("execute_script"))
            executeScript();
    }

    private void executeUpdate() {
        var value = JOptionPane.showInputDialog(speaker.speak("Input marines id"));
        try {
            long id = Long.parseLong(value);
            var marine = commandManager.executeGetById(id);
            if (marine == null) JOptionPane.showMessageDialog(this, speaker.speak("MarineDoesNotExist"));

        } catch (NumberFormatException | NullPointerException e){
            JOptionPane.showMessageDialog(this, speaker.speak("InvalidId"));
        }
    }

    private void executeRemoveById() {
        var value = JOptionPane.showInputDialog(speaker.speak("Input marines id"));
        try {
            long id = Long.parseLong(value);
            JOptionPane.showMessageDialog(this,
                    commandManager.executeRemoveByIdCommand(id));
        } catch (NumberFormatException | NullPointerException e){
            JOptionPane.showMessageDialog(this, speaker.speak("InvalidId"));
        }

    }

    private void executeScript() {
        var value = JOptionPane.showInputDialog(speaker.speak("Input script's filename"));
        try {
            var filePath = Path.of(value);
            if (!Files.exists(filePath) || !Files.isReadable(filePath))
                JOptionPane.showMessageDialog(this, speaker.speak("File not exist"));
            else commandManager.executeScript(filePath);
        } catch (InvalidPathException e){
            JOptionPane.showMessageDialog(this, speaker.speak("Invalid filename"));
        }
    }
}

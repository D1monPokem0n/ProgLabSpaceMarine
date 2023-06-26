package ru.prog.itmo.gui;

import ru.prog.itmo.speaker.Speaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SortPanel extends JPanel implements ActionListener {
    private Store store;
    private Speaker speaker;
    private JButton applyButton;
    private JLabel sortByLabel;
    private JLabel reversedSortLabel;
    private JTextField filterTextFiled;
    private JButton findButton;
    private JComboBox<String> sortComboBox;
    private JCheckBox reversedSortBox;

    public SortPanel(Store store, Speaker speaker) {
        initFrame();
        initComponents(store, speaker);
        setSizes();
        addComponents();
        addEvents();
    }

    private void addEvents() {
        applyButton.addActionListener(this);
        findButton.addActionListener(this);
    }

    private void initFrame() {
        setBounds(200, 200, 220, 350);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    private void setSizes() {
        sortByLabel.setBounds(10, 40, 75, 30);
        sortComboBox.setBounds(100, 40, 75, 30);
        reversedSortLabel.setBounds(10, 120, 100, 30);
        reversedSortBox.setBounds(120, 120, 25, 25);
        applyButton.setBounds(40, 210, 70, 30);
        filterTextFiled.setBounds(10, 250, 100, 25);
        filterTextFiled.setMinimumSize(new Dimension(100,25));
        filterTextFiled.setPreferredSize(new Dimension(100, 25));
        filterTextFiled.setMaximumSize(new Dimension(100, 25));
        findButton.setBounds(130, 250, 100, 300);
    }

    private void addComponents() {
        var sort = new JPanel();
        sort.add(sortByLabel);
        sort.add(sortComboBox);
        var reverse = new JPanel();
        reverse.add(reversedSortLabel);
        reverse.add(reversedSortBox);
        var apply = new JPanel();
        apply.add(applyButton);
        sort.add(reverse);
        sort.add(apply);
        add(sort);
        var find = new JPanel();
        find.add(filterTextFiled);
        find.add(findButton);
        add(find);
//        add(reverse,BorderLayout.CENTER);
//        add(apply,BorderLayout.CENTER);
    }

    private void initComponents(Store store, Speaker speaker) {
        this.store = store;
        this.speaker = speaker;
        applyButton = new JButton(speaker.speak("Apply"));
        sortByLabel = new JLabel(speaker.speak("Sort by"));
        reversedSortLabel = new JLabel(speaker.speak("Reverse sorting"));
        sortComboBox = new JComboBox<>();
        sortComboBox.addItem(speaker.speak("None"));
        for (var filedName : Store.FIELDS_NAME) {
            sortComboBox.addItem(filedName);
        }
        reversedSortBox = new JCheckBox();
        filterTextFiled = new JTextField();
        findButton = new JButton(speaker.speak("Find"));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == applyButton) {
            var sortItem = (String) sortComboBox.getSelectedItem();
            var isReversedSort = reversedSortBox.isSelected();
            store.setSortItem(sortItem);
            store.setReversedSort(isReversedSort);
        }
        if (e.getSource() == findButton) {
            var filterItem = filterTextFiled.getText();
            store.setFilterItem(filterItem);
        }
    }
}

package ru.prog.itmo.gui;

import javax.swing.*;
import java.awt.*;

public class WorkPanel extends JPanel {
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel centerPanel;

    public WorkPanel() {
        initPanels();
        setLayout(new GridBagLayout());
        setCenterPanel();
        setLeftPanel();
        setRightPanel();
    }

    private void initPanels() {
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        centerPanel = new JPanel();
    }

    private void setLeftPanel(){
        var leftConstraints = new GridBagConstraints();
        leftConstraints.weightx = 100;
        leftConstraints.weighty = 100;
        leftConstraints.gridx = 0;
        leftConstraints.gridy = 0;
        leftConstraints.gridheight = 3;
        leftConstraints.gridwidth = 1;
        add(leftPanel, leftConstraints);
    }

    private void setRightPanel(){
        var rightConstraints = new GridBagConstraints();
        rightConstraints.weightx = 100;
        rightConstraints.weighty = 100;
        rightConstraints.gridx = 6;
        rightConstraints.gridy = 0;
        rightConstraints.gridheight = 3;
        rightConstraints.gridwidth = 1;
        add(rightPanel, rightConstraints);
    }

    private void setCenterPanel(){
        var southConstraints = new GridBagConstraints();
        southConstraints.weightx = 100;
        southConstraints.weighty = 100;
        southConstraints.gridx = 1;
        southConstraints.gridy = 0;
        southConstraints.gridheight = 3;
        southConstraints.gridwidth = 5;
        add(centerPanel, southConstraints);
    }
}

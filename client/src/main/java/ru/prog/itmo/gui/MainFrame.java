package ru.prog.itmo.gui;

import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.control.ClientState;
import ru.prog.itmo.control.CommandManager;
import ru.prog.itmo.control.Controller;
import ru.prog.itmo.speaker.Speaker;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class MainFrame extends JFrame {
    private JPanel leftUpPanel;
    private JPanel leftDownPanel;
    private JPanel rightUpPanel;
    private JPanel rightDownPanel;
    private SortPanel sortPanel;
    private JMenuBar menuBar;
    private JMenu langMenu;
    private Store store;
    private Speaker speaker;
    private ClientState clientState;
    private CommandManager commandManager;



    public MainFrame(CommandManager commandManager, Speaker speaker, ClientState clientState) {
        initFrame(commandManager, speaker, clientState);
        setComponents();
        addComponents();
        setVisible(true);
        startRefreshTable();
    }

    private void startRefreshTable() {
        var updateThread = new Thread(() -> {
            while (clientState.isWorkStatus()) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    var marines = commandManager.executeShowCommand();
                    store.setMarines(marines);
                    repaint();
                    rightDownPanel.repaint();
                } catch (InterruptedException | InvalidConnectionException e) {
                    JOptionPane.showMessageDialog(this, speaker.speak("Some mistakes..."));
                }
            }
        }
        );
        updateThread.start();
    }

    private void initFrame(CommandManager commandManager, Speaker speaker, ClientState clientState) {
        setBounds(0, 0, 1500, 800);
        var layout = new GridBagLayout();
        setLayout(layout);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        var marines = commandManager.executeShowCommand();
        store = new Store(marines);
        this.commandManager = commandManager;
        this.speaker = speaker;
        this.clientState = clientState;
    }

    private void addComponents() {
        menuBar = new JMenuBar();
        add(leftUpPanel, getLeftUpConstraints());
        add(rightUpPanel, getRightUpConstraints());
        add(leftDownPanel, getLeftDownConstraints());
        add(rightDownPanel, getRightDownConstraints());
        add(sortPanel, getSortConstraints());
//        langMenu = new JMenu();
//        langMenu.add(new JRadioButtonMenuItem("Russian"));
//        langMenu.add(new JRadioButtonMenuItem("Bulgarian"));
//        langMenu.add(new JRadioButtonMenuItem("Macedonian"));
//        langMenu.add(new JRadioButtonMenuItem("Spain(HCA)"));
//        menuBar.add(langMenu);
//        menuBar.setSize(new Dimension(1500, 30));
//        setJMenuBar(menuBar);
    }

    private GridBagConstraints getSortConstraints(){
        return new GBC(1, 0, 1, 1)
                .setWeight(100, 100)
                .setAnchor(GridBagConstraints.CENTER)
                .setInsets(10)
                .setFill(GridBagConstraints.BOTH);
    }

    private GridBagConstraints getLeftUpConstraints() {
        return new GBC(0, 0, 1, 1)
                .setWeight(100, 100)
                .setAnchor(GBC.NORTHWEST)
                .setInsets(10)
                .setFill(GBC.EAST);
    }

    private GridBagConstraints getLeftDownConstraints() {
        return new GBC(0, 1, 1, 1)
                .setWeight(100, 100)
                .setAnchor(GBC.WEST)
                .setInsets(10)
                .setFill(GBC.EAST);
    }

    private GridBagConstraints getRightUpConstraints() {
        return new GBC(2, 0, 5, 1)
                .setWeight(0, 0)
                .setInsets(0, 0, 0, 20);
    }

    private GridBagConstraints getRightDownConstraints() {
        return new GBC(2, 1, 5, 1)
                .setWeight(0, 0)
                .setAnchor(GBC.CENTER)
                .setInsets(5);
    }

    private void setComponents() {
        leftUpPanel = getLeftUpPanel();
        leftDownPanel = getLeftDownPanel();
        rightUpPanel = getRightUpPanel();
        rightDownPanel = getRightDownPanel();
        sortPanel = getSortPanel();
    }

    private SortPanel getSortPanel() {
        var panel = new SortPanel(store, speaker);
        panel.setMinimumSize(new Dimension(220, 300));
        panel.setPreferredSize(new Dimension(220, 300));
        panel.setMinimumSize(new Dimension(230, 310));
        return panel;
    }

    private JPanel getRightDownPanel() {
        //        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
//        var axis = new PaintCoordinates();
//        panel.add(axis);
        return new PaintCoordinates(store);
    }

    private JPanel getRightUpPanel() {
        var tableModel = new SpaceMarineTableModel(store, speaker);
        var table = new JTable(tableModel);
        setColumnsSize(table);
        table.setAutoCreateRowSorter(false);
        var scrollPane = new JScrollPane(table);
        var panel = new JPanel();
        var panelSize = new Dimension(900, 350);
        panel.setMinimumSize(panelSize);
        panel.setPreferredSize(panelSize);
        panel.setMaximumSize(panelSize);
        scrollPane.setMinimumSize(panelSize);
        scrollPane.setMaximumSize(panelSize);
        scrollPane.setPreferredSize(panelSize);
        panel.add(scrollPane);
        return panel;
    }

    private void setColumnsSize(JTable table) {
        setColumnWidth(table, 0, 50);
        setColumnWidth(table, 1, 100);
        setColumnWidth(table, 2, 75);
        setColumnWidth(table, 3, 75);
        setColumnWidth(table, 4, 125);
        setColumnWidth(table, 5, 75);
        setColumnWidth(table, 6, 50);
        setColumnWidth(table, 7, 50);
        setColumnWidth(table, 8, 75);
        setColumnWidth(table, 9, 50);
        setColumnWidth(table, 10, 50);
        setColumnWidth(table, 11, 50);
        setColumnWidth(table, 12, 50);
        setColumnWidth(table, 13, 75);
    }

    private void setColumnWidth(JTable table, int columnIndex, int width) {
        var column = table.getColumnModel().getColumn(columnIndex);
        column.setMaxWidth(width + 5);
        column.setMinWidth(width - 5);
        column.setPreferredWidth(width);
    }

    private JPanel getLeftDownPanel() {
        return new CommandsPanel(commandManager, speaker);
    }


    private JPanel getLeftUpPanel() {
        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        var label = new JLabel(speaker.speak("user: "));
        label.setBounds(10, 10, 50, 25);
        var loginLabel = new JLabel(Controller.getUser().getLogin());
        loginLabel.setBounds(60, 10, 75, 25);
//       var logOutButton = new JButton(speaker.speak("logout"));
//        logOutButton.setBounds(panel.getWidth() / 2, 40, 50, 25);
        panel.add(label);
        panel.add(loginLabel);
//        panel.add(logOutButton);
        return panel;
    }


    public static class GBC extends GridBagConstraints {

        public GBC(int gridx, int gridy) {
            this.gridx = gridx;
            this.gridy = gridy;
        }

        public GBC(int gridx, int gridy, int gridwidth, int gridheight) {
            this.gridx = gridx;
            this.gridy = gridy;
            this.gridwidth = gridwidth;
            this.gridheight = gridheight;
        }

        public GBC setAnchor(int anchor) {
            this.anchor = anchor;
            return this;
        }

        public GBC setFill(int fill) {
            this.fill = fill;
            return this;
        }

        public GBC setWeight(double weightx, double weighty) {
            this.weightx = weightx;
            this.weighty = weighty;
            return this;
        }

        public GBC setInsets(int distance) {
            this.insets = new Insets(distance, distance, distance, distance);
            return this;
        }

        public GBC setInsets(int top, int left,
                             int bottom, int right) {
            this.insets = new Insets(top, left, bottom, right);
            return this;
        }

        public GBC setIpad(int ipadx, int ipady) {
            this.ipadx = ipadx;
            this.ipady = ipady;
            return this;
        }

    }
}

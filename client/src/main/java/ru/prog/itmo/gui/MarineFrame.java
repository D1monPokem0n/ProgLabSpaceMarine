package ru.prog.itmo.gui;

import ru.prog.itmo.control.CommandManager;
import ru.prog.itmo.control.Controller;
import ru.prog.itmo.spacemarine.AstartesCategory;
import ru.prog.itmo.spacemarine.MeleeWeapon;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.spacemarine.coordinates.Coordinates;
import ru.prog.itmo.speaker.Speaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;


public class MarineFrame extends JFrame implements ActionListener {
    private final Icon redHeart = new ImageIcon("redHeart");
    private final Icon blackHeart = new ImageIcon("blackHeart");
    private final CommandManager commandManager;
    private final Speaker speaker;
    private final Container container = getContentPane();
    private final JButton outerAddButton;
    private final JButton outerUpdateButton;
    private final JButton outerAddIfMinButton;
    private final JButton outerRemoveGreaterButton;
    private JButton lastPressedOuterButton;
    private JLabel nameLabel;
    private JLabel coordinatesXLabel;
    private JLabel coordinatesYLabel;
    private JLabel healthLabel;
    private JLabel heartCountLabel;
    private JLabel categoryLabel;
    private JLabel meleeWeaponLabel;
    private JLabel doesChapterExistLabel;
    private JLabel chapterNameLabel;
    private JLabel chapterParentLegionLabel;
    private JLabel chapterMarinesCountLabel;
    private JLabel chapterWorldLabel;
    private JTextField nameTextField;
    private JTextField coordinateXTextField;
    private JTextField coordinateYTextField;
    private JTextField healthTextField;
    private ButtonGroup heartCountGroup;
    private XRadioButton heartCount1;
    private XRadioButton heartCount2;
    private XRadioButton heartCount3;
    private ButtonGroup categoryGroup;
    private JRadioButton scoutButton;
    private JRadioButton librarianButton;
    private JRadioButton chaplainButton;
    private JRadioButton apothecaryButton;
    private JRadioButton noneButton;
    private ButtonGroup weaponGroup;
    private JRadioButton manreaperButton;
    private JRadioButton lightingClawButton;
    private JRadioButton powerFistButton;
    private JCheckBox doesChapterExistCheck;
    private JTextField chapterNameTextField;
    private JTextField chapterParentLegionTextField;
    private JSlider chapterMarinesCountSlider;
    private JTextField chapterWorldTextField;

    private JButton applyButton;
    private JPanel namePanel;
    private JPanel coordinateXPanel;
    private JPanel coordinateYPanel;
    private JPanel healthPanel;
    private JPanel heartCountPanel;
    private JPanel categoryPanel;
    private JPanel weaponPanel;
    private JPanel chapterExistPanel;
    private JPanel chapterPanel;
    private JPanel chapterNamePanel;
    private JPanel chapterParentLegionPanel;
    private JPanel chapterMarinesCountPanel;
    private JPanel chapterWorldPanel;
    private JPanel applyPanel;

    private SpaceMarine marineToUpdate;


    public MarineFrame(CommandManager commandManager,
                       Speaker speaker,
                       JButton outerAddButton,
                       JButton outerAddIfMinButton,
                       JButton outerRemoveGreaterButton,
                       JButton outerUpdateButton) {
        this.commandManager = commandManager;
        this.speaker = speaker;
        this.outerAddButton = outerAddButton;
        this.outerAddIfMinButton = outerAddIfMinButton;
        this.outerRemoveGreaterButton = outerRemoveGreaterButton;
        this.outerUpdateButton= outerUpdateButton;
        initComponents();
        setLayoutManager();
        setLocationAndSize();
//        addComponentsToContainer();
        addActionEvent();
        doesChapterExistCheck.setSelected(true);
    }

    private void initComponents() {
        initLabels();
        initTextFields();
        initRadioButtons();
//        initPanels();
        initOther();
    }

    private void initLabels() {
        nameLabel = new JLabel(speaker.speak("Name"));
        coordinatesXLabel = new JLabel(speaker.speak("Coordinate X"));
        coordinatesYLabel = new JLabel(speaker.speak("Coordinate Y"));
        healthLabel = new JLabel(speaker.speak("Health"));
        heartCountLabel = new JLabel(speaker.speak("Heart count"));
        categoryLabel = new JLabel(speaker.speak("Astartes category"));
        meleeWeaponLabel = new JLabel(speaker.speak("Melee weapon"));
        doesChapterExistLabel = new JLabel(speaker.speak("Does marine belong to any chapter"));
        chapterNameLabel = new JLabel(speaker.speak("Chapter name"));
        chapterParentLegionLabel = new JLabel(speaker.speak("Parent legion"));
        chapterMarinesCountLabel = new JLabel(speaker.speak("Marines count"));
        chapterWorldLabel = new JLabel(speaker.speak("World"));
    }

    private void initTextFields() {
        nameTextField = new JTextField(100);
        coordinateXTextField = new JTextField(100);
        coordinateYTextField = new JTextField(100);
        healthTextField = new JTextField(100);
        chapterNameTextField = new JTextField(100);
        chapterParentLegionTextField = new JTextField(100);
        chapterWorldTextField = new JTextField(100);
    }

    private void initRadioButtons() {
        heartCountGroup = new ButtonGroup();
        heartCount1 = new XRadioButton();
        heartCount1.setSelected(true);
        heartCount2 = new XRadioButton();
        heartCount3 = new XRadioButton();

        categoryGroup = new ButtonGroup();
        scoutButton = new JRadioButton(speaker.speak("Scout"));
        librarianButton = new JRadioButton(speaker.speak("Librarian"));
        chaplainButton = new JRadioButton(speaker.speak("Chaplain"));
        apothecaryButton = new JRadioButton(speaker.speak("Apothecary"));
        noneButton = new JRadioButton(speaker.speak("None"));
        noneButton.setSelected(true);

        weaponGroup = new ButtonGroup();
        manreaperButton = new JRadioButton(speaker.speak("Manreaper"));
        lightingClawButton = new JRadioButton(speaker.speak("Lighting Claw"));
        powerFistButton = new JRadioButton(speaker.speak("Power Fist"));
        powerFistButton.setSelected(true);
    }

//    private void initPanels() {
//        namePanel = new JPanel();
//        coordinateXPanel = new JPanel();
//        coordinateYPanel = new JPanel();
//        healthPanel = new JPanel();
//        heartCountPanel = new JPanel();
//        categoryPanel = new JPanel();
//        weaponPanel = new JPanel();
//        chapterExistPanel = new JPanel();
//        chapterPanel = new JPanel();
//        chapterNamePanel = new JPanel();
//        chapterParentLegionPanel = new JPanel();
//        chapterMarinesCountPanel = new JPanel();
//        chapterWorldPanel = new JPanel();
//        applyPanel = new JPanel();
//    }

    private void initOther() {
        doesChapterExistCheck = new JCheckBox();
        chapterMarinesCountSlider = new JSlider(1, 1000, 1);
        chapterMarinesCountSlider.setMajorTickSpacing(100);
        chapterMarinesCountSlider.setMinorTickSpacing(1);
        chapterMarinesCountSlider.setPaintTicks(true);
        chapterMarinesCountSlider.setSnapToTicks(true);
        chapterMarinesCountSlider.setPaintLabels(true);
        applyButton = new JButton(speaker.speak("Apply"));
    }

    private void setLayoutManager() {
//        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
//        container.setLayout(new FlowLayout());
//        container.setLayout(null);
        container.setLayout(new GridBagLayout());
    }

    private void setLocationAndSize() {
        setBounds(new Rectangle(750, 1500));
        setLabels();
        setTextFields();
        setRadioButtons();
        setPanels();
    }

    private void setLabels() {
        nameLabel.setBounds(20, 20, 200, 25);
        coordinatesXLabel.setBounds(20, 60, 200, 25);
        coordinatesYLabel.setBounds(20, 100, 200, 25);
        healthLabel.setBounds(20, 140, 200, 25);
        heartCountLabel.setBounds(20, 180, 200, 25);
        categoryLabel.setBounds(20, 220, 200, 25);
        meleeWeaponLabel.setBounds(20, 260, 200, 25);
        doesChapterExistLabel.setBounds(20, 300, 200, 25);
        chapterNameLabel.setBounds(20, 340, 200, 25);
        chapterParentLegionLabel.setBounds(20, 380, 200, 25);
        chapterMarinesCountLabel.setBounds(20, 420, 200, 25);
        chapterWorldLabel.setBounds(20, 460, 200, 25);
    }

    private void setTextFields() {
        var textSize = new Dimension(200,25);
        nameTextField.setBounds(20, 20, 200, 25);
        nameTextField.setMinimumSize(textSize);
        nameTextField.setPreferredSize(textSize);
        nameTextField.setMaximumSize(textSize);

        coordinateXTextField.setBounds(20, 60, 200, 25);
        coordinateXTextField.setMinimumSize(textSize);
        coordinateXTextField.setPreferredSize(textSize);
        coordinateYTextField.setMaximumSize(textSize);

        coordinateYTextField.setBounds(20, 100, 200, 25);
        coordinateYTextField.setMinimumSize(textSize);
        coordinateYTextField.setPreferredSize(textSize);
        coordinateYTextField.setMaximumSize(textSize);

        healthTextField.setBounds(20, 140, 200, 25);
        healthTextField.setMinimumSize(textSize);
        healthTextField.setPreferredSize(textSize);
        healthTextField.setMaximumSize(textSize);

        chapterNameTextField.setBounds(20, 340, 200, 25);
        chapterNameTextField.setMinimumSize(textSize);
        chapterNameTextField.setPreferredSize(textSize);
        chapterNameTextField.setMaximumSize(textSize);

        chapterParentLegionTextField.setBounds(220, 380, 200, 25);
        chapterParentLegionTextField.setMinimumSize(textSize);
        chapterParentLegionTextField.setPreferredSize(textSize);
        chapterParentLegionTextField.setMaximumSize(textSize);

        chapterMarinesCountSlider.setBounds(260, 420, 500, 50);
        chapterMarinesCountSlider.setMinimumSize(new Dimension(500, 50));
        chapterMarinesCountSlider.setPreferredSize(new Dimension(500, 50));
        chapterMarinesCountSlider.setMaximumSize(new Dimension(500, 50));

        chapterWorldTextField.setBounds(20, 460, 200, 25);
        chapterWorldTextField.setMinimumSize(textSize);
        chapterWorldTextField.setPreferredSize(textSize);
        chapterWorldTextField.setMaximumSize(textSize);
    }

    private void setRadioButtons() {
        heartCount1.addToGroup(heartCountGroup);
        heartCount1.setBounds(250, 180, 25, 25);
        heartCount2.addToGroup(heartCountGroup);
        heartCount2.setBounds(300, 180, 25, 25);
        heartCount3.addToGroup(heartCountGroup);
        heartCount1.setBounds(350, 180, 25, 25);
        categoryGroup.add(scoutButton);
        scoutButton.setBounds(250, 220, 25, 25);
        categoryGroup.add(librarianButton);
        scoutButton.setBounds(300, 220, 25, 25);
        categoryGroup.add(chaplainButton);
        scoutButton.setBounds(350, 220, 25, 25);
        categoryGroup.add(apothecaryButton);
        scoutButton.setBounds(400, 220, 25, 25);
        categoryGroup.add(noneButton);
        scoutButton.setBounds(450, 220, 25, 25);
        weaponGroup.add(manreaperButton);
        manreaperButton.setBounds(250, 260, 25, 25);
        weaponGroup.add(lightingClawButton);
        manreaperButton.setBounds(300, 260, 25, 25);
        weaponGroup.add(powerFistButton);
        manreaperButton.setBounds(350, 260, 25, 25);
    }

    private void setPanels() {
        container.add(nameLabel, new MainFrame.GBC(0, 0, 1, 1)
                .setFill(GridBagConstraints.WEST).setInsets(10));
        container.add(nameTextField, new MainFrame.GBC(1,0,1,1)
                .setFill(GridBagConstraints.EAST).setInsets(10));

        container.add(coordinatesXLabel, new MainFrame.GBC(0,1,1,1)
                .setFill(GridBagConstraints.WEST).setInsets(10));
        container.add(coordinateXTextField, new MainFrame.GBC(1,1,1,1)
                .setFill(GridBagConstraints.EAST).setInsets(10));

        container.add(coordinatesYLabel, new MainFrame.GBC(0,2,1,1)
                .setFill(GridBagConstraints.WEST).setInsets(10));
        container.add(coordinateYTextField, new MainFrame.GBC(1,2,1,1)
                .setFill(GridBagConstraints.EAST).setInsets(10));

        container.add(healthLabel, new MainFrame.GBC(0,3,1,1)
                .setFill(GridBagConstraints.WEST).setInsets(10));
        container.add(healthTextField,new MainFrame.GBC(1,3,1,1)
                .setFill(GridBagConstraints.EAST).setInsets(10));

        container.add(heartCountLabel, new MainFrame.GBC(0,4,1,1)
                .setFill(GridBagConstraints.WEST).setInsets(10));
        container.add(heartCount1,new MainFrame.GBC(1,4,1,1)
                .setFill(GridBagConstraints.EAST).setAnchor(GridBagConstraints.WEST).setInsets(10));
        container.add(heartCount2, new MainFrame.GBC(2,4,1,1)
                .setFill(GridBagConstraints.EAST).setAnchor(GridBagConstraints.WEST).setInsets(10));
        container.add(heartCount3, new MainFrame.GBC(3,4,1,1)
                .setFill(GridBagConstraints.EAST).setAnchor(GridBagConstraints.WEST).setInsets(10));

        container.add(categoryLabel, new MainFrame.GBC(0,5,1,1)
                .setFill(GridBagConstraints.WEST).setInsets(10));
        container.add(scoutButton,new MainFrame.GBC(1,5,1,1)
                .setFill(GridBagConstraints.EAST).setAnchor(GridBagConstraints.WEST).setInsets(10));
        container.add(librarianButton,new MainFrame.GBC(2,5,1,1)
                .setFill(GridBagConstraints.EAST).setAnchor(GridBagConstraints.WEST).setInsets(10));
        container.add(chaplainButton,new MainFrame.GBC(3,5,1,1)
                .setFill(GridBagConstraints.EAST).setAnchor(GridBagConstraints.WEST).setInsets(10));
        container.add(apothecaryButton, new MainFrame.GBC(4,5,1,1)
                .setFill(GridBagConstraints.EAST).setAnchor(GridBagConstraints.WEST).setInsets(10));
        container.add(noneButton, new MainFrame.GBC(5,5,1,1)
                .setFill(GridBagConstraints.EAST).setAnchor(GridBagConstraints.WEST).setInsets(10));

        container.add(meleeWeaponLabel, new MainFrame.GBC(0,6,1,1)
                .setFill(GridBagConstraints.WEST).setInsets(10));
        container.add(manreaperButton, new MainFrame.GBC(1,6,1,1)
                .setFill(GridBagConstraints.EAST).setAnchor(GridBagConstraints.WEST).setInsets(10));
        container.add(lightingClawButton, new MainFrame.GBC(2,6,1,1)
                .setFill(GridBagConstraints.EAST).setAnchor(GridBagConstraints.WEST).setInsets(10));
        container.add(powerFistButton, new MainFrame.GBC(3,6,1,1)
                .setFill(GridBagConstraints.EAST).setAnchor(GridBagConstraints.WEST).setInsets(10));

        container.add(doesChapterExistLabel, new MainFrame.GBC(0,7,1,1)
                .setFill(GridBagConstraints.WEST).setInsets(10));
        container.add(doesChapterExistCheck,new MainFrame.GBC(1,7,1,1)
                .setFill(GridBagConstraints.EAST).setInsets(10));

        container.add(chapterNameLabel, new MainFrame.GBC(0,8,1,1)
                .setFill(GridBagConstraints.WEST).setInsets(10));
        container.add(chapterNameTextField,new MainFrame.GBC(1,8,1,1)
                .setFill(GridBagConstraints.EAST).setInsets(10));

        container.add(chapterParentLegionLabel, new MainFrame.GBC(0,9,1,1)
                .setFill(GridBagConstraints.WEST).setInsets(10));
        container.add(chapterParentLegionTextField,new MainFrame.GBC(1,9,1,1)
                .setFill(GridBagConstraints.EAST).setInsets(10));

        container.add(chapterMarinesCountLabel, new MainFrame.GBC(0,10,1,1)
                .setFill(GridBagConstraints.WEST).setInsets(10));
        container.add(chapterMarinesCountSlider,new MainFrame.GBC(1,10,5,1)
                .setFill(GridBagConstraints.EAST).setInsets(10));

        container.add(chapterWorldLabel, new MainFrame.GBC(0,11,1,1)
                .setFill(GridBagConstraints.WEST).setInsets(10));
        container.add(chapterWorldTextField,new MainFrame.GBC(1,11,1,1)
                .setFill(GridBagConstraints.EAST).setInsets(10));

//        chapterPanel.add(chapterNameLabel);
//        chapterPanel.add(chapterNameTextField);
//        chapterPanel.add(chapterParentLegionLabel);
//        chapterPanel.add(chapterParentLegionTextField);
//        chapterPanel.add(chapterMarinesCountLabel);
//        chapterPanel.add(chapterMarinesCountSlider);
//        chapterPanel.add(chapterWorldLabel);
//        chapterPanel.add(chapterWorldTextField);
//        chapterPanel.add(chapterParentLegionPanel);
//        chapterPanel.add(chapterMarinesCountPanel);
//        chapterPanel.add(chapterWorldPanel);
//        chapterPanel.setVisible(false);

        container.add(applyButton, new MainFrame.GBC(0,12,2,1)
                .setFill(GridBagConstraints.BOTH).setInsets(10).setAnchor(GridBagConstraints.CENTER));
    }


//    private void addComponentsToContainer() {
//        container.add(namePanel, new MainFrame.GBC());
//        container.add(coordinateXPanel);
//        container.add(coordinateYPanel);
//        container.add(healthPanel);
//        container.add(heartCountPanel);
//        container.add(categoryPanel);
//        container.add(weaponPanel);
//        container.add(chapterExistPanel);
//        container.add(chapterPanel);
//        container.add(applyPanel);
//    }

    private void addActionEvent() {
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        addVerifiersToTextFields();
        addEventsToRadioButtons();
        addEventsToFunctionalButtons();
        doesChapterExistCheck.addActionListener(this);
    }

    private void addEventsToFunctionalButtons() {
        applyButton.addActionListener(this);
        outerAddButton.addActionListener(this);
        outerAddIfMinButton.addActionListener(this);
        outerRemoveGreaterButton.addActionListener(this);
    }

    private void addEventsToRadioButtons() {
        heartCount1.addActionListener(this);
        heartCount2.addActionListener(this);
        heartCount3.addActionListener(this);
    }

    private void addVerifiersToTextFields() {
        nameTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                var textInput = (JTextField) input;
                if ((textInput.getText() == null)) return false;
                return !textInput.getText().trim().equals("");
            }
        });


        coordinateXTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                var textInput = (JTextField) input;
                try {
                    Float.parseFloat(textInput.getText());
                    return true;
                } catch (NullPointerException | NumberFormatException e) {
                    return false;
                }
            }
        });


        coordinateYTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                var textInput = (JTextField) input;
                try {
                    var value = Double.parseDouble(textInput.getText());
                    return value <= 431;
                } catch (NullPointerException | NumberFormatException e) {
                    return false;
                }
            }
        });


        healthTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                var textInput = (JTextField) input;
                try {
                    var value = Integer.parseInt(textInput.getText());
                    return value > 0;
                } catch (NullPointerException | NumberFormatException e) {
                    return false;
                }
            }
        });


        chapterNameTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                var textInput = (JTextField) input;
                if (textInput.getText() == null) return false;
                return !textInput.getText().trim().equals("");
            }
        });


        chapterWorldTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                var textInput = (JTextField) input;
                if (textInput.getText() == null) return false;
                return !textInput.getText().trim().equals("");
            }
        });
        offValidators();
    }

    private void offValidators() {
        nameTextField.setVerifyInputWhenFocusTarget(false);
        coordinateXTextField.setVerifyInputWhenFocusTarget(false);
        coordinateYTextField.setVerifyInputWhenFocusTarget(false);
        healthTextField.setVerifyInputWhenFocusTarget(false);
        chapterNameTextField.setVerifyInputWhenFocusTarget(false);
        chapterWorldTextField.setVerifyInputWhenFocusTarget(false);
    }

    private void onValidators() {
        nameTextField.setVerifyInputWhenFocusTarget(true);
        coordinateXTextField.setVerifyInputWhenFocusTarget(true);
        coordinateYTextField.setVerifyInputWhenFocusTarget(true);
        healthTextField.setVerifyInputWhenFocusTarget(true);
        chapterNameTextField.setVerifyInputWhenFocusTarget(true);
        chapterWorldTextField.setVerifyInputWhenFocusTarget(true);
    }


    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == heartCount1) {
            heartCount1.setIcon(redHeart);
            heartCount2.setIcon(blackHeart);
            heartCount3.setIcon(blackHeart);
        }
        if (event.getSource() == heartCount2) {
            heartCount1.setIcon(redHeart);
            heartCount2.setIcon(redHeart);
            heartCount3.setIcon(blackHeart);
        }
        if (event.getSource() == heartCount3) {
            heartCount1.setIcon(redHeart);
            heartCount2.setIcon(redHeart);
            heartCount3.setIcon(redHeart);
        }
        if (event.getSource() == outerAddButton ||
            event.getSource() == outerAddIfMinButton ||
            event.getSource() == outerRemoveGreaterButton) {
            setVisible(true);
            lastPressedOuterButton = (JButton) event.getSource();
        }
        if (event.getSource() == doesChapterExistCheck) {
            var selected = doesChapterExistCheck.isSelected();
            chapterNameLabel.setVisible(selected);
            chapterNameTextField.setVisible(selected);
            chapterParentLegionLabel.setVisible(selected);
            chapterParentLegionTextField.setVisible(selected);
            chapterMarinesCountLabel.setVisible(selected);
            chapterMarinesCountSlider.setVisible(selected);
            chapterWorldLabel.setVisible(selected);
            chapterWorldTextField.setVisible(selected);
        }
        if (event.getSource() == applyButton) {
            boolean valid = true;
            onValidators();
            if (!nameTextField.getInputVerifier().verify(nameTextField)) {
                JOptionPane.showMessageDialog(this,
                        speaker.speak("Invalid name. Name should not be an empty string"));
                valid = false;
            } else if (!coordinateXTextField.getInputVerifier().verify(coordinateXTextField)) {
                JOptionPane.showMessageDialog(this,
                        speaker.speak("Invalid coordinate X. Should be a number."));
                valid = false;
            } else if (!coordinateYTextField
                    .getInputVerifier().verify(coordinateYTextField)) {
                JOptionPane.showMessageDialog(this,
                        speaker.speak("Invalid coordinate Y. Should be a number and less or equals 431."));
                valid = false;
            } else if (!healthTextField.getInputVerifier().verify(healthTextField)) {
                JOptionPane.showMessageDialog(this,
                        speaker.speak("Invalid health. Should be a number and more than 0."));
                valid = false;
            } else if (doesChapterExistCheck.isSelected()) {
                if (!chapterNameTextField.getInputVerifier().verify(chapterNameTextField)) {
                    JOptionPane.showMessageDialog(this,
                            speaker.speak("Invalid chapter name. Name should not be an empty string."));
                    valid = false;
                } else if (!chapterWorldTextField.getInputVerifier().verify(chapterWorldTextField)) {
                    JOptionPane.showMessageDialog(this,
                            speaker.speak("Invalid chapter world. World should not be an empty string."));
                    valid = false;
                }
            }

            if (valid) {
                var name = nameTextField.getText();
                var x = Float.parseFloat(coordinateXTextField.getText());
                var y = Double.parseDouble(coordinateYTextField.getText());
                var health = Integer.parseInt(healthTextField.getText());
                long heartCount = 1;
                if (heartCount2.isSelected()) heartCount++;
                if (heartCount3.isSelected()) heartCount += 2;
                AstartesCategory category = null;
                if (scoutButton.isSelected()) category = AstartesCategory.SCOUT;
                if (librarianButton.isSelected()) category = AstartesCategory.LIBRARIAN;
                if (chaplainButton.isSelected()) category = AstartesCategory.CHAPLAIN;
                if (apothecaryButton.isSelected()) category = AstartesCategory.APOTHECARY;
                MeleeWeapon weapon = null;
                if (manreaperButton.isSelected()) weapon = MeleeWeapon.MANREAPER;
                if (lightingClawButton.isSelected()) weapon = MeleeWeapon.LIGHTING_CLAW;
                if (powerFistButton.isSelected()) weapon = MeleeWeapon.POWER_FIST;
                Chapter chapter = null;
                if (doesChapterExistCheck.isSelected()) {
                    var chapterName = chapterNameTextField.getText();
                    var parentLegion = chapterParentLegionTextField.getText();
                    var marineCount = chapterMarinesCountSlider.getValue();
                    var world = chapterWorldTextField.getText();
                    chapter = new Chapter(chapterName, parentLegion, (long) marineCount, world);
                }
                var marine = new SpaceMarine(
                        1,
                        name,
                        new Coordinates(x, y),
                        LocalDateTime.now(),
                        health,
                        heartCount,
                        category,
                        weapon,
                        chapter,
                        Controller.getUser().getLogin()
                );
                if (lastPressedOuterButton == outerAddButton) {
                    var answer = commandManager.executeAddCommand(marine);
                    JOptionPane.showMessageDialog(this, speaker.speak(answer));
                }
                if (lastPressedOuterButton == outerAddIfMinButton) {
                    var answer = commandManager.executeAddIfMinCommand(marine);
                    JOptionPane.showMessageDialog(this, speaker.speak(answer));
                }
                if (lastPressedOuterButton == outerRemoveGreaterButton) {
                    var answer = commandManager.executeRemoveGreaterCommand(marine);
                    JOptionPane.showMessageDialog(this, speaker.speak(answer));
                }
            }

        }

    }
    public void setMarineToUpdate(SpaceMarine marineToUpdate) {
        this.marineToUpdate = marineToUpdate;
        nameTextField.setText(marineToUpdate.getName());
        coordinateXTextField.setText(String.valueOf(marineToUpdate.getCoordinatesX()));
        coordinateYTextField.setText(String.valueOf(marineToUpdate.getCoordinatesY()));
        healthTextField.setText(String.valueOf(marineToUpdate.getHealth()));
        if (marineToUpdate.getHeartCount() == 1) heartCount1.setSelected(true);
        if (marineToUpdate.getHeartCount() == 2) heartCount2.setSelected(true);
        if (marineToUpdate.getHeartCount() == 3) heartCount3.setSelected(true);

    }
    public void setLastPressedOuterButton(JButton lastPressedOuterButton){
        this.lastPressedOuterButton = lastPressedOuterButton;
    }
}

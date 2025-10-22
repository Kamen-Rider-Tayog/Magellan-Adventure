package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class UIManager {
    private GamePanel gamePanel;
    private JPanel arrowPanel;
    private ArrowButton upArrow, downArrow, leftArrow, rightArrow;
    private SettingsPanel settingsPanel;
    private InfoBox infoBox;

    private final int ARROW_SIZE = 80;
    private final int ARROW_PANEL_SIZE = 240;

    public UIManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setupUIControls();
    }

    private void setupUIControls() {
        settingsPanel = new SettingsPanel(gamePanel);
        settingsPanel.setBounds(0, 0, gamePanel.getWidth(), gamePanel.getHeight());

        // Create info box FIRST (so it's at the bottom of the z-order)
        infoBox = new InfoBox();

        arrowPanel = new JPanel();
        arrowPanel.setOpaque(false);
        arrowPanel.setLayout(new GridLayout(3, 3, 0, 0));
        arrowPanel.setBorder(BorderFactory.createEmptyBorder());

        upArrow = new ArrowButton("UP", ARROW_SIZE);
        downArrow = new ArrowButton("DOWN", ARROW_SIZE);
        leftArrow = new ArrowButton("LEFT", ARROW_SIZE);
        rightArrow = new ArrowButton("RIGHT", ARROW_SIZE);

        setupArrowListeners();

        arrowPanel.add(createTransparentPanel());
        arrowPanel.add(upArrow);
        arrowPanel.add(createTransparentPanel());
        arrowPanel.add(leftArrow);
        arrowPanel.add(createTransparentPanel());
        arrowPanel.add(rightArrow);
        arrowPanel.add(createTransparentPanel());
        arrowPanel.add(downArrow);
        arrowPanel.add(createTransparentPanel());

        // Add components in reverse order of z-index (lowest first)
        gamePanel.add(infoBox); // Should be on top (added first = lowest z-order)
        gamePanel.add(arrowPanel);
        gamePanel.add(settingsPanel); // Should be on bottom (added last = highest z-order)
    }

    private JPanel createTransparentPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    private void setupArrowListeners() {
        upArrow.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (gamePanel.canAcceptInput()) {
                    gamePanel.getInputHandler().setKeyPressed(KeyEvent.VK_W, true);
                    upArrow.setLit(true);
                    Timer timer = new Timer(200, ev -> {
                        gamePanel.getInputHandler().setKeyPressed(KeyEvent.VK_W, false);
                        upArrow.setLit(false);
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        });

        downArrow.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (gamePanel.canAcceptInput()) {
                    gamePanel.getInputHandler().setKeyPressed(KeyEvent.VK_S, true);
                    downArrow.setLit(true);
                    Timer timer = new Timer(200, ev -> {
                        gamePanel.getInputHandler().setKeyPressed(KeyEvent.VK_S, false);
                        downArrow.setLit(false);
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        });

        leftArrow.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (gamePanel.canAcceptInput()) {
                    gamePanel.getInputHandler().setKeyPressed(KeyEvent.VK_A, true);
                    leftArrow.setLit(true);
                    Timer timer = new Timer(200, ev -> {
                        gamePanel.getInputHandler().setKeyPressed(KeyEvent.VK_A, false);
                        leftArrow.setLit(false);
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        });

        rightArrow.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (gamePanel.canAcceptInput()) {
                    gamePanel.getInputHandler().setKeyPressed(KeyEvent.VK_D, true);
                    rightArrow.setLit(true);
                    Timer timer = new Timer(200, ev -> {
                        gamePanel.getInputHandler().setKeyPressed(KeyEvent.VK_D, false);
                        rightArrow.setLit(false);
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        });
    }

    public void updateUIPositions() {
        arrowPanel.setBounds(20, gamePanel.getHeight() - ARROW_PANEL_SIZE - 20, ARROW_PANEL_SIZE, ARROW_PANEL_SIZE);
        settingsPanel.setBounds(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
        infoBox.updatePosition(gamePanel.getWidth(), gamePanel.getHeight());
    }

    public void updateUIVisibility(boolean showArrows) {
        arrowPanel.setVisible(showArrows);
    }

    public SettingsPanel getSettingsPanel() {
        return settingsPanel;
    }

    public InfoBox getInfoBox() {
        return infoBox;
    }

    public ArrowButton getUpArrow() { return upArrow; }
    public ArrowButton getDownArrow() { return downArrow; }
    public ArrowButton getLeftArrow() { return leftArrow; }
    public ArrowButton getRightArrow() { return rightArrow; }

    public void updateComponentLayering() {
        if (gamePanel.getComponentCount() > 0) {
            // Set z-order: 0 = bottom, highest number = top
            gamePanel.setComponentZOrder(settingsPanel, 0); // Top layer (covers everything)
            gamePanel.setComponentZOrder(arrowPanel, 1);    // Middle layer
            gamePanel.setComponentZOrder(infoBox, 2);       // Bottom layer (behind everything)

            // Force the info box to be on top by setting it to highest z-order
            gamePanel.setComponentZOrder(infoBox, 0); // Now info box should be on top
        }
    }
}
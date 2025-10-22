package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class InfoBox extends JPanel {
    private JTextArea infoTextArea;
    private JLabel titleLabel;
    private Font customFont;

    private final int WIDTH = 300; // Increased width
    private final int HEIGHT = 200;
    private final Color BROWN_BORDER = new Color(139, 69, 19); // Dark brown
    private final Color LIGHT_BROWN = new Color(222, 184, 135); // Light brown for background
    private final Color DARK_BROWN = new Color(101, 67, 33); // Darker brown for title

    // Reference to the current scene manager or game state
    private static int currentSceneIndex = 0;

    public InfoBox() {
        setOpaque(false);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // Load custom font
        loadCustomFont();

        // Create title label
        titleLabel = new JLabel("MISSION INFO", SwingConstants.CENTER);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(DARK_BROWN);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Set title font
        if (customFont != null) {
            titleLabel.setFont(customFont.deriveFont(Font.BOLD, 16f));
        } else {
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        }

        // Create the text area for mission info
        infoTextArea = new JTextArea();
        infoTextArea.setEditable(false);
        infoTextArea.setLineWrap(true);
        infoTextArea.setWrapStyleWord(true);
        infoTextArea.setAlignmentX(CENTER_ALIGNMENT);

        // Set info text font
        if (customFont != null) {
            infoTextArea.setFont(customFont.deriveFont(14f));
        } else {
            infoTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        }

        infoTextArea.setForeground(Color.BLACK);
        infoTextArea.setOpaque(false);
        infoTextArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Center the text in the text area
        infoTextArea.setMargin(new Insets(5, 10, 5, 10));

        // Create a panel to hold the text area for proper centering
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(infoTextArea, BorderLayout.CENTER);

        // Add components to main panel
        add(titleLabel, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER); // Add text panel directly instead of scroll pane

        // Set always visible
        setVisible(true);
    }

    private void loadCustomFont() {
        try {
            // REMOVE the file system check and only use classpath resource loading
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("assets/font/customFont.ttf")) {
                if (is != null) {
                    customFont = Font.createFont(Font.TRUETYPE_FONT, is);
                    System.out.println("Custom font loaded from resources");
                } else {
                    System.err.println("Custom font file not found in resources: assets/font/customFont.ttf");
                }
            }
        } catch (FontFormatException | IOException e) {
            System.err.println("Error loading custom font: " + e.getMessage());
            customFont = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw main background with rounded corners
        g2d.setColor(LIGHT_BROWN);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        // Draw brown border with rounded corners
        g2d.setColor(BROWN_BORDER);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);

        // Draw inner subtle border
        g2d.setColor(new Color(160, 120, 80));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(4, 4, getWidth() - 9, getHeight() - 9, 16, 16);

        g2d.dispose();
    }

    public void setInfoText(String text) {
        infoTextArea.setText(text);
        infoTextArea.setCaretPosition(0); // Scroll to top
        infoTextArea.setAlignmentX(CENTER_ALIGNMENT);
    }

    public void updatePosition(int parentWidth, int parentHeight) {
        int x = parentWidth - WIDTH - 10;
        int y = 10;
        setBounds(x, y, WIDTH, HEIGHT);
    }

    public Font getCustomFont() {
        return customFont;
    }

    public void setInfoFontSize(float size) {
        if (customFont != null) {
            infoTextArea.setFont(customFont.deriveFont(size));
        } else {
            infoTextArea.setFont(new Font("Arial", Font.PLAIN, (int)size));
        }
    }

    public static void setCurrentSceneIndex(int sceneIndex) {
        currentSceneIndex = sceneIndex;
    }

    public void updateMission() {
        String missionText = getCurrentMissionText();
        setInfoText(missionText);
    }

    private String getCurrentMissionText() {
        return switch (currentSceneIndex) {
            case 0 -> SceneOne.getCurrentMission(); // Throne room - King Manuel
            case 1 -> SceneTwo.getCurrentMission();
            case 2 -> SceneThree.getCurrentMission();
            case 3 -> SceneFour.getCurrentMission();
            case 4 -> SceneFive.getCurrentMission();
            case 5 -> SceneSix.getCurrentMission();
            default -> "Current Mission:\n\nExplore and find your objective.";
        };
    }
}

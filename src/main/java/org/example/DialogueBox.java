package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class DialogueBox extends JPanel {
    private BufferedImage dialogueBoxImage;
    private boolean isVisible = false;
    private boolean imageLoaded = false;
    private String currentText = "";
    private String currentSpeaker = ""; // Speaker name
    private final JPanel parentPanel;

    private int boxWidth;
    private int boxHeight;

    private final Font dialogueFont;
    private final Font speakerFont; // Separate font for speaker name
    private final Color textColor;
    private final Color speakerColor; // Separate color for speaker name

    public DialogueBox(JPanel parent) {
        this.parentPanel = parent;

        setOpaque(false);
        setLayout(null);

        loadDialogueBoxImage();

        dialogueFont = loadCustomFont().deriveFont(16f);
        speakerFont = loadCustomFont().deriveFont(Font.BOLD, 24f); // Bold and slightly larger for speaker
        textColor = new Color(111, 78, 55);
        speakerColor = new Color(80, 50, 30); // Darker color for speaker name

        updateDialogueBoxSize();

        setFocusable(false);
        setVisible(false);
    }

    private void loadDialogueBoxImage() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("assets/image/dialogue.png");
            if (is != null) {
                dialogueBoxImage = ImageIO.read(is);
                is.close();
                imageLoaded = true;
            } else {
                dialogueBoxImage = createFallbackDialogueBox();
                imageLoaded = true;
            }
        } catch (Exception e) {
            dialogueBoxImage = createFallbackDialogueBox();
            imageLoaded = true;
        }
    }

    private BufferedImage createFallbackDialogueBox() {
        BufferedImage fallback = new BufferedImage(600, 150, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = fallback.createGraphics();

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Dialogue Box (Fallback)", 20, 30);
        g.drawString("Press E to continue", 20, 50);

        g.dispose();
        return fallback;
    }

    private void updateDialogueBoxSize() {
        int screenWidth = parentPanel.getWidth();
        int screenHeight = parentPanel.getHeight();

        if (imageLoaded && dialogueBoxImage != null) {
            float aspectRatio = (float) dialogueBoxImage.getWidth() / dialogueBoxImage.getHeight();

            boxWidth = screenWidth;
            boxHeight = (int)(boxWidth / aspectRatio);

            if (boxHeight > screenHeight * 0.3) {
                boxHeight = (int)(screenHeight * 0.3);
                boxWidth = (int)(boxHeight * aspectRatio);
            }
        } else {
            boxWidth = (int)(screenWidth * 0.6);
            boxHeight = (int)(screenHeight * 0.2);
        }

        int boxX = (screenWidth - boxWidth) / 2;
        int boxY = screenHeight - boxHeight - 30;

        setBounds(boxX, boxY, boxWidth, boxHeight);
    }

    public void showDialogue(String speaker, String text) {
        this.currentSpeaker = speaker;
        this.currentText = text;
        this.isVisible = true;
        updateDialogueBoxSize();
        setVisible(true);

        if (getParent() != null) {
            getParent().setComponentZOrder(this, 0);
        }

        revalidate();
        repaint();
    }

    public void hideDialogue() {
        this.isVisible = false;
        this.currentSpeaker = "";
        this.currentText = "";
        setVisible(false);
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!isVisible || !imageLoaded || dialogueBoxImage == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.drawImage(dialogueBoxImage, 0, 0, boxWidth, boxHeight, null);

        drawText(g2d);

        g2d.dispose();
    }

    private void drawText(Graphics2D g) {
        int textPadding = 25;
        int lineHeight = 30;
        int y = textPadding + lineHeight + 25;

        int textStartX = 50;
        if (currentSpeaker != null && !currentSpeaker.isEmpty()) {
            g.setColor(speakerColor);
            g.setFont(speakerFont);
            g.drawString(currentSpeaker + ":", textStartX, y);
            y += lineHeight + 5; // Extra space after speaker name
        }

        g.setColor(textColor);
        g.setFont(dialogueFont);

        String[] words = currentText.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine + (!currentLine.isEmpty() ? " " : "") + word;
            int testWidth;
            testWidth = g.getFontMetrics().stringWidth(testLine);

            if (testWidth > boxWidth - (textPadding * 2)) {
                g.drawString(currentLine.toString(), textStartX, y);
                currentLine = new StringBuilder(word);
                y += lineHeight;

                if (y > boxHeight - textPadding) {
                    break;
                }
            } else {
                currentLine.append(!currentLine.isEmpty() ? " " : "").append(word);
            }
        }

        if (!currentLine.isEmpty() && y <= boxHeight - textPadding) {
            g.drawString(currentLine.toString(), textStartX, y);
        }
    }

    public void update() {
        if (isVisible) {
            updateDialogueBoxSize();
        }
    }

    public boolean isDialogueVisible() {
        return isVisible;
    }

    private Font loadCustomFont() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("assets/font/customFont.ttf");
            if (is != null) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);
                is.close();
                return customFont.deriveFont(Font.PLAIN, 16f);
            } else {
                System.err.println("Custom font not found in resources: assets/font/customFont.ttf");
                return new Font("Arial", Font.PLAIN, 16);
            }
        } catch (FontFormatException | IOException e) {
            System.err.println("Error loading custom font: " + e.getMessage());
            return new Font("Arial", Font.PLAIN, 16);
        }
    }
}

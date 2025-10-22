package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.InputStream;

public class NarrativeScreen extends JPanel {
    private boolean isVisible = false;
    private String[] narrativeTexts;
    private int currentTextIndex = 0;
    private Font narrativeFont;
    private Font continueFont;
    private final Color textColor = new Color(220, 220, 220);
    private final Color continueColor = new Color(255, 215, 0);

    private GamePanel gamePanel;

    public NarrativeScreen(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        setOpaque(true);
        setBackground(Color.BLACK);
        setLayout(null);
        setVisible(false);
        setFocusable(false);

        narrativeFont = loadCustomFont().deriveFont(24f);
        continueFont = loadCustomFont().deriveFont(18f);
    }

    private Font loadCustomFont() {
    try {
        // Use ClassLoader for resource loading
        InputStream fontStream = getClass().getClassLoader()
                .getResourceAsStream("assets/font/customFont.ttf");
        if (fontStream != null) {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            return customFont.deriveFont(Font.PLAIN, 24f);
        } else {
            // Fallback to file system
            File fontFile = new File("assets/font/customFont.ttf");
            if (fontFile.exists()) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                return customFont.deriveFont(Font.PLAIN, 24f);
            }
        }
    } catch (Exception e) {
        System.err.println("Error loading custom font: " + e.getMessage());
    }
    return new Font("Arial", Font.PLAIN, 24);
}

    public void showNarrative(String[] texts) {
        this.narrativeTexts = texts;
        this.currentTextIndex = 0;
        this.isVisible = true;

        // Make sure we have the correct bounds
        if (gamePanel != null) {
            setBounds(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
        }
        setVisible(true);

        // Force to front
        if (getParent() != null) {
            getParent().setComponentZOrder(this, 0);
        }

        revalidate();
        repaint();

        System.out.println("Narrative shown with " + texts.length + " pages");
    }

    public boolean advance() {
        System.out.println("Advance called. Current index: " + currentTextIndex + " / " + (narrativeTexts.length - 1));

        if (currentTextIndex < narrativeTexts.length - 1) {
            currentTextIndex++;
            System.out.println("Advanced to index: " + currentTextIndex);
            repaint();
            return false; // Not finished
        } else {
            System.out.println("Narrative finished, hiding...");
            hideNarrative();
            return true; // Finished
        }
    }

    public void hideNarrative() {
        this.isVisible = false;
        this.currentTextIndex = 0;
        setVisible(false);

        revalidate();
        repaint();

        System.out.println("Narrative hidden");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!isVisible || narrativeTexts == null || currentTextIndex >= narrativeTexts.length) {
            // If not visible, fill with transparent background
            g.setColor(new Color(0, 0, 0, 0));
            g.fillRect(0, 0, getWidth(), getHeight());
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();

        // Fill with semi-transparent black background
        g2d.setColor(new Color(0, 0, 0, 220));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawCenteredText(g2d);
        drawContinuePrompt(g2d);

        g2d.dispose();
    }

    private void drawCenteredText(Graphics2D g) {
        g.setFont(narrativeFont);
        g.setColor(textColor);

        String text = narrativeTexts[currentTextIndex];

        String[] words = text.split(" ");
        java.util.List<String> lines = new java.util.ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        FontMetrics fm = g.getFontMetrics();
        int maxWidth = getWidth() - 200;

        for (String word : words) {
            String testLine = currentLine + (!currentLine.isEmpty() ? " " : "") + word;
            int testWidth = fm.stringWidth(testLine);

            if (testWidth > maxWidth && !currentLine.isEmpty()) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            } else {
                currentLine.append(!currentLine.isEmpty() ? " " : "").append(word);
            }
        }

        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
        }

        int lineHeight = fm.getHeight();
        int totalHeight = lines.size() * lineHeight;
        int startY = (getHeight() - totalHeight) / 2;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int lineWidth = fm.stringWidth(line);
            int x = (getWidth() - lineWidth) / 2;
            int y = startY + (i * lineHeight) + fm.getAscent();

            g.drawString(line, x, y);
        }
    }

    private void drawContinuePrompt(Graphics2D g) {
        g.setFont(continueFont);
        g.setColor(continueColor);

        String prompt = "Press E to continue";
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(prompt);

        int x = getWidth() - textWidth - 30;
        int y = getHeight() - 30;

        long time = System.currentTimeMillis();
        int alpha = (int) (200 + 55 * Math.sin(time / 300.0));
        g.setColor(new Color(255, 215, 0, alpha));

        g.drawString(prompt, x, y);
    }

    public boolean isNarrativeVisible() {
        return isVisible;
    }

    public void update() {
        if (isVisible) {
            repaint();
        }
    }
}
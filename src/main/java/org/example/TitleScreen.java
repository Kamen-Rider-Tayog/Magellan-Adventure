package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.InputStream;

public class TitleScreen extends JPanel {
    private boolean isVisible = false;
    private BufferedImage titleImage;
    private Font titleFont;
    private Font continueFont;
    private final Color textColor = new Color(255, 215, 0);
    private GamePanel gamePanel;

    public TitleScreen(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        setOpaque(true);
        setBackground(Color.BLACK);
        setLayout(null);
        setVisible(false);
        setFocusable(false);

        loadTitleImage();

        // Load custom font or use fallback
        try {
            InputStream fontStream = getClass().getClassLoader()
                    .getResourceAsStream("assets/font/customFont.ttf");
            if (fontStream != null) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                titleFont = customFont.deriveFont(Font.BOLD, 36f);
                continueFont = customFont.deriveFont(Font.PLAIN, 18f);
            } else {
                titleFont = new Font("Arial", Font.BOLD, 36);
                continueFont = new Font("Arial", Font.PLAIN, 18);
            }
        } catch (Exception e) {
            titleFont = new Font("Arial", Font.BOLD, 36);
            continueFont = new Font("Arial", Font.PLAIN, 18);
        }
    }

    private void loadTitleImage() {
        try {
            // Try to load from resources first
            InputStream imageStream = getClass().getClassLoader()
                    .getResourceAsStream("assets/backgrounds/title.png");
            if (imageStream != null) {
                titleImage = ImageIO.read(imageStream);
            } else {
                // Fallback to file system
                File imageFile = new File("assets/backgrounds/title.png");
                if (imageFile.exists()) {
                    titleImage = ImageIO.read(imageFile);
                } else {
                    System.err.println("Title image not found: assets/backgrounds/title.png");
                    // Create a placeholder
                    titleImage = new BufferedImage(400, 200, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = titleImage.createGraphics();
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Arial", Font.BOLD, 24));
                    g2d.drawString("MAGELLAN'S JOURNEY", 50, 100);
                    g2d.dispose();
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading title image: " + e.getMessage());
            // Create a placeholder
            titleImage = new BufferedImage(400, 200, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = titleImage.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.drawString("MAGELLAN'S JOURNEY", 50, 100);
            g2d.dispose();
        }
    }

    public void showTitle() {
        this.isVisible = true;

        if (gamePanel != null) {
            setBounds(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
        }
        setVisible(true);

        if (getParent() != null) {
            getParent().setComponentZOrder(this, 0);
        }

        revalidate();
        repaint();

        System.out.println("Title screen shown");
    }

    public void hideTitle() {
        this.isVisible = false;
        setVisible(false);
        revalidate();
        repaint();
        System.out.println("Title screen hidden");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!isVisible) {
            g.setColor(new Color(0, 0, 0, 0));
            g.fillRect(0, 0, getWidth(), getHeight());
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();

        // Fill with black background
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw title image centered
        if (titleImage != null) {
            int imageWidth = titleImage.getWidth();
            int imageHeight = titleImage.getHeight();
            int x = (getWidth() - imageWidth) / 2;
            int y = (getHeight() - imageHeight) / 3;
            g2d.drawImage(titleImage, x, y, null);
        }

        g2d.dispose();
    }

    public boolean isTitleVisible() {
        return isVisible;
    }
}
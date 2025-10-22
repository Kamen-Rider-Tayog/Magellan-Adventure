package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SettingsPanel extends JPanel {
    private boolean isVisible = false;
    private Font customFont;
    private BufferedImage backgroundImage;
    private BufferedImage resumeButtonImage;
    private BufferedImage quitButtonImage;
    private GamePanel gamePanel;

    private int originalBgWidth, originalBgHeight;
    private int originalResumeWidth, originalResumeHeight;
    private int originalQuitWidth, originalQuitHeight;

    public SettingsPanel() {
        setOpaque(true);
        setBackground(new Color(0, 0, 0, 200));
        setLayout(new BorderLayout());
        setVisible(false);

        // Get reference to GamePanel
        if (SwingUtilities.getWindowAncestor(this) != null) {
            Container parent = getParent();
            while (parent != null && !(parent instanceof GamePanel)) {
                parent = parent.getParent();
            }
            if (parent instanceof GamePanel) {
                gamePanel = (GamePanel) parent;
            }
        }

        // Load all resources using Maven-compatible approach
        loadResources();

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(130, 0, 0, 0));

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(220, 20, 20, 20));

        JButton resumeButton = createImageButton("", resumeButtonImage, originalResumeWidth, originalResumeHeight );
        JButton quitButton = createImageButton("", quitButtonImage, originalQuitWidth, originalQuitHeight);

        resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(resumeButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        contentPanel.add(quitButton);

        add(contentPanel, BorderLayout.CENTER);

        resumeButton.addActionListener(e -> hideSettings());
        quitButton.addActionListener(e -> System.exit(0));

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "closeSettings");
        getActionMap().put("closeSettings", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideSettings();
            }
        });
    }

    // Constructor with GamePanel reference
    public SettingsPanel(GamePanel gamePanel) {
        this();
        this.gamePanel = gamePanel;
    }

    private void loadResources() {
        // Load custom font
        try {
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("assets/font/customFont.ttf");
            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(24f);
                fontStream.close();
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);
            } else {
                // Fallback to file system for development
                customFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/font/customFont.ttf")).deriveFont(24f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);
            }
        } catch (FontFormatException | IOException e) {
            System.err.println("Failed to load custom font, using fallback: " + e.getMessage());
            customFont = new Font("Arial", Font.BOLD, 24);
        }

        // Load background image
        try {
            InputStream bgStream = getClass().getClassLoader().getResourceAsStream("assets/ui/settingsBackground.png");
            if (bgStream != null) {
                backgroundImage = javax.imageio.ImageIO.read(bgStream);
                bgStream.close();
            } else {
                // Fallback to file system for development
                backgroundImage = javax.imageio.ImageIO.read(new File("assets/ui/settingsBackground.png"));
            }
            originalBgWidth = backgroundImage.getWidth();
            originalBgHeight = backgroundImage.getHeight();
        } catch (Exception e) {
            System.err.println("Failed to load settings background: " + e.getMessage());
            backgroundImage = null;
        }

        // Load resume button image
        try {
            InputStream resumeStream = getClass().getClassLoader().getResourceAsStream("assets/ui/resumeButton.png");
            if (resumeStream != null) {
                resumeButtonImage = javax.imageio.ImageIO.read(resumeStream);
                resumeStream.close();
            } else {
                // Fallback to file system for development
                resumeButtonImage = javax.imageio.ImageIO.read(new File("assets/ui/resumeButton.png"));
            }
            originalResumeWidth = resumeButtonImage.getWidth();
            originalResumeHeight = resumeButtonImage.getHeight();
        } catch (Exception e) {
            System.err.println("Failed to load resume button image: " + e.getMessage());
            resumeButtonImage = null;
        }

        // Load quit button image
        try {
            InputStream quitStream = getClass().getClassLoader().getResourceAsStream("assets/ui/quitButton.png");
            if (quitStream != null) {
                quitButtonImage = javax.imageio.ImageIO.read(quitStream);
                quitStream.close();
            } else {
                // Fallback to file system for development
                quitButtonImage = javax.imageio.ImageIO.read(new File("assets/ui/quitButton.png"));
            }
            originalQuitWidth = quitButtonImage.getWidth();
            originalQuitHeight = quitButtonImage.getHeight();
        } catch (Exception e) {
            System.err.println("Failed to load quit button image: " + e.getMessage());
            quitButtonImage = null;
        }
    }

    private JButton createImageButton(String text, BufferedImage image, int originalWidth, int originalHeight) {
        JButton button;

        if (image != null) {

            int baseSize = 200;
            double aspectRatio = (double) originalWidth / originalHeight;
            int buttonWidth = baseSize;
            int buttonHeight = (int) (baseSize / aspectRatio);

            button = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    g.drawImage(image, 0, 0, buttonWidth, buttonHeight, this);

                    if (getText() != null && !getText().isEmpty()) {
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2d.setColor(Color.WHITE);
                        g2d.setFont(customFont.deriveFont(14f));

                        FontMetrics fm = g2d.getFontMetrics();
                        int textWidth = fm.stringWidth(getText());
                        int textHeight = fm.getHeight();

                        int textX = (getWidth() - textWidth) / 2;
                        int textY = (getHeight() + textHeight - fm.getDescent()) / 2;

                        g2d.drawString(getText(), textX, textY);
                    }
                }
            };

            button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            button.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
            button.setMaximumSize(new Dimension(buttonWidth, buttonHeight));

        } else {

            button = new JButton(text);
            button.setPreferredSize(new Dimension(80, 30));
            button.setMinimumSize(new Dimension(80, 30));
            button.setMaximumSize(new Dimension(80, 30));
            button.setFont(customFont.deriveFont(12f));
            button.setBackground(new Color(70, 70, 70));
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            button.setFocusPainted(false);
        }

        button.setText(text);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(Cursor.getDefaultCursor());
            }
        });

        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {

            Dimension scaled = getScaledDimension(originalBgWidth, originalBgHeight, getWidth(), getHeight());
            int x = (getWidth() - scaled.width) / 2;
            int y = (getHeight() - scaled.height) / 2;

            g.drawImage(backgroundImage, x, y, scaled.width, scaled.height, this);
        } else {

            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private Dimension getScaledDimension(int originalWidth, int originalHeight, int targetWidth, int targetHeight) {
        double widthRatio = (double) targetWidth / originalWidth;
        double heightRatio = (double) targetHeight / originalHeight;
        double ratio = Math.min(widthRatio, heightRatio);

        int scaledWidth = (int) (originalWidth * ratio);
        int scaledHeight = (int) (originalHeight * ratio);

        return new Dimension(scaledWidth, scaledHeight);
    }

    public void showSettings() {
        isVisible = true;
        setVisible(true);
        setCursor(Cursor.getDefaultCursor());

        // Hide InfoBox when settings are shown
        if (gamePanel != null && gamePanel.getUIManager() != null) {
            gamePanel.getUIManager().getInfoBox();
        }
    }

    public void hideSettings() {
        isVisible = false;
        setVisible(false);
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB),
                new Point(0, 0), "blank cursor"));

        // InfoBox remains hidden when settings close - user can press Q to show it again
    }

    public boolean isSettingsVisible() {
        return isVisible;
    }

    // Method to set GamePanel reference if not set in constructor
    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
}
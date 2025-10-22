package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class MapOverlay extends JPanel {
    private BufferedImage mapImage;
    private boolean isFullscreenMode = false;
    private boolean imageLoaded = false;

    private JPanel parentPanel;
    private int minimapWidth;
    private int minimapHeight;
    private float imageAspectRatio;

    public MapOverlay(JPanel parent) {
        this.parentPanel = parent;

        setOpaque(false);
        setLayout(null);

        setBackground(new Color(0, 0, 0, 0));

        loadMapImage();
        setupKeyBindings();

        updateMinimapSize();
        showMinimap();

        setFocusable(false);
    }

    private void loadMapImage() {
    try {
        // OLD: mapImage = ImageIO.read(new File("assets/image/map.png"));
        // NEW: Use classpath resource loading
        InputStream is = getClass().getClassLoader().getResourceAsStream("assets/image/map.png");
        if (is != null) {
            mapImage = ImageIO.read(is);
            is.close();
            imageLoaded = true;

            if (mapImage != null) {
                imageAspectRatio = (float) mapImage.getWidth() / mapImage.getHeight();
                System.out.println("Map image loaded: " + mapImage.getWidth() + "x" + mapImage.getHeight() +
                        " Aspect ratio: " + imageAspectRatio +
                        " Transparency: " + (mapImage.getTransparency() == BufferedImage.TRANSLUCENT));
            }
        } else {
            System.err.println("Map image not found in resources: assets/image/map.png");
            mapImage = createFallbackMap();
            imageLoaded = true;
            imageAspectRatio = 4.0f / 3.0f;
        }
    } catch (Exception e) {
        System.err.println("Error loading map image: " + e.getMessage());
        mapImage = createFallbackMap();
        imageLoaded = true;
        imageAspectRatio = 4.0f / 3.0f;
    }
}

    private BufferedImage createFallbackMap() {

        BufferedImage fallback = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = fallback.createGraphics();

        g.setColor(new Color(30, 30, 70, 200));
        g.fillRect(0, 0, 800, 600);

        g.setColor(new Color(50, 120, 50, 200));
        g.fillRect(100, 100, 200, 150);
        g.fillRect(400, 200, 250, 180);
        g.fillRect(150, 400, 300, 120);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("MAP (Fallback)", 300, 300);
        g.drawString("Press M to toggle", 250, 350);

        g.dispose();
        return fallback;
    }

    private void setupKeyBindings() {
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "toggleMap");
        getActionMap().put("toggleMap", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (parentPanel instanceof GamePanel) {
                    GamePanel gamePanel = (GamePanel) parentPanel;
                    if (gamePanel.getDialogueBox().isDialogueVisible()) {
                        return;
                    }
                }
                toggleMap();
            }
        });
    }

    public void toggleMap() {

        if (parentPanel instanceof GamePanel) {
            GamePanel gamePanel = (GamePanel) parentPanel;
            if (gamePanel.getDialogueBox().isDialogueVisible()) {
                return;
            }
        }

        if (isFullscreenMode) {
            showMinimap();
        } else {
            showFullMap();
        }
    }

    public void showMinimap() {
        isFullscreenMode = false;
        updateMinimapSize();
        int x = 10;
        int y = 10;

        setBounds(x, y, minimapWidth, minimapHeight);
        setVisible(true);
        revalidate();
        repaint();
    }

    public void showFullMap() {
        isFullscreenMode = true;
        setBounds(0, 0, parentPanel.getWidth(), parentPanel.getHeight());
        setVisible(true);
        revalidate();
        repaint();
    }

    private void updateMinimapSize() {
        int baseSize = (int)(parentPanel.getWidth() * 0.20);

        if (imageAspectRatio > 1) {

            minimapWidth = baseSize;
            minimapHeight = (int)(baseSize / imageAspectRatio);
        } else {

            minimapHeight = baseSize;
            minimapWidth = (int)(baseSize * imageAspectRatio);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!imageLoaded || mapImage == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();

        if (isFullscreenMode) {
            paintFullMap(g2d);
        } else {
            paintMinimap(g2d);
        }

        g2d.dispose();
    }

    private void paintMinimap(Graphics2D g) {


        g.drawImage(mapImage, 0, 0, minimapWidth, minimapHeight, null);
    }

    private void paintFullMap(Graphics2D g) {

        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, getWidth(), getHeight());

        float scale = 0.8f;
        int maxWidth = (int)(getWidth() * scale);
        int maxHeight = (int)(getHeight() * scale);

        int scaledWidth, scaledHeight;

        if ((float)maxWidth / maxHeight > imageAspectRatio) {

            scaledHeight = maxHeight;
            scaledWidth = (int)(maxHeight * imageAspectRatio);
        } else {

            scaledWidth = maxWidth;
            scaledHeight = (int)(maxWidth / imageAspectRatio);
        }

        int x = (getWidth() - scaledWidth) / 2;
        int y = (getHeight() - scaledHeight) / 2;

        g.drawImage(mapImage, x, y, scaledWidth, scaledHeight, null);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 18));
    }

    public void update() {

        if (!isFullscreenMode) {
            updateMinimapSize();
            showMinimap();
        }
    }

    public boolean isMapVisible() {
        return isVisible();
    }

    public boolean isFullscreenMode() {
        return isFullscreenMode;
    }

    public boolean isMinimapMode() {
        return !isFullscreenMode;
    }
}
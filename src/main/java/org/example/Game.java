package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Game extends JFrame {
    public static final int TILE_SIZE = 32;

    public Game() {
        setTitle("Magellan's Adventure");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        int dynamicCols = screenWidth / TILE_SIZE;
        int dynamicRows = screenHeight / TILE_SIZE;

        setUndecorated(true);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        setResizable(false);

        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");
        setCursor(blankCursor);

        GamePanel gamePanel = new GamePanel(dynamicCols, dynamicRows);
        gamePanel.setCursor(blankCursor);
        add(gamePanel);

        setVisible(true);

        gamePanel.requestFocusInWindow();
    }
}

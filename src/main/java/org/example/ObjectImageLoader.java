package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

public class ObjectImageLoader {

    public static BufferedImage loadImage(ObjectType type) {
    try {
        // Use ClassLoader.getResourceAsStream for Maven compatibility
        InputStream inputStream = ObjectImageLoader.class.getClassLoader()
                .getResourceAsStream(type.getImagePath());
        if (inputStream != null) {
            return ImageIO.read(inputStream);
        } else {
            // Fallback to file system for development
            return ImageIO.read(new File(type.getImagePath()));
        }
    } catch (Exception e) {
        return createFallbackImage(type);
    }
}

    private static BufferedImage createFallbackImage(ObjectType type) {
        int pixelWidth = type.getWidthInTiles() * 32;
        int pixelHeight = type.getHeightInTiles() * 32;

        BufferedImage fallback = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = fallback.createGraphics();

        Color color = getColorForType(type);
        g.setColor(color);
        g.fillRect(0, 0, pixelWidth, pixelHeight);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, pixelWidth - 1, pixelHeight - 1);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString(type.getSymbol(), 10, 20);
        g.drawString(type.getWidthInTiles() + "x" + type.getHeightInTiles(), 10, 35);

        g.dispose();
        return fallback;
    }

    private static Color getColorForType(ObjectType type) {
        switch (type) {
            case NPC_KING: return Color.YELLOW;
            case CHARLES: return Color.ORANGE;
            case KAWAL: return Color.WHITE;
            case SAILOR1: return Color.RED;
            case SAILOR2: return Color.BLUE;
            case HUMABON: return Color.CYAN;
            default: return Color.GRAY;
        }
    }
}
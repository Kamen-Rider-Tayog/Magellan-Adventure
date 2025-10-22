package org.example;


import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {
    private static Map<String, BufferedImage> images = new HashMap<>();
    private static Map<String, BufferedImage> scaledImages = new HashMap<>();

    public static BufferedImage loadImage(String path) {
        return loadImage(path, Game.TILE_SIZE, Game.TILE_SIZE);
    }

    public static BufferedImage loadImage(String path, int width, int height) {
        String scaledKey = path + "_" + width + "x" + height;

        if (scaledImages.containsKey(scaledKey)) {
            return scaledImages.get(scaledKey);
        }

        BufferedImage original = loadOriginalImage(path);
        if (original == null) {
            return createPlaceholderImage(width, height);
        }

        BufferedImage scaledImage = scaleImage(original, width, height);
        scaledImages.put(scaledKey, scaledImage);

        return scaledImage;
    }

    private static BufferedImage loadOriginalImage(String path) {
        if (images.containsKey(path)) {
            return images.get(path);
        }

        try {
            // Use classpath resource loading instead of File
            InputStream is = ImageLoader.class.getClassLoader().getResourceAsStream(path);
            if (is != null) {
                BufferedImage image = ImageIO.read(is);
                images.put(path, image);
                is.close();
                return image;
            } else {
                System.err.println("Image not found in resources: " + path);
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error loading image: " + path);
            e.printStackTrace();
            return null;
        }
    }

    private static BufferedImage scaleImage(BufferedImage original, int width, int height) {
        if (original.getWidth() == width && original.getHeight() == height) {
            return original;
        }

        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaled.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(original, 0, 0, width, height, null);
        g2d.dispose();

        return scaled;
    }

    private static BufferedImage createPlaceholderImage(int width, int height) {
        BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = placeholder.createGraphics();
        g2d.setColor(Color.MAGENTA);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.BLACK);
        g2d.drawString("X", width/2 - 3, height/2 + 3);
        g2d.dispose();
        return placeholder;
    }

    public static void preloadImages(String[] paths) {
        for (String path : paths) {
            loadImage(path);
        }
    }

    public static void clearCache() {
        scaledImages.clear();
    }

    public static BufferedImage loadUnscaledImage(String path) {
        if (images.containsKey(path)) {
            return images.get(path);
        }

        try {
            // Use classpath resource loading
            InputStream is = ImageLoader.class.getClassLoader().getResourceAsStream(path);
            if (is != null) {
                BufferedImage image = ImageIO.read(is);
                images.put(path, image);
                is.close();
                return image;
            } else {
                System.err.println("Unscaled image not found in resources: " + path);
                return createPlaceholderImage();
            }
        } catch (IOException e) {
            System.err.println("Error loading unscaled image: " + path);
            e.printStackTrace();
            return createPlaceholderImage();
        }
    }

    private static BufferedImage createPlaceholderImage() {
        return createPlaceholderImage(Game.TILE_SIZE, Game.TILE_SIZE);
    }
}

package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ArrowButton extends JPanel {
    private final String direction;
    private boolean isLit = false;
    private BufferedImage normalImage;
    private BufferedImage coloredImage;
    private boolean imageLoaded = false;
    private final int arrowSize;

    public ArrowButton(String direction, int size) {
        this.direction = direction;
        this.arrowSize = size;
        setOpaque(false);
        setPreferredSize(new Dimension(size, size));
        setBorder(null);
        setBackground(new Color(0, 0, 0, 0));

        loadImages();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setLit(true);
            }
        });
    }

    private void loadImages() {
        try {
            // Use getResourceAsStream for JAR compatibility
            String imagePath = "/assets/ui/arrow" + direction + ".png";
            InputStream inputStream = getClass().getResourceAsStream(imagePath);

            if (inputStream != null) {
                coloredImage = ImageIO.read(inputStream);
                coloredImage = scaleImage(coloredImage, arrowSize, arrowSize);
                normalImage = convertToGrayscale(coloredImage);
                imageLoaded = true;
                inputStream.close(); // Don't forget to close the stream
            } else {
                System.err.println("Could not find resource: " + imagePath);
                imageLoaded = false;
            }
        } catch (Exception e) {
            System.err.println("Error loading arrow image: " + e.getMessage());
            imageLoaded = false;
        }
    }

    private BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaled.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(original, 0, 0, width, height, null);
        g2d.dispose();
        return scaled;
    }

    private BufferedImage convertToGrayscale(BufferedImage coloredImage) {
        BufferedImage grayscale = new BufferedImage(
                coloredImage.getWidth(),
                coloredImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        ColorConvertOp op = new ColorConvertOp(
                ColorSpace.getInstance(ColorSpace.CS_GRAY),
                null
        );
        op.filter(coloredImage, grayscale);

        return grayscale;
    }

    public void setLit(boolean lit) {
        this.isLit = lit;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imageLoaded) {
            BufferedImage image = isLit ? coloredImage : normalImage;
            if (image != null) {
                int x = (getWidth() - image.getWidth()) / 2;
                int y = (getHeight() - image.getHeight()) / 2;
                g.drawImage(image, x, y, null);
            }
        } else {
            drawFallbackArrow(g);
        }
    }

    private void drawFallbackArrow(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color arrowColor = isLit ? Color.YELLOW : Color.LIGHT_GRAY;
        g2d.setColor(arrowColor);

        int[] xPoints, yPoints;
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int size = arrowSize / 3;

        switch (direction) {
            case "UP":
                xPoints = new int[]{centerX, centerX - size, centerX + size};
                yPoints = new int[]{centerY - size, centerY + size, centerY + size};
                break;
            case "DOWN":
                xPoints = new int[]{centerX, centerX - size, centerX + size};
                yPoints = new int[]{centerY + size, centerY - size, centerY - size};
                break;
            case "LEFT":
                xPoints = new int[]{centerX - size, centerX + size, centerX + size};
                yPoints = new int[]{centerY, centerY - size, centerY + size};
                break;
            case "RIGHT":
                xPoints = new int[]{centerX + size, centerX - size, centerX - size};
                yPoints = new int[]{centerY, centerY - size, centerY + size};
                break;
            default:
                return;
        }

        g2d.fillPolygon(xPoints, yPoints, 3);
    }
}

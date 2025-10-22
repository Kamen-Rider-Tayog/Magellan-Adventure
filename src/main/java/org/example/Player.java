package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class Player {
    private int x, y;
    private int width, height;
    private Color color;
    private int speed;
    private String direction;
    private boolean isMoving;

    private Map<String, BufferedImage> directionSheets;
    private Map<String, BufferedImage[]> directionFrames;
    private final int COLS = 3;

    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private final long FRAME_DELAY = 200;
    private boolean wasMoving = false;

    private final float SIZE_MULTIPLIER = 2f;

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.width = 1;
        this.height = 1;
        this.color = Color.RED;
        this.speed = 1;
        this.direction = "DOWN";
        this.isMoving = false;

        loadAndExtractFrames();
    }

    private void loadAndExtractFrames() {
        directionSheets = new HashMap<>();
        directionFrames = new HashMap<>();

        try {
            extractDirectionFrames("DOWN", "assets/player/magellanDown.png");
            extractDirectionFrames("UP", "assets/player/magellanUp.png");
            extractDirectionFrames("LEFT", "assets/player/magellanLeft.png");
            extractDirectionFrames("RIGHT", "assets/player/magellanRight.png");

        } catch (Exception e) {
            System.err.println("Error loading player frames: " + e.getMessage());
            directionSheets = null;
            directionFrames = null;
        }
    }

    private void extractDirectionFrames(String direction, String path) {
        BufferedImage originalSheet = loadUnscaledImage(path);
        if (originalSheet == null) {
            System.err.println("Failed to load image: " + path);
            return;
        }

        directionSheets.put(direction, originalSheet);

        BufferedImage[] frames = new BufferedImage[COLS];
        int spriteWidth = originalSheet.getWidth() / COLS;
        int spriteHeight = originalSheet.getHeight();

        for (int col = 0; col < COLS; col++) {
            int x = col * spriteWidth;
            frames[col] = originalSheet.getSubimage(x, 0, spriteWidth, spriteHeight);
        }

        directionFrames.put(direction, frames);
    }

    private BufferedImage loadUnscaledImage(String path) {
        try {
            // Try loading from resources (works in JAR)
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            if (inputStream != null) {
                return ImageIO.read(inputStream);
            } else {
                // Fallback to file system (works in development)
                return ImageIO.read(new File(path));
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + path + " - " + e.getMessage());
            return null;
        }
    }

    public void setMoving(boolean moving, int dx, int dy) {
        this.isMoving = moving;

        if (moving) {
            if (dx > 0) direction = "RIGHT";
            else if (dx < 0) direction = "LEFT";
            else if (dy > 0) direction = "DOWN";
            else if (dy < 0) direction = "UP";
        }
    }

    public void move(int dx, int dy, TileType[][] worldTiles) {
        int newX = x + dx * speed;
        int newY = y + dy * speed;

        if (isValidPosition(newX, newY, worldTiles)) {
            x = newX;
            y = newY;
        }
    }

    public void updateAnimation() {
        long currentTime = System.currentTimeMillis();

        if (isMoving) {
            if (currentTime - lastFrameTime > FRAME_DELAY) {
                currentFrame = (currentFrame + 1) % 2;
                lastFrameTime = currentTime;
            }
            wasMoving = true;
        } else {
            if (wasMoving) {
                currentFrame = 1;
                wasMoving = false;
            }
        }
    }

    private boolean isValidPosition(int tileX, int tileY, TileType[][] worldTiles) {

        if (tileX < 0 || tileX >= worldTiles.length ||
                tileY < 0 || tileY >= worldTiles[0].length) {
            return false;
        }

        TileType tile = worldTiles[tileX][tileY];
        if (tile != null && tile.isCollidable()) {
            return false;
        }

        return true;
    }

    public void draw(Graphics g, Camera camera) {
        if (!camera.isVisible(x, y)) return;

        int viewportX = camera.getViewportX(x) * Game.TILE_SIZE;
        int viewportY = camera.getViewportY(y) * Game.TILE_SIZE;

        int playerSize = (int)(Game.TILE_SIZE * SIZE_MULTIPLIER);

        int centeredX = viewportX - (playerSize - Game.TILE_SIZE) / 2;
        int centeredY = viewportY - (playerSize - Game.TILE_SIZE) / 2;

        if (directionFrames != null && directionFrames.containsKey(direction)) {
            drawAnimatedSprite(g, centeredX, centeredY, playerSize);
        } else {
            drawFallbackPlayer(g, centeredX, centeredY, playerSize);
        }
    }

    private void drawAnimatedSprite(Graphics g, int x, int y, int size) {
        BufferedImage[] frames = directionFrames.get(direction);

        if (frames != null) {
            int frameIndex;
            if (isMoving) {
                frameIndex = currentFrame == 0 ? 0 : 2;
            } else {
                frameIndex = 1;
            }

            BufferedImage frame = frames[frameIndex];
            if (frame != null) {
                g.drawImage(frame, x, y, size, size, null);
            }
        }
    }

    private void drawFallbackPlayer(Graphics g, int x, int y, int size) {
        g.setColor(color);
        g.fillRect(x, y, size, size);

        g.setColor(Color.YELLOW);
        switch (direction) {
            case "UP" -> g.fillRect(x + size/2 - 2, y, 4, 3);
            case "DOWN" -> g.fillRect(x + size/2 - 2, y + size - 3, 4, 3);
            case "LEFT" -> g.fillRect(x, y + size/2 - 2, 3, 4);
            case "RIGHT" -> g.fillRect(x + size - 3, y + size/2 - 2, 3, 4);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString("P", x + size/2 - 3, y + size/2 + 3);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public float getSizeMultiplier() {
        return SIZE_MULTIPLIER;
    }

    public String getDirection() {
        return direction;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getTileX() { return x; }
    public int getTileY() { return y; }
}
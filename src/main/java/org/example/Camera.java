package org.example;

import java.awt.*;

public class Camera {
    private int x, y;
    private final int viewportWidth;
    private final int viewportHeight;
    private int currentSceneWidth;
    private int currentSceneHeight;

    public Camera(int viewportWidth, int viewportHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.x = 0;
        this.y = 0;
        this.currentSceneWidth = 0;
        this.currentSceneHeight = 0;
    }

    public void update(int targetX, int targetY, int sceneWidth, int sceneHeight) {
        this.currentSceneWidth = sceneWidth;
        this.currentSceneHeight = sceneHeight;

        int sceneWidthPixels = sceneWidth * Game.TILE_SIZE;
        int sceneHeightPixels = sceneHeight * Game.TILE_SIZE;
        int viewportWidthPixels = viewportWidth * Game.TILE_SIZE;
        int viewportHeightPixels = viewportHeight * Game.TILE_SIZE;

        if (sceneWidthPixels <= viewportWidthPixels) {
            x = -(viewportWidth - sceneWidth) / 2;
        } else {
            x = targetX - viewportWidth / 2;
            x = Math.max(0, Math.min(x, sceneWidth - viewportWidth));
        }

        if (sceneHeightPixels <= viewportHeightPixels) {
            y = -(viewportHeight - sceneHeight) / 2;
        } else {
            y = targetY - viewportHeight / 2;
            y = Math.max(0, Math.min(y, sceneHeight - viewportHeight));
        }
    }

    public boolean isVisible(int worldX, int worldY) {
        int sceneWidthPixels = currentSceneWidth * Game.TILE_SIZE;
        int sceneHeightPixels = currentSceneHeight * Game.TILE_SIZE;
        int viewportWidthPixels = viewportWidth * Game.TILE_SIZE;
        int viewportHeightPixels = viewportHeight * Game.TILE_SIZE;

        if (sceneWidthPixels <= viewportWidthPixels || sceneHeightPixels <= viewportHeightPixels) {
            return worldX >= 0 && worldX < currentSceneWidth &&
                    worldY >= 0 && worldY < currentSceneHeight;
        }

        return worldX >= x && worldX < x + viewportWidth &&
                worldY >= y && worldY < y + viewportHeight;
    }

    public int getViewportX(int worldX) {
        return worldX - x;
    }

    public int getViewportY(int worldY) {
        return worldY - y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return viewportWidth; }
    public int getHeight() { return viewportHeight; }

    public Rectangle getVisibleWorldArea() {
        int sceneWidthPixels = currentSceneWidth * Game.TILE_SIZE;
        int sceneHeightPixels = currentSceneHeight * Game.TILE_SIZE;
        int viewportWidthPixels = viewportWidth * Game.TILE_SIZE;
        int viewportHeightPixels = viewportHeight * Game.TILE_SIZE;

        if (sceneWidthPixels <= viewportWidthPixels && sceneHeightPixels <= viewportHeightPixels) {
            return new Rectangle(0, 0, currentSceneWidth, currentSceneHeight);
        } else {
            return new Rectangle(x, y, viewportWidth, viewportHeight);
        }
    }
}

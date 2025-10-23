package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameRenderer {
    private GamePanel gamePanel;
    private boolean debugPrinted = false;

    public GameRenderer(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void drawGame(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Only draw world if title and narrative are not visible
        if (!gamePanel.getTitleScreen().isTitleVisible() &&
                !gamePanel.getNarrativeScreen().isNarrativeVisible()) {
            drawWorld(g2d);
            drawExitDoors(g2d);
            drawInteractiveObjects(g2d);
            drawPlayer(g2d);
            drawFPS(g2d);
        }
    }

    private void drawWorld(Graphics2D g) {
        int cameraX = gamePanel.getCamera().getX();
        int cameraY = gamePanel.getCamera().getY();
        int tileSize = Game.TILE_SIZE;

        if (gamePanel.getSceneTile().usesBackgroundImage()) {
            drawBackgroundImageAsObject(g, cameraX, cameraY, tileSize);
            return;
        }

        Rectangle visibleArea = gamePanel.getCamera().getVisibleWorldArea();

        for (int worldX = visibleArea.x; worldX < visibleArea.x + visibleArea.width; worldX++) {
            for (int worldY = visibleArea.y; worldY < visibleArea.y + visibleArea.height; worldY++) {
                if (worldX >= 0 && worldX < gamePanel.getSceneTile().getCols() &&
                        worldY >= 0 && worldY < gamePanel.getSceneTile().getRows()) {

                    int screenX = (worldX - cameraX) * tileSize;
                    int screenY = (worldY - cameraY) * tileSize;

                    TileType tile = gamePanel.getSceneTile().getTile(worldX, worldY);

                    if (tile == TileType.DOOR) {
                        continue;
                    }

                    if (tile != null) {
                        BufferedImage tileImage = tile.getImage();
                        if (tileImage != null) {
                            g.drawImage(tileImage, screenX, screenY, null);
                        } else {
                            drawFallbackTile(g, tile, screenX, screenY);
                        }
                    }
                }
            }
        }
    }

    private void drawExitDoors(Graphics2D g) {
        int cameraX = gamePanel.getCamera().getX();
        int cameraY = gamePanel.getCamera().getY();
        int tileSize = Game.TILE_SIZE;

        Rectangle visibleArea = gamePanel.getCamera().getVisibleWorldArea();

        for (int worldX = visibleArea.x; worldX < visibleArea.x + visibleArea.width; worldX++) {
            for (int worldY = visibleArea.y; worldY < visibleArea.y + visibleArea.height; worldY++) {
                if (worldX >= 0 && worldX < gamePanel.getSceneTile().getCols() &&
                        worldY >= 0 && worldY < gamePanel.getSceneTile().getRows()) {

                    TileType tile = gamePanel.getSceneTile().getTile(worldX, worldY);

                    if (tile == TileType.DOOR) {
                        int screenX = (worldX - cameraX) * tileSize;
                        int screenY = (worldY - cameraY) * tileSize;

                        BufferedImage tileImage = tile.getImage();
                        if (tileImage != null) {
                            g.drawImage(tileImage, screenX, screenY, null);
                        } else {
                            drawFallbackTile(g, tile, screenX, screenY);
                        }
                    }
                }
            }
        }
    }

    private void drawBackgroundImageAsObject(Graphics2D g, int cameraX, int cameraY, int tileSize) {
        BufferedImage bgImage = gamePanel.getSceneTile().getBackgroundImage();
        if (bgImage != null) {

            int sceneCols = gamePanel.getSceneTile().getCols();
            int sceneRows = gamePanel.getSceneTile().getRows();

            int scenePixelWidth = sceneCols * tileSize;
            int scenePixelHeight = sceneRows * tileSize;

            int screenX = (0 - cameraX) * tileSize;
            int screenY = (0 - cameraY) * tileSize;

            g.drawImage(bgImage, screenX, screenY, scenePixelWidth, scenePixelHeight, null);

        } else {

            g.setColor(Color.GREEN);
            g.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
        }
    }

    private void drawFallbackTile(Graphics2D g, TileType tile, int x, int y) {
        Color color = Color.MAGENTA;

        if (tile != null) {
            color = switch (tile) {
                case GRASS -> Color.GREEN;
                case WATER -> Color.BLUE;
                case SAND -> Color.YELLOW;
                case TILE1 -> Color.GRAY;
                case TILE2 -> Color.DARK_GRAY;
                case CARPET_LEFT -> Color.RED;
                case CARPET_MIDDLE -> Color.RED;
                case CARPET_RIGHT -> Color.RED;
                case DOOR -> new Color(139, 69, 19);
                default -> Color.MAGENTA;
            };
        }

        g.setColor(color);
        g.fillRect(x, y, Game.TILE_SIZE, Game.TILE_SIZE);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, Game.TILE_SIZE, Game.TILE_SIZE);

        if (tile == TileType.DOOR) {
            g.setColor(new Color(210, 180, 140));
            g.fillRect(x + 8, y + 4, Game.TILE_SIZE - 16, Game.TILE_SIZE - 8);
            g.setColor(Color.BLACK);
            g.drawRect(x + 8, y + 4, Game.TILE_SIZE - 16, Game.TILE_SIZE - 8);
        }
    }

    private void drawInteractiveObjects(Graphics2D g) {

        int cameraX = gamePanel.getCamera().getX();
        int cameraY = gamePanel.getCamera().getY();
        int tileSize = Game.TILE_SIZE;

        for (InteractiveObject obj : gamePanel.getInteractiveObjects()) {
            if (gamePanel.getCamera().isVisible(obj.getTileX(), obj.getTileY())) {
                int screenX = (obj.getWorldX() - cameraX) * tileSize;
                int screenY = (obj.getWorldY() - cameraY) * tileSize;
                int width = obj.getWidthInTiles() * tileSize;
                int height = obj.getHeightInTiles() * tileSize;

                if (obj.getObjectImage() != null) {
                    g.drawImage(obj.getObjectImage(), screenX, screenY, width, height, null);
                } else {

                    g.setColor(Color.ORANGE);
                    g.fillRect(screenX, screenY, width, height);
                    g.setColor(Color.BLACK);
                    g.drawRect(screenX, screenY, width, height);
                }
            }
        }
    }

    private void drawPlayer(Graphics2D g) {
        gamePanel.getPlayer().draw(g, gamePanel.getCamera());
    }

    private void drawFPS(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("FPS: " + gamePanel.getCurrentFps(), 10, gamePanel.getHeight() - 30);
    }
}

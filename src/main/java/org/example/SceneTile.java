package org.example;

import java.awt.image.BufferedImage;

public class SceneTile {
    private int cols;
    private int rows;
    private TileType[][] tiles;
    private boolean[][] exitTiles;
    private BufferedImage backgroundImage;
    private boolean useBackgroundImage = false;

    public SceneTile(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        tiles = new TileType[cols][rows];
        exitTiles = new boolean[cols][rows];
    }

    public void setBackgroundImage(BufferedImage image) {
        this.backgroundImage = image;
        this.useBackgroundImage = true;
    }

    public boolean usesBackgroundImage() {
        return useBackgroundImage;
    }

    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    public TileType getTile(int x, int y) {
        if (x >= 0 && x < cols && y >= 0 && y < rows) {
            return tiles[x][y];
        }
        return null;
    }

    public void setTile(int x, int y, TileType tileType) {
        if (x >= 0 && x < cols && y >= 0 && y < rows) {
            tiles[x][y] = tileType;
        }
    }

    public void setExitTile(int x, int y, boolean isExit) {
        if (x >= 0 && x < cols && y >= 0 && y < rows) {
            exitTiles[x][y] = isExit;
        }
    }

    public boolean isExitTile(int x, int y) {
        if (x >= 0 && x < cols && y >= 0 && y < rows) {
            return exitTiles[x][y];
        }
        return false;
    }

    public boolean isCollidable(int x, int y) {
        TileType tile = getTile(x, y);

        if (tile == null) {
            return false;
        }
        return tile.isCollidable();
    }

    public boolean hasExitTiles() {
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                if (exitTiles[x][y]) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getCols() { return cols; }
    public int getRows() { return rows; }
    public TileType[][] getTiles() { return tiles; }
}
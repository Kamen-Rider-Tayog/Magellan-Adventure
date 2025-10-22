package org.example;

import java.awt.image.BufferedImage;

public enum TileType {
    GRASS("grass.png", false),
    WATER("water.png", true),
    SAND("sand.png", false),
    TILE1("tile1.png", false),
    TILE2("tile2.png", false),
    CARPET_LEFT("carpetLeft.png", false),
    CARPET_MIDDLE("carpetMiddle.png", false),
    CARPET_RIGHT("carpetRight.png", false),
    DOOR("door.png", false);  // NEW: Door tile for exits

    private final String imagePath;
    private final boolean collidable;
    private BufferedImage image;

    TileType(String imagePath, boolean collidable) {
        this.imagePath = "assets/tiles/" + imagePath;
        this.collidable = collidable;
    }

    public void loadImage() {
        this.image = ImageLoader.loadImage(imagePath, Game.TILE_SIZE, Game.TILE_SIZE);
    }

    public BufferedImage getImage() {
        if (image == null) {
            loadImage();
        }
        return image;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public static void preloadAll() {
        for (TileType type : values()) {
            type.loadImage();
        }
    }
}
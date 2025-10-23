package org.example;

public enum ObjectType {
    NPC_KING(true, 3, 3, "assets/objects/manuel.png", "M"),
    CHARLES(true, 2, 2, "assets/objects/charles.png", "C"),
    KAWAL(true, 2, 2, "assets/objects/kawal.png", "K"),
    SAILOR1(true, 2, 2, "assets/objects/sailor1.png", "S1"),
    SAILOR2(true, 2, 2, "assets/objects/sailor2.png", "S2"),
    HUMABON(true, 2, 2, "assets/objects/rajah.png", "H");

    private final boolean collidable;
    private final int widthInTiles;
    private final int heightInTiles;
    private final String imagePath;
    private final String symbol;

    ObjectType(boolean collidable, int widthInTiles, int heightInTiles, String imagePath, String symbol) {
        this.collidable = collidable;
        this.widthInTiles = widthInTiles;
        this.heightInTiles = heightInTiles;
        this.imagePath = imagePath;
        this.symbol = symbol;
    }

    public boolean isCollidable() { return collidable; }
    public int getWidthInTiles() { return widthInTiles; }
    public int getHeightInTiles() { return heightInTiles; }
    public String getImagePath() { return imagePath; }
    public String getSymbol() { return symbol; }
}
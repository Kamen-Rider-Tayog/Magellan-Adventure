package org.example;

public class CollisionDetector {
    private final GamePanel gamePanel;

    public CollisionDetector(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public boolean isValidPosition(int x, int y) {
        if (x < 0 || x >= gamePanel.getSceneTile().getCols() ||
                y < 0 || y >= gamePanel.getSceneTile().getRows()) {
            return false;
        }

        if (gamePanel.getSceneTile().isCollidable(x, y)) {
            return false;
        }

        for (InteractiveObject obj : gamePanel.getInteractiveObjects()) {
            if (obj.isCollidable() && isCollidingWithObject(x, y, obj)) {
                return false;
            }
        }

        return true;
    }

    private boolean isCollidingWithObject(int x, int y, InteractiveObject obj) {
        int objX = obj.getTileX();
        int objY = obj.getTileY();
        int objWidth = obj.getWidthInTiles();
        int objHeight = obj.getHeightInTiles();

        return x >= objX && x < objX + objWidth &&
                y >= objY && y < objY + objHeight;
    }

    public InteractiveObject getInteractableObjectInFront(int playerX, int playerY, String playerDirection) {
        for (InteractiveObject obj : gamePanel.getInteractiveObjects()) {
            if (obj.isPlayerFacing(playerX, playerY, playerDirection)) {
                return obj;
            }
        }
        return null;
    }
}

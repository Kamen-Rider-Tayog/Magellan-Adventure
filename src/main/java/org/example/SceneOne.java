package org.example;

public class SceneOne {
    private static SceneTile currentScene;
    private static int exitX;
    private static int exitY;

    public static void setup(SceneTile scene) {
        currentScene = scene;

        for (int x = 0; x < scene.getCols(); x++) {
            for (int y = 0; y < scene.getRows(); y++) {
                if ((x + y) % 2 == 0) {
                    scene.setTile(x, y, TileType.TILE1);
                } else {
                    scene.setTile(x, y, TileType.TILE2);
                }
            }
        }

        int sceneCols = scene.getCols();
        int middleStart = sceneCols / 2 - 2;
        int middleEnd = sceneCols / 2 + 1;

        for (int y = 4; y < scene.getRows(); y++) {
            if (middleStart >= 0 && middleStart < sceneCols) {
                scene.setTile(middleStart + 1, y, TileType.CARPET_LEFT);
            }
            if (middleStart + 2 >= 0 && middleStart + 2 < sceneCols) {
                scene.setTile(middleStart + 2, y, TileType.CARPET_MIDDLE);
            }
            if (middleEnd >= 0 && middleEnd < sceneCols) {
                scene.setTile(middleEnd, y, TileType.CARPET_RIGHT);
            }
        }

        // Set the single exit tile in the middle of the carpet at the bottom
        exitX = sceneCols / 2;
        exitY = scene.getRows() - 1;

        // Don't show as door initially - use carpet tile
        scene.setTile(exitX, exitY, TileType.CARPET_MIDDLE);

        updateExitTiles();
    }

    public static void updateExitTiles() {
        if (currentScene == null) return;

        // Only enable and show exit tile if player has talked to King Manuel
        if (MissionManager.hasTalkedToKingManuel()) {
            currentScene.setTile(exitX, exitY, TileType.DOOR); // Now show as door
            currentScene.setExitTile(exitX, exitY, true);
            System.out.println("Exit tile ENABLED and VISIBLE at (" + exitX + ", " + exitY + ") - player can leave the throne room!");
        } else {
            currentScene.setTile(exitX, exitY, TileType.CARPET_MIDDLE); // Keep as carpet
            currentScene.setExitTile(exitX, exitY, false);
            System.out.println("Exit tile DISABLED and HIDDEN - player must talk to King Manuel first");
        }
    }

    public static void setHasTalkedToKing(boolean talked) {
        MissionManager.setTalkedToKingManuel(talked);
        updateExitTiles(); // Refresh exit tiles immediately
    }

    public static boolean getHasTalkedToKing() {
        return MissionManager.hasTalkedToKingManuel();
    }

    public static String getCurrentMission() {
        if (!MissionManager.hasTalkedToKingManuel()) {
            return "\nPresent your westward route proposal to King Manuel I of Portugal.";
        } else {
            return "\nKing Manuel has rejected your proposal. Travel to Spain to seek support from King Charles V.";
        }
    }

    public static boolean canExitScene() {
        return MissionManager.hasTalkedToKingManuel();
    }
}
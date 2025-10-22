package org.example;

import java.awt.image.BufferedImage;

public class SceneTwo {
    private static SceneTile currentScene;
    private static int exitX = 59;
    private static int exitY = 45;

    public static void setup(SceneTile scene) {
        currentScene = scene;

        BufferedImage bgImage = ImageLoader.loadUnscaledImage("assets/backgrounds/dock.png");

        if (bgImage != null) {
            System.out.println("Background image loaded: " + bgImage.getWidth() + "x" + bgImage.getHeight());

            scene.setBackgroundImage(bgImage);

            initializeAllTilesAsWalkable(scene);

            setupCollisionAreas(scene);

            System.out.println("Scene 2 ready with background image covering 64x64 tiles");
        } else {
            System.err.println("Failed to load background image for scene 2, using tile fallback");
            setupFallback(scene);
        }

        updateExitTile();
    }

    private static void initializeAllTilesAsWalkable(SceneTile scene) {
        for (int x = 0; x < scene.getCols(); x++) {
            for (int y = 0; y < scene.getRows(); y++) {
                scene.setTile(x, y, TileType.GRASS);
            }
        }
    }

    private static void setupCollisionAreas(SceneTile scene) {
        for (int x = 20; x <= 60; x++) {
            scene.setTile(x, 41, TileType.WATER);
        }

        for (int x = 20; x <= 60; x++) {
            scene.setTile(x, 48, TileType.WATER);
        }

        for (int y = 41; y <= 49; y++) {
            scene.setTile(20, y, TileType.WATER);
        }

        for (int y = 41; y <= 49; y++) {
            scene.setTile(60, y, TileType.WATER);
        }
    }

    public static void updateExitTile() {
        if (currentScene == null) return;

        // Only set exit tile if player has talked to King Charles
        if (MissionManager.hasTalkedToKingCharles()) {
            currentScene.setExitTile(exitX, exitY, true);
            currentScene.setTile(exitX, exitY, TileType.DOOR);
            System.out.println("Exit tile ENABLED at (" + exitX + ", " + exitY + ") - player can sail now");
        } else {
            currentScene.setExitTile(exitX, exitY, false);
            currentScene.setTile(exitX, exitY, TileType.WATER); // Make it inaccessible
            System.out.println("Exit tile DISABLED - player must talk to Emperor Charles V first");
        }
    }

    private static void setupFallback(SceneTile scene) {
        for (int x = 0; x < scene.getCols(); x++) {
            for (int y = 0; y < scene.getRows(); y++) {
                scene.setTile(x, y, TileType.GRASS);
            }
        }

        for (int x = 0; x < scene.getCols(); x++) {
            scene.setTile(x, 0, TileType.WATER);
            scene.setTile(x, scene.getRows() - 1, TileType.WATER);
        }
        for (int y = 0; y < scene.getRows(); y++) {
            scene.setTile(0, y, TileType.WATER);
            scene.setTile(scene.getCols() - 1, y, TileType.WATER);
        }

        updateExitTile();
    }

    public static String getCurrentMission() {
        if (!MissionManager.hasTalkedToKingCharles()) {
            return "\nFind King Charles V and convince him to support your westward voyage to the Spice Islands.";
        } else {
            return "\nKing Charles has granted you ships and crew! Sail from the eastern dock to begin your historic journey.";
        }
    }

    // Call this when Emperor Charles dialogue completes
    public static void onKingCharlesDialogueComplete() {
        MissionManager.setTalkedToKingCharles(true);
        updateExitTile(); // Refresh exit tile immediately
    }

    // Add this method to match SceneOne's structure
    public static boolean getHasTalkedToKingCharles() {
        return MissionManager.hasTalkedToKingCharles();
    }

    // Add this method to match SceneOne's structure
    public static void setHasTalkedToKingCharles(boolean talked) {
        MissionManager.setTalkedToKingCharles(talked);
        updateExitTile();
    }
}
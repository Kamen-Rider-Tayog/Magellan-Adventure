package org.example;

import java.awt.image.BufferedImage;

public class SceneSix {
    private static SceneTile currentScene;
    private static int exitX = 0;
    private static int exitY;

    public static void setup(SceneTile scene) {
        currentScene = scene;
        exitY = scene.getRows() / 2;

        BufferedImage bgImage = ImageLoader.loadUnscaledImage("assets/backgrounds/cebu.png");

        if (bgImage != null) {
            System.out.println("Background image loaded for scene 6: " + bgImage.getWidth() + "x" + bgImage.getHeight());

            scene.setBackgroundImage(bgImage);
            initializeAllTilesAsWalkable(scene);
            setupCollisionAreas(scene);

            System.out.println("Scene 6 ready with final background image");
        } else {
            System.err.println("Failed to load background image for scene 6, using tile fallback");
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
        for (int y = 0; y <= 64; y++) {
            scene.setTile(50, y, TileType.WATER);
        }
    }

    public static void updateExitTile() {
        if (currentScene == null) return;

        // Only set exit tile if player has talked to Rajah Humabon
        if (MissionManager.hasTalkedToHumabon()) {
            currentScene.setExitTile(exitX, exitY, true);
            currentScene.setTile(exitX, exitY, TileType.DOOR);
            System.out.println("Exit tile ENABLED at (" + exitX + ", " + exitY + ") - blood compact completed!");
        } else {
            currentScene.setExitTile(exitX, exitY, false);
            currentScene.setTile(exitX, exitY, TileType.GRASS);
            System.out.println("Exit tile DISABLED - player must talk to Rajah Humabon first");
        }
    }

    private static void setupFallback(SceneTile scene) {
        for (int x = 0; x < scene.getCols(); x++) {
            for (int y = 0; y < scene.getRows(); y++) {
                if ((x + y) % 3 == 0) {
                    scene.setTile(x, y, TileType.SAND);
                } else if ((x + y) % 3 == 1) {
                    scene.setTile(x, y, TileType.GRASS);
                } else {
                    scene.setTile(x, y, TileType.TILE1);
                }
            }
        }
        updateExitTile();
    }

    public static String getCurrentMission() {
        if (!MissionManager.hasTalkedToHumabon()) {
            return "\nMeet with Rajah Humabon to establish an alliance and perform the blood compact ritual.";
        } else {
            return "\nThe blood compact is complete. Your historic journey has reached its conclusion.";
        }
    }

    // Mission management methods
    public static boolean getHasTalkedToHumabon() {
        return MissionManager.hasTalkedToHumabon();
    }

    public static void setHasTalkedToHumabon(boolean talked) {
        MissionManager.setTalkedToHumabon(talked);
        updateExitTile();
    }

    // Helper method to check if player can exit
    public static boolean canExitScene() {
        return MissionManager.hasTalkedToHumabon();
    }
}
package org.example;

import java.awt.image.BufferedImage;

public class SceneFive {
    private static SceneTile currentScene;
    private static int exitX = 45;
    private static int exitY = 17;

    public static void setup(SceneTile scene) {
        currentScene = scene;

        BufferedImage bgImage = ImageLoader.loadUnscaledImage("assets/backgrounds/ship.png");

        if (bgImage != null) {
            scene.setBackgroundImage(bgImage);
            initializeAllTilesAsWalkable(scene);
            setupCollisionAreas(scene);
        } else {
            System.err.println("Failed to load background image for scene 5, using tile fallback");
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
        // Upper water area
        for (int x = 0; x <= 20; x++) {
            scene.setTile(x, 10, TileType.WATER);
        }
        for (int x = 20; x <= 44; x++) {
            scene.setTile(x, 11, TileType.WATER);
        }
        for (int x = 44; x <= 61; x++) {
            scene.setTile(x, 10, TileType.WATER);
        }

        // Lower water area
        for (int x = 0; x <= 8; x++) {
            scene.setTile(x, 22, TileType.WATER);
        }
        for (int x = 8; x <= 20; x++) {
            scene.setTile(x, 23, TileType.WATER);
        }
        for (int x = 19; x <= 20; x++) {
            scene.setTile(x, 24, TileType.WATER);
        }
        for (int x = 22; x <= 44; x++) {
            scene.setTile(x, 25, TileType.WATER);
        }
        for (int x = 44; x <= 45; x++) {
            scene.setTile(x, 24, TileType.WATER);
        }
        for (int x = 45; x <= 61; x++) {
            scene.setTile(x, 23, TileType.WATER);
        }

        for (int x = 42; x <= 50; x++) {
            scene.setTile(x, 13, TileType.WATER);
        }

        for (int x = 42; x <= 44; x++) {
            scene.setTile(x, 14, TileType.WATER);
        }

        for (int x = 42; x <= 44; x++) {
            scene.setTile(x, 15, TileType.WATER);
        }

        for (int x = 42; x <= 44; x++) {
            scene.setTile(x, 16, TileType.WATER);
        }

        for (int x = 42; x <= 44; x++) {
            scene.setTile(x, 17, TileType.WATER);
        }

        for (int x = 42; x <= 44; x++) {
            scene.setTile(x, 18, TileType.WATER);
        }

        for (int x = 42; x <= 50; x++) {
            scene.setTile(x, 19, TileType.WATER);
        }
    }

    public static void updateExitTile() {
        if (currentScene == null) return;

        // Only set exit tile if player has talked to all sailors
        if (MissionManager.hasTalkedToAllSailors()) {
            currentScene.setExitTile(exitX, exitY, true);
            currentScene.setTile(exitX, exitY, TileType.DOOR);
            System.out.println("Exit tile ENABLED at (" + exitX + ", " + exitY + ") - all sailors talked to!");
        } else {
            currentScene.setExitTile(exitX, exitY, false);
            currentScene.setTile(exitX, exitY, TileType.GRASS);
            System.out.println("Exit tile DISABLED - player must talk to all crew members first");
        }
    }

    private static void setupFallback(SceneTile scene) {
        for (int x = 0; x < scene.getCols(); x++) {
            for (int y = 0; y < scene.getRows(); y++) {
                int centerX = scene.getCols() / 2;
                int centerY = scene.getRows() / 2;
                double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));

                if (distance < 5) {
                    scene.setTile(x, y, TileType.SAND);
                } else {
                    scene.setTile(x, y, TileType.GRASS);
                }
            }
        }
        updateExitTile();
    }

    public static String getCurrentMission() {
        if (!MissionManager.hasTalkedToAllSailors()) {
            return "\nCheck on your crew members. They are suffering from hunger and losing hope after the long Pacific crossing.";
        } else {
            return "\nLand has been sighted! Continue to Guam for much-needed provisions and rest.";
        }
    }

    // Mission management methods
    public static boolean getHasTalkedToAllSailors() {
        return MissionManager.hasTalkedToAllSailors();
    }

    public static void setHasTalkedToAllSailors(boolean talked) {
        MissionManager.setTalkedToAllSailors(talked);
        updateExitTile();
    }

    // Helper method to check if player can exit
    public static boolean canExitScene() {
        return MissionManager.hasTalkedToAllSailors();
    }
}
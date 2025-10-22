package org.example;

import java.awt.image.BufferedImage;

public class SceneThree {
    private static SceneTile currentScene;
    private static int exitX = 45;
    private static int exitY = 17;

    public static void setup(SceneTile scene) {
        currentScene = scene;

        BufferedImage bgImage = ImageLoader.loadUnscaledImage("assets/backgrounds/ship.png");

        if (bgImage != null) {
            System.out.println("Background image loaded for scene 3: " + bgImage.getWidth() + "x" + bgImage.getHeight());

            scene.setBackgroundImage(bgImage);
            initializeAllTilesAsWalkable(scene);
            setupCollisionAreas(scene);

            System.out.println("Scene 3 ready with ship background image");
        } else {
            System.err.println("Failed to load background image for scene 3, using tile fallback");
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

        boolean canExit = MissionManager.hasTalkedToSailor();

        if (canExit) {
            currentScene.setExitTile(exitX, exitY, true);
            currentScene.setTile(exitX, exitY, TileType.DOOR);
            System.out.println("Exit tile ENABLED at (" + exitX + ", " + exitY + ") - player can continue the voyage!");
        } else {
            currentScene.setExitTile(exitX, exitY, false);
            currentScene.setTile(exitX, exitY, TileType.GRASS);
            System.out.println("Exit tile DISABLED - player must talk to the crew member first");
        }
    }

    private static void setupFallback(SceneTile scene) {
        for (int x = 0; x < scene.getCols(); x++) {
            for (int y = 0; y < scene.getRows(); y++) {
                if (y < 3) {
                    scene.setTile(x, y, TileType.WATER);
                } else if (y < 6) {
                    scene.setTile(x, y, TileType.SAND);
                } else {
                    scene.setTile(x, y, TileType.GRASS);
                }
            }
        }
        updateExitTile();
    }

    public static String getCurrentMission() {
        if (!MissionManager.hasTalkedToSailor()) {
            return "\nNavigate south along the American coastline and search for the passage to the Pacific Ocean. Talk to your crew member for updates.";
        } else {
            return "\nContinue sailing south. The crew is concerned about finding the passage, but you must press on toward the southern tip of the continent.";
        }
    }

    // Mission management methods
    public static boolean getHasTalkedToSailor() {
        return MissionManager.hasTalkedToSailor();
    }

    public static void setHasTalkedToSailor(boolean talked) {
        MissionManager.setTalkedToSailor(talked);
        updateExitTile();
        // Mission display will be updated automatically through the InfoBox system
    }

    // Helper method to check if player can exit
    public static boolean canExitScene() {
        return MissionManager.hasTalkedToSailor();
    }
}
package org.example;

import java.awt.image.BufferedImage;

public class SceneFour {
    private static SceneTile currentScene;
    private static int exitX;
    private static int exitY;

    public static void setup(SceneTile scene) {
        currentScene = scene;
        exitX = scene.getCols() / 2;
        exitY = 20;

        BufferedImage bgImage = ImageLoader.loadUnscaledImage("assets/backgrounds/island.png");

        if (bgImage != null) {
            System.out.println("Background image loaded for scene 4: " + bgImage.getWidth() + "x" + bgImage.getHeight());

            scene.setBackgroundImage(bgImage);
            initializeAllTilesAsWalkable(scene);
            setupCollisionAreas(scene);

            System.out.println("Scene 4 ready with forest background image");
        } else {
            System.err.println("Failed to load background image for scene 4, using tile fallback");
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
        for (int x = 15; x <= 40; x++) {
            scene.setTile(x, 5, TileType.WATER);
        }

        for (int y = 5; y <= 15; y++) {
            scene.setTile(17, y, TileType.WATER);
        }

        for (int x = 8; x <= 15; x++) {
            scene.setTile(x, 17, TileType.WATER);
        }

        for (int x = 35; x <= 40; x++) {
            scene.setTile(x, 35, TileType.WATER);
        }

        for (int x = 0; x <= 40; x++) {
            scene.setTile(x, 40, TileType.WATER);
        }

        for (int x = 35; x <= 40; x++) {
            scene.setTile(x, 30, TileType.WATER);
        }

        for (int y = 32; y <= 40; y++) {
            scene.setTile(35, y, TileType.WATER);
        }

        for (int y = 0; y <= 40; y++) {
            scene.setTile(8, y, TileType.WATER);
        }

        for (int y = 0; y <= 40; y++) {
            scene.setTile(40, y, TileType.WATER);
        }
    }

    public static void updateExitTile() {
        if (currentScene == null) return;

        // Only set exit tile if player has dealt with the mutineers
        if (MissionManager.hasDealtWithMutineers()) {
            currentScene.setExitTile(exitX, exitY, true);
            currentScene.setTile(exitX, exitY, TileType.DOOR);
            System.out.println("Exit tile ENABLED at (" + exitX + ", " + exitY + ") - mutiny resolved!");
        } else {
            currentScene.setExitTile(exitX, exitY, false);
            currentScene.setTile(exitX, exitY, TileType.GRASS);
            System.out.println("Exit tile DISABLED - player must confront the mutineers first");
        }
    }

    private static void setupFallback(SceneTile scene) {
        for (int x = 0; x < scene.getCols(); x++) {
            for (int y = 0; y < scene.getRows(); y++) {
                if (x < 2 || x >= scene.getCols() - 2) {
                    scene.setTile(x, y, TileType.WATER);
                } else {
                    scene.setTile(x, y, TileType.GRASS);
                }
            }
        }
        updateExitTile();
    }

    public static String getCurrentMission() {
        if (!MissionManager.hasDealtWithMutineers()) {
            return "\nConfront the mutineers who are plotting against your command. The fate of the expedition depends on your leadership.";
        } else {
            return "\nThe mutiny has been crushed. Continue your journey with a more unified, if smaller, fleet.";
        }
    }

    // Mission management methods
    public static boolean getHasDealtWithMutineers() {
        return MissionManager.hasDealtWithMutineers();
    }

    public static void setHasDealtWithMutineers(boolean dealtWith) {
        MissionManager.setDealtWithMutineers(dealtWith);
        updateExitTile();
    }

    // Helper method to check if player can exit
    public static boolean canExitScene() {
        return MissionManager.hasDealtWithMutineers();
    }
}
package org.example;

public class GameStateManager {
    private GamePanel gamePanel;
    private long lastFpsTime = 0;
    private int fps = 0;
    private int currentFps = 0;
    private long lastMoveTime = 0;
    private final long MOVE_DELAY = 75;

    public GameStateManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void update() {
        updateFPS();
        updateUIState();
        updateGameState();
    }

    private void updateFPS() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFpsTime >= 1000) {
            currentFps = fps;
            fps = 0;
            lastFpsTime = currentTime;
        }
        fps++;
    }

    private void updateUIState() {
        gamePanel.getMapOverlay().update();
        gamePanel.getDialogueBox().update();
        gamePanel.getNarrativeScreen().update();

        boolean isMapFullscreen = gamePanel.getMapOverlay().isFullscreenMode();
        boolean isSettingsVisible = gamePanel.getSettingsPanel().isSettingsVisible();
        boolean isDialogueVisible = gamePanel.getDialogueBox().isDialogueVisible();
        boolean isNarrativeVisible = gamePanel.getNarrativeScreen().isNarrativeVisible();
        boolean showArrows = !isMapFullscreen && !isSettingsVisible && !isNarrativeVisible;
        boolean showSettingsIcon = !isMapFullscreen && !isSettingsVisible && !isNarrativeVisible;
        boolean showMinimap = !isSettingsVisible && !isNarrativeVisible;

        gamePanel.getUIManager().updateUIVisibility(showArrows);

        if (isSettingsVisible || isNarrativeVisible) {
            gamePanel.getMapOverlay().setVisible(false);
        } else {
            gamePanel.getMapOverlay().setVisible(true);
        }

        if (isMapFullscreen && isSettingsVisible) {
            gamePanel.hideSettings();
        }
    }

    private void updateGameState() {
        if (canProcessGameInput()) {
            processPlayerMovement();
            updatePlayerAndCamera();
            gamePanel.checkForSceneTransition();
        }
    }

    private boolean canProcessGameInput() {
        return !gamePanel.getMapOverlay().isFullscreenMode() &&
                !gamePanel.getDialogueBox().isDialogueVisible() &&
                !gamePanel.getSettingsPanel().isSettingsVisible() &&
                !gamePanel.getNarrativeScreen().isNarrativeVisible();
    }

    private void processPlayerMovement() {
        long currentTime = System.currentTimeMillis();
        boolean canMove = (currentTime - lastMoveTime) >= MOVE_DELAY;

        int[] direction = gamePanel.getInputHandler().getMovementDirection();
        int dx = direction[0];
        int dy = direction[1];

        if ((dx != 0 || dy != 0) && canMove) {
            int newX = gamePanel.getPlayer().getTileX() + dx;
            int newY = gamePanel.getPlayer().getTileY() + dy;

            if (gamePanel.getCollisionDetector().isValidPosition(newX, newY)) {
                gamePanel.getPlayer().setMoving(true, dx, dy);
                gamePanel.getPlayer().move(dx, dy, gamePanel.getSceneTile().getTiles());
                lastMoveTime = currentTime;
            }
        } else if (dx == 0 && dy == 0) {
            gamePanel.getPlayer().setMoving(false, 0, 0);
        }
    }

    private void updatePlayerAndCamera() {
        gamePanel.getPlayer().updateAnimation();
        gamePanel.getCamera().update(
                gamePanel.getPlayer().getTileX(),
                gamePanel.getPlayer().getTileY(),
                gamePanel.getSceneTile().getCols(),
                gamePanel.getSceneTile().getRows()
        );
    }

    public int getCurrentFps() {
        return currentFps;
    }
}

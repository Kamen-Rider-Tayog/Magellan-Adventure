package org.example;

import java.awt.event.KeyEvent;

public class InputHandler {
    private boolean[] keys;
    private GamePanel gamePanel;

    public InputHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.keys = new boolean[256];
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Check title screen first
        if (gamePanel.getTitleScreen().isTitleVisible()) {
            handleTitleScreenInput(e);
            return;
        }

        if (keyCode == KeyEvent.VK_ESCAPE) {
            if (gamePanel.getDialogueBox().isDialogueVisible()) {
                e.consume();
                return;
            }
            handleEscapeKey();
            e.consume();
            return;
        }

        if (keyCode == KeyEvent.VK_Q) {
            // Toggle info box visibility
            if (!gamePanel.getDialogueBox().isDialogueVisible() &&
                    !gamePanel.getNarrativeScreen().isNarrativeVisible()) {
                gamePanel.getUIManager().getInfoBox();
                e.consume();
                return;
            }
        }

        if (keyCode < keys.length) {
            keys[keyCode] = true;
        }

        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
            gamePanel.getUIManager().getUpArrow().setLit(true);
        } else if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
            gamePanel.getUIManager().getDownArrow().setLit(true);
        } else if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            gamePanel.getUIManager().getLeftArrow().setLit(true);
        } else if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            gamePanel.getUIManager().getRightArrow().setLit(true);
        }

        if (keyCode == KeyEvent.VK_E) {
            handleInteractKey();
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode < keys.length) {
            keys[keyCode] = false;
        }

        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
            gamePanel.getUIManager().getUpArrow().setLit(false);
        } else if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
            gamePanel.getUIManager().getDownArrow().setLit(false);
        } else if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            gamePanel.getUIManager().getLeftArrow().setLit(false);
        } else if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            gamePanel.getUIManager().getRightArrow().setLit(false);
        }
    }

    private void handleEscapeKey() {
        if (gamePanel.getMapOverlay().isFullscreenMode()) {
            gamePanel.getMapOverlay().showMinimap();
        } else if (gamePanel.getSettingsPanel().isSettingsVisible()) {
            gamePanel.hideSettings();
        } else {
            gamePanel.showSettings();
        }
    }

    private void handleInteractKey() {
        if (gamePanel.getNarrativeScreen().isNarrativeVisible()) {
            gamePanel.advanceNarrative();
            return;
        }

        if (gamePanel.getDialogueBox().isDialogueVisible()) {
            gamePanel.checkForInteraction();
        } else {
            gamePanel.checkForInteraction();
        }
    }

    private void handleTitleScreenInput(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            gamePanel.getTitleScreen().hideTitle();
            // Start the narrative after title screen
            gamePanel.getSceneManager().startGameWithNarrative();
        }
    }

    public boolean isKeyPressed(int keyCode) {
        return keyCode < keys.length && keys[keyCode];
    }

    public void setKeyPressed(int keyCode, boolean pressed) {
        if (keyCode < keys.length) {
            keys[keyCode] = pressed;
        }
    }

    public int[] getMovementDirection() {
        if (gamePanel.getDialogueBox().isDialogueVisible()) {
            return new int[]{0, 0};
        }

        int dx = 0, dy = 0;
        if (isKeyPressed(KeyEvent.VK_W)) dy -= 1;
        if (isKeyPressed(KeyEvent.VK_S)) dy += 1;
        if (isKeyPressed(KeyEvent.VK_A)) dx -= 1;
        if (isKeyPressed(KeyEvent.VK_D)) dx += 1;
        return new int[]{dx, dy};
    }
}
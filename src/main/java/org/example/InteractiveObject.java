package org.example;

import java.awt.image.BufferedImage;

public class InteractiveObject {
    private final int worldX;
    private final int worldY;
    private final String name;
    private final ObjectType type;
    private final String[] dialogues;
    private Runnable onDialogueComplete;

    private BufferedImage objectImage;
    private int currentDialogueIndex = 0;
    private boolean imageLoaded = false;
    private String[] alternatingSpeakers;

    public void setAlternatingSpeakers(String[] speakers) {
        this.alternatingSpeakers = speakers;
    }

    public String getCurrentSpeaker() {
        if (alternatingSpeakers != null && alternatingSpeakers.length > currentDialogueIndex) {
            return alternatingSpeakers[currentDialogueIndex];
        }
        return name; // fallback to original name
    }

    public InteractiveObject(int x, int y, String name, String dialogue, ObjectType type) {
        this(x, y, name, new String[]{dialogue}, type);
    }

    public InteractiveObject(int x, int y, String name, String[] dialogues, ObjectType type) {
        this.worldX = x;
        this.worldY = y;
        this.name = name;
        this.dialogues = dialogues;
        this.type = type;

        loadObjectImage();
    }

    private void loadObjectImage() {
        objectImage = ObjectImageLoader.loadImage(type);
        imageLoaded = true;
    }

    public boolean isPlayerFacing(int playerX, int playerY, String playerDirection) {
        for (int ox = 0; ox < type.getWidthInTiles(); ox++) {
            for (int oy = 0; oy < type.getHeightInTiles(); oy++) {
                int objX = worldX + ox;
                int objY = worldY + oy;

                int dx = objX - playerX;
                int dy = objY - playerY;

                boolean isAdjacent = (Math.abs(dx) + Math.abs(dy) == 1);
                if (!isAdjacent) continue;

                return switch (playerDirection) {
                    case "UP" -> dy == -1;
                    case "DOWN" -> dy == 1;
                    case "LEFT" -> dx == -1;
                    case "RIGHT" -> dx == 1;
                    default -> false;
                };
            }
        }
        return false;
    }

    // Dialogue management - KEEP THIS VERSION OF getNextDialogue()
    public String getNextDialogue() {
        if (currentDialogueIndex < dialogues.length - 1) {
            currentDialogueIndex++;
        } else {
            // If this is the last dialogue, trigger completion callback
            triggerDialogueComplete();
        }
        return getCurrentDialogue();
    }

    public void resetDialogue() {
        currentDialogueIndex = 0;
    }

    public String getCurrentDialogue() {
        return dialogues[currentDialogueIndex];
    }

    public boolean hasMultipleDialogues() {
        return dialogues.length > 1;
    }

    public boolean isLastDialogue() {
        return currentDialogueIndex >= dialogues.length - 1;
    }

    // Callback for dialogue completion
    public void setOnDialogueComplete(Runnable onDialogueComplete) {
        this.onDialogueComplete = onDialogueComplete;
    }

    public void triggerDialogueComplete() {
        if (onDialogueComplete != null) {
            onDialogueComplete.run();
        }
    }

    // Getters
    public int getWorldX() { return worldX; }
    public int getWorldY() { return worldY; }
    public int getTileX() { return worldX; }
    public int getTileY() { return worldY; }
    public int getWidthInTiles() { return type.getWidthInTiles(); }
    public int getHeightInTiles() { return type.getHeightInTiles(); }
    public String getName() { return name; }
    public String getDialogue() { return getCurrentDialogue(); }
    public ObjectType getType() { return type; }
    public boolean isCollidable() { return type.isCollidable(); }
    public String[] getDialogues() { return dialogues; }
    public BufferedImage getObjectImage() { return objectImage; }
    public int getCurrentDialogueIndex() { return currentDialogueIndex; }
}
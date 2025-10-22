package org.example;

import java.awt.image.BufferedImage;

public class SceneManager {
    private GamePanel gamePanel;
    private SceneTile currentScene;
    private int currentSceneIndex = 0;
    private boolean waitingForNarrative = false;
    private boolean gameCompleted = false;
    private boolean hasShownInitialNarrative = false;

    private static final int[][] SCENE_DIMENSIONS = {
            {21, 30},
            {64, 64},
            {61, 32},
            {48, 48},
            {61, 32},
            {64, 64}
    };

    private static final String[][] SCENE_NARRATIVES = {
            {
                    "Portugal and Spain, two formidable maritime powers,",
                    "found themselves in a fierce contest.",
                    "Both sought new lands and lucrative trade routes to the East,",
                    "particularly for the coveted spices that fueled European nobility.",
                    "",
                    "A young, determined FERDINAND MAGELLAN",
                    "stands before the Portuguese monarch..."
            },
            {
                    "Denied by his homeland, Magellan made a momentous decision:",
                    "He renounced his allegiance to Portugal and became a naturalized Spanish citizen,",
                    "determined to find support for his ambitious westward voyage."
            },
            {
                    "Magellan's fleet set sail, crossing the Atlantic from Seville, Spain.",
                    "They navigated south along the American coastline, seeking the elusive passage to the Pacific."
            },
            {
                    "During the harsh winter months, while anchored at Port Julian in southern Argentina, tensions flared.",
                    "A plot to overthrow Magellan's command, led by Portuguese captains in his fleet, began to unfold."
            },
            {
                    "Another ship was lost at Port Julian. Then, on October 21st, 1520,",
                    "after months of relentless searching, Magellan finally located the long-sought waterway:",
                    "the Strait of Magellan. For thirty-eight harrowing days, they navigated the treacherous passage,",
                    "finally emerging into the vast expanse of the Pacific Ocean."
            },
            {
                    "On March 6th, 1521, Magellan's expedition made landfall on the island of Guam,",
                    "finding much-needed fresh provisions and rest.",
                    "Ten days later, on March 16th, 1521, they arrived in the Philippines,",
                    "marking a pivotal moment in their epic voyage of discovery.",

            },
            {
                    "As a gesture of goodwill and to formalize their alliance,",
                    "Magellan and Rajah Humabon performed the ancient ritual of the blood compact,",
                    "mixing their blood as a testament to their unbreakable friendship.",
                    "This marked the beginning of a complex and fateful chapter",
                    "in the history of the Philippines.",
                    "",
                    "Thank you for experiencing Magellan's Journey!"
            }
    };

    public SceneManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.currentSceneIndex = -1;
        initializeFirstScene();
    }

    public void startGameWithNarrative() {
        if (!hasShownInitialNarrative && SCENE_NARRATIVES.length > 0) {
            System.out.println("Showing initial narrative...");
            gamePanel.showNarrative(SCENE_NARRATIVES[0]);
            waitingForNarrative = true;
            hasShownInitialNarrative = true;
        }
    }

    private void initializeFirstScene() {
        currentScene = new SceneTile(SCENE_DIMENSIONS[0][0], SCENE_DIMENSIONS[0][1]);
        SceneOne.setup(currentScene);
    }

    public void transitionToNextScene() {
        if (waitingForNarrative || gameCompleted) {
            return;
        }

        // For scene 0, check if king dialogue is completed
        if (currentSceneIndex == 0) {
            if (!SceneOne.getHasTalkedToKing()) {
                if (gamePanel.getDialogueBox() != null) {
                    gamePanel.getDialogueBox().showDialogue("System", "You should talk to King Manuel before leaving.");
                }
                return;
            }
        }

        // For scene 1, check if King Charles dialogue is completed
        if (currentSceneIndex == 1) {
            if (!MissionManager.hasTalkedToKingCharles()) {
                if (gamePanel.getDialogueBox() != null) {
                    gamePanel.getDialogueBox().showDialogue("System", "You should talk to King Charles before sailing.");
                }
                return;
            }
        }

        // ADD THIS: For scene 2, check if sailor dialogue is completed
        if (currentSceneIndex == 2) {
            if (!MissionManager.hasTalkedToSailor()) {
                if (gamePanel.getDialogueBox() != null) {
                    gamePanel.getDialogueBox().showDialogue("System", "You should talk to your crew member before continuing.");
                }
                return;
            }
        }

        if (currentSceneIndex == 3) {
            if (!MissionManager.hasDealtWithMutineers()) {
                if (gamePanel.getDialogueBox() != null) {
                    gamePanel.getDialogueBox().showDialogue("System", "You must confront the mutineers before continuing.");
                }
                return;
            }
        }

        if (currentSceneIndex == 4) {
            if (!MissionManager.hasTalkedToAllSailors()) {
                if (gamePanel.getDialogueBox() != null) {
                    gamePanel.getDialogueBox().showDialogue("System", "You should check on all your crew members before continuing.");
                }
                return;
            }
        }

        // ADD THIS: For scene 5, check if Humabon dialogue is completed
        if (currentSceneIndex == 5) {
            if (!MissionManager.hasTalkedToHumabon()) {
                if (gamePanel.getDialogueBox() != null) {
                    gamePanel.getDialogueBox().showDialogue("System", "You must speak with Rajah Humabon to complete your journey.");
                }
                return;
            }
        }


        int nextSceneIndex = currentSceneIndex + 1;

        if (nextSceneIndex >= SCENE_DIMENSIONS.length) {
            // Show FINAL narrative (index 6) when completing the last scene
            int finalNarrativeIndex = SCENE_NARRATIVES.length - 1; // This will be 6
            if (finalNarrativeIndex < SCENE_NARRATIVES.length) {
                gamePanel.showNarrative(SCENE_NARRATIVES[finalNarrativeIndex]);
                waitingForNarrative = true;
                gameCompleted = true;
            } else {
                exitGame();
            }
            return;
        }

        int narrativeIndex = nextSceneIndex;
        if (narrativeIndex >= 0 && narrativeIndex < SCENE_NARRATIVES.length) {
            gamePanel.showNarrative(SCENE_NARRATIVES[narrativeIndex]);
            waitingForNarrative = true;
        } else {
            currentSceneIndex = nextSceneIndex;
            proceedToNextScene();
        }
    }

    public void proceedToNextScene() {
        waitingForNarrative = false;

        if (gameCompleted) {
            exitGame();
            return;
        }

        // Special case: if we just finished the initial narrative, go to scene 0
        if (currentSceneIndex == -1) {
            currentSceneIndex = 0;
            currentScene = new SceneTile(SCENE_DIMENSIONS[0][0], SCENE_DIMENSIONS[0][1]);
            SceneOne.setup(currentScene);

            // Position player in the throne room
            gamePanel.getPlayer().setPosition(
                    SCENE_DIMENSIONS[0][0] / 2,
                    SCENE_DIMENSIONS[0][1] / 2
            );

            gamePanel.clearInteractiveObjects();
            gamePanel.initializeInteractiveObjectsForScene(0);
            gamePanel.resetCamera();

            // Update mission info
            InfoBox.setCurrentSceneIndex(0);
            if (gamePanel.getUIManager() != null && gamePanel.getUIManager().getInfoBox() != null) {
                gamePanel.getUIManager().getInfoBox().updateMission();
            }

            return;
        }

        currentSceneIndex++;

        if (currentSceneIndex >= SCENE_DIMENSIONS.length) {
            exitGame();
            return;
        }

        currentScene = new SceneTile(
                SCENE_DIMENSIONS[currentSceneIndex][0],
                SCENE_DIMENSIONS[currentSceneIndex][1]
        );

        switch(currentSceneIndex) {
            case 0:
                SceneOne.setup(currentScene);
                break;
            case 1:
                SceneTwo.setup(currentScene);
                break;
            case 2:
                SceneThree.setup(currentScene);
                break;
            case 3:
                SceneFour.setup(currentScene);
                break;
            case 4:
                SceneFive.setup(currentScene);
                break;
            case 5:
                SceneSix.setup(currentScene);
                break;
        }

        // Position player appropriately for each scene
        switch(currentSceneIndex) {
            case 0:
                gamePanel.getPlayer().setPosition(
                        SCENE_DIMENSIONS[currentSceneIndex][0] / 2,
                        SCENE_DIMENSIONS[currentSceneIndex][1] / 2
                );
                break;
            case 1:
                gamePanel.getPlayer().setPosition(25, 45);
                break;
            case 2:
                gamePanel.getPlayer().setPosition(25, 20);
                break;
            case 3:
                gamePanel.getPlayer().setPosition(24, 24);
                break;
            case 4:
                gamePanel.getPlayer().setPosition(15, 15);
                break;
            case 5:
                gamePanel.getPlayer().setPosition(9, 9);
                break;
            default:
                gamePanel.getPlayer().setPosition(
                        SCENE_DIMENSIONS[currentSceneIndex][0] / 2,
                        SCENE_DIMENSIONS[currentSceneIndex][1] / 2
                );
                break;
        }

        // Initialize interactive objects for the new scene
        gamePanel.clearInteractiveObjects();
        gamePanel.initializeInteractiveObjectsForScene(currentSceneIndex);

        // Reset camera to follow player
        gamePanel.resetCamera();

        // UPDATE INFOBOX WITH CURRENT SCENE
        if (gamePanel.getUIManager() != null && gamePanel.getUIManager().getInfoBox() != null) {
            InfoBox.setCurrentSceneIndex(currentSceneIndex);
            gamePanel.getUIManager().getInfoBox().updateMission();
        }

        System.out.println("Transitioned to scene " + currentSceneIndex + ": " +
                SCENE_DIMENSIONS[currentSceneIndex][0] + "x" + SCENE_DIMENSIONS[currentSceneIndex][1]);
    }

    private void exitGame() {
        System.out.println("Game completed! Exiting...");
        System.exit(0);
    }

    public int getCurrentSceneIndex() {
        return currentSceneIndex;
    }

    public SceneTile getCurrentScene() {
        return currentScene;
    }

    public boolean isExitTile(int x, int y) {
        return currentScene.isExitTile(x, y);
    }

    public boolean isKingDialogueCompleted() {
        return SceneOne.getHasTalkedToKing();
    }

    public boolean isWaitingForNarrative() {
        return waitingForNarrative;
    }
}
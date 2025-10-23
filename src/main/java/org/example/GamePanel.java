package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

public class GamePanel extends JPanel implements KeyListener {
    private Player player;
    private Camera camera;
    private SceneTile sceneTile;
    private MapOverlay mapOverlay;
    private DialogueBox dialogueBox;

    private InteractiveObject currentInteractingObject;
    private NarrativeScreen narrativeScreen;

    private UIManager uiManager;
    private InputHandler inputHandler;
    private GameRenderer gameRenderer;
    private CollisionDetector collisionDetector;
    private GameStateManager gameStateManager;
    private TitleScreen titleScreen;

    private InteractiveObject[] interactiveObjects;
    private final int viewportCols;
    private final int viewportRows;

    private SceneManager sceneManager;

    public GamePanel(int viewportCols, int viewportRows) {
        this.viewportCols = viewportCols;
        this.viewportRows = viewportRows;

        initializeGame();
        setupUI(); // This must be called before initializeInteractiveObjects()
        startGameLoop();
    }

    private void initializeGame() {
        setBackground(Color.BLACK);
        setFocusable(true);
        setLayout(null);
        addKeyListener(this);

        System.out.println("Initializing game...");
        System.out.println("Viewport: " + viewportCols + "x" + viewportRows);

        // INITIALIZE TITLE SCREEN FIRST
        titleScreen = new TitleScreen(this);
        add(titleScreen);

        // THEN INITIALIZE NARRATIVE SCREEN
        narrativeScreen = new NarrativeScreen(this);
        add(narrativeScreen);

        TileType.preloadAll();

        // THEN INITIALIZE SCENE MANAGER
        sceneManager = new SceneManager(this);
        sceneTile = sceneManager.getCurrentScene();

        int sceneCols = sceneTile.getCols();
        int sceneRows = sceneTile.getRows();

        System.out.println("Scene size: " + sceneCols + "x" + sceneRows);
        System.out.println("Viewport tiles: " + viewportCols + "x" + viewportRows);

        int startX = sceneCols / 2;
        int startY = sceneRows / 2;
        player = new Player(startX, startY);

        System.out.println("Player starting at: " + startX + ", " + startY);

        camera = new Camera(viewportCols, viewportRows);

        mapOverlay = new MapOverlay(this);
        add(mapOverlay);

        dialogueBox = new DialogueBox(this);
        add(dialogueBox);

        inputHandler = new InputHandler(this);
        gameRenderer = new GameRenderer(this);
        collisionDetector = new CollisionDetector(this);
        gameStateManager = new GameStateManager(this);

        System.out.println("Game initialization complete!");

        // SHOW TITLE SCREEN FIRST
        SwingUtilities.invokeLater(() -> {
            titleScreen.showTitle();
        });
    }

    private void setupUI() {
        uiManager = new UIManager(this);

        // Initialize mission text
        uiManager.getInfoBox().updateMission();

        updateUIPositions();
        updateComponentLayering();

        // NOW initialize interactive objects after UI is set up
        initializeInteractiveObjects();

        requestFocusInWindow();
    }

    private void initializeInteractiveObjects() {
        interactiveObjects = InteractiveObjectFactory.createSceneObjects(
                sceneManager.getCurrentSceneIndex(),
                sceneTile.getCols()
        );

        System.out.println("Created " + interactiveObjects.length + " interactive objects");

        // Ensure mission is set correctly for the current scene
        if (sceneManager.getCurrentSceneIndex() == 0) {
            uiManager.getInfoBox().updateMission();
        }
    }

    private void startGameLoop() {
        Timer timer = new Timer(16, e -> {
            gameStateManager.update();
            repaint();
        });
        timer.start();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        updateUIPositions();
    }

    public void updateUIPositions() {
        if (uiManager != null) {
            uiManager.updateUIPositions();
        }
    }

    public void updateComponentLayering() {
        if (uiManager != null) {
            uiManager.updateComponentLayering();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameRenderer.drawGame(g);
    }

    public void showSettings() {
        if (mapOverlay.isFullscreenMode()) {
            return;
        }

        mapOverlay.setVisible(false);
        uiManager.getSettingsPanel().showSettings();
        setComponentZOrder(uiManager.getSettingsPanel(), 0);
        revalidate();
        repaint();
        setCursor(Cursor.getDefaultCursor());
    }

    public void hideSettings() {
        uiManager.getSettingsPanel().hideSettings();
        mapOverlay.setVisible(true);

        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB),
                new Point(0, 0), "blank cursor"));

        resetInteraction();
    }

    public void checkForInteraction() {
        int playerX = player.getTileX();
        int playerY = player.getTileY();
        String playerDirection = player.getDirection();

        InteractiveObject obj = collisionDetector.getInteractableObjectInFront(playerX, playerY, playerDirection);
        if (obj != null) {
            handleObjectInteraction(obj);
        }
    }

    private void handleObjectInteraction(InteractiveObject obj) {
        if (dialogueBox.isDialogueVisible() && currentInteractingObject == obj) {
            if (isLastDialogue(obj)) {
                handleFinalDialogue(obj);
                resetInteraction();
            } else {
                String nextDialogue = obj.getNextDialogue();
                String speaker = obj.getCurrentSpeaker(); // Get the alternating speaker
                dialogueBox.showDialogue(speaker, nextDialogue);
            }
        } else {
            currentInteractingObject = obj;
            obj.resetDialogue();
            String initialSpeaker = obj.getCurrentSpeaker(); // Get initial speaker
            dialogueBox.showDialogue(initialSpeaker, obj.getDialogue());
        }
    }

    private boolean isLastDialogue(InteractiveObject obj) {
        return obj.isLastDialogue();
    }

    private void handleFinalDialogue(InteractiveObject obj) {
        if (obj.getType() == ObjectType.NPC_KING) {
            // Update mission system for King Manuel
            SceneOne.setHasTalkedToKing(true);
            uiManager.getInfoBox().updateMission();
            System.out.println("King Manuel dialogue completed - Mission updated!");
            updateTilesAfterKingDialogue();
        } else if (obj.getType() == ObjectType.CHARLES) {
            // Update mission system for King Charles
            MissionManager.setTalkedToKingCharles(true);
            SceneTwo.updateExitTile(); // Refresh Scene 2 exit tile
            uiManager.getInfoBox().updateMission();
            System.out.println("King Charles dialogue completed - Mission updated!");
        }

        // Trigger dialogue completion callback
        obj.triggerDialogueComplete();
    }

    public void resetInteraction() {
        currentInteractingObject = null;
        if (dialogueBox.isDialogueVisible()) {
            dialogueBox.hideDialogue();
        }
    }

    public boolean canAcceptInput() {
        return !titleScreen.isTitleVisible() &&
                !mapOverlay.isFullscreenMode() &&
                !dialogueBox.isDialogueVisible() &&
                !narrativeScreen.isNarrativeVisible() &&
                !uiManager.getSettingsPanel().isSettingsVisible();
    }

    public void checkForSceneTransition() {
        int playerX = player.getTileX();
        int playerY = player.getTileY();

        int currentSceneIndex = sceneManager.getCurrentSceneIndex();

        if (sceneManager.isExitTile(playerX, playerY) && !sceneManager.isWaitingForNarrative()) {
            // Check mission requirements based on current scene
            boolean canTransition = false;

            if (currentSceneIndex == 0) {
                // Scene 1: Need to talk to King Manuel
                canTransition = MissionManager.hasTalkedToKingManuel();
            } else if (currentSceneIndex == 1) {
                // Scene 2: Need to talk to King Charles
                canTransition = MissionManager.hasTalkedToKingCharles();
            } else {
                // Other scenes: No mission requirements
                canTransition = true;
            }

            if (canTransition) {
                sceneManager.transitionToNextScene();
                if (!sceneManager.isWaitingForNarrative()) {
                    sceneTile = sceneManager.getCurrentScene();
                }
            } else {
                // Show appropriate message
                String message = currentSceneIndex == 0 ?
                        "You should talk to King Manuel before leaving." :
                        "You should talk to King Charles before sailing.";
                dialogueBox.showDialogue("System", message);
            }
        }
    }

    public void showNarrative(String[] texts) {
        narrativeScreen.showNarrative(texts);
        setComponentZOrder(narrativeScreen, 0);
        revalidate();
        repaint();
    }

    public void advanceNarrative() {
        boolean isFinished = narrativeScreen.advance();

        if (isFinished) {
            sceneManager.proceedToNextScene();
            sceneTile = sceneManager.getCurrentScene();
        }
    }

    public void initializeInteractiveObjectsForScene(int sceneIndex) {
        clearInteractiveObjects();
        interactiveObjects = InteractiveObjectFactory.createSceneObjects(sceneIndex, sceneTile.getCols());

        // UPDATE INFOBOX WITH CURRENT SCENE
        if (uiManager != null && uiManager.getInfoBox() != null) {
            InfoBox.setCurrentSceneIndex(sceneIndex);
            uiManager.getInfoBox().updateMission();
        }

        System.out.println("Created " + interactiveObjects.length + " interactive objects for scene " + sceneIndex);
    }

    private void updateTilesAfterKingDialogue() {
        int sceneCols = sceneTile.getCols();
        int middleStart = sceneCols / 2 - 2;

        for (int x = middleStart + 2; x <= middleStart + 2; x++) {
            if (x >= 0 && x < sceneCols) {
                sceneTile.setTile(x, sceneTile.getRows() - 1, TileType.DOOR);
            }
        }

        System.out.println("Exit door revealed after king dialogue");
        repaint();
    }

    public void resetCamera() {
        camera.update(player.getTileX(), player.getTileY(),
                sceneTile.getCols(), sceneTile.getRows());
    }

    public NarrativeScreen getNarrativeScreen() {
        return narrativeScreen;
    }

    public void clearInteractiveObjects() {
        interactiveObjects = new InteractiveObject[0];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        inputHandler.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        inputHandler.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    // Getters
    public Player getPlayer() { return player; }
    public Camera getCamera() { return camera; }
    public SceneTile getSceneTile() { return sceneTile; }
    public MapOverlay getMapOverlay() { return mapOverlay; }
    public DialogueBox getDialogueBox() { return dialogueBox; }
    public UIManager getUIManager() { return uiManager; }
    public InputHandler getInputHandler() { return inputHandler; }
    public CollisionDetector getCollisionDetector() { return collisionDetector; }
    public SettingsPanel getSettingsPanel() { return uiManager.getSettingsPanel(); }
    public InteractiveObject[] getInteractiveObjects() { return interactiveObjects; }
    public int getCurrentFps() { return gameStateManager.getCurrentFps(); }
    public SceneManager getSceneManager() { return sceneManager; }
    public int getViewportCols() { return viewportCols; }
    public int getViewportRows() { return viewportRows; }
    public TitleScreen getTitleScreen() { return titleScreen; }
}

package com.comp2042.controller;

import com.comp2042.game.*;
import com.comp2042.ui.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Slider;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;


import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main GUI controller for the Tetris game.
 * Manages the JavaFX UI components, handles rendering, input, animations, and game state display.
 * Implements both Initializable for FXML loading and GameListener for game state updates.
 */
public class GuiController implements Initializable, GameListener {

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane gamePanel;

    @FXML
    Group groupNotification;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private VBox previewBox;

    @FXML
    private VBox leftDummy;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label levelLabel;

    @FXML
    private Label pauseLabel;

    @FXML Slider volumeSlider;

    private Rectangle[][] displayMatrix;

    InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Rectangle[][] ghostRectangles;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private GameController controller;

    private Sound sound;

    private PreviewRenderer previewRenderer;

    private HoldRenderer holdRenderer;

    private BoardRenderer boardRenderer;

    boolean spacePressed = false;

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public Rectangle[][] getDisplayMatrix() {
        return displayMatrix;
    }

    public GridPane getGamePanel() {
        return gamePanel;
    }

    public GameController getController() {
        return controller;
    }

    public boolean isPause() {
        return isPause.get();
    }

    @Override
    public void onBoardChanged(int[][] boardMatrix) { refreshGameBackground(boardMatrix); }

    /**
     * Initializes UI components, loads fonts, sets up input handlers, and configures audio.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        previewBox.setFillWidth(false);
        leftDummy.setFillWidth(false);
        previewBox.getChildren().forEach(n -> {
            if (n instanceof Region) {
                ((Region) n).setMaxHeight(Region.USE_PREF_SIZE);
                VBox.setVgrow(n, javafx.scene.layout.Priority.NEVER);
            }
        });
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        InputHandler inputHandler = new InputHandler(this);
        inputHandler.attach(gamePanel);
        gameOverPanel.setVisible(false);
        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
        sound = new Sound();
        sound.startMusic();
        volumeSlider.setFocusTraversable(false);
        volumeSlider.valueProperty().addListener((obs, oldV, newV) -> {
            double v = newV.doubleValue();
            if (v > 0 && sound.isMuted()) {
                sound.toggleMute();
            }
            sound.setVolume(v);
            Platform.runLater(() -> gamePanel.requestFocus());
        });
    }

    /**
     * Initializes all renderers, creates rectangle arrays for board/piece/ghost,
     * and starts the game loop timeline (600ms interval).
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        final int TOTAL_ROWS = boardMatrix.length;
        final int TOTAL_COLS = boardMatrix[0].length;

        displayMatrix = new Rectangle[TOTAL_ROWS][TOTAL_COLS];
        int[][] brickData = brick.getBrickData();
        rectangles = new Rectangle[brickData.length][brickData[0].length];
        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[0].length; j++) {
                rectangles[i][j] = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangles[i][j].getStyleClass().add("gameCell");
            }
        }

        int[][] ghostData = brick.getGhostBrick();
        ghostRectangles = new Rectangle[ghostData.length][ghostData[0].length];

        for (int i = 0; i < ghostData.length; i++) {
            for (int j = 0; j < ghostData[i].length; j++) {
                Rectangle rect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rect.setStroke(Color.WHITE);
                rect.setFill(Color.TRANSPARENT);
                rect.setVisible(false);
                ghostRectangles[i][j] = rect;
            }
        }

        boardRenderer = new BoardRenderer(gamePanel);
        boardRenderer.initBoard(boardMatrix, brick, displayMatrix, rectangles, ghostRectangles);

        previewRenderer = new PreviewRenderer(previewBox);
        holdRenderer = new HoldRenderer(leftDummy);

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(600),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();

        refreshBrick(brick);
    }



    private void refreshBrick(ViewData brick) {
        boardRenderer.refreshBrick(brick);
    }

    public void refreshGameBackground(int[][] board) {
        boardRenderer.refreshGameBackground(board);
    }

    /**
     * Handles automatic piece movement, plays sounds, shows score notifications,
     * and updates the display. Only processes if game is not paused.
     */
    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.isLocked()) {
                sound.playPlace();
            }
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                int baseBonus = downData.getClearRow().getScoreBonus();
                int level = controller.getBoard().getScore().getLevel();
                int displayBonus = baseBonus * level;
                NotificationPanel notificationPanel = new NotificationPanel("+" + displayBonus);
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty scoreProperty) {
        scoreLabel.textProperty().bind(scoreProperty.asString());
    }

    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

    /**
     * Resets game state, hides overlays, restarts timeline, and resets hold display.
     */
    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        holdRenderer.updateHold(new ViewData(new int[0][0], 0, 0, null, new int[0][0], 0, 0, new int[4][4]));
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
        pauseLabel.setVisible(false);
    }

    /**
     * Toggles pause state, pausing/resuming the timeline and showing/hiding pause overlay.
     * Notifies game controller of state change.
     */
    private void togglePause() {
        if (isGameOver.get()) return;
        boolean nowPaused = !isPause.get();
        isPause.set(nowPaused);

        if (nowPaused) {
            if (timeLine != null) timeLine.pause();
            showPauseOverlay();
        } else {
            hidePauseOverlay();
            if (timeLine != null) timeLine.play();
        }
        notifyPause(nowPaused);
        gamePanel.requestFocus();
    }

    public void bindLevel(IntegerProperty levelProperty) {
        levelLabel.textProperty().bind(levelProperty.asString());
    }

    private void showPauseOverlay() {
        pauseLabel.setVisible(true);
    }

    private void hidePauseOverlay() {
        pauseLabel.setVisible(false);
    }

    public Sound getSound() {
        return sound;
    }

    public void updatePreview(ViewData data) {
        previewRenderer.updatePreview(data);
    }

    public void updateHold(ViewData data) {
        holdRenderer.updateHold(data);
    }

    public void callRefreshBrick(ViewData brick) {
        refreshBrick(brick);
    }

    public void callMoveDown(MoveEvent event) {
        moveDown(event);
    }

    public void callTogglePause() {
        togglePause();
    }

    public boolean isGameOver() {
        return isGameOver.get();
    }

    public void onPreviewChanged(ViewData data) {
        updatePreview(data);
    }

    public void onHoldChanged(ViewData data) {
        updateHold(data);
    }

    public void onGameOver() {
        gameOver();
    }

    public void blockFallSpeed(int speedMillis) {
        if (timeLine == null) return;

        timeLine.stop();
        timeLine.getKeyFrames().setAll(new KeyFrame(
                Duration.millis(speedMillis),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.play();
    }

    public void notifyPause(boolean paused) {
        controller.onPauseStateChanged(paused);
    }
}

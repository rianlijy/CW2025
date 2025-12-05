package com.comp2042;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Slider;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;


import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

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

    @FXML private Slider volumeSlider;

    private GridPane holdGrid;
    private Rectangle[][] holdRects;
    private static final int HOLD_CELL = 12;

    private final List<GridPane> previewGrids = new ArrayList<>();

    private final List<Rectangle[][]> previewRectangles = new ArrayList<>();

    private static final int PREVIEW_CELL = 12;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Rectangle[][] ghostRectangles;

    private Timeline timeLine;

    private Timeline flashTimeline;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private GameController controller;

    private Timeline garbageTimer;

    private Sound sound;


    public void setController(GameController controller) {
        this.controller = controller;
        controller.getBoard().getScore().levelProperty().addListener((obs, oldV, newV) -> {
            int level = newV.intValue();
            double newSpeed = Math.max(200, 600 - (level - 1) * 100);

            timeLine.stop();
            timeLine.getKeyFrames().setAll(new KeyFrame(
                    Duration.millis(newSpeed),
                    ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
            ));
            timeLine.play();
        });
    }

    private boolean spacePressed = false;

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
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        if (spacePressed) return;
                        spacePressed = true;

                        DownData data = controller.hardDrop();
                        sound.playPlace();
                        refreshBrick(data.getViewData());
                        if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0) {
                            int baseBonus = data.getClearRow().getScoreBonus();
                            int level = controller.getBoard().getScore().getLevel();
                            int displayBonus = baseBonus * level;
                            NotificationPanel p = new NotificationPanel("+" + displayBonus);
                            groupNotification.getChildren().add(p);
                            p.showScore(groupNotification.getChildren());
                        }
                        keyEvent.consume();
                    }

                    if (keyEvent.getCode() == KeyCode.SHIFT || keyEvent.getCode() == KeyCode.C) {
                        ViewData data = controller.hold();
                        refreshBrick(data);
                        updateHold(data);
                        keyEvent.consume();
                    }
                }
                if (keyEvent.getCode() == KeyCode.P || keyEvent.getCode() == KeyCode.ESCAPE) {
                    togglePause();
                    keyEvent.consume();
                }
                if (keyEvent.getCode() == KeyCode.M) {
                    sound.toggleMute();

                    if (sound.isMuted()) {
                        volumeSlider.setValue(0);
                    } else {
                        volumeSlider.setValue(sound.getLastVolume());
                    }
                    keyEvent.consume();
                }
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }
            }
        });
        gamePanel.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.SPACE) {
                spacePressed = false;
            }
        });
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
        startGarbageTimer();
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        final int TOTAL_ROWS = boardMatrix.length;
        final int VISIBLE_ROWS = TOTAL_ROWS - 2;
        final int TOTAL_COLS = boardMatrix[0].length;
        gamePanel.getColumnConstraints().clear();
        for (int c = 0; c < TOTAL_COLS; c++) {
            javafx.scene.layout.ColumnConstraints cc = new javafx.scene.layout.ColumnConstraints();
            cc.setMinWidth(BRICK_SIZE);
            cc.setPrefWidth(BRICK_SIZE);
            cc.setMaxWidth(BRICK_SIZE);
            cc.setHgrow(javafx.scene.layout.Priority.NEVER);
            gamePanel.getColumnConstraints().add(cc);
        }
        gamePanel.getRowConstraints().clear();
        for (int r = 0; r < VISIBLE_ROWS; r++) {
            javafx.scene.layout.RowConstraints rc = new javafx.scene.layout.RowConstraints();
            rc.setMinHeight(BRICK_SIZE);
            rc.setPrefHeight(BRICK_SIZE);
            rc.setMaxHeight(BRICK_SIZE);
            rc.setVgrow(javafx.scene.layout.Priority.NEVER);
            gamePanel.getRowConstraints().add(rc);
        }
        double prefW = TOTAL_COLS * BRICK_SIZE + Math.max(0, TOTAL_COLS - 1) * gamePanel.getHgap();
        double prefH = VISIBLE_ROWS * BRICK_SIZE + Math.max(0, VISIBLE_ROWS - 1) * gamePanel.getVgap();
        gamePanel.setPrefWidth(prefW);
        gamePanel.setPrefHeight(prefH);

        displayMatrix = new Rectangle[TOTAL_ROWS][TOTAL_COLS];
        for (int i = 2; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < TOTAL_COLS; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.getStyleClass().add("gameCell");
                rectangle.setArcWidth(0);
                rectangle.setArcHeight(0);
                displayMatrix[i][j] = rectangle;
                int gridRow = i - 2;
                gamePanel.add(rectangle, j, gridRow);
            }
        }
        int[][] brickData = brick.getBrickData();
        rectangles = new Rectangle[brickData.length][brickData[0].length];
        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setArcWidth(0);
                rectangle.setArcHeight(0);
                rectangles[i][j] = rectangle;
                setRectangleData(brickData[i][j], rectangle);
                rectangle.setVisible(false);

                int col = brick.getxPosition() + j;
                int row = brick.getyPosition() + i - 2;
                int addRow = Math.max(0, row);
                if (col >= 0 && col < TOTAL_COLS && addRow >= 0 && addRow < VISIBLE_ROWS) {
                    gamePanel.add(rectangle, col, addRow);
                } else {
                    int addCol = Math.max(0, Math.min(col, TOTAL_COLS - 1));
                    int addRowSafe = Math.max(0, Math.min(addRow, Math.max(0, VISIBLE_ROWS - 1)));
                    gamePanel.add(rectangle, addCol, addRowSafe);
                }
            }
        }

        int[][] ghostData = brick.getGhostBrick();
        ghostRectangles = new Rectangle[ghostData.length][ghostData[0].length];
        for (int i = 0; i < ghostData.length; i++) {
            for (int j = 0; j < ghostData[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.WHITE);
                rectangle.setStrokeWidth(1);
                rectangle.setArcWidth(0);
                rectangle.setArcHeight(0);
                rectangle.setVisible(false);
                ghostRectangles[i][j] = rectangle;

                int col = brick.getGhostX() + j;
                int row = brick.getGhostY() + i - 2;
                int addRow = Math.max(0, row);
                if (col >= 0 && col < TOTAL_COLS && addRow >= 0 && addRow < VISIBLE_ROWS) {
                    gamePanel.add(rectangle, col, addRow);
                } else {
                    int addCol = Math.max(0, Math.min(col, TOTAL_COLS - 1));
                    int addRowSafe = Math.max(0, Math.min(addRow, Math.max(0, VISIBLE_ROWS - 1)));
                    gamePanel.add(rectangle, addCol, addRowSafe);
                }
            }
        }
        initializePreviewPanels();
        initializeHoldPanel();

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(600),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
        refreshBrick(brick);
    }

    private Paint getFillColor(int i) {
        return switch (i) {
            case 1 -> Color.web("#00AFFF"); // I
            case 2 -> Color.web("#B044FF"); // J
            case 3 -> Color.web("#FF2DAE"); // L
            case 4 -> Color.web("#FFF65B"); // O
            case 5 -> Color.web("#63FFA7"); // S
            case 6 -> Color.web("#FF4E3D"); // Z
            case 7 -> Color.web("#FF6BFA"); // T
            case 8 -> Color.web("#777777");  // garbage
            default -> Color.TRANSPARENT;
        };
    }

    private void refreshBrick(ViewData brick) {
        final int VISIBLE_ROWS = gamePanel.getRowConstraints().size();
        final int TOTAL_COLS = gamePanel.getColumnConstraints().size();

        int[][] ghostData = brick.getGhostBrick();
        for (int i = 0; i < ghostRectangles.length; i++) {
            for (int j = 0; j < ghostRectangles[i].length; j++) {
                Rectangle r = ghostRectangles[i][j];
                boolean cellHasGhost = ghostData[i][j] != 0;

                int newCol = brick.getGhostX() + j;
                int newRow = brick.getGhostY() + i - 2;
                int clampedRow = Math.max(0, newRow);
                int clampedCol = Math.max(0, Math.min(newCol, TOTAL_COLS - 1));
                GridPane.setColumnIndex(r, clampedCol);
                GridPane.setRowIndex(r, Math.min(clampedRow, Math.max(0, VISIBLE_ROWS - 1)));
                r.setVisible(cellHasGhost && newRow >= 0);
            }
        }

        if (isPause.getValue() == Boolean.FALSE) {
            int[][] bData = brick.getBrickData();
            for (int i = 0; i < bData.length; i++) {
                for (int j = 0; j < bData[i].length; j++) {
                    Rectangle r = rectangles[i][j];

                    int newCol = brick.getxPosition() + j;
                    int newRow = brick.getyPosition() + i - 2;
                    int clampedRow = Math.max(0, newRow);
                    int clampedCol = Math.max(0, Math.min(newCol, TOTAL_COLS - 1));
                    setRectangleData(bData[i][j], r);
                    GridPane.setColumnIndex(r, clampedCol);
                    GridPane.setRowIndex(r, Math.min(clampedRow, Math.max(0, VISIBLE_ROWS - 1)));
                    r.setVisible(bData[i][j] != 0 && newRow >= 0);
                }
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        if (color == 0) {
            rectangle.getStyleClass().setAll("gameCell");
            rectangle.setFill(Color.TRANSPARENT);
        } else {
            if (!rectangle.getStyleClass().contains("gameCell")) {
                rectangle.getStyleClass().add("gameCell");
            }
            rectangle.setFill(getFillColor(color));
        }

        rectangle.setArcHeight(0);
        rectangle.setArcWidth(0);
    }
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

    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        updateHold(new ViewData(new int[0][0], 0, 0, null, new int[0][0], 0, 0, new int[4][4]));
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
        pauseLabel.setVisible(false);
        if (garbageTimer != null) {
            garbageTimer.stop();
        }
        if (flashTimeline != null) {
            flashTimeline.stop();
            flashTimeline = null;
        }
        startGarbageTimer();
    }

    public void pauseGame(ActionEvent actionEvent) {
        gamePanel.requestFocus();
    }

    private void initializePreviewPanels() {
        previewBox.getChildren().removeIf(node -> node instanceof GridPane);
        previewGrids.clear();
        previewRectangles.clear();

        for (int i = 0; i < 5; i++) {
            GridPane mini = new GridPane();
            mini.setHgap(1);
            mini.setVgap(1);
            mini.setAlignment(Pos.CENTER);

            Rectangle[][] rects = new Rectangle[4][4];

            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    Rectangle rect = new Rectangle(PREVIEW_CELL, PREVIEW_CELL);
                    rect.setFill(Color.TRANSPARENT);
                    rect.setVisible(false);
                    mini.add(rect, c, r);
                    rects[r][c] = rect;
                }
            }

            previewBox.getChildren().add(mini);
            previewGrids.add(mini);
            previewRectangles.add(rects);
        }
    }


    public void updatePreview(ViewData view) {
        List<int[][]> nextFive = view.getNextFive();

        for (int idx = 0; idx < previewRectangles.size(); idx++) {
            Rectangle[][] rects = previewRectangles.get(idx);
            int[][] mat = nextFive.get(idx);
            int[] b = getBounds(mat);

            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    Rectangle rect = rects[r][c];

                    if (r < b[0] || r > b[1] || c < b[2] || c > b[3] || mat[r][c] == 0) {
                        rect.setVisible(false);
                    } else {
                        rect.setFill(getFillColor(mat[r][c]));
                        rect.setVisible(true);
                    }
                }
            }
        }
    }

    private void initializeHoldPanel() {
        holdGrid = new GridPane();
        holdGrid.setHgap(1);
        holdGrid.setVgap(1);
        holdGrid.setAlignment(Pos.CENTER);

        holdRects = new Rectangle[4][4];

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                Rectangle rect = new Rectangle(HOLD_CELL, HOLD_CELL);
                rect.setFill(Color.TRANSPARENT);
                rect.setVisible(false);
                holdGrid.add(rect, c, r);
                holdRects[r][c] = rect;
            }
        }

        leftDummy.getChildren().add(holdGrid);

    }

    public void updateHold(ViewData view) {
        int[][] mat = view.getHeldBrick();
        int[] b = getBounds(mat);

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                Rectangle rect = holdRects[r][c];

                if (r < b[0] || r > b[1] || c < b[2] || c > b[3] || mat[r][c] == 0) {
                    rect.setVisible(false);
                } else {
                    rect.setFill(getFillColor(mat[r][c]));
                    rect.setVisible(true);
                }
            }
        }
    }


    private int[] getBounds(int[][] mat) {
        int minR = 4, maxR = -1;
        int minC = 4, maxC = -1;

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (mat[r][c] != 0) {
                    if (r < minR) minR = r;
                    if (r > maxR) maxR = r;
                    if (c < minC) minC = c;
                    if (c > maxC) maxC = c;
                }
            }
        }
        return new int[]{minR, maxR, minC, maxC};
    }

    private void togglePause() {
        if (isGameOver.get()) return;
        boolean nowPaused = !isPause.get();
        isPause.set(nowPaused);

        if (nowPaused) {
            if (timeLine != null) timeLine.pause();
            if (garbageTimer != null) garbageTimer.pause();
            if (flashTimeline != null) flashTimeline.pause();
            showPauseOverlay();
        } else {
            hidePauseOverlay();
            if (garbageTimer != null) garbageTimer.play();
            if (timeLine != null) timeLine.play();
            if (flashTimeline != null) flashTimeline.play();
        }
        gamePanel.requestFocus();
    }

    private void startGarbageWarning() {
        if (isPause.get()) return;
        flashIncomingGarbageRow(() -> {
            ((SimpleBoard) controller.getBoard()).addGarbageRow();
            Platform.runLater(() -> {
                refreshGameBackground(controller.getBoard().getBoardMatrix());
                forceRedrawBottomRow();
            });
            startGarbageTimer();
        });
    }

    private void flashIncomingGarbageRow(Runnable onFinished) {
        if (flashTimeline != null) {
            flashTimeline.stop();
        }
        int TOTAL_ROWS = displayMatrix.length;
        int VISIBLE_ROWS = gamePanel.getRowConstraints().size();
        int bottomVisibleRow = VISIBLE_ROWS - 1 + 2;
        Rectangle[] rowRects = displayMatrix[bottomVisibleRow];
        flashTimeline = new Timeline();
        flashTimeline.setCycleCount(8);
        flashTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(300), e -> {
            if (isPause.get()) return;
            sound.playWarning();
            for (Rectangle r : rowRects) {
                r.setFill(Color.web("#999999"));
                r.setOpacity(1.0);
            }
        }));

        flashTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(600), e -> {
            if (isPause.get()) return;
            int[][] board = controller.getBoard().getBoardMatrix();
            for (int col = 0; col < rowRects.length; col++) {
                int cellValue = board[bottomVisibleRow][col];
                setRectangleData(cellValue, rowRects[col]);
            }
        }));

        flashTimeline.setOnFinished(ev -> {
            if (!isPause.get()) {
                onFinished.run();
            }
            flashTimeline = null;
        });

        flashTimeline.play();
    }

    private void forceRedrawBottomRow() {
        int[][] board = controller.getBoard().getBoardMatrix();
        int TOTAL_ROWS = board.length;
        int VISIBLE_ROWS = gamePanel.getRowConstraints().size();
        int bottomVisibleRow = VISIBLE_ROWS - 1 + 2;

        Rectangle[] rowRects = displayMatrix[bottomVisibleRow];

        for (int col = 0; col < rowRects.length; col++) {
            int value = board[bottomVisibleRow][col];
            setRectangleData(value, rowRects[col]);
            rowRects[col].setOpacity(1.0);
            rowRects[col].setVisible(true);
        }
    }

    private void startGarbageTimer() {
        if (garbageTimer != null) {
            garbageTimer.stop();
        }
        garbageTimer = new Timeline(new KeyFrame(
                Duration.seconds(30),
                e -> startGarbageWarning()
        ));
        garbageTimer.setCycleCount(Timeline.INDEFINITE);
        garbageTimer.play();
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
}

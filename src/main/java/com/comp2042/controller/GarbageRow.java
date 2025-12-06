package com.comp2042.controller;

import com.comp2042.ui.ColorUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GarbageRow {

    private final GuiController gui;

    public interface GarbageCallback {
        void onApplyGarbage();
    }
    private GarbageCallback callback;

    Timeline garbageTimer;
    Timeline flashTimeline;

    public GarbageRow(GuiController gui) {
        this.gui = gui;
    }

    public void setCallback(GarbageCallback callback) {
        this.callback = callback;
    }

    public void startGarbageTimer() {
        if (garbageTimer != null) garbageTimer.stop();

        garbageTimer = new Timeline(new KeyFrame(
                Duration.seconds(30),
                e -> startGarbageWarning()
        ));
        garbageTimer.setCycleCount(Timeline.INDEFINITE);
        garbageTimer.play();
    }

    public void pause() {
        if (garbageTimer != null) garbageTimer.pause();
        if (flashTimeline != null) flashTimeline.pause();
    }

    public void resume() {
        if (garbageTimer != null) garbageTimer.play();
        if (flashTimeline != null) flashTimeline.play();
    }

    private void startGarbageWarning() {
        if (gui.isPause()) return;

        flashIncomingGarbageRow(() -> {
            if (callback != null) callback.onApplyGarbage();
            Platform.runLater(() -> {
                gui.refreshGameBackground(gui.getController().getBoard().getBoardMatrix());
                forceRedrawBottomRow();
            });
            startGarbageTimer();
        });
    }

    private void flashIncomingGarbageRow(Runnable onFinished) {
        if (flashTimeline != null) flashTimeline.stop();

        int[][] board = gui.getController().getBoard().getBoardMatrix();
        Rectangle[][] displayMatrix = gui.getDisplayMatrix();
        GridPane gamePanel = gui.getGamePanel();

        int TOTAL_ROWS = board.length;
        int visibleRows = gamePanel.getRowConstraints().size();
        int bottomVisibleRow = visibleRows - 1 + 2;

        Rectangle[] rowRects = displayMatrix[bottomVisibleRow];

        flashTimeline = new Timeline();
        flashTimeline.setCycleCount(8);

        flashTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(300), e -> {
            if (gui.isPause()) return;
            gui.getSound().playWarning();
            for (Rectangle r : rowRects) {
                r.setFill(Color.web("#999999"));
                r.setOpacity(1.0);
            }
        }));

        flashTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(600), e -> {
            if (gui.isPause()) return;
            int[][] newBoard = gui.getController().getBoard().getBoardMatrix();
            for (int col = 0; col < rowRects.length; col++) {
                int cellValue = newBoard[bottomVisibleRow][col];
                rowRects[col].setFill(ColorUtil.getFillColor(cellValue));
            }
        }));

        flashTimeline.setOnFinished(ev -> {
            if (!gui.isPause()) onFinished.run();
            flashTimeline = null;
        });

        flashTimeline.play();
    }

    private void forceRedrawBottomRow() {
        int[][] board = gui.getController().getBoard().getBoardMatrix();
        Rectangle[][] displayMatrix = gui.getDisplayMatrix();
        GridPane gamePanel = gui.getGamePanel();
        int visibleRows = gamePanel.getRowConstraints().size();
        int bottomVisibleRow = visibleRows - 1 + 2;
        Rectangle[] rowRects = displayMatrix[bottomVisibleRow];
        for (int col = 0; col < rowRects.length; col++) {
            int value = board[bottomVisibleRow][col];
            rowRects[col].setFill(ColorUtil.getFillColor(value));
            rowRects[col].setOpacity(1.0);
        }
    }

    public void reset() {
        if (garbageTimer != null) {
            garbageTimer.stop();
            garbageTimer = null;
        }
        if (flashTimeline != null) {
            flashTimeline.stop();
            flashTimeline = null;
        }
    }
}

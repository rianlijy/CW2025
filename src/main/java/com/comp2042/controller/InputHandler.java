package com.comp2042.controller;

import com.comp2042.game.*;
import com.comp2042.ui.NotificationPanel;
import com.comp2042.ui.Sound;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.application.Platform;

public class InputHandler {

    private final GuiController gui;

    public InputHandler(GuiController gui) {
        this.gui = gui;
    }

    public void attach(GridPane gamePanel) {

        gamePanel.setOnKeyPressed(this::handleKeyPressed);

        gamePanel.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                gui.spacePressed = false;
            }
        });
    }

    private void handleKeyPressed(KeyEvent keyEvent) {

        if (!gui.isPause() && !gui.isGameOver()) {

            if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                gui.callRefreshBrick(gui.eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                keyEvent.consume();
            }

            if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                gui.callRefreshBrick(gui.eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                keyEvent.consume();
            }

            if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                gui.callRefreshBrick(gui.eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                keyEvent.consume();
            }

            if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                gui.callMoveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                keyEvent.consume();
            }

            if (keyEvent.getCode() == KeyCode.SPACE) {
                if (gui.spacePressed) return;
                gui.spacePressed = true;

                DownData data = gui.getController().hardDrop();
                gui.getSound().playPlace();

                gui.callRefreshBrick(data.getViewData());

                if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0) {
                    int baseBonus = data.getClearRow().getScoreBonus();
                    int level = gui.getController().getBoard().getScore().getLevel();
                    int displayBonus = baseBonus * level;
                    NotificationPanel p = new NotificationPanel("+" + displayBonus);
                    gui.groupNotification.getChildren().add(p);
                    p.showScore(gui.groupNotification.getChildren());
                }
                keyEvent.consume();
            }

            if (keyEvent.getCode() == KeyCode.SHIFT || keyEvent.getCode() == KeyCode.C) {
                ViewData data = gui.getController().hold();
                gui.callRefreshBrick(data);
                gui.updateHold(data);
                keyEvent.consume();
            }
        }

        if (keyEvent.getCode() == KeyCode.P || keyEvent.getCode() == KeyCode.ESCAPE) {
            gui.callTogglePause();
            keyEvent.consume();
        }

        if (keyEvent.getCode() == KeyCode.M) {
            Sound sound = gui.getSound();
            sound.toggleMute();

            if (sound.isMuted()) {
                gui.volumeSlider.setValue(0);
            } else {
                gui.volumeSlider.setValue(sound.getLastVolume());
            }
            keyEvent.consume();
        }

        if (keyEvent.getCode() == KeyCode.N) {
            gui.newGame(null);
        }
    }
}


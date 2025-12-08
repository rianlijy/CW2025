package com.comp2042.controller;

import com.comp2042.game.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Main game controller that coordinates between the game logic (Board) and the UI (GuiController).
 * Handles input events, manages game state, and notifies listeners of game changes.
 */
public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;

    private GarbageRow garbageRow;

    /**
     * Initializes game board, sets up listeners, configures garbage row system,
     * binds UI properties, and sets up level-based speed adjustment.
     */
    public GameController(GuiController c) {
        viewGuiController = c;
        addGameListener(viewGuiController);
        board.createNewBrick();
        this.garbageRow = new GarbageRow(viewGuiController);
        garbageRow.setCallback(() -> {
            ((SimpleBoard) board).addGarbageRow();
            notifyBoardChanged();
        });
        this.garbageRow.startGarbageTimer();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        viewGuiController.bindLevel(board.getScore().levelProperty());
        board.getScore().levelProperty().addListener((obs, oldV, newV) -> {
            int level = newV.intValue();
            int speed = (int) Math.max(200, 600 - (level - 1) * 100);
            viewGuiController.blockFallSpeed(speed);
        });
        viewGuiController.updatePreview(board.getViewData());
    }

    /**
     * Handles downward movement. If piece locks, merges to board, clears rows,
     * calculates score (base × level), spawns new piece, and notifies listeners.
     * Awards 1 point for user soft drops.
     */
    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                int level = board.getScore().getLevel();
                int multiplied = clearRow.getScoreBonus() * level;
                board.getScore().add(multiplied);
                board.getScore().addLines(clearRow.getLinesRemoved());
            }
            boolean gameOver = board.createNewBrick();
            if (gameOver) {
                notifyGameOver();
            }
            notifyBoardChanged();
            notifyPreviewChanged();
            return new DownData(clearRow, board.getViewData(), true);
        }
        else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
            return new DownData(null, board.getViewData(), false);
        }
    }

    /**
     * Instantly drops piece to bottom, awards 1 point per row dropped,
     * then merges, clears rows, and spawns new piece.
     */
    public DownData hardDrop() {
        int rowsDropped = 0;
        while (board.moveBrickDown()) {
            rowsDropped++;
        }
        if (rowsDropped > 0) {
            board.getScore().add(rowsDropped);
        }
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();

        if (clearRow.getLinesRemoved() > 0) {
            int level = board.getScore().getLevel();
            int multiplied = clearRow.getScoreBonus() * level;
            board.getScore().add(multiplied);
            board.getScore().addLines(clearRow.getLinesRemoved());
        }

        if (board.createNewBrick()) {
            notifyGameOver();
        }
        notifyBoardChanged();
        notifyPreviewChanged();
        return new DownData(clearRow, board.getViewData(), false);
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }


    @Override
    public void createNewGame() {
        board.newGame();
        notifyBoardChanged();
        notifyPreviewChanged();
        garbageRow.reset();
        garbageRow.startGarbageTimer();
    }

    public ViewData hold() {
        ViewData data = ((SimpleBoard) board).holdPiece();
        notifyHoldChanged(data);
        return data;
    }

    public Board getBoard() {
        return board;
    }

    public void onPauseStateChanged(boolean paused) {
        if (garbageRow == null) return;

        if (paused) {
            garbageRow.pause();
        } else {
            garbageRow.resume();
        }
    }

    private final List<GameListener> listeners = new ArrayList<>();

    public void addGameListener(GameListener listener) {
        listeners.add(listener);
    }

    private void notifyGameOver() {
        for (GameListener listener : listeners) {
            listener.onGameOver();
        }
    }

    private void notifyBoardChanged() {
        for (GameListener listener : listeners) {
            listener.onBoardChanged(board.getBoardMatrix());
        }
    }

    private void notifyPreviewChanged() {
        for (GameListener listener : listeners) {
            listener.onPreviewChanged(board.getViewData());
        }
    }

    private void notifyHoldChanged(ViewData data) {
        for (GameListener listener : listeners) {
            listener.onHoldChanged(data);
        }
    }
}

package com.comp2042.controller;

import com.comp2042.game.*;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;

    private GarbageRow garbageRow;

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        this.garbageRow = new GarbageRow(viewGuiController);
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
                viewGuiController.onGameOver();
            }
            viewGuiController.onGameBackgroundChanged(board.getBoardMatrix());
            viewGuiController.onPreviewChanged(board.getViewData());
            return new DownData(clearRow, board.getViewData(), true);
        }
        else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
            return new DownData(null, board.getViewData(), false);
        }
    }

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
            viewGuiController.onGameOver();
        }
        viewGuiController.onGameBackgroundChanged(board.getBoardMatrix());
        viewGuiController.onPreviewChanged(board.getViewData());
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
        viewGuiController.onGameBackgroundChanged(board.getBoardMatrix());
        viewGuiController.onPreviewChanged(board.getViewData());
        garbageRow.reset();
        garbageRow.startGarbageTimer();
    }

    public ViewData hold() {
        ViewData data = ((SimpleBoard) board).holdPiece();
        viewGuiController.onHoldChanged(data);
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
}

package com.comp2042.controller;

import com.comp2042.game.*;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        viewGuiController.bindLevel(board.getScore().levelProperty());
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
                viewGuiController.gameOver();
            }
            viewGuiController.refreshGameBackground(board.getBoardMatrix());
            viewGuiController.updatePreview(board.getViewData());
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
            viewGuiController.gameOver();
        }
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.updatePreview(board.getViewData());
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
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.updatePreview(board.getViewData());
    }

    public ViewData hold() {
        ViewData data = ((SimpleBoard) board).holdPiece();
        viewGuiController.updateHold(data);
        return data;
    }

    public Board getBoard() {
        return board;
    }
}

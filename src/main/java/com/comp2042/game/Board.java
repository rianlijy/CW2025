package com.comp2042.game;

/**
 * Interface representing the game board for Tetris.
 * Defines operations for moving, rotating, and managing pieces on the board.
 */
public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    void newGame();

    void addGarbageRow();
}

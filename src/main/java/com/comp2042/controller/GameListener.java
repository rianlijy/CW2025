package com.comp2042.controller;

import com.comp2042.game.ViewData;

/**
 * Interface for objects that listen to game state changes.
 * Allows the UI to be notified when the board, preview, hold, or game over state changes.
 */
public interface GameListener {
    void onGameOver();
    void onBoardChanged(int[][] boardMatrix);
    void onPreviewChanged(ViewData data);
    void onHoldChanged(ViewData data);
}

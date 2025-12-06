package com.comp2042.controller;

import com.comp2042.game.ViewData;

public interface GameListener {
    void onGameOver();
    void onBoardChanged(int[][] boardMatrix);
    void onPreviewChanged(ViewData data);
    void onHoldChanged(ViewData data);
}

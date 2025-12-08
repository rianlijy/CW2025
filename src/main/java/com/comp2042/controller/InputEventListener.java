package com.comp2042.controller;

import com.comp2042.game.DownData;
import com.comp2042.game.MoveEvent;
import com.comp2042.game.ViewData;

/**
 * Interface for handling input events from the user or game thread.
 * Defines methods for processing movement and rotation events, as well as creating new games.
 */
public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    void createNewGame();
}

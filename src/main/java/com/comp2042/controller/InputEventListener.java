package com.comp2042.controller;

import com.comp2042.game.DownData;
import com.comp2042.game.MoveEvent;
import com.comp2042.game.ViewData;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    void createNewGame();
}

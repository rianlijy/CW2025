package com.comp2042.game;

/**
 * Downward move operation.
 * Store whether the piece was locked, rows cleared, and updated view data.
 */
public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;
    private final boolean locked;

    public DownData(ClearRow clearRow, ViewData viewData, boolean locked) {
        this.clearRow = clearRow;
        this.viewData = viewData;
        this.locked = locked;
    }

    public ClearRow getClearRow() {
        return clearRow;
    }

    public ViewData getViewData() {
        return viewData;
    }

    public boolean isLocked() {
        return locked;
    }
}

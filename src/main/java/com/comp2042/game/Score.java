package com.comp2042.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    public void reset() {
        score.setValue(0);
        linesClearedTotal = 0;
        level.set(1);
    }

    private final IntegerProperty level = new SimpleIntegerProperty(1);
    private int linesClearedTotal = 0;

    public IntegerProperty levelProperty() {
        return level;
    }

    public int getLevel() {
        return level.get();
    }

    public void addLines(int lines) {
        if (lines <= 0) return;
        linesClearedTotal += lines;
        int newLevel = Math.min(5, (linesClearedTotal / 10) + 1);
        if (newLevel != level.get()) {
            level.set(newLevel);
        }
    }
}

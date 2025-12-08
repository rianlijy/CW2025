package com.comp2042.logic.bricks;

/**
 * Interface for generating Tetris bricks.
 * Provides methods to get the current brick and preview the next brick.
 */
public interface BrickGenerator {

    Brick getBrick();

    Brick getNextBrick();
}

package com.comp2042.logic.bricks;

import java.util.List;

/**
 * Interface representing a Tetris brick (tetromino).
 * Each brick has multiple rotation states represented as matrices.
 */
public interface Brick {

    List<int[][]> getShapeMatrix();
}

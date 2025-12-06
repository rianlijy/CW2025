package com.comp2042.logic.bricks;

import com.comp2042.game.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the O-shaped Tetris piece (also known as the "square" piece).
 */
final class OBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public OBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 4, 4, 0},
                {0, 4, 4, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

}

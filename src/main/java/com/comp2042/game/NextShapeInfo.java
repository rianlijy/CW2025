package com.comp2042.game;

/**
 * Contains information about the next rotation state of a brick.
 * Stores both the shape matrix and the position index in the rotation sequence.
 */
public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    public int getPosition() {
        return position;
    }
}

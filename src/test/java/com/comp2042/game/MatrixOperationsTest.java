package com.comp2042.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MatrixOperations utility methods.
 */
class MatrixOperationsTest {

    private int[][] emptyBoard;
    private int[][] filledBoard;
    private int[][] singleBrick;

    @BeforeEach
    void setUp() {
        emptyBoard = new int[10][10];
        filledBoard = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                filledBoard[i][j] = 1;
            }
        }
        singleBrick = new int[][]{
                {1, 1},
                {1, 1}
        };
    }

    @Test
    void testIntersect_NoCollision() {
        assertFalse(MatrixOperations.intersect(emptyBoard, singleBrick, 0, 0));
        assertFalse(MatrixOperations.intersect(emptyBoard, singleBrick, 5, 5));
    }

    @Test
    void testIntersect_CollisionWithFilledCell() {
        int[][] board = new int[10][10];
        board[5][5] = 1;
        assertTrue(MatrixOperations.intersect(board, singleBrick, 4, 4));
    }

    @Test
    void testIntersect_OutOfBoundsLeft() {
        assertTrue(MatrixOperations.intersect(emptyBoard, singleBrick, -1, 0));
    }

    @Test
    void testIntersect_OutOfBoundsRight() {
        assertTrue(MatrixOperations.intersect(emptyBoard, singleBrick, 9, 0));
    }

    @Test
    void testIntersect_OutOfBoundsBottom() {
        assertTrue(MatrixOperations.intersect(emptyBoard, singleBrick, 0, 9));
    }

    @Test
    void testCopy_DeepCopy() {
        int[][] original = new int[][]{{1, 2}, {3, 4}};
        int[][] copied = MatrixOperations.copy(original);

        assertNotSame(original, copied);
        assertArrayEquals(original, copied);

        copied[0][0] = 99;
        assertEquals(1, original[0][0]);
    }

    @Test
    void testMerge_ValidPosition() {
        int[][] board = new int[10][10];
        int[][] brick = new int[][]{{1, 1}, {1, 1}};

        int[][] result = MatrixOperations.merge(board, brick, 2, 2);

        assertEquals(1, result[2][2]);
        assertEquals(1, result[2][3]);
        assertEquals(1, result[3][2]);
        assertEquals(1, result[3][3]);
        assertEquals(0, result[0][0]);
    }

    @Test
    void testMerge_OriginalUnchanged() {
        int[][] board = new int[10][10];
        int[][] brick = new int[][]{
                {1, 1},
                {1, 1}
        };

        int[][] result = MatrixOperations.merge(board, brick, 0, 0);

        assertEquals(0, board[0][0]);
        assertEquals(0, board[1][1]);

        assertEquals(1, result[0][0]);
        assertEquals(1, result[0][1]);
        assertEquals(1, result[1][0]);
        assertEquals(1, result[1][1]);
    }

    @Test
    void testCheckRemoving_NoRowsToClear() {
        int[][] board = new int[5][10];
        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(0, result.getLinesRemoved());
        assertEquals(0, result.getScoreBonus());
        assertNotNull(result.getNewMatrix());
    }

    @Test
    void testCheckRemoving_SingleRow() {
        int[][] board = new int[5][10];
        for (int j = 0; j < 10; j++) {
            board[2][j] = 1;
        }

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(1, result.getLinesRemoved());
        assertEquals(100, result.getScoreBonus());
    }

    @Test
    void testCheckRemoving_TwoRows() {
        int[][] board = new int[5][10];
        for (int i = 1; i < 3; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = 1;
            }
        }

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(2, result.getLinesRemoved());
        assertEquals(300, result.getScoreBonus());
    }

    @Test
    void testCheckRemoving_FourRows() {
        int[][] board = new int[5][10];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = 1;
            }
        }

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(4, result.getLinesRemoved());
        assertEquals(800, result.getScoreBonus());
    }

    @Test
    void testCheckRemoving_GravityEffect() {
        int[][] board = new int[5][10];
        board[0][0] = 1;
        board[1][0] = 1;
        for (int j = 0; j < 10; j++) {
            board[2][j] = 1;
        }

        ClearRow result = MatrixOperations.checkRemoving(board);
        int[][] newMatrix = result.getNewMatrix();

        assertEquals(1, newMatrix[1][0]);
        assertEquals(1, newMatrix[2][0]);
        assertEquals(0, newMatrix[0][0]);
    }

    @Test
    void testCheckRemoving_PartialRow() {
        int[][] board = new int[5][10];
        for (int j = 0; j < 9; j++) {
            board[2][j] = 1;
        }

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(0, result.getLinesRemoved());
    }
}




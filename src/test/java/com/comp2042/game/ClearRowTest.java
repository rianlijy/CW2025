package com.comp2042.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClearRow.
 */
class ClearRowTest {

    @Test
    void testClearRow_Constructor() {
        int[][] matrix = new int[5][10];
        ClearRow clearRow = new ClearRow(2, matrix, 300);

        assertEquals(2, clearRow.getLinesRemoved());
        assertEquals(300, clearRow.getScoreBonus());
        assertNotNull(clearRow.getNewMatrix());
    }

    @Test
    void testClearRow_GetNewMatrix_Immutability() {
        int[][] original = new int[5][10];
        ClearRow clearRow = new ClearRow(0, original, 0);

        int[][] retrieved1 = clearRow.getNewMatrix();
        int[][] retrieved2 = clearRow.getNewMatrix();

        assertNotSame(retrieved1, retrieved2);
    }

    @Test
    void testClearRow_ScoreBonuses() {
        int[][] matrix = new int[5][10];

        ClearRow single = new ClearRow(1, matrix, 100);
        assertEquals(100, single.getScoreBonus());

        ClearRow double_ = new ClearRow(2, matrix, 300);
        assertEquals(300, double_.getScoreBonus());

        ClearRow triple = new ClearRow(3, matrix, 500);
        assertEquals(500, triple.getScoreBonus());

        ClearRow tetris = new ClearRow(4, matrix, 800);
        assertEquals(800, tetris.getScoreBonus());
    }
}




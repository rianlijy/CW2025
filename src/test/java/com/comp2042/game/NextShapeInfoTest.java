package com.comp2042.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for NextShapeInfo.
 */
class NextShapeInfoTest {

    @Test
    void testNextShapeInfo_Constructor() {
        int[][] shape = new int[][]{{1, 1}, {1, 1}};
        NextShapeInfo info = new NextShapeInfo(shape, 2);

        assertEquals(2, info.getPosition());
        assertNotNull(info.getShape());
    }

    @Test
    void testNextShapeInfo_GetShape_Immutability() {
        int[][] original = new int[][]{{1, 1}, {1, 1}};
        NextShapeInfo info = new NextShapeInfo(original, 0);

        int[][] retrieved1 = info.getShape();
        int[][] retrieved2 = info.getShape();

        assertNotSame(retrieved1, retrieved2);
        assertNotSame(original, retrieved1);
    }

    @Test
    void testNextShapeInfo_ShapeModification() {
        int[][] original = new int[][]{{1, 1}, {1, 1}};
        NextShapeInfo info = new NextShapeInfo(original, 0);

        int[][] retrieved = info.getShape();
        retrieved[0][0] = 99;

        assertEquals(1, info.getShape()[0][0]);
    }
}




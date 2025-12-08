package com.comp2042.game;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.SevenBagGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for BrickRotator.
 */
class BrickRotatorTest {

    private BrickRotator rotator;
    private SevenBagGenerator generator;

    @BeforeEach
    void setUp() {
        rotator = new BrickRotator();
        generator = new SevenBagGenerator();
    }

    @Test
    void testSetBrick() {
        Brick brick = generator.getBrick();
        rotator.setBrick(brick);
        assertNotNull(rotator.getCurrentShape());
    }

    @Test
    void testGetCurrentShape() {
        Brick brick = generator.getBrick();
        rotator.setBrick(brick);
        int[][] shape = rotator.getCurrentShape();

        assertNotNull(shape);
        assertEquals(4, shape.length);
    }

    @Test
    void testGetNextShape() {
        Brick brick = generator.getBrick();
        rotator.setBrick(brick);
        NextShapeInfo next = rotator.getNextShape();

        assertNotNull(next);
        assertNotNull(next.getShape());
        assertTrue(next.getPosition() >= 0);
    }

    @Test
    void testSetCurrentShape() {
        Brick brick = generator.getBrick();
        rotator.setBrick(brick);
        int numRotations = brick.getShapeMatrix().size();

        if (numRotations > 1) {
            rotator.setCurrentShape(1);
            NextShapeInfo next = rotator.getNextShape();
            assertTrue(next.getPosition() >= 0);
        }
    }

    @Test
    void testRotationCycle() {
        Brick brick = generator.getBrick();
        rotator.setBrick(brick);

        int numRotations = brick.getShapeMatrix().size();
        if (numRotations > 1) {
            NextShapeInfo next = rotator.getNextShape();
            rotator.setCurrentShape(next.getPosition());
            int[][] rotated = rotator.getCurrentShape();

            assertNotNull(rotated);
        }
    }

    @Test
    void testGetBrick() {
        Brick brick = generator.getBrick();
        rotator.setBrick(brick);

        assertSame(brick, rotator.getBrick());
    }

    @Test
    void testMultipleRotations() {
        Brick brick = generator.getBrick();
        rotator.setBrick(brick);
        int numRotations = brick.getShapeMatrix().size();

        for (int i = 0; i < numRotations; i++) {
            int[][] shape = rotator.getCurrentShape();
            assertNotNull(shape);

            if (i < numRotations - 1) {
                NextShapeInfo next = rotator.getNextShape();
                rotator.setCurrentShape(next.getPosition());
            }
        }
    }
}


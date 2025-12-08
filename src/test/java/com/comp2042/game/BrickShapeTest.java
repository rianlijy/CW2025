package com.comp2042.game;

import com.comp2042.logic.bricks.SevenBagGenerator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.comp2042.logic.bricks.Brick;

/**
 * Test class for brick shape implementations.
 * Tests bricks through the generator to access package-private classes.
 */
class BrickShapeTest {

    @Test
    void testBrickShapes_ThroughGenerator() {
        SevenBagGenerator generator = new SevenBagGenerator();
        java.util.Set<Integer> rotationCounts = new java.util.HashSet<>();

        for (int i = 0; i < 7; i++) {
            Brick brick = generator.getBrick();
            var shapes = brick.getShapeMatrix();
            assertNotNull(shapes);
            assertTrue(shapes.size() > 0);
            rotationCounts.add(shapes.size());
        }

        assertTrue(rotationCounts.size() > 0);
    }

    @Test
    void testBrickShapes_DeepCopy() {
        SevenBagGenerator generator = new SevenBagGenerator();
        Brick brick = generator.getBrick();

        var shapes1 = brick.getShapeMatrix();
        var shapes2 = brick.getShapeMatrix();

        assertNotSame(shapes1, shapes2);
        if (!shapes1.isEmpty() && !shapes2.isEmpty()) {
            assertNotSame(shapes1.get(0), shapes2.get(0));
        }
    }

    @Test
    void testBrickShapes_ValidMatrices() {
        SevenBagGenerator generator = new SevenBagGenerator();

        for (int i = 0; i < 7; i++) {
            Brick brick = generator.getBrick();
            var shapes = brick.getShapeMatrix();

            for (int[][] shape : shapes) {
                assertNotNull(shape);
                assertEquals(4, shape.length);
                for (int[] row : shape) {
                    assertNotNull(row);
                    assertEquals(4, row.length);
                }
            }
        }
    }

    @Test
    void testBrickShapes_NonEmptyShapes() {
        SevenBagGenerator generator = new SevenBagGenerator();

        for (int i = 0; i < 7; i++) {
            Brick brick = generator.getBrick();
            var shapes = brick.getShapeMatrix();

            for (int[][] shape : shapes) {
                boolean hasNonZero = false;
                for (int[] row : shape) {
                    for (int cell : row) {
                        if (cell != 0) {
                            hasNonZero = true;
                            break;
                        }
                    }
                }
                assertTrue(hasNonZero, "Each shape should have at least one non-zero cell");
            }
        }
    }

    @Test
    void testBrickShapes_ConsistentTypes() {
        SevenBagGenerator generator = new SevenBagGenerator();
        java.util.Map<Integer, Integer> typeCounts = new java.util.HashMap<>();

        for (int i = 0; i < 7; i++) {
            Brick brick = generator.getBrick();
            int[][] firstShape = brick.getShapeMatrix().get(0);

            int type = findBrickType(firstShape);
            typeCounts.put(type, typeCounts.getOrDefault(type, 0) + 1);
        }

        assertEquals(7, typeCounts.size(), "All 7 brick types should be present");
    }

    private int findBrickType(int[][] shape) {
        for (int[] row : shape) {
            for (int cell : row) {
                if (cell != 0) {
                    return cell;
                }
            }
        }
        return 0;
    }
}


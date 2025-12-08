package com.comp2042.game;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ViewData.
 */
class ViewDataTest {

    @Test
    void testViewData_Immutability() {
        int[][] brickData = new int[][]{{1, 1}, {1, 1}};
        List<int[][]> nextFive = new ArrayList<>();
        nextFive.add(new int[][]{{2, 2}});
        int[][] ghostBrick = new int[][]{{1, 1}};
        int[][] heldBrick = new int[][]{{3, 3}};

        ViewData viewData = new ViewData(brickData, 5, 10, nextFive,
                ghostBrick, 5, 12, heldBrick);

        int[][] retrieved = viewData.getBrickData();
        retrieved[0][0] = 99;

        assertEquals(1, viewData.getBrickData()[0][0]);
    }

    @Test
    void testViewData_Positions() {
        ViewData viewData = new ViewData(
                new int[][]{{1}}, 5, 10, null,
                new int[][]{{1}}, 5, 12, new int[][]{{1}}
        );

        assertEquals(5, viewData.getxPosition());
        assertEquals(10, viewData.getyPosition());
        assertEquals(5, viewData.getGhostX());
        assertEquals(12, viewData.getGhostY());
    }

    @Test
    void testViewData_NextFive() {
        List<int[][]> nextFive = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            nextFive.add(new int[][]{{i}});
        }

        ViewData viewData = new ViewData(
                new int[][]{{1}}, 0, 0, nextFive,
                new int[][]{{1}}, 0, 0, new int[][]{{1}}
        );

        List<int[][]> retrieved = viewData.getNextFive();
        assertEquals(5, retrieved.size());

        retrieved.get(0)[0][0] = 99;
        assertEquals(0, viewData.getNextFive().get(0)[0][0]);
    }

    @Test
    void testViewData_HeldBrick() {
        int[][] heldBrick = new int[][]{{5, 5}, {5, 5}};
        ViewData viewData = new ViewData(
                new int[][]{{1}}, 0, 0, null,
                new int[][]{{1}}, 0, 0, heldBrick
        );

        int[][] retrieved = viewData.getHeldBrick();
        assertNotNull(retrieved);
        assertEquals(5, retrieved[0][0]);

        retrieved[0][0] = 99;
        assertEquals(5, viewData.getHeldBrick()[0][0]);
    }

    @Test
    void testViewData_NullNextFive() {
        ViewData viewData = new ViewData(
                new int[][]{{1}}, 0, 0, null,
                new int[][]{{1}}, 0, 0, new int[][]{{1}}
        );

        assertNotNull(viewData.getNextFive());
        assertEquals(0, viewData.getNextFive().size());
    }
}


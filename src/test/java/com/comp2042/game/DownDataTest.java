package com.comp2042.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DownData.
 */
class DownDataTest {

    @Test
    void testDownData_Constructor() {
        ClearRow clearRow = new ClearRow(2, new int[5][10], 300);
        ViewData viewData = new ViewData(
                new int[][]{{1}}, 0, 0, null,
                new int[][]{{1}}, 0, 0, new int[][]{{1}}
        );

        DownData downData = new DownData(clearRow, viewData, true);

        assertSame(clearRow, downData.getClearRow());
        assertSame(viewData, downData.getViewData());
        assertTrue(downData.isLocked());
    }

    @Test
    void testDownData_NotLocked() {
        ViewData viewData = new ViewData(
                new int[][]{{1}}, 0, 0, null,
                new int[][]{{1}}, 0, 0, new int[][]{{1}}
        );

        DownData downData = new DownData(null, viewData, false);

        assertNull(downData.getClearRow());
        assertFalse(downData.isLocked());
    }

    @Test
    void testDownData_NullClearRow() {
        ViewData viewData = new ViewData(
                new int[][]{{1}}, 0, 0, null,
                new int[][]{{1}}, 0, 0, new int[][]{{1}}
        );

        DownData downData = new DownData(null, viewData, true);

        assertNull(downData.getClearRow());
        assertTrue(downData.isLocked());
    }
}




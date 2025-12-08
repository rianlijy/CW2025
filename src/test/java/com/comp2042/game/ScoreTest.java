package com.comp2042.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Score management.
 */
class ScoreTest {

    private Score score;

    @BeforeEach
    void setUp() {
        score = new Score();
    }

    @Test
    void testInitialScore() {
        assertEquals(0, score.scoreProperty().get());
        assertEquals(1, score.getLevel());
    }

    @Test
    void testAddScore() {
        score.add(100);
        assertEquals(100, score.scoreProperty().get());

        score.add(50);
        assertEquals(150, score.scoreProperty().get());
    }

    @Test
    void testReset() {
        score.add(500);
        score.addLines(15);
        score.reset();

        assertEquals(0, score.scoreProperty().get());
        assertEquals(1, score.getLevel());
    }

    @Test
    void testLevelProgression() {
        score.addLines(10);
        assertEquals(2, score.getLevel());

        score.addLines(10);
        assertEquals(3, score.getLevel());

        score.addLines(10);
        assertEquals(4, score.getLevel());

        score.addLines(10);
        assertEquals(5, score.getLevel());
    }

    @Test
    void testLevelCap() {
        score.addLines(100);
        assertEquals(5, score.getLevel());
    }

    @Test
    void testAddLines_ZeroOrNegative() {
        int initialLevel = score.getLevel();
        score.addLines(0);
        assertEquals(initialLevel, score.getLevel());

        score.addLines(-5);
        assertEquals(initialLevel, score.getLevel());
    }

    @Test
    void testLevelPropertyBinding() {
        assertNotNull(score.levelProperty());
        assertNotNull(score.scoreProperty());
    }

    @Test
    void testPartialLineClears() {
        score.addLines(5);
        assertEquals(1, score.getLevel());

        score.addLines(5);
        assertEquals(2, score.getLevel());
    }
}




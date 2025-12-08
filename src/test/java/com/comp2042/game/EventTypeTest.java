package com.comp2042.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EventType enum.
 */
class EventTypeTest {

    @Test
    void testEventType_Values() {
        EventType[] values = EventType.values();
        assertEquals(4, values.length);

        assertTrue(java.util.Arrays.asList(values).contains(EventType.DOWN));
        assertTrue(java.util.Arrays.asList(values).contains(EventType.LEFT));
        assertTrue(java.util.Arrays.asList(values).contains(EventType.RIGHT));
        assertTrue(java.util.Arrays.asList(values).contains(EventType.ROTATE));
    }

    @Test
    void testEventType_ValueOf() {
        assertEquals(EventType.DOWN, EventType.valueOf("DOWN"));
        assertEquals(EventType.LEFT, EventType.valueOf("LEFT"));
        assertEquals(EventType.RIGHT, EventType.valueOf("RIGHT"));
        assertEquals(EventType.ROTATE, EventType.valueOf("ROTATE"));
    }
}




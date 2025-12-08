package com.comp2042.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for MoveEvent.
 */
class MoveEventTest {

    @Test
    void testMoveEvent_Constructor() {
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);

        assertEquals(EventType.DOWN, event.getEventType());
        assertEquals(EventSource.USER, event.getEventSource());
    }

    @Test
    void testMoveEvent_ThreadSource() {
        MoveEvent event = new MoveEvent(EventType.LEFT, EventSource.THREAD);

        assertEquals(EventType.LEFT, event.getEventType());
        assertEquals(EventSource.THREAD, event.getEventSource());
    }

    @Test
    void testMoveEvent_AllTypes() {
        for (EventType type : EventType.values()) {
            MoveEvent event = new MoveEvent(type, EventSource.USER);
            assertEquals(type, event.getEventType());
        }
    }
}




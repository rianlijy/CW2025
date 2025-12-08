package com.comp2042.game;

/**
 * Represents a movement event in the game, containing both the event type
 * (direction/rotation) and the source (user input or automatic).
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventSource getEventSource() {
        return eventSource;
    }
}

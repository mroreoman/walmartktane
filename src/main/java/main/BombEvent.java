package main;

import javafx.event.Event;
import javafx.event.EventType;

public class BombEvent extends Event {
    public static final EventType<BombEvent> STRIKE = new EventType<>(Event.ANY, "STRIKE");
    public static final EventType<BombEvent> PLAY = new EventType<>(Event.ANY);
    public static final EventType<BombEvent> PAUSE = new EventType<>(Event.ANY, "PAUSE");
    public static final EventType<BombEvent> EXPLODE = new EventType<>(PAUSE, "EXPLODE");
    public static final EventType<BombEvent> DEFUSE = new EventType<>(PAUSE, "DEFUSE");

    public BombEvent(EventType<BombEvent> eventType) {
        super(eventType);
    }
}

package main;

import javafx.event.Event;
import javafx.event.EventType;

public class KtaneEvent extends Event {
    public static final EventType<KtaneEvent> STRIKE = new EventType<>(Event.ANY, "STRIKE");
    public static final EventType<KtaneEvent> PLAY = new EventType<>(Event.ANY);
    public static final EventType<KtaneEvent> PAUSE = new EventType<>(Event.ANY, "PAUSE");
    public static final EventType<KtaneEvent> EXPLODE = new EventType<>(PAUSE, "EXPLODE");
    public static final EventType<KtaneEvent> DEFUSE = new EventType<>(PAUSE, "DEFUSE");

    public KtaneEvent(EventType<KtaneEvent> eventType) {
        super(eventType);
    }
}

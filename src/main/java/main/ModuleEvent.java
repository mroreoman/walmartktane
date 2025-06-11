package main;

import javafx.event.Event;
import javafx.event.EventType;
import main.modules.ModuleBase;

public class ModuleEvent extends Event {
    public static final EventType<ModuleEvent> SOLVE = new EventType<>(Event.ANY, "SOLVE");
    public static final EventType<ModuleEvent> STRIKE = new EventType<>(Event.ANY, "STRIKE");

    private final ModuleBase module;

    public ModuleEvent(ModuleBase module, EventType<ModuleEvent> eventType) {
        super(eventType);
        this.module = module;
    }

    public ModuleBase getModule() {
        return module;
    }
}

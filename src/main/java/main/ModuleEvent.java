package main;

import javafx.event.Event;
import javafx.event.EventType;
import main.modules.ModuleBase;

public class ModuleEvent extends Event {
    public static final EventType<ModuleEvent> SOLVE = new EventType<>(Event.ANY, "SOLVE");

    private final ModuleBase module;

    public ModuleEvent(ModuleBase module) {
        super(SOLVE);
        this.module = module;
    }

    public ModuleBase getModule() {
        return module;
    }
}

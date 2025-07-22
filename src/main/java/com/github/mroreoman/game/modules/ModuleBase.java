package com.github.mroreoman.game.modules;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import com.github.mroreoman.game.Bomb;
import com.github.mroreoman.game.ModuleEvent;
import com.github.mroreoman.game.widgets.Timer;
import com.github.mroreoman.game.widgets.Edgework;

import java.util.Random;

public abstract class ModuleBase extends Region {
    public enum Module {
        WIRES(),
        THE_BUTTON(),
        KEYPADS(),
        SIMON_SAYS(),
        WHOS_ON_FIRST(),
        MEMORY(),
        MORSE_CODE(),
        COMPLICATED_WIRES(),
        WIRE_SEQUENCES(),
        MAZES(),
        PASSWORDS();

        public ModuleBase instantiate(Bomb bomb, Random rand) {
            return switch (this) {
                case WIRES -> new WiresModule(bomb, rand);
                case THE_BUTTON -> new TheButtonModule(bomb, rand);
                case KEYPADS -> new KeypadsModule(bomb, rand);
                case SIMON_SAYS -> new SimonSaysModule(bomb, rand);
                case WHOS_ON_FIRST -> new WhosOnFirstModule(bomb, rand);
                case MEMORY -> new MemoryModule(bomb, rand);
                case MORSE_CODE -> new MorseCodeModule(bomb, rand);
                case COMPLICATED_WIRES -> new ComplicatedWiresModule(bomb, rand);
                case WIRE_SEQUENCES -> new WireSequencesModule(bomb, rand);
                case MAZES -> new MazesModule(bomb, rand);
                case PASSWORDS -> new PasswordsModule(bomb, rand);
            };
        }

        public String toString() {
            return switch (this) {
                case WIRES -> WiresModule.NAME;
                case THE_BUTTON -> TheButtonModule.NAME;
                case KEYPADS -> KeypadsModule.NAME;
                case SIMON_SAYS -> SimonSaysModule.NAME;
                case WHOS_ON_FIRST -> WhosOnFirstModule.NAME;
                case MEMORY -> MemoryModule.NAME;
                case MORSE_CODE -> MorseCodeModule.NAME;
                case COMPLICATED_WIRES -> ComplicatedWiresModule.NAME;
                case WIRE_SEQUENCES -> WireSequencesModule.NAME;
                case MAZES -> MazesModule.NAME;
                case PASSWORDS -> PasswordsModule.NAME;
            };
        }
    }

    private final static double MODULE_SIZE = 233;
    private final static double LIGHT_RADIUS = 10;
    protected final static double PADDING = 15;

    private final String name;
    protected final Edgework edgework;
    protected final Timer timer;
    private final Circle light;
    private final Timeline timeline;
    private boolean solved = false;

    public ModuleBase(Bomb bomb) {
        this("Module", bomb);
    }

    public ModuleBase(String name, Bomb bomb) {
        this.name = name;
        this.edgework = bomb.getEdgework();
        this.timer = bomb.getTimer();
        setMinSize(MODULE_SIZE, MODULE_SIZE);
        setMaxSize(MODULE_SIZE, MODULE_SIZE);
        setBackground(new Background(new BackgroundFill(Color.SILVER, null, null)));
        light = new Circle(getMaxWidth() - (PADDING + LIGHT_RADIUS), PADDING + LIGHT_RADIUS, LIGHT_RADIUS, Color.TRANSPARENT);
        light.setStroke(Color.BLACK);
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (!solved) {
                light.setFill(Color.TRANSPARENT);
            }
        }));
        getChildren().add(light);
    }

    public abstract void play();

    public abstract void pause();

    protected final void submit(boolean correct) {
        if (correct) {
            solved = true;
            fireEvent(new ModuleEvent(this, ModuleEvent.SOLVE));
            light.setFill(Color.LIME);
        } else {
            ModuleEvent.fireEvent(timer, new ModuleEvent(this, ModuleEvent.STRIKE));
            light.setFill(Color.RED);
            timeline.play();
        }
    }

    protected final void submitSolved(boolean correct) { //FIXME this method is sus...
        if (!correct) {
            ModuleEvent.fireEvent(timer, new ModuleEvent(this, ModuleEvent.STRIKE));
        }
    }

    protected final void initSubPane(Pane pane) {
        pane.setMinSize(getMinWidth(), getMinHeight());
        pane.setMaxSize(getMaxWidth(), getMaxHeight());
    }

    protected final Edgework getEdgework() {
        return edgework;
    }

    protected final Timer getTimer() {
        return timer;
    }

    public final boolean isSolved() {
        return solved;
    }

    public final String toString() {
        return name + (isSolved() ? " - Solved" : "");
    }

}
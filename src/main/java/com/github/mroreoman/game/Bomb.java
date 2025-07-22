package com.github.mroreoman.game;

import java.util.*;

import com.github.mroreoman.game.widgets.Timer;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import com.github.mroreoman.Util;
import com.github.mroreoman.game.modules.ModuleBase;
import com.github.mroreoman.game.widgets.Edgework;

public class Bomb {
    public enum State {RUNNING, EXPLODED, DEFUSED}

    private final GridPane root;
    private final Edgework edgework;
    private final com.github.mroreoman.game.widgets.Timer timer;
    private final Map<ModuleBase, Button> modules;
    private State state;
    private Pane currentModule;
    private final String name;
    private final Runnable bombExitAction;

    public Bomb(Random rand, int numModules, int startTimeSecs, int maxStrikes, Runnable bombExitAction, String name) {
        this(rand, numModules, startTimeSecs, maxStrikes, List.of(ModuleBase.Module.values()), bombExitAction, name);
    }

    public Bomb(Random rand, int numModules, int startTimeSecs, int maxStrikes, List<ModuleBase.Module> availableModules, Runnable bombExitAction, String name) {
        this(rand, startTimeSecs, maxStrikes, generateModuleList(rand, numModules, availableModules), bombExitAction, name);
    }

    /**
     * Creates a bomb with the specified modules
     *
     * @param moduleList list of modules in the order that they should be added to the bomb
     */
    public Bomb(Random rand, int startTimeSecs, int maxStrikes, List<ModuleBase.Module> moduleList, Runnable bombExitAction, String name) {
        root = new GridPane();
        edgework = new Edgework(rand);
        timer = new com.github.mroreoman.game.widgets.Timer(maxStrikes, startTimeSecs);
        modules = new LinkedHashMap<>();
        this.name = name;
        this.bombExitAction = bombExitAction;
        initModules(moduleList, rand);
        initGUI();

        root.addEventHandler(ModuleEvent.SOLVE, e -> checkDefused(e.getModule()));
        root.addEventHandler(BombEvent.PAUSE, e -> modules.keySet().forEach(ModuleBase::pause));
        root.addEventHandler(BombEvent.EXPLODE, e -> {
            modules.keySet().forEach(module -> module.setDisable(true));
            state = State.EXPLODED;
            ImageView boom = new ImageView(new Image(Objects.requireNonNull(Bomb.class.getResourceAsStream("boom.png"))));
            boom.setFitHeight(100);
            boom.setFitWidth(100);
            root.add(boom, 2, 1);
        });
        root.addEventHandler(BombEvent.DEFUSE, e -> {
            modules.keySet().forEach(module -> module.setDisable(true));
            state = State.DEFUSED;
        });

        state = State.RUNNING;
    }

    public Region getView() {
        return root;
    }

    private static List<ModuleBase.Module> generateModuleList(Random rand, int numModules, List<ModuleBase.Module> availableModules) {
        List<ModuleBase.Module> moduleList = new ArrayList<>();
        for (int i = 0; i < numModules; i++) {
            moduleList.add(availableModules.get(rand.nextInt(availableModules.size())));
        }
        return moduleList;
    }

    private void initModules(List<ModuleBase.Module> moduleList, Random rand) {
        if (moduleList.isEmpty()) {
            throw new IllegalArgumentException("Module types cannot empty");
        }

        for (ModuleBase.Module moduleType : moduleList) {
            ModuleBase module = moduleType.instantiate(this, rand);
            Button buton = new Button(module.toString());
            buton.setOnAction(event -> setCurrentModule(module));
            buton.setFont(Util.bodyFont(15));
            modules.put(module, buton);
        }
    }

    private void initGUI() {
        currentModule = new Pane();
        currentModule.setMinSize(233, 233);
        currentModule.setMaxSize(233, 233);

        VBox moduleButtonsBox = new VBox(10);
        moduleButtonsBox.getChildren().addAll(modules.values());
        ScrollPane moduleButtonsScroll = new ScrollPane(moduleButtonsBox);
        moduleButtonsScroll.setPrefHeight(50);

        Button exitButton = new Button("exit");
        exitButton.setOnAction(event -> exit());

        root.add(timer, 0, 0);
        GridPane.setHalignment(timer, HPos.LEFT);
        GridPane.setValignment(timer, VPos.TOP);
        root.add(edgework, 1, 0);
        root.add(exitButton, 2, 0);
        GridPane.setHalignment(exitButton, HPos.RIGHT);
        GridPane.setValignment(exitButton, VPos.TOP);
        root.add(moduleButtonsScroll, 0, 1);
        root.add(currentModule, 1, 1);

        root.setHgap(10);
        root.setVgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(25);
        root.getColumnConstraints().addAll(col1, col2, col3);

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);
        root.getRowConstraints().addAll(row1, row2);
    }

    public void play() {
        if (state == State.RUNNING) {
            BombEvent.fireEvent(timer, new BombEvent(BombEvent.PLAY));
            for (ModuleBase module : modules.keySet()) {
                module.play();
            }
        }
    }

    /**
     * called when exiting bomb from exit button
     */
    public void exit() {
        if (state == State.RUNNING) {
            BombEvent.fireEvent(timer, new BombEvent(BombEvent.EXPLODE));
        }
        bombExitAction.run();
    }

    private void checkDefused(ModuleBase currentModule) {
        modules.get(currentModule).setText(currentModule.toString());
        boolean defused = true;
        for (ModuleBase module : modules.keySet()) {
            defused &= module.isSolved();
        }
        if (defused) {
            BombEvent.fireEvent(timer, new BombEvent(BombEvent.DEFUSE));
        }
    }

    private void setCurrentModule(ModuleBase module) {
        currentModule.getChildren().clear();
        currentModule.getChildren().add(module);
    }

    public State getState() {
        return state;
    }

    public Edgework getEdgework() {
        return edgework;
    }

    public Timer getTimer() {
        return timer;
    }

    public String toString() {
        return name + (state != State.RUNNING ? " - " + state : "");
    }

}
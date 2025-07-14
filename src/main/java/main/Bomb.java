package main;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import main.modules.*;
import main.widgets.Edgework;

//TODO not going to split bomb into MVC yet, just restructure to have all three components available to other classes
//  - stop extending scene
//  - make state field observable so bomb buttons can respond to changes
//  -
public class Bomb extends Scene {
  public static final List<Class<? extends ModuleBase>> allModules = List.of(WiresModule.class, TheButtonModule.class, KeypadsModule.class, SimonSaysModule.class, WhosOnFirstModule.class, MemoryModule.class, MorseCodeModule.class, ComplicatedWiresModule.class, WireSequencesModule.class, MazesModule.class, PasswordsModule.class);

  private static final Random rand = new Random();
  public enum State { RUNNING, EXPLODED, DEFUSED }

  private final GridPane root;
  private final Edgework edgework;
  private final Timer timer;
  private final Map<ModuleBase, Button> modules;
  private State state;
  private Pane currentModule;
  private final Runnable setViewMainMenu;

  public Bomb(int numModules, int startTimeSecs, int maxStrikes, Runnable setViewMainMenu) {
    this(numModules, startTimeSecs, maxStrikes, allModules, setViewMainMenu);
  }

  public Bomb(int numModules, int startTimeSecs, int maxStrikes, List<Class<? extends ModuleBase>> availableModules, Runnable setViewMainMenu) {
    this(startTimeSecs, maxStrikes, generateModuleList(numModules, availableModules), setViewMainMenu);
  }

  /**
   * Creates a bomb with the specified modules
   * @param moduleList list of modules in the order that they should be added to the bomb
   */
  public Bomb(int startTimeSecs, int maxStrikes, List<Class<? extends ModuleBase>> moduleList, Runnable setViewMainMenu) {
    super(new GridPane());
    root = (GridPane)getRoot();
    edgework = new Edgework();
    timer = new Timer(maxStrikes, startTimeSecs);
    modules = new LinkedHashMap<>();
    this.setViewMainMenu = setViewMainMenu;
    initModules(moduleList);
    initGUI();

    addEventHandler(ModuleEvent.SOLVE, e -> checkDefused(e.getModule()));
    addEventHandler(BombEvent.PAUSE, e -> modules.keySet().forEach(ModuleBase::pause));
    addEventHandler(BombEvent.EXPLODE, e -> {
      modules.keySet().forEach(module -> module.setDisable(true)); //FIXME not working?
      state = State.EXPLODED;
    });
    addEventHandler(BombEvent.DEFUSE, e -> {
      modules.keySet().forEach(module -> module.setDisable(true));
      state = State.DEFUSED;
    });

    state = State.RUNNING;
  }

  public Region getView() {
    return root;
  }

  private static List<Class<? extends ModuleBase>> generateModuleList(int numModules, List<Class<? extends ModuleBase>> availableModules) {
    List<Class<? extends ModuleBase>> moduleList = new ArrayList<>();
    for (int i = 0; i < numModules; i++) {
      moduleList.add(availableModules.get(rand.nextInt(availableModules.size())));
    }
    return moduleList;
  }

  private void initModules(List<Class<? extends ModuleBase>> moduleList) {
    if (moduleList == null || moduleList.isEmpty()) {
      throw new IllegalArgumentException("Module types cannot be null or empty");
    }

    for (Class<? extends ModuleBase> moduleType : moduleList) {
      try {
        ModuleBase module = moduleType.getConstructor(Bomb.class).newInstance(this);
        Button buton = new Button(module.toString());
        buton.setOnAction(event -> setCurrentModule(module));
        buton.setFont(Util.bodyFont(15));
        modules.put(module, buton);
      } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
        throw new RuntimeException("Bomb could not instantiate " + moduleType.getName(), e);
      }
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
    exitButton.setOnAction(event -> setViewMainMenu.run());

    root.add(timer, 0, 0);
    GridPane.setHalignment(timer, HPos.LEFT);
    GridPane.setValignment(timer, VPos.TOP);
    root.add(edgework, 1, 0);
    root.add(exitButton, 2, 0);
    GridPane.setHalignment(exitButton, HPos.RIGHT);
    GridPane.setValignment(exitButton, VPos.TOP);
    root.add(moduleButtonsScroll, 0, 1);
    root.add(currentModule, 1, 1, 2, 1);

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

  //TODO check if we need this
  /**called to stop bomb prematurely (should only be when application is closed)*/
  public void stop() {
    System.out.println("Bomb.stop() called");
    BombEvent.fireEvent(timer, new BombEvent(BombEvent.PAUSE));
  }

  /**called when exiting bomb from exit button*/
  public void exit() {
    if (state == State.RUNNING) {
      BombEvent.fireEvent(timer, new BombEvent(BombEvent.EXPLODE));
    }
    setViewMainMenu.run();
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

  public Edgework getEdgework() {
    return edgework;
  }

  public Timer getTimer() {
    return timer;
  }

  public String toString() {
    return "Bomb" + (state != State.RUNNING ? " - " + state : "");
  }
  
}
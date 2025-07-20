package main;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

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

import main.modules.*;
import main.widgets.Edgework;

//TODO not going to split bomb into MVC yet, just restructure to have all three components available to other classes
//  - make state field observable so bomb buttons can respond to changes
public class Bomb extends GridPane {
  public static final List<Class<? extends ModuleBase>> allModules = List.of(WiresModule.class, TheButtonModule.class, KeypadsModule.class, SimonSaysModule.class, WhosOnFirstModule.class, MemoryModule.class, MorseCodeModule.class, ComplicatedWiresModule.class, WireSequencesModule.class, MazesModule.class, PasswordsModule.class);

  private static final Random rand = new Random();
  public enum State { RUNNING, EXPLODED, DEFUSED }

  private final Edgework edgework;
  private final Timer timer;
  private final Map<ModuleBase, Button> modules;
  private State state;
  private Pane currentModule;
  private final Runnable bombExitAction;
  private final String name;

  public Bomb(int numModules, int startTimeSecs, int maxStrikes, Runnable bombExitAction, String name) {
    this(numModules, startTimeSecs, maxStrikes, allModules, bombExitAction, name);
  }

  public Bomb(int numModules, int startTimeSecs, int maxStrikes, List<Class<? extends ModuleBase>> availableModules, Runnable bombExitAction, String name) {
    this(startTimeSecs, maxStrikes, generateModuleList(numModules, availableModules), bombExitAction, name);
  }

  /**
   * Creates a bomb with the specified modules
   * @param moduleList list of modules in the order that they should be added to the bomb
   */
  public Bomb(int startTimeSecs, int maxStrikes, List<Class<? extends ModuleBase>> moduleList, Runnable bombExitAction, String name) {
    edgework = new Edgework();
    timer = new Timer(maxStrikes, startTimeSecs);
    modules = new LinkedHashMap<>();
    this.bombExitAction = bombExitAction;
    this.name = name;
    initModules(moduleList);
    initGUI();

    addEventHandler(ModuleEvent.SOLVE, e -> checkDefused(e.getModule()));
    addEventHandler(BombEvent.PAUSE, e -> modules.keySet().forEach(ModuleBase::pause));
    addEventHandler(BombEvent.EXPLODE, e -> {
      modules.keySet().forEach(module -> module.setDisable(true));
      state = State.EXPLODED;
      ImageView boom = new ImageView(new Image(Objects.requireNonNull(Bomb.class.getResourceAsStream("boom.png"))));
      boom.setFitHeight(100);
      boom.setFitWidth(100);
      add(boom, 2, 1);
    });
    addEventHandler(BombEvent.DEFUSE, e -> {
      modules.keySet().forEach(module -> module.setDisable(true));
      state = State.DEFUSED;
    });

    state = State.RUNNING;
  }

  public Region getView() {
    return this;
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
    exitButton.setOnAction(event -> exit());

    add(timer, 0, 0);
    GridPane.setHalignment(timer, HPos.LEFT);
    GridPane.setValignment(timer, VPos.TOP);
    add(edgework, 1, 0);
    add(exitButton, 2, 0);
    GridPane.setHalignment(exitButton, HPos.RIGHT);
    GridPane.setValignment(exitButton, VPos.TOP);
    add(moduleButtonsScroll, 0, 1);
    add(currentModule, 1, 1);

    setHgap(10);
    setVgap(10);

    ColumnConstraints col1 = new ColumnConstraints();
    col1.setPercentWidth(25);
    ColumnConstraints col2 = new ColumnConstraints();
    col2.setPercentWidth(50);
    ColumnConstraints col3 = new ColumnConstraints();
    col3.setPercentWidth(25);
    getColumnConstraints().addAll(col1, col2, col3);

    RowConstraints row1 = new RowConstraints();
    RowConstraints row2 = new RowConstraints();
    row2.setVgrow(Priority.ALWAYS);
    getRowConstraints().addAll(row1, row2);
  }

  public void play() {
    if (state == State.RUNNING) {
      BombEvent.fireEvent(timer, new BombEvent(BombEvent.PLAY));
      for (ModuleBase module : modules.keySet()) {
        module.play();
      }
    }
  }

  /**called when exiting bomb from exit button*/
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
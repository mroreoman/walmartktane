package main;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.lang.RuntimeException;

import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

import main.modules.*;
import main.widgets.Edgework;

public class Bomb extends Scene {
  public static final List<Class<? extends ModuleBase>> allModules = List.of(WiresModule.class, TheButtonModule.class, KeypadsModule.class, SimonSaysModule.class, WhosOnFirstModule.class, MemoryModule.class, MorseCodeModule.class, ComplicatedWiresModule.class, WireSequencesModule.class, MazesModule.class, PasswordsModule.class);

  private static final Random rand = new Random();

  private Button buton;
  private final Edgework edgework;
  private final Timer timer;
  private final Map<ModuleBase, Button> modules;
  private Pane currentModule;
  private final GridPane root;
  private boolean exploded = false;
  private boolean defused = false;

  public Bomb(int numModules, int startTimeSecs, int maxStrikes) {
    this(numModules, startTimeSecs, maxStrikes, allModules);
  }

  public Bomb(int numModules, int startTimeSecs, int maxStrikes, List<Class<? extends ModuleBase>> availableModules) {
    this(startTimeSecs, maxStrikes, generateModuleList(numModules, availableModules));
  }

  /**
   * Creates a bomb with the specified modules
   * @param moduleList list of modules in the order that they should be added to the bomb
   */
  public Bomb(int startTimeSecs, int maxStrikes, List<Class<? extends ModuleBase>> moduleList) {
    super(new GridPane());
    root = (GridPane)getRoot();
    edgework = new Edgework();
    timer = new Timer(maxStrikes, startTimeSecs);
    modules = new HashMap<>();
    initModules(moduleList);
    initGUI();

    //TODO test event handlers, i think module solved is good
    addEventHandler(ModuleEvent.SOLVE, e -> checkDefused(e.getModule()));
    addEventHandler(KtaneEvent.PAUSE, e -> modules.keySet().forEach(ModuleBase::pause));
    addEventHandler(KtaneEvent.EXPLODE, e -> {
      modules.keySet().forEach(module -> module.setDisable(true));
      exploded = true;
    });
    addEventHandler(KtaneEvent.DEFUSE, e -> {
      modules.keySet().forEach(module -> module.setDisable(true));
      defused = true;
    });
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
      throw new IllegalArgumentException("Module types cannot be null or empty"); //TODO cancel bomb creation
    }

    for (Class<? extends ModuleBase> moduleType : moduleList) {
      try {
        ModuleBase module = moduleType.getConstructor(Bomb.class).newInstance(this);
        Button buton = new Button(module.toString());
        buton.setOnAction(event -> setCurrentModule(module));
        buton.setFont(Util.font(15));
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

    //TODO set column/row percent widths
    root.add(moduleButtonsScroll, 0, 0, 1, 2);
    root.add(edgework, 1, 0);
    root.add(timer, 2, 0);
    root.add(currentModule, 1, 1, 2, 1);
    root.add(exitButton, 3, 0);
  }

  public void play(Stage stage) {
    stage.setScene(this);
    if (!exploded && !defused) {
      KtaneEvent.fireEvent(this, new KtaneEvent(KtaneEvent.PLAY));
      for (ModuleBase module : modules.keySet()) {
        module.play();
      }
    }
  }

  /**called to stop bomb prematurely (should only be when application is closed)*/
  public void stop() {
//    timeline.pause();
    KtaneEvent.fireEvent(this, new KtaneEvent(KtaneEvent.PAUSE));
  }

  /**called when exiting bomb from exit button*/
  private void exit() {
    KtaneEvent.fireEvent(this, new KtaneEvent(KtaneEvent.PAUSE));
    KTANE.openMenu();
  }
  
  public void checkDefused(ModuleBase currentModule) {
    modules.get(currentModule).setText(currentModule.toString());
    boolean defused = true;
    for (ModuleBase module : modules.keySet()) {
      defused &= module.isSolved();
    }
    if (defused) {
      KtaneEvent.fireEvent(this, new KtaneEvent(KtaneEvent.DEFUSE));
    }
  }

  public void setCurrentModule(ModuleBase module) {
    currentModule.getChildren().clear();
    currentModule.getChildren().add(module);
  }

  public final Button getButton(Stage stage) {
    if (buton == null) {
      buton = new Button(toString());
      buton.setOnAction(event -> play(stage));
      buton.setFont(new Font("Roboto Condensed", 15));
    }
    buton.setText(toString());
    return buton;
  }

  public Edgework getEdgework() {
    return edgework;
  }

  public Timer getTimer() {
    return timer;
  }

  public String toString() { //TODO was the verbose toString necessary
//    return "Bomb" + (exploded ? " - Exploded " : (defused ? " - Defused " : " ")) +
//        "(" +
//        modules.size() + (modules.size() == 1 ? " module, " : " modules, ") +
//        strikes + (strikes == 1 ? " strike, " : " strikes, ") +
//        getTime() +
//        ")";
    return "Bomb" + (exploded ? " - Exploded " : (defused ? " - Defused " : " "));
  }
  
}
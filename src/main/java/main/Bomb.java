package main;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.RuntimeException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import main.modules.*;
import main.widgets.Edgework;

public class Bomb extends Scene {
  public static final List<Class<? extends ModuleBase>> allModules = List.of(WiresModule.class, TheButtonModule.class, KeypadsModule.class, SimonSaysModule.class, WhosOnFirstModule.class, MemoryModule.class, MorseCodeModule.class, ComplicatedWiresModule.class, WireSequencesModule.class, MazesModule.class, PasswordsModule.class);

  private static final Random rand = new Random();

  private Button buton;
  
  private Edgework edgework;
  private ModuleBase[] modules;
  private int strikes = 0;
  private int maxStrikes = 3;
  private boolean running = false;
  private boolean exploded = false;
  private boolean defused = false;
  
  private int timeSecs;
  private int startTimeSecs;
  private Timeline timeline = new Timeline(new KeyFrame(Duration.millis(250), e -> tick()));
  private int tick = 0;

  private Text[] strikeTexts = new Text[maxStrikes - 1];
  private Text timerText;
  private HBox timerBox;
  private Button exitButton;
  private AnchorPane infoBox;
  private VBox moduleButtons;
  private ScrollPane moduleButtonsBox;
  private Pane currentModule;
  private AnchorPane moduleBox;

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
    super(new VBox(25));
    this.startTimeSecs = startTimeSecs;
    this.maxStrikes = maxStrikes;
    edgework = new Edgework();
    initModules(moduleList);
    initTimer(startTimeSecs);
    initGUI();
  }

  private static List<Class<? extends ModuleBase>> generateModuleList(int numModules, List<Class<? extends ModuleBase>> availableModules) {
    List<Class<? extends ModuleBase>> moduleList = new ArrayList<>();
    for (int i = 0; i < numModules; i++) {
      moduleList.add(availableModules.get(rand.nextInt(availableModules.size())));
    }
    return moduleList;
  }

  private void initModules(List<Class<? extends ModuleBase>> moduleTypes) {
    if (moduleTypes == null || moduleTypes.isEmpty()) {
      throw new IllegalArgumentException("Module types cannot be null or empty");
    }

    modules = new ModuleBase[moduleTypes.size()];
    for (int i = 0; i < modules.length; i++) {
      try {
        modules[i] = moduleTypes.get(i).getConstructor(Bomb.class).newInstance(this);
      } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
        throw new RuntimeException("Bomb could not instantiate " + moduleTypes.get(i).getName(), e);
      }
    }
  }

  private void initTimer(int startTimeSecs) {
    this.startTimeSecs = startTimeSecs;
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  private void tick(){
    if (running) {
      tick++;
      if (tick >= maxStrikes + 1 - strikes) {
        tick = 0;
        timeSecs++;
        updateBombText();
      }
    }
    
    if (timeSecs >= startTimeSecs) {
      exploded = true;
      finish();
    }
  }
  

  private void initGUI() {
    //make info gui
    for (int i = 0; i < strikeTexts.length; i++) {
      strikeTexts[i] = new Text("X");
      Util.setupText(strikeTexts[i]);
    }
    
    timerText = new Text(getTime());
    Util.setupText(timerText);
    
    exitButton = new Button("exit");
    exitButton.setOnAction(event -> exit());
    
    timerBox = new HBox(25, new HBox(10, strikeTexts), timerText, exitButton);
    AnchorPane.setRightAnchor(timerBox, 0.0);
    AnchorPane.setTopAnchor(timerBox, 0.0);
    
    infoBox = new AnchorPane(edgework, timerBox);

    //make modules gui
    currentModule = new Pane();
    currentModule.setBackground(new Background(new BackgroundFill(Color.SILVER, null, null)));
    currentModule.setMinSize(233, 233);
    currentModule.setMaxSize(233, 233);
    AnchorPane.setRightAnchor(currentModule, 10.0);

    moduleButtons = new VBox(10);
    for (ModuleBase module: modules) {
      moduleButtons.getChildren().add(module.getButton());
    }
    moduleButtonsBox = new ScrollPane(moduleButtons);
    moduleButtonsBox.setMinViewportWidth(50);
    moduleButtonsBox.setPrefViewportHeight(250);
    
    moduleBox = new AnchorPane(moduleButtonsBox, currentModule);

    //add all to bomb scene
    ((VBox)getRoot()).getChildren().addAll(infoBox, moduleBox);
  }

  public void play() {
    KTANE.getStage().setScene(this);
    if (!exploded && !defused) {
      running = true;
      for (ModuleBase module: modules) {
        module.play();
      }
    }
  }

  /**called to stop bomb prematurely (should only be when application is closed)*/
  public void stop() {
    timeline.pause();
  }

  /**called when exiting bomb from exit button*/
  private void exit() {
    running = false;
    for (ModuleBase module: modules) {
      module.pause();
    }
    KTANE.openMenu();
  }

  /**called when bomb is defused or exploded*/
  private void finish() {
    running = false;
    for (ModuleBase module: modules) {
      module.pause();
    }
    timeline.pause();
    if (strikes >= maxStrikes) {
      exploded = true;
    }
    updateBombText();
    for (ModuleBase module: modules) {
      module.setDisable(true);
    }
  }
  
  public void addStrike() {
    strikes++;
    if (strikes >= maxStrikes) {
      exploded = true;
      finish();
    } else if (strikes > 0) {
      strikeTexts[strikes - 1].setFill(Color.RED);
    }
  }
  
  public void checkDefused(ModuleBase currentModule) {
    currentModule.updateButton();
    boolean temp = true;
    for (ModuleBase module: modules) {
      temp &= module.isSolved();
    }
    if (temp) {
      defused = true;
      finish();
    }
  }

  public void setCurrentModule(ModuleBase module) {
    currentModule.getChildren().clear();
    currentModule.getChildren().add(module);
  }

  private void updateBombText() {
    if (running) {
      timerText.setText(getTime());
    } else {
      timerText.setText((exploded ? "Exploded at " : "Defused at ") + getTime());
    }
  }
  
  private void updateModuleButtons() {
    for (ModuleBase module: modules) {
      module.updateButton();
    }
  }

  public final Button getButton() {
    if (buton == null) {
      buton = new Button(toString());
      buton.setOnAction(event -> play());
      buton.setFont(new Font("Roboto Condensed", 15));
    }
    return buton;
  }

  public final boolean updateButton() {
    if (buton.getText().equals(toString())) {
      return false;
    } else {
      buton.setText(toString());
      return true;
    }
  }

  public boolean timerContains(int i) {
    return getTime().indexOf(""+i) != -1;
  }

  public String getTime() {
    return Util.toMinutes(startTimeSecs - timeSecs);
  }

  public Edgework getEdgework() {
    return edgework;
  }

  public int getStrikes() {
    return strikes;
  }

  public boolean isRunning() {
    return running;
  }

  public String toString() {
    return "Bomb" + (exploded ? " - Exploded " : (defused ? " - Defused " : " ")) +
        "(" +
        modules.length + (modules.length == 1 ? " module, " : " modules, ") +
        strikes + (strikes == 1 ? " strike, " : " strikes, ") +
        getTime() +
        ")";
  }
  
}
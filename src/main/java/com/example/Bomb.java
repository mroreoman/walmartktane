package com.example;

import java.util.Scanner;
import java.util.Random;
import java.lang.RuntimeException;
import java.lang.reflect.Constructor;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import com.example.widgets.Edgework;
import com.example.modules.*;

public class Bomb extends Scene {
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
  
  public Bomb() {
    this(5);
  }
  
  public Bomb(int numModules) {
    this(numModules, 300);
  }

  public Bomb(int numModules, int startTimeSecs, Class<? extends ModuleBase>... moduleTypes) {
    super(new VBox(25));
    edgework = new Edgework();
    initModules(numModules, moduleTypes);
    initTimer(startTimeSecs);
    initGUI();
  }

  public Bomb(int startTimeSecs, int maxStrikes, int[] numModules, Class <? extends ModuleBase>[]... modules){
    super(new VBox(25));
    this.maxStrikes = maxStrikes;
    edgework = new Edgework();
    initModules(numModules, modules);
    initTimer(startTimeSecs);
    initGUI();
  }

  private void initModules(int numModules, Class<? extends ModuleBase>... moduleTypes) {
    modules = new ModuleBase[numModules];

    if (moduleTypes.length < 1) {
      // moduleTypes = new Class[] {WiresModule.class, TheButtonModule.class, KeypadsModule.class, SimonSaysModule.class, WhosOnFirstModule.class, MemoryModule.class, MorseCodeModule.class, ComplicatedWiresModule.class, WireSequencesModule.class, MazesModule.class};
      // moduleTypes = new Class[] {PasswordsModule.class, MemoryModule.class, MazesModule.class};
      moduleTypes = new Class[] {TheButtonModule.class, SimonSaysModule.class, WhosOnFirstModule.class, MemoryModule.class, MorseCodeModule.class, MazesModule.class};
    }
    
    for (int i = 0; i < modules.length; i++) {
      Class<? extends ModuleBase> moduleType = moduleTypes[rand.nextInt(moduleTypes.length)];
      try {
        modules[i] = moduleType.getConstructor(Bomb.class).newInstance(this);
      } catch (Exception e) {
        throw new RuntimeException("Invalid Module Type " + moduleType, e);
      }
    }
  }

  private void initModules(int[] numModules, Class<? extends ModuleBase>[]... modules) {
    int sum = 0;
    for (int num: numModules){
      sum += num;
    }
    this.modules = new ModuleBase[sum];

    for (int i = 0; i < modules.length; i++){
      modules[i] = Util.randomUniqueIndexes(modules[i], numModules[i]);
    }

    for (Class<? extends ModuleBase>[] modulesArr: modules){
      for (int i = 0; i < modulesArr.length; i++) {
        Class<? extends ModuleBase> moduleType = modulesArr[i];
        try {
          this.modules[i] = moduleType.getConstructor(Bomb.class).newInstance(this);
        } catch (Exception e) {
          throw new RuntimeException("Invalid Module Type " + moduleType, e);
        }
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

  //called to stop bomb prematurely (should only be when application is closed)
  public void stop() {
    timeline.pause();
  }

  //called when exiting bomb from exit button
  private void exit() {
    running = false;
    for (ModuleBase module: modules) {
      module.pause();
    }
    KTANE.openMenu();
  }

  //called when bomb is defused or exploded
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
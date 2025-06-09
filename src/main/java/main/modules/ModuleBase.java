package main.modules;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javafx.util.Duration;

import main.Bomb;
import main.BombEvent;
import main.ModuleEvent;
import main.Timer;
import main.widgets.Edgework;

public abstract class ModuleBase extends Region {
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
      BombEvent.fireEvent(timer, new BombEvent(BombEvent.STRIKE));
      light.setFill(Color.RED);
      timeline.play();
    }
  }

  protected final void submitSolved(boolean correct) { //FIXME this method is sus...
    if (!correct) {
      BombEvent.fireEvent(timer, new BombEvent(BombEvent.STRIKE));
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
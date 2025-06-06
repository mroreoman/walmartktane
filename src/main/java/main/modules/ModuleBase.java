package main.modules;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import main.Bomb;
import main.widgets.Edgework;

public abstract class ModuleBase extends Region {
  private final static double MODULE_SIZE = 233;
  private final static double LIGHT_RADIUS = 10;
  protected final static double PADDING = 15;
  
  private Button buton;
  private final String name;
  private final Bomb bomb;
  private final Circle light;
  private final Timeline timeline;
  private boolean solved = false;

  public ModuleBase(Bomb bomb) {
    this("Module", bomb);
  }
  
  public ModuleBase(String name, Bomb bomb) {
    this.name = name;
    this.bomb = bomb;
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

  public final Button getButton() {
    if (buton == null) {
      buton = new Button(toString());
      buton.setOnAction(event -> bomb.setCurrentModule(this));
      buton.setFont(new Font("Roboto Condensed", 15));
    }
    buton.setText(toString());
    return buton;
  }

  public final void updateButton() {
    buton.setText(toString());
  }

  protected final void submit(boolean correct) {
    if (correct) {
      solved = true;
      bomb.checkDefused(this);
      light.setFill(Color.LIME);
    } else {
      bomb.addStrike();
      light.setFill(Color.RED);
      timeline.play();
    }
  }

  protected final void submitSolved(boolean correct) {
    if (!correct) {
      bomb.addStrike();
    }
  }

  protected final void initSubPane(Pane pane) {
    pane.setMinSize(getMinWidth(), getMinHeight());
    pane.setMaxSize(getMaxWidth(), getMaxHeight());
  }

  protected final Bomb getBomb() {
    return bomb;
  }

  protected final Edgework getEdgework() {
    return bomb.getEdgework();
  }
  
  public final boolean isSolved() {
    return solved;
  }

  public final String toString() {
    return name + (isSolved() ? " - Solved" : "");
  }
  
}
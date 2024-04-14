package main.modules;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import main.Bomb;
import main.widgets.Edgework;

public abstract class ModuleBase extends Pane {
  protected final static double MARGIN = 15;
  
  private Button buton;
  
  protected String name;
  protected Bomb bomb;
  private boolean solved = false;
  private Circle light;
  private Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
    if (!solved) {
      light.setFill(Color.TRANSPARENT);
    }
  }));

  public ModuleBase(Bomb bomb) {
    this("Module", bomb);
  }
  
  public ModuleBase(String name, Bomb bomb) {
    this.name = name;
    this.bomb = bomb;
    setMinSize(233, 233);
    setMaxSize(233, 233);
    light = new Circle(getMaxWidth() - (MARGIN + 10), MARGIN + 10, 10, Color.TRANSPARENT);
    light.setStroke(Color.BLACK);
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

  // protected static final void initButton(Button button) {
  //   Border old = button.getBorder();
  //   button.setOnMouseEntered(event -> button.setBorder(new Border()));
  //   button.setOnMouseExited(event -> button.setBorder(old));
  // }
  
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
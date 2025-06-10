package main.modules;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

import main.Bomb;

public class TheButtonModule extends ModuleBase {
  private static final String[] LABELS = {"Abort", "Detonate", "Hold", "Press"};
  private static final Color[] COLORS = {Color.BLUE, Color.RED, Color.WHITE, Color.YELLOW, Color.BLACK};
  private static final Random rand = new Random();
  
  private String label;
  private Color color;
  private Rectangle strip;
  private Color stripColor;
  private int stripSolution;
  private boolean tapSolve;
  private boolean isHeld = false;
  private Timeline timeline = new Timeline(new KeyFrame(Duration.millis(250), e -> holdButton()));
  
  public TheButtonModule(Bomb bomb) {
    this(bomb, COLORS[rand.nextInt(5)], LABELS[rand.nextInt(4)]);
  }

  public TheButtonModule(Bomb bomb, Color color, String label) {
    super("The Button", bomb);
    this.color = color;
    this.label = label;
    setSolution();
    initGUI();
  }

  public void setSolution() {
    if (color == Color.BLUE && label == "Abort") {
      tapSolve = false;
    } else if (getEdgework().numBatteries() > 1 && label == ("Detonate")) {
      tapSolve = true;
    } else if (color == Color.WHITE && getEdgework().hasIndicator("CAR", true)) {
      tapSolve = false;
    } else if (getEdgework().numBatteries() > 2 && getEdgework().hasIndicator("FRK", true)) {
      tapSolve = true;
    } else if (color == Color.YELLOW) {
      tapSolve = false;
    } else if (color == Color.RED && label == "Hold") {
      tapSolve = true;
    } else {
      tapSolve = false;
    }
  }

  private void initGUI() {
    AnchorPane box = new AnchorPane();
    initSubPane(box);

    Button buton = new Button(label);
    buton.setTextFill(color == Color.WHITE || color == Color.YELLOW ? Color.BLACK : Color.WHITE);
    buton.setPadding(Insets.EMPTY);
    buton.setStyle("-fx-background-color: #" + color.toString().substring(2,8) + "; -fx-background-radius: 75em; -fx-min-width: 150px; -fx-min-height: 150px; -fx-max-width: 150px; -fx-max-height: 150px;");
    buton.setFont(new Font("Roboto Condensed", 25));
    buton.setOnMousePressed(event -> press());
    buton.setOnMouseReleased(event -> release());
    
    strip = new Rectangle(25, 125, Color.TRANSPARENT);
    strip.setStroke(Color.BLACK);

    AnchorPane.setLeftAnchor(buton, PADDING);
    AnchorPane.setBottomAnchor(buton, PADDING);
    AnchorPane.setRightAnchor(strip, PADDING);
    AnchorPane.setBottomAnchor(strip, PADDING);
    box.getChildren().add(buton);
    box.getChildren().add(strip);
    this.getChildren().add(box);
  }

  public void press() {
    timeline.playFromStart();
  }

  public void release() {
    timeline.pause();
    if (!isHeld){
      tapButton();
    } else {
      strip.setFill(Color.TRANSPARENT);
      submit(timer.contains(stripSolution));
    }
  }
  
  public void genStrip() {
    stripColor = COLORS[rand.nextInt(4)];
    setStripSolution();
    strip.setFill(stripColor);
  }

  public void setStripSolution() {
    if (stripColor == Color.BLUE)
      stripSolution = 4;
    else if (stripColor == Color.YELLOW)
      stripSolution = 5;
    else
      stripSolution = 1;
  }

  public void tapButton() {
    submit(tapSolve);
  }

  public void holdButton() {
    isHeld = true;
    genStrip();
  }

  public void play() {}

  public void pause() {}
  
}
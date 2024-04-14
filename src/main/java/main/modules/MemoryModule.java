package main.modules;

import java.util.Random;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import main.Bomb;
import main.Util;

public class MemoryModule extends ModuleBase {
  private static Random rand = new Random();

  private String[] labels = new String[5];
  private int[] positions = new int[5];
  private int stage = 0;
  private Text displayLabel;
  private Mem[] mems = new Mem[4];

  private AnchorPane box;
  private StackPane display;
  private StackPane moduleBox;
  private HBox buttonBox;
  private VBox lightBox;
  private Rectangle[] lights = new Rectangle[5];

  private class Mem extends Button {
    String label;
    int position;
    boolean correct = false;

    private Mem(String label, int position) {
      super(label);
      this.label = label;
      this.position = position + 1;
      setOnAction(event -> press());
      setPadding(Insets.EMPTY);
      setStyle("-fx-min-width: 35px; -fx-min-height: 60px; -fx-max-width: 35px; -fx-max-height: 60px; -fx-background-color: darkkhaki; -fx-font-family: 'Roboto Condensed'; -fx-font-size: 45; -fx-font-weight: bold; -fx-text-fill: black");
    }

    private void press() {
      if (isSolved()) {
        submitSolved(true);
        return;
      }
      if (correct) {
        labels[stage] = label;
        positions[stage] = position;
        lights[4 - stage++].setFill(Color.LIME);
        if (stage == 5) {
          submit(true);
        } else {
          initMemory();
          updateGUI();
        }
      } else {
        submit(false);
        stage = 0;
        for (Rectangle light: lights) {
          light.setFill(Color.TRANSPARENT);
        }
        initMemory();
        updateGUI();
      }
    }
  }

  public MemoryModule(Bomb bomb) {
    super("Memory", bomb);
    initMemory();
    initGUI();
  }

  private void initMemory() {
    displayLabel = new Text(String.valueOf(rand.nextInt(4) + 1));
    displayLabel.setFill(Color.WHITE);
    displayLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 60));
    int[] butons = Util.goodUniqueIndexes(4, 4);
    for (int i = 0; i < 4; i++) {
      mems[i] = new Mem(String.valueOf(butons[i] + 1), i);
    }
    setSolution();
  }

  private void initGUI() {
    box = new AnchorPane();
    initSubPane(box);

    display = new StackPane();
    display.getChildren().addAll(new Rectangle(175, 125, Color.web("#444444")), new Rectangle(125, 100, Color.GRAY), new Rectangle(100, 75, Color.BLACK), new Rectangle(80, 65, Color.SEAGREEN), new Rectangle(80, 3, Color.LIGHTSEAGREEN), new Rectangle(3, 65, Color.PLUM), displayLabel);

    buttonBox = new HBox(5);
    buttonBox.getChildren().addAll(mems);
    buttonBox.setAlignment(Pos.CENTER);

    moduleBox = new StackPane();
    moduleBox.getChildren().addAll(new Rectangle(175, 75, Color.GRAY), new Rectangle(165, 65, Color.BLACK), buttonBox);

    for (int i = 0; i < 5; i++) {
      lights[i] = new Rectangle(25, 15, Color.TRANSPARENT);
      lights[i].setStroke(Color.GRAY);
    }

    lightBox = new VBox(5);
    lightBox.getChildren().addAll(lights);
    lightBox.setAlignment(Pos.CENTER);
    lightBox.setMinSize(35, 150);
    lightBox.setMaxSize(35, 150);
    lightBox.setBackground(Util.simpleBackground(Color.BLACK));

    AnchorPane.setLeftAnchor(display, 5.0);
    AnchorPane.setTopAnchor(display, 15.0);
    AnchorPane.setLeftAnchor(moduleBox, 5.0);
    AnchorPane.setBottomAnchor(moduleBox, 15.0);
    AnchorPane.setRightAnchor(lightBox, 5.0);
    AnchorPane.setBottomAnchor(lightBox, 10.0);

    box.getChildren().addAll(display, moduleBox, lightBox);
    this.getChildren().add(box);
  }

  private void updateGUI() {
    box.getChildren().clear();
    display.getChildren().clear();
    display.getChildren().addAll(new Rectangle(175, 125, Color.web("#444444")), new Rectangle(125, 100, Color.GRAY), new Rectangle(100, 75, Color.BLACK), new Rectangle(80, 65, Color.SEAGREEN), new Rectangle(80, 3, Color.LIGHTSEAGREEN), new Rectangle(3, 65, Color.PLUM), displayLabel);
    moduleBox.getChildren().clear();
    buttonBox.getChildren().clear();
    buttonBox.getChildren().addAll(mems);
    moduleBox.getChildren().addAll(new Rectangle(175, 75, Color.GRAY), new Rectangle(165, 65, Color.BLACK), buttonBox);
    box.getChildren().addAll(display, moduleBox, lightBox);
  }

  private void setSolution() {
    if (stage == 0) {
      if (displayLabel.getText().equals("1")) {
        setCorrect(2);
      } else if (displayLabel.getText().equals("2")) {
        setCorrect(2);
      } else if (displayLabel.getText().equals("3")) {
        setCorrect(3);
      } else {
        setCorrect(4);
      }
    } else if (stage == 1) {
      if (displayLabel.getText().equals("1")) {
        setCorrect("4");
      } else if (displayLabel.getText().equals("2")) {
        setCorrect(positions[0]);
      } else if (displayLabel.getText().equals("3")) {
        setCorrect(1);
      } else {
        setCorrect(positions[0]);
      }
    } else if (stage == 2) {
      if (displayLabel.getText().equals("1")) {
        setCorrect(labels[1]);
      } else if (displayLabel.getText().equals("2")) {
        setCorrect(labels[0]);
      } else if (displayLabel.getText().equals("3")) {
        setCorrect(3);
      } else {
        setCorrect("4");
      }
    } else if (stage == 3) {
      if (displayLabel.getText().equals("1")) {
        setCorrect(positions[0]);
      } else if (displayLabel.getText().equals("2")) {
        setCorrect(1);
      } else if (displayLabel.getText().equals("3")) {
        setCorrect(positions[1]);
      } else {
        setCorrect(positions[1]);
      }
    } else {
      if (displayLabel.getText().equals("1")) {
        setCorrect(labels[0]);
      } else if (displayLabel.getText().equals("2")) {
        setCorrect(labels[1]);
      } else if (displayLabel.getText().equals("3")) {
        setCorrect(labels[3]);
      } else {
        setCorrect(labels[2]);
      }
    }
  }

  private void setCorrect(int pos) {
    for (Mem mem: mems) {
      if (mem.position == pos) {
        mem.correct = true;
        break;
      }
    }
  }

  private void setCorrect(String lab) {
    for (Mem mem: mems) {
      if (mem.label.equals(lab)) {
        mem.correct = true;
        break;
      }
    }
  }

  public void play() {}
  
  public void pause() {}
}
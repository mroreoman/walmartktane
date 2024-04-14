package com.example.modules;

import java.util.HashMap;
import java.util.Map;
import static java.util.Map.entry;
import java.util.Random;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import com.example.Bomb;
import com.example.Util;

public class WhosOnFirstModule extends ModuleBase {
  private static Random rand = new Random();
  private int stage = 0;
  private WOF[] wofs = new WOF[6];
  private Text displayLabel;
  private Rectangle[] lights = new Rectangle[3];
  private TilePane buttonBox;
  private StackPane display;
  private StackPane moduleBox;
  private AnchorPane box;
  private VBox lightBox;
 
  private static HashMap<String, Integer> DISPLAY_LABELS = new HashMap<>(Map.ofEntries(
    entry("YES", 2),
    entry("FIRST", 1),
    entry("DISPLAY", 5),
    entry("OKAY", 1),
    entry("SAYS", 5),
    entry("NOTHING", 2),
    entry("", 4),
    entry("BLANK", 3),
    entry("NO", 5),
    entry("LED", 2),
    entry("LEAD", 5),
    entry("READ", 3),
    entry("RED", 3),
    entry("REED", 4),
    entry("LEED", 4),
    entry("HOLD ON", 5),
    entry("YOU", 3),
    entry("YOU ARE", 5),
    entry("YOUR", 3),
    entry("YOU'RE", 3),
    entry("UR", 0),
    entry("THERE", 5),
    entry("THEY'RE", 4),
    entry("THEIR", 3),
    entry("THEY ARE", 2),
    entry("SEE", 5),
    entry("C", 1),
    entry("CEE", 5)
  ));
  private static String[] BUTTON_LABELS = {"READY", "FIRST", "NO", "BLANK", "NOTHING", "YES", "WHAT", "UHHH", "LEFT", "RIGHT", "MIDDLE", "OKAY", "WAIT", "PRESS", "YOU", "YOU ARE", "YOUR", "YOU'RE", "UR", "U", "UH HUH", "UH UH", "WHAT?", "DONE", "NEXT", "HOLD", "SURE", "LIKE"};
  
  private static HashMap<String, String[]> BUTTON_LISTS = new HashMap<>(Map.ofEntries(
  entry("READY", new String[]{"YES", "OKAY", "WHAT", "MIDDLE", "LEFT", "PRESS", "RIGHT", "BLANK", "READY"}),
  entry("FIRST", new String[]{"LEFT", "OKAY", "YES", "MIDDLE", "NO", "RIGHT", "NOTHING", "UHHH", "WAIT", "READY", "BLANK", "WHAT", "PRESS", "FIRST"}), 
  entry("NO", new String[]{"BLANK", "UHHH", "WAIT", "FIRST", "WHAT", "READY", "RIGHT", "YES", "NOTHING", "LEFT", "PRESS", "OKAY", "NO"}), 
  entry("BLANK", new String[]{"WAIT", "RIGHT", "OKAY", "MIDDLE", "BLANK"}),
  entry("NOTHING", new String[]{"UHHH", "RIGHT", "OKAY", "MIDDLE", "YES", "BLANK", "NO", "PRESS", "LEFT", "WHAT", "WAIT", "FIRST", "NOTHING"}),
  entry("YES", new String[]{"OKAY", "RIGHT", "UHHH", "MIDDLE", "FIRST", "WHAT", "PRESS", "READY", "NOTHING", "YES"}),
  entry("WHAT", new String[]{"UHHH", "WHAT"}),
  entry("UHHH", new String[]{"READY", "NOTHING", "LEFT", "WHAT", "OKAY", "YES", "RIGHT", "NO", "PRESS", "BLANK", "UHHH"}),
  entry("LEFT", new String[]{"RIGHT", "LEFT"}),
  entry("RIGHT", new String[]{"YES", "NOTHING", "READY", "PRESS", "NO", "WAIT", "WHAT", "RIGHT"}),
  entry("MIDDLE", new String[]{"BLANK", "READY", "OKAY", "WHAT", "NOTHING", "PRESS", "NO", "WAIT", "LEFT", "MIDDLE"}),
  entry("OKAY", new String[]{"MIDDLE", "NO", "FIRST", "YES", "UHHH", "NOTHING", "WAIT", "OKAY"}),
  entry("WAIT", new String[]{"UHHH", "NO", "BLANK", "OKAY", "YES", "LEFT", "FIRST", "PRESS", "WHAT", "WAIT"}),
  entry("PRESS", new String[]{"RIGHT", "MIDDLE", "YES", "READY", "PRESS"}),
  entry("YOU", new String[]{"SURE", "YOU ARE", "YOUR", "YOU'RE", "NEXT", "UH HUH", "UR", "HOLD", "WHAT?", "YOU"}),
  entry("YOU ARE", new String[]{"YOUR", "NEXT", "LIKE", "UH HUH", "WHAT?", "DONE", "UH UH", "HOLD", "YOU", "U", "YOU'RE", "SURE", "UR", "YOU ARE"}),
  entry("YOUR", new String[]{"UH UH", "YOU ARE", "UH HUH", "YOUR"}),
  entry("YOU'RE", new String[]{"YOU", "YOU'RE"}),
  entry("UR", new String[]{"DONE", "U", "UR"}),
  entry("U", new String[]{"UH HUH", "SURE", "NEXT", "WHAT?", "YOU'RE", "UR", "UH UH", "DONE", "U"}),
  entry("UH HUH", new String[]{"UH HUH"}),
  entry("UH UH", new String[]{"UR", "U", "YOU ARE", "YOU'RE", "NEXT", "UH UH"}),
  entry("WHAT?", new String[]{"YOU", "HOLD", "YOU'RE", "YOUR", "U", "DONE", "UH UH", "LIKE", "YOU ARE", "UH HUH", "UR", "NEXT", "WHAT?"}),
  entry("DONE", new String[]{"SURE", "UH HUH", "NEXT", "WHAT?", "YOUR", "UR", "YOU'RE", "HOLD", "LIKE", "YOU", "U", "YOU ARE", "UH UH", "DONE"}),
  entry("NEXT", new String[]{"WHAT?", "UH HUH", "UH UH", "YOUR", "HOLD", "SURE", "NEXT"}),
  entry("HOLD", new String[]{"YOU ARE", "U", "DONE", "UH UH", "YOU", "UR", "SURE", "WHAT?", "YOU'RE", "NEXT", "HOLD"}),
  entry("SURE", new String[]{"YOU ARE", "DONE", "LIKE", "YOU'RE", "YOU", "HOLD", "UH HUH", "UR", "SURE"}),
  entry("LIKE", new String[]{"YOU'RE", "NEXT", "U", "UR", "HOLD", "DONE", "UH UH", "WHAT?", "UH HUH", "YOU", "LIKE"})
));
  
  private class WOF extends Button {
    String label;
    int position;
    
    private WOF(String label, int position){
      super(label);
      this.label = label;
      this.position = position;
      setOnAction(event -> press());
      setPadding(Insets.EMPTY);
      setStyle("-fx-min-width: 65px; -fx-min-height: 30px; -fx-max-width: 65px; -fx-max-height: 30px; -fx-background-color: darkkhaki; -fx-font-family: 'Roboto Condensed'; -fx-font-size: 12; -fx-font-weight: bold;");
    }
      
    private void press() {
      if (isSolved()) {
        submitSolved(false);
        return;
      }
      if (label == getSolution()) {
        lights[2 - stage++].setFill(Color.LIME);
        if (stage >= 3) {
          submit(true);
        } else {
          initWhosOnFirst();
          updateGUI();
        }
      }
      else {
        submit(false);
        initWhosOnFirst();
        updateGUI();
      }
    }
  }

  public WhosOnFirstModule(Bomb bomb) {
    super("Who's on First", bomb);
    initWhosOnFirst();
    initGUI();
  }

  private void initWhosOnFirst() {
    displayLabel = new Text((String)(DISPLAY_LABELS.keySet().toArray()[rand.nextInt(DISPLAY_LABELS.size())]));
    displayLabel.setFill(Color.WHITE);
    displayLabel.setFont(Font.font("Roboto Condensed", FontWeight.SEMI_BOLD, 20));
    int[] butons = Util.randomUniqueIndexes(Util.intAsArray(BUTTON_LABELS.length), 6);
    for (int i = 0; i < 6; i++) {
      wofs[i] = new WOF(BUTTON_LABELS[butons[i]], i);
    }
  }

  private void initGUI() {
    box = new AnchorPane();
    initSubPane(box);

    display = new StackPane();
    display.getChildren().addAll(new Rectangle(150, 65, Color.GRAY), new Rectangle(130, 45, Color.BLACK), new Rectangle(110, 35, Color.CADETBLUE), displayLabel);

    buttonBox = new TilePane();
    buttonBox.setPrefColumns(2);
    buttonBox.setPrefRows(3);
    buttonBox.setHgap(10);
    buttonBox.setVgap(10);
    buttonBox.getChildren().addAll(wofs);
    buttonBox.setMinSize(150, 135);
    buttonBox.setMaxSize(150, 135);
    buttonBox.setAlignment(Pos.CENTER);

    moduleBox = new StackPane();
    moduleBox.getChildren().addAll(new Rectangle(170, 155, Color.GRAY), new Rectangle(150, 135, Color.BLACK), buttonBox);

    for (int i = 0; i < 3; i++) {
      lights[i] = new Rectangle(25, 15, Color.TRANSPARENT);
      lights[i].setStroke(Color.GRAY);
    }

    lightBox = new VBox(25);
    lightBox.getChildren().addAll(lights);
    lightBox.setAlignment(Pos.CENTER);
    lightBox.setMinSize(35, 150);
    lightBox.setMaxSize(35, 150);
    lightBox.setBackground(Util.simpleBackground(Color.BLACK));

    AnchorPane.setLeftAnchor(display, 5.0);
    AnchorPane.setTopAnchor(display, 5.0);
    AnchorPane.setBottomAnchor(moduleBox, 5.0);
    AnchorPane.setLeftAnchor(moduleBox, 5.0);
    AnchorPane.setRightAnchor(lightBox, 5.0);
    AnchorPane.setBottomAnchor(lightBox, 10.0);

    box.getChildren().addAll(display, moduleBox, lightBox);
    this.getChildren().add(box);
  }

  private String getSolution() {
    String answer = "";
    String[] solution = BUTTON_LISTS.get(wofs[DISPLAY_LABELS.get(displayLabel.getText())].label);
    for (String word: solution) {
      if (answer.equals("")) {
        for (WOF buton: wofs) {
          if (buton.label == word) {
            answer = word;
            break;
          }
        }
      }
    }
    return answer;
  }

  private void updateGUI() {
    box.getChildren().clear();
    display.getChildren().clear();
    display.getChildren().addAll(new Rectangle(150, 65, Color.GRAY), new Rectangle(130, 45, Color.BLACK), new Rectangle(110, 35, Color.CADETBLUE), displayLabel);
    moduleBox.getChildren().clear();
    buttonBox.getChildren().clear();
    buttonBox.getChildren().addAll(wofs);
    moduleBox.getChildren().addAll(new Rectangle(170, 155, Color.GRAY), new Rectangle(150, 135, Color.BLACK), buttonBox);
    box.getChildren().addAll(display, moduleBox, lightBox);
  }

  public void play() {}

  public void pause() {}
  
}
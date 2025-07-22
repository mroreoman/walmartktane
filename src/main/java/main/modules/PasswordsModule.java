package main.modules;

import java.util.Random;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import main.Bomb;

public class PasswordsModule extends ModuleBase {
  private final static String[] passwords = {"about", "after", "again", "below", "could", "every", "first", "found", "great", "house", "large", "learn", "never", "other", "place", "plant", "point", "right", "small", "sound", "spell", "still", "study", "their", "there", "these", "thing", "think", "three", "water", "where", "which", "world", "would", "write"};
  private final static String alphabet = "abcdefghijklmnopqrstuvwxyz";

  private Character[][] slots = {new Character[6], new Character[6], new Character[6], new Character[6], new Character[6]};

  private AnchorPane box;
  private StackPane display;
  private HBox letters;
  private HBox topArrows;
  private HBox bottomArrows;
  private Pass[] upPasses;
  private Pass[] downPasses;
  private Word[] words;
  private String solution;

  private class Word extends StackPane {
    Character[] slot;
    int index;
    Text text;

    private Word(Character[] slot) {
      super();
      this.slot = slot;
      index = 0;
      text = new Text(this.slot[index].toString().toUpperCase());
      text.setFont(Font.font("Roboto", FontWeight.BOLD, 30));

      getChildren().addAll(new Rectangle(30, 50, Color.LIME), text);
    }

    private void down() {
      getChildren().clear();
      index = (index + 1) % 6;
      text.setText(slot[index].toString().toUpperCase());
      getChildren().addAll(new Rectangle(30, 50, Color.LIME), text);
    }

    private void up() {
      getChildren().clear();
      index = (index + 5) % 6;
      text.setText(slot[index].toString().toUpperCase());
      getChildren().addAll(new Rectangle(30, 50, Color.LIME), text);
    }
  }

  private class Pass extends Button {
    private Pass() {
      super();

      setGraphic(new Polygon(new double[] {
        0.0, 0.0,
        -2.5, 5.0,
        2.5, 5.0
      }));
      setStyle("-fx-background-color: darkkhaki; -fx-background-radius: 7.5em; -fx-min-width: 15px; -fx-min-height: 15px; -fx-max-width: 15px; -fx-max-height: 15px; -fx-border-radius:7.5em; -fx-border-color: darkgoldenrod");
    }
  }

  public PasswordsModule(Bomb bomb, Random rand) {
    super("Passwords", bomb);
    do {
      initPasswords(rand);
    } while (!checkSolvable());
    initGUI();
  }
  
  private void initPasswords(Random rand) {
    String word = passwords[rand.nextInt(passwords.length)];

    for (int i = 0; i < slots.length; i++) {
      String alphabet = PasswordsModule.alphabet;
      
      char ans = word.toCharArray()[i];
      slots[i][rand.nextInt(6)] = ans;
      int index = alphabet.indexOf(ans);
      alphabet = alphabet.substring(0, index) + alphabet.substring(index+1);
      
      for (int j = 0; j < slots[i].length; j++) {
        if (slots[i][j] == null) {
          int n = rand.nextInt(alphabet.length());
          slots[i][j] = alphabet.toCharArray()[n];
          alphabet = alphabet.substring(0, n) + alphabet.substring(n+1);
        }
      }
    }
  }

  private boolean checkSolvable() { //FIXME unoptimal as hell
    String solution = "";
    for (String password: passwords) {
      int stage = 0;
      for (int i = 0; i < slots[1].length; i++) {
        if (slots[stage][i] == (password.charAt(stage))) {
          i = -1;
          stage++;
        }
        if (stage == 5) {
          break;
        }
      }
      if (stage == 5) {
        if (solution.equals("")) {
          solution = password;
        } else {
          return false;
        }
      } else {
        continue;
      }
    }
    this.solution = solution;
    return true;
  }

  private void checkSolved() {
    String input = "";
    for (int i = 0; i < 5; i++) {
      input += words[i].slot[words[i].index];
    }

    if(isSolved()) {
      submitSolved(input.equals(solution));
    } else {
      submit(input.equals(solution));
    }
  }

  private void initGUI() {
    box = new AnchorPane();
    initSubPane(box);

    display = new StackPane();

    words = new Word[5];
    for (int i = 0; i < 5; i++) {
      words[i] = new Word(slots[i]);
    }

    letters = new HBox(3.0, words);
    letters.setMaxSize(170, 50);
    letters.setMinSize(170, 50);

    topArrows = new HBox(17.5);
    bottomArrows = new HBox(17.5);
    upPasses = new Pass[5];
    downPasses = new Pass[5];

    for (int i = 0; i < 5; i++) {
      final int j = i;
      upPasses[j] = new Pass();
      upPasses[j].setOnAction(event -> words[j].up());

      downPasses[j] = new Pass();
      downPasses[j].setOnAction(event -> words[j].down());
      downPasses[j].setRotate(180);
    }
    topArrows.getChildren().addAll(upPasses);
    bottomArrows.getChildren().addAll(downPasses);

    Button buton = new Button("SUBMIT");
    buton.setPadding(Insets.EMPTY);
    buton.setStyle("-fx-min-width: 65px; -fx-min-height: 20px; -fx-max-width: 65px; -fx-max-height: 20px; -fx-background-color: darkkhaki; -fx-font-family: 'Roboto Condensed'; -fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: black; -fx-border-color: darkgoldenrod;");
    buton.setOnAction(event -> checkSolved());

    display.getChildren().addAll(new Rectangle(200, 100, Color.DARKSLATEGREY), new Rectangle(175, 75, Color.GREENYELLOW));

    Rectangle leftPlate = new Rectangle(5, 75, Color.DARKGOLDENROD);
    Rectangle rightPlate = new Rectangle(10, 75, Color.DARKGOLDENROD);
    Rectangle leftWire = new Rectangle(7, 50, Color.BLACK);
    Rectangle rightWire = new Rectangle(10, 50, Color.BLACK);
  
    AnchorPane.setLeftAnchor(display, 12.0);
    AnchorPane.setTopAnchor(display, 75.0);
    AnchorPane.setLeftAnchor(topArrows, 37.5);
    AnchorPane.setTopAnchor(topArrows, 57.5);
    AnchorPane.setLeftAnchor(bottomArrows, 37.5);
    AnchorPane.setTopAnchor(bottomArrows, 177.5);
    AnchorPane.setLeftAnchor(letters, 30.0);
    AnchorPane.setTopAnchor(letters, 100.0);
    AnchorPane.setLeftAnchor(leftPlate, 7.5);
    AnchorPane.setTopAnchor(leftPlate, 87.5);
    AnchorPane.setRightAnchor(rightPlate, 10.0);
    AnchorPane.setTopAnchor(rightPlate, 87.5);
    AnchorPane.setLeftAnchor(leftWire, 0.0);
    AnchorPane.setTopAnchor(leftWire, 100.0);
    AnchorPane.setRightAnchor(rightWire, 0.0);
    AnchorPane.setTopAnchor(rightWire, 100.0);
    AnchorPane.setLeftAnchor(buton, 80.0);
    AnchorPane.setTopAnchor(buton, 200.0);
    
    box.getChildren().addAll(display, topArrows, bottomArrows, leftPlate, rightPlate, leftWire, rightWire, letters, buton);
    this.getChildren().add(box);
  }
  
  public void play() {
  }
  
  public void pause() {
  }

}
package main.modules;

import java.util.Random;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import main.Bomb;
import main.Util;

public class PasswordsModule extends ModuleBase {
  private static Random rand = new Random();
  private static String[] passwords = {"about", "after", "again", "below", "could", "every", "first", "found", "great", "house", "large", "learn", "never", "other", "place", "plant", "point", "right", "small", "sound", "spell", "still", "study", "their", "there", "these", "thing", "think", "three", "water", "where", "which", "world", "would", "write"};
  private static String alphabet = "abcdefghijklmnopqrstuvwxyz";

  private Character[][] slots = {new Character[6], new Character[6], new Character[6], new Character[6], new Character[6]};

  private AnchorPane box;
  private StackPane display;
  private HBox letters;
  private HBox topArrows;
  private HBox bottomArrows;
  private Word[] words;

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

  // private class Pass extends Button {
  //   private int index;

  //   private Pass(int index) {
  //     super();
  //     this.index = index;
  //     setStyle("-fx-background-color: darkkhaki; -fx-background-radius: 7.5em; -fx-min-width: 15px; -fx-min-height: 15px; -fx-max-width: 15px; -fx-max-height: 15px;");
  //   }
  // }

  public PasswordsModule(Bomb bomb) {
    super("Passwords", bomb);
    initPasswords();
    initGUI();
  }
  
  private void initPasswords() {
    String word = passwords[rand.nextInt(passwords.length)];
    for (int i = 0; i < slots.length; i++) {
      String alphabet = this.alphabet;
      
      Character ans = word.toCharArray()[i];
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
    if (!checkSolvable()) {
      initPasswords();
    }
  }

  private boolean checkSolvable() {
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
        if (solution == "") {
          solution = password;
        } else {
          return false;
        }
      } else {
        continue;
      }
    }
    return true;
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

    // Button buton = new Button();
    // buton.setOnAction(event -> passes[i])
    
    display.getChildren().addAll(new Rectangle(200, 100, Color.DARKSLATEGREY), new Rectangle(175, 75, Color.GREENYELLOW));

    Rectangle leftPlate = new Rectangle(5, 75, Color.DARKGOLDENROD);
    Rectangle rightPlate = new Rectangle(10, 75, Color.DARKGOLDENROD);
    Rectangle leftWire = new Rectangle(7, 50, Color.BLACK);
    Rectangle rightWire = new Rectangle(10, 50, Color.BLACK);

    AnchorPane.setLeftAnchor(display, 12.0);
    AnchorPane.setTopAnchor(display, 75.0);
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
    
    box.getChildren().addAll(display, leftPlate, rightPlate, leftWire, rightWire, letters);
    this.getChildren().add(box);
  }
  
  public void play() {
  }
  
  public void pause() {
  }

}
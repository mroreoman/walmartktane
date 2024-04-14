package main.modules;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import main.Bomb;
import main.Util;

public class KeypadsModule extends ModuleBase {
  private static final String[] COLUMN1 = {"modules/keypad_symbols/28-balloon.png", "modules/keypad_symbols/13-at.png", "modules/keypad_symbols/30-upsidedowny.png", "modules/keypad_symbols/12-squigglyn.png", "modules/keypad_symbols/7-squidknife.png", "modules/keypad_symbols/9-hookn.png", "modules/keypad_symbols/23-leftc.png"};
  private static final String[] COLUMN2 = {"modules/keypad_symbols/16-euro.png", "modules/keypad_symbols/28-balloon.png", "modules/keypad_symbols/23-leftc.png", "modules/keypad_symbols/26-cursive.png", "modules/keypad_symbols/3-hollowstar.png", "modules/keypad_symbols/9-hookn.png", "modules/keypad_symbols/20-questionmark.png"};
  private static final String[] COLUMN3 = {"modules/keypad_symbols/1-copyright.png", "modules/keypad_symbols/8-pumpkin.png", "modules/keypad_symbols/26-cursive.png", "modules/keypad_symbols/5-doublek.png", "modules/keypad_symbols/15-meltedthree.png", "modules/keypad_symbols/30-upsidedowny.png", "modules/keypad_symbols/3-hollowstar.png"};
  private static final String[] COLUMN4 = {"modules/keypad_symbols/11-six.png", "modules/keypad_symbols/21-paragraph.png", "modules/keypad_symbols/31-bt.png", "modules/keypad_symbols/7-squidknife.png", "modules/keypad_symbols/5-doublek.png", "modules/keypad_symbols/20-questionmark.png", "modules/keypad_symbols/4-smileyface.png"};
  private static final String[] COLUMN5 = {"modules/keypad_symbols/24-pitchfork.png", "modules/keypad_symbols/4-smileyface.png", "modules/keypad_symbols/31-bt.png", "modules/keypad_symbols/22-rightc.png", "modules/keypad_symbols/21-paragraph.png", "modules/keypad_symbols/19-dragon.png", "modules/keypad_symbols/2-filledstar.png"};
  private static final String[] COLUMN6 = {"modules/keypad_symbols/11-six.png", "modules/keypad_symbols/16-euro.png", "modules/keypad_symbols/27-tracks.png", "modules/keypad_symbols/14-ae.png", "modules/keypad_symbols/24-pitchfork.png", "modules/keypad_symbols/18-nwithhat.png", "modules/keypad_symbols/6-omega.png"};
  private static final String[][] SYMBOLS = {COLUMN1, COLUMN2, COLUMN3, COLUMN4, COLUMN5, COLUMN6};
  
  private static final Random rand = new Random();
  
  private Keypad[] keypads = new Keypad[4];
  private int column;
  private int press = 0;

  private class Keypad {
    private int pressNum;
    private Button buton;
    private String path;
    private Rectangle light;
    private boolean isPressed = false;
    private Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> light.setFill(Color.TRANSPARENT)));

    private Keypad(int column, int index) {
      path = SYMBOLS[column][index];
      ImageView img = new ImageView(new Image(path));
      img.setFitHeight(60);
      img.setFitWidth(60);
      light = new Rectangle(30, 5, Color.TRANSPARENT);
      light.setStroke(Color.BLACK);
      VBox vbox = new VBox(light, img);
      vbox.setAlignment(Pos.CENTER);
      buton = new Button("", vbox);
      buton.setMinSize(75, 75);
      buton.setMaxSize(75, 75);
      buton.setOnAction(event -> press());
    }

    private void press() {
      if (!isPressed) {
        if (press == pressNum) {
          isPressed = true;
          light.setFill(Color.LIME);
          timeline.pause();
          if (press == 3) {
            submit(true);
          }
          press++;
        } else {
          submit(false);
          light.setFill(Color.RED);
          timeline.playFromStart();
        }
      } else {
        System.out.println("Keypad already pressed.");
      }
    }
  }

  public KeypadsModule(Bomb bomb) {
    super("Keypads", bomb);
    initKeypads();
    initGUI();
  }

  private void initKeypads() {
    column = rand.nextInt(6);
    int[] indexes = Util.randomUniqueIndexes(Util.intAsArray(7), 4);
    for (int i = 0; i < 4; i++) {
      keypads[i] = new Keypad(column, indexes[i]);
    }
    setSolution();
  }

  private void setSolution() {
    int i = 0;
    for (String path: SYMBOLS[column]) {
      for (Keypad keypad: keypads) {
        if (keypad.path.equals(path)) {
          keypad.pressNum = i++;
        }
      }
    }
  }

  private void initGUI() {
    AnchorPane box = new AnchorPane();
    initSubPane(box);
    
    box.setLeftAnchor(keypads[0].buton, MARGIN);
    box.setBottomAnchor(keypads[0].buton, MARGIN + 80);
    box.setLeftAnchor(keypads[1].buton, MARGIN + 80);
    box.setBottomAnchor(keypads[1].buton, MARGIN + 80);
    box.setLeftAnchor(keypads[2].buton, MARGIN);
    box.setBottomAnchor(keypads[2].buton, MARGIN);
    box.setLeftAnchor(keypads[3].buton, MARGIN + 80);
    box.setBottomAnchor(keypads[3].buton, MARGIN);

    for (Keypad keypad : keypads){
      box.getChildren().add(keypad.buton);
    }
    this.getChildren().add(box);
  }

  public void disable(){
    for (Keypad keypad: keypads){
      keypad.buton.setDisable(true);
    }
  }

  public void play() {}

  public void pause() {}
}
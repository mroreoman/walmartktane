package main.modules;

import java.util.HashMap;
import java.util.Map;
import static java.util.Map.entry;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import main.Bomb;

public class MorseCodeModule extends ModuleBase {
  private static final String[] words = {"shell", "halls", "slick", "trick", "boxes", "leaks", "strobe", "bistro", "flick", "bombs", "break", "brick", "steak", "sting", "vector", "beats"};
  private static final double[] frequencies = {3.505, 3.515, 3.522, 3.532, 3.535, 3.542, 3.545, 3.552, 3.555, 3.565, 3.572, 3.575, 3.582, 3.592, 3.595, 3.600};

  private final String word;
  private final double frequency;

  private AnchorPane box;
  private Polygon leftWire;
  private Polygon leftCap;
  private Rectangle leftHolder;
  private Light light;
  private Rectangle rightHolder;
  private Polygon rightCap;
  private Rectangle rightWire;
  private AnchorPane moduleBox;
  private Button submit;
  private StackPane screen;
  private Text frqText;
  private int frqNum = 0;

  private Rectangle dial;
  private Rectangle pointer;

  private class Light extends Rectangle {

    private Timeline dotTimeline = new Timeline(new KeyFrame(Duration.seconds(1.0/4), e -> setEffect(null)));
    private Timeline dashTimeline = new Timeline(new KeyFrame(Duration.seconds(1.0), e -> setEffect(null)));
    private String morseWord = "";
    private Timeline flasher = new Timeline();
    private int ticks = 0;

    private HashMap<Character, String> morse = new HashMap<>(Map.ofEntries(
      entry('a', ".- "),
      entry('b', "-... "),
      entry('c', "-.-. "),
      entry('d', "-.. "),
      entry('e', ". "),
      entry('f', "..-. "),
      entry('g', "--. "),
      entry('h', ".... "),
      entry('i', ".. "),
      entry('j', ".--- "),
      entry('k', "-.- "),
      entry('l', ".-.. "),
      entry('m', "-- "),
      entry('n', "-. "),
      entry('o', "--- "),
      entry('p', ".--. "),
      entry('q', "--.- "),
      entry('r', ".-. "),
      entry('s', "... "),
      entry('t', "- "),
      entry('u', "..- "),
      entry('v', "...- "),
      entry('w', ".-- "),
      entry('x', "-..- "),
      entry('y', "-.-- "),
      entry('z', "--.. ")
    ));

    private Light() {
      super(50, 30, Color.web("0x808000"));
      for (char character: word.toCharArray()) {
        morseWord += morse.get(character);
      }
      for (char character: morseWord.toCharArray()) {
        if (character == ' ') {
          ticks += 3;
      } else if (character == '.') {
          flasher.getKeyFrames().add(new KeyFrame(Duration.seconds((16.0 + ticks) / 4), e -> dot()));
          ticks += 2;
        } else if (character == '-') {
          flasher.getKeyFrames().add(new KeyFrame(Duration.seconds((16.0 + ticks) / 4), e -> dash()));
          ticks += 5;
        }
      }
      flasher.setCycleCount(Timeline.INDEFINITE);
    }

    private void dot() {
      setEffect(new Glow());
      dotTimeline.playFromStart();
    }

    private void dash() {
      setEffect(new Glow());
      dashTimeline.playFromStart();
    }
  }

  public MorseCodeModule(Bomb bomb, Random rand) {
    super("Morse Code", bomb);
    int index = rand.nextInt(words.length);
    word = words[index];
    frequency = frequencies[index];
    initGUI();
  }

  private void initGUI() {
    box = new AnchorPane();
    initSubPane(box);

    leftWire = new Polygon();
    leftWire.getPoints().addAll(new Double[] {
      0.0, 0.0,
      20.0, 20.0,
      20.0, 30.0,
      0.0, 10.0
    });
    leftWire.setFill(Color.web("#78470C"));

    leftCap = new Polygon();
    leftCap.getPoints().addAll(new Double[] {
      0.0, 0.0,
      -15.0, 10.0 / 3,
      -15.0, 20.0 / 3,
      -20.0, 10.0,
      -20.0, 20.0,
      -15.0, 70.0 / 3,
      -15.0, 80.0 / 3,
      0.0, 30.0
    });

    leftHolder = new Rectangle(10, 40, Color.GRAY);

    light = new Light();

    rightHolder = new Rectangle(10, 40, Color.GRAY);

    rightCap = new Polygon();
    rightCap.getPoints().addAll(new Double[] {
      0.0, 0.0,
      15.0, 10.0 / 3,
      17.5, 20.0 / 3,
      22.5, 20.0 / 3,
      25.0, 10.0,
      25.0, 20.0,
      22.5, 70.0 / 3,
      17.5, 70.0 / 3,
      15.0, 80.0 / 3,
      0.0, 30.0
    });

    rightWire = new Rectangle(65, 10, Color.web("#78470C"));

    dial = new Rectangle(170, 15, Color.WHITE);
    pointer = new Rectangle(3, 20, Color.RED);

    HBox screenBox = new HBox(10);
    Button leftArrow = new Button();
    leftArrow.setShape(new Polygon(new double[] {
      0.0, 0.0,
      20.0, -20.0,
      20.0, 20.0
    }));
    leftArrow.setMinSize(20, 40);
    leftArrow.setMaxSize(20, 40);
    leftArrow.setStyle("-fx-background-color: black;");
    leftArrow.setOnAction(event -> {
      if (frqNum > 0) {
        frqText.setText(String.format("%.3f", frequencies[--frqNum]) + " MHz");
        Timeline animation = new Timeline(new KeyFrame(Duration.seconds(1.0/3), new KeyValue(pointer.translateXProperty(), 10 * (frqNum))));
        animation.playFromStart();
      }
    });
    screen = new StackPane();
    frqText = new Text("3.505 MHz");
    frqText.setFill(Color.ORANGE);
    frqText.setFont(Font.font("Roboto", 20));
    screen.getChildren().addAll(new Rectangle(125, 40, Color.BLACK), frqText);
    Button rightArrow = new Button();
    rightArrow.setShape(new Polygon(new double[] {
      0.0, 0.0,
      -20.0, -20.0,
      -20.0, 20.0
    }));
    rightArrow.setMinSize(20, 40);
    rightArrow.setMaxSize(20, 40);
    rightArrow.setStyle("-fx-background-color: black;");
    rightArrow.setOnAction(event -> {
      if (frqNum < frequencies.length - 1) {
        frqText.setText(String.format("%.3f", frequencies[++frqNum]) + " MHz");
        Timeline animation = new Timeline(new KeyFrame(Duration.seconds(1.0/3), new KeyValue(pointer.translateXProperty(), 10 * (frqNum))));
        animation.playFromStart();
      }
    });
    screenBox.getChildren().addAll(leftArrow, screen, rightArrow);

    submit = new Button("TX");
    submit.setStyle("-fx-min-width: 60px; -fx-min-height: 30px; -fx-max-width: 60px; -fx-max-height: 30px; -fx-background-color: darkkhaki; -fx-font-family: 'Roboto Condensed'; -fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: black");
    submit.setPadding(Insets.EMPTY);
    submit.setOnAction(event -> {
      if (isSolved()) {
        submitSolved(frqText.getText().equals(String.format("%.3f", frequency) + " MHz"));
      } else {
        submit(frqText.getText().equals(String.format("%.3f", frequency) + " MHz"));
      }
    });

    AnchorPane.setTopAnchor(dial, 10.0);
    AnchorPane.setLeftAnchor(dial, 15.0);
    AnchorPane.setTopAnchor(pointer, 7.5);
    AnchorPane.setLeftAnchor(pointer, 25.0);
    AnchorPane.setBottomAnchor(screenBox, 15.0);
    AnchorPane.setLeftAnchor(screenBox, 7.5);

    moduleBox = new AnchorPane();
    moduleBox.setMinSize(200, 100);
    moduleBox.setMaxSize(200, 100);
    moduleBox.getChildren().addAll(new Rectangle(200, 100, Color.GRAY), dial, pointer, screenBox);

    AnchorPane.setLeftAnchor(leftWire, 0.0);
    AnchorPane.setTopAnchor(leftWire, 0.0);
    AnchorPane.setLeftAnchor(leftCap, 20.0);
    AnchorPane.setTopAnchor(leftCap, 10.0);
    AnchorPane.setLeftAnchor(leftHolder, 40.0);
    AnchorPane.setTopAnchor(leftHolder, 5.0);
    AnchorPane.setLeftAnchor(light, 50.0);
    AnchorPane.setTopAnchor(light, 10.0);
    AnchorPane.setLeftAnchor(rightHolder, 100.0);
    AnchorPane.setTopAnchor(rightHolder, 5.0);
    AnchorPane.setLeftAnchor(rightCap, 110.0);
    AnchorPane.setTopAnchor(rightCap, 10.0);
    AnchorPane.setLeftAnchor(rightWire, 135.0);
    AnchorPane.setTopAnchor(rightWire, 20.0);
    AnchorPane.setLeftAnchor(moduleBox, 16.5);
    AnchorPane.setBottomAnchor(moduleBox, 55.0);
    AnchorPane.setLeftAnchor(submit, 86.5);
    AnchorPane.setBottomAnchor(submit, 15.0);

    box.getChildren().addAll(leftWire, leftCap, leftHolder, light, rightHolder, rightCap, rightWire, submit, moduleBox);
    this.getChildren().add(box);
  }

  public void play() {
    light.flasher.playFromStart();
  }

  public void pause() {
    light.flasher.stop();
  }

}
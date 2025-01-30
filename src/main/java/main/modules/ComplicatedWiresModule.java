package main.modules;

import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import main.Bomb;

public class ComplicatedWiresModule extends ModuleBase {

  private static Random rand = new Random();
  private static final Color[] COLORS = {Color.WHITE, Color.RED, Color.BLUE};
  private final ComplicatedWire[] wires = new ComplicatedWire[6];
  private final boolean[] stars = new boolean[6];
  private final boolean[] lights = new boolean[6];

  private class ComplicatedWire extends Button {

    private final Color[] color;
    private boolean isCorrect = false;
    private boolean isCut = false;

    private ComplicatedWire(Image image, Color[] color) {
      super();
      this.color = color;
      setClip(new ImageView(image));
      if (color.length == 1) {
        setEffect(new ColorInput(0, 0, image.getWidth(), image.getHeight(), color[0]));
      } else if (color[1] == COLORS[1]) {
        ImageView stripe = new ImageView(new Image(ComplicatedWiresModule.class.getResourceAsStream("complicated_wire_drawings/stripe-red-white.png")));
        stripe.setViewport(new Rectangle2D(125.0, 125.0, 150.0, 150.0));
        setGraphic(stripe);
      } else if (color[0] == COLORS[1]) {
        ImageView stripe = new ImageView(new Image(ComplicatedWiresModule.class.getResourceAsStream("complicated_wire_drawings/stripe-red-blue.png")));
        stripe.setViewport(new Rectangle2D(125.0, 125.0, 150.0, 150.0));
        setGraphic(stripe);
      } else {
        ImageView stripe = new ImageView(new Image(ComplicatedWiresModule.class.getResourceAsStream("complicated_wire_drawings/stripe-blue-white.png")));
        stripe.setViewport(new Rectangle2D(125.0, 125.0, 150.0, 150.0));
        setGraphic(stripe);
      }
      setPadding(Insets.EMPTY);
      setOnAction(event -> cut());
    }

    private void cut() {
      if (isCut) {
        System.out.println("Wire already cut.");
      } else {
        isCut = true;
        setDisable(true);
        checkSolved(isCorrect);
      }
    }
  }

  public ComplicatedWiresModule(Bomb bomb) {
    this(bomb, rand.nextInt(3) + 4);
  }

  public ComplicatedWiresModule(Bomb bomb, int numWires) {
    super("Complicated Wires", bomb);
    initComplicatedWires(numWires);
    initGUI();
  }

  private void initComplicatedWires(int numWires) {
    ArrayList<Integer> positions = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5));
    int emptyWires = wires.length - numWires;
    while (emptyWires-- > 0) {
      positions.remove(rand.nextInt(positions.size()));
    }
    for (int i = 0; i < wires.length; i++) {
      if (positions.contains(i)) {
        int color = rand.nextInt(6);
        if (color == 0) {
          wires[i] = new ComplicatedWire(imageFromPosition(i), new Color[] {COLORS[0]});
        } else if (color == 1) {
          wires[i] = new ComplicatedWire(imageFromPosition(i), new Color[] {COLORS[1]});
        } else if (color == 2) {
          wires[i] = new ComplicatedWire(imageFromPosition(i), new Color[] {COLORS[2]});
        } else if (color == 3) {
          wires[i] = new ComplicatedWire(imageFromPosition(i), new Color[] {COLORS[0], COLORS[1]});
        } else if (color == 4) {
          wires[i] = new ComplicatedWire(imageFromPosition(i), new Color[] {COLORS[0], COLORS[2]});
        } else {
          wires[i] = new ComplicatedWire(imageFromPosition(i), new Color[] {COLORS[1], COLORS[2]});
        }
        stars[i] = rand.nextBoolean();
        lights[i] = rand.nextBoolean();
      }
    }
    setSolution();
  }

  private void setSolution() {
    for (int i = 0; i < 6; i++) {
      if (wires[i] != null) {
        if (Arrays.equals(wires[i].color, new Color[] {COLORS[0]})) {
          if (lights[i]) {
            if (stars[i]) {
              if (getEdgework().numBatteries() >= 2) {
                setCorrect(wires[i]);
              }
            }
          } else if (stars[i]) {
            setCorrect(wires[i]);
          } else {
            setCorrect(wires[i]);
          }
        } else if (Arrays.equals(wires[i].color, new Color[] {COLORS[1]}) || Arrays.equals(wires[i].color, new Color[] {COLORS[0], COLORS[1]})) {
          if (lights[i]) {
            if (getEdgework().numBatteries() >= 2) {
              setCorrect(wires[i]);
            }
          } else if(stars[i]) {
            setCorrect(wires[i]);
          } else {
            if (getEdgework().lastSerialDigit() % 2 == 0) {
              setCorrect(wires[i]);
            }
          }
        } else if (Arrays.equals(wires[i].color, new Color[] {COLORS[2]}) || Arrays.equals(wires[i].color, new Color[] {COLORS[0], COLORS[2]})) {
          if (lights[i]) {
            if (getEdgework().hasPort("PAR")) {
              setCorrect(wires[i]);
            }
          } else if (!stars[i]) {
            if (getEdgework().lastSerialDigit() % 2 == 0) {
              setCorrect(wires[i]);
            }
          }
        } else if (Arrays.equals(wires[i].color, new Color[] {COLORS[1], COLORS[2]})) {
          if (lights[i]) {
            if (!stars[i]) {
              if (getEdgework().lastSerialDigit() % 2 == 0) {
                setCorrect(wires[i]);
              }
            }
          } else if (stars[i]) {
            if (getEdgework().hasPort("PAR")) {
              setCorrect(wires[i]);
            }
          } else {
            if (getEdgework().lastSerialDigit() % 2 == 0) {
              setCorrect(wires[i]);
            }
          }
        }
      }
    }
    if (!checkSolvable()) {
      initComplicatedWires(rand.nextInt(3) + 4);
    }
  }

  private void setCorrect(ComplicatedWire wire) {
    wire.isCorrect = true;
  }

  private boolean checkSolvable() {
    boolean temp = false;
    for (int i = 0; i < 6; i++) {
      if (wires[i] != null) {
        temp |= wires[i].isCorrect;
      }
    }
    return temp;
  }

  private void checkSolved(boolean solve) {
    boolean temp = true;
    if (solve) {
      for (int i = 0; i < 6; i++) {
        if (wires[i] != null) {
          if (wires[i].isCorrect) {
            temp &= wires[i].isCut;
          }
        }
      }
      if (temp) {
      submit(true);
    }
    } else {
      submit(false);
    }
  }

  private void initGUI() {
    AnchorPane box = new AnchorPane();
    initSubPane(box);

    Rectangle top = new Rectangle(150, 25, Color.GRAY);
    Circle[] leds = new Circle[6];
    for (int i = 0; i < 6; i++) {
      leds[i] = new Circle(6.25);
      leds[i].setStroke(Color.BLACK);
      leds[i].setStrokeType(StrokeType.INSIDE);
      if (lights[i]) {
        leds[i].setFill(Color.WHITE);
      } else {
        leds[i].setFill(Color.TRANSPARENT);
      }
    }
    HBox lightBox = new HBox(11.5);
    lightBox.getChildren().addAll(leds);
    lightBox.setMinSize(150, 25);
    lightBox.setMaxSize(150, 25);
    lightBox.setAlignment(Pos.CENTER);
  
    StackPane topBox = new StackPane(top, lightBox);
    AnchorPane.setTopAnchor(topBox, 25.0);
    AnchorPane.setLeftAnchor(topBox, 15.0);
    box.getChildren().add(topBox);

    Rectangle[] topHoles = new Rectangle[6];
    for (int i = 0; i < 6; i ++) {
      topHoles[i] = new Rectangle(25, 10, Color.BLACK);
      topHoles[i].setStroke(Color.GRAY);
      topHoles[i].setStrokeType(StrokeType.INSIDE);
    }
    HBox topHoleHbox = new HBox(-1);
    topHoleHbox.getChildren().addAll(topHoles);
    AnchorPane.setTopAnchor(topHoleHbox, 50.0);
    AnchorPane.setLeftAnchor(topHoleHbox, 15.0);
    box.getChildren().add(topHoleHbox);
    
    Rectangle[] bottomHoles = new Rectangle[6];
    for (int i = 0; i < 6; i++) {
      bottomHoles[i] = new Rectangle(30, 10, Color.BLACK);
      bottomHoles[i].setStroke(Color.GRAY);
      bottomHoles[i].setStrokeType(StrokeType.INSIDE);
    }
    
    HBox bottomHoleHbox = new HBox(-1);
    bottomHoleHbox.getChildren().addAll(bottomHoles);
    AnchorPane.setBottomAnchor(bottomHoleHbox, 35.0);
    AnchorPane.setLeftAnchor(bottomHoleHbox, 15.0);
    box.getChildren().add(bottomHoleHbox);

    Rectangle bottom = new Rectangle(180, 30, Color.GRAY);
    StackPane[] starBoxes = new StackPane[6];
    for (int i = 0; i < 6; i++) {
      starBoxes[i] = new StackPane();
      starBoxes[i].getChildren().add(new Rectangle(25, 25, Color.BURLYWOOD));
      if (stars[i]) {
        starBoxes[i].getChildren().add(new ImageView(new Image(ComplicatedWiresModule.class.getResourceAsStream("complicated_wire_drawings/compstar" + (i + 1) + ".png"))));
      }
      starBoxes[i].setMinSize(25, 25);
      starBoxes[i].setMaxSize(25, 25);
    }

    HBox starBox = new HBox(4);
    starBox.getChildren().addAll(starBoxes);
    starBox.setMinSize(180, 30);
    starBox.setMaxSize(180, 30);
    starBox.setAlignment(Pos.CENTER);

    StackPane bottomBox = new StackPane();
    bottomBox.getChildren().addAll(bottom, starBox);
    AnchorPane.setBottomAnchor(bottomBox, 5.0);
    AnchorPane.setLeftAnchor(bottomBox, 15.0);
    box.getChildren().add(bottomBox);

    for (int i = 0; i < wires.length; i++) {
      if (wires[i] != null) {
        AnchorPane.setLeftAnchor(wires[i], 15.0 + 25*i);
        AnchorPane.setTopAnchor(wires[i], 50.0);
        box.getChildren().add(wires[i]);
      }
    }
    
    getChildren().add(box);
  }

  private double placeWire(int i){
    if (i == 0) {
      return 24;
    } else if (i == 1) {
      return 60;
    } else if (i == 2) {
      return 85;
    } else if (i == 3) {
      return 115;
    } else if (i == 4) {
      return 144;
    } else {
      return 185;
    }
  }

  private Image imageFromPosition(int position) {
    return new Image(ComplicatedWiresModule.class.getResourceAsStream("complicated_wire_drawings/compwire" + (position + 1) + ".png"));
  }

  public void play() {}

  public void pause() {}
}
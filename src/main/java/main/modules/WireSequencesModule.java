package main.modules;

import java.util.Random;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import main.Bomb;
import main.Util;

public class WireSequencesModule extends ModuleBase {
  private static final Random rand = new Random();
  private final Color[] COLORS = {Color.RED, Color.BLUE, Color.BLACK};

  private int redCount = 0;
  private int blueCount = 0;
  private int blackCount = 0;

  private String[] redOccurences = {"C", "B", "A", "AC", "B", "AC", "ABC", "AB", "B", ""};
  private String[] blueOccurences = {"B", "AC", "B", "A", "B", "BC", "C", "AC", "A", ""};
  private String[] blackOccurences = {"ABC", "AC", "B", "AC", "B", "BC", "AB", "C", "C", ""};
  private final String[] CONNECTIONS = {"A","B","C"};

  private Panel[] panels = new Panel[4];
  private int panelNum = 0;

  private AnchorPane box;
  private VBox lightBox;
  private Rectangle[] lights = new Rectangle[4];
  private Button up;
  private Button down;

  private class Panel extends AnchorPane {
    private WireSequence[] wires;
    private boolean isSolved = false;

    private Panel(int numWires) {
      wires = new WireSequence[numWires];
      int[] positions = new int[numWires];
      positions = Util.goodUniqueIndexes(3, numWires);
      int j = 0;
      for (int i = 0; i < 3; i++) {
        for (int position: positions) {
          if (i == position) {
            wires[j++] = new WireSequence(COLORS[rand.nextInt(3)], i + 1, CONNECTIONS[rand.nextInt(3)]);
            if (redCount == 10) {
              redCount--;
              wires[--j] = new WireSequence(COLORS[rand.nextInt(3)], i+1, CONNECTIONS[rand.nextInt(3)]);
              j++;
            } else if (blueCount == 10) {
              blueCount--;
              wires[--j] = new WireSequence(COLORS[rand.nextInt(3)], i+1, CONNECTIONS[rand.nextInt(3)]);
              j++;
            } else if (blackCount == 10) {
              blackCount--;
              wires[--j] = new WireSequence(COLORS[rand.nextInt(3)], i+1, CONNECTIONS[rand.nextInt(3)]);
              j++;
            }
          } 
        }
      }
    }

    private void submitPanel() {
      for (WireSequence wire : wires) {
        if (wire.isCorrect) {
          if (!wire.isCut) {
            submit(false);
          }
        }
      }
      isSolved = true;
      if (panelNum == 3) {
        submit(true);
        close();
      } else {
        panelNum++;
        updateGUI();
      }
    }
  }
  
  private class WireSequence extends Button {
    private final Color color;
    private final int position;
    private final String connection;
    private boolean isCorrect = false;
    private boolean isCut = false;

    private WireSequence(Color color, int position, String connection) {
      this(color, position, connection, imageFromPosition(position, connection));
    }

    private WireSequence(Color color, int position, String connection, Image image) {
      super("", new ImageView(image));
      this.color = color;
      this.position = position;
      this.connection = connection;
      setClip(new ImageView(image));
      setSolution();
      setEffect(new ColorInput(0, 0, image.getWidth(), image.getHeight(), color));
      setOnAction(event -> cut());
    }

    private void setSolution() {
      if (color == COLORS[0]) {
        if (redOccurences[redCount++].indexOf(connection) != -1) {
          isCorrect = true;
        }
      } else if (color == COLORS[1]) {
        if (blueOccurences[blueCount++].indexOf(connection) != -1) {
          isCorrect = true;
        }
      } else {
        if (blackOccurences[blackCount++].indexOf(connection) != -1) {
          isCorrect = true;
        }
      }
    }

    private void cut() {
      if (isCut) {
        System.out.println("Wire already cut.");
      } else {
        isCut = true;
        setDisable(true);
        if (!isCorrect) {
          submit(false);
        }
      }
    }
  }

  public WireSequencesModule(Bomb bomb) {
    super("Wire Sequences", bomb);
    initWireSequences();
    initGUI();
  }

  private void initWireSequences() {
    for (int i = 0; i < 4; i++) {
      panels[i] = new Panel(rand.nextInt(3) + 1);
    }
  }

  private boolean checkSolved() {
    boolean temp = true;
    for (Panel panel: panels) {
      for (WireSequence wire: panel.wires) {
        if (wire.isCorrect) {
          temp &= wire.isCut;
        }
      }
    }
    if (temp) {
      return true;
    } else {
      return false;
    }
  }

  private void initGUI() {
    box = new AnchorPane();
    initSubPane(box);

    for (int i = 0; i < 4; i++) {
      lights[i] = new Rectangle(25, 15, Color.TRANSPARENT);
      lights[i].setStroke(Color.GRAY);
    }

    lightBox = new VBox(10);
    lightBox.getChildren().addAll(lights);
    lightBox.setAlignment(Pos.CENTER);
    lightBox.setMinSize(35, 150);
    lightBox.setMaxSize(35, 150);
    lightBox.setBackground(Util.simpleBackground(Color.BLACK));

    up = new Button();
    up.setShape(new Rectangle(40, 20));
    up.setMinSize(40, 20);
    up.setMaxSize(40, 20);
    StackPane upPane = new StackPane();
    Polygon upangle = new Polygon();
    upangle.getPoints().addAll(new Double[] {
      0.0, 0.0,
      -7.5, 10.0,
      7.5, 10.0
    });
    upangle.setFill(Color.SADDLEBROWN);
    upPane.getChildren().add(upangle);
    up.setGraphic(upPane);
    up.setStyle("-fx-background-color: darkkhaki; -fx-border-color: darkgoldenrod");
    up.setOnAction(event -> {
      if (panelNum != 0) {
        panelNum--;
        updateGUI();
      }
    });

    down = new Button();
    down.setShape(new Rectangle(40,20));
    down.setMinSize(40,20);
    down.setMaxSize(40,20);
    StackPane downPane = new StackPane();
    Polygon downangle = new Polygon();
    downangle.getPoints().addAll(new Double[] {
      0.0, 0.0,
      -7.5, -10.0,
      7.5, -10.0
    });
    downangle.setFill(Color.SADDLEBROWN);
    downPane.getChildren().add(downangle);
    down.setGraphic(downPane);
    down.setStyle("-fx-background-color: darkkhaki; -fx-border-color: darkgoldenrod");
    down.setOnAction(event -> {
      if (panels[panelNum].isSolved) {
        panelNum++;
        updateGUI();
      } else {
        panels[panelNum].submitPanel();
      }});

    for (int i = 0; i < 4; i++) {
      panels[i].setMinSize(150, 150);
      panels[i].setMaxSize(150,150);
      Rectangle pBackground1 = new Rectangle(50, 149, Color.BLANCHEDALMOND);
      Rectangle pBackground2 = new Rectangle(49, 149, Color.BURLYWOOD);
      Rectangle pBackground3 = new Rectangle(50, 149, Color.BLANCHEDALMOND);

      VBox leftVbox = new VBox(20);
      leftVbox.setAlignment(Pos.CENTER);
      for (int j = 0; j < 3; j++) {
        Rectangle square = new Rectangle(20, 20, Color.TRANSPARENT);
        square.setStroke(Color.BLACK);
        leftVbox.getChildren().add(square);
      }

      VBox numbers = new VBox(30);
      numbers.setAlignment(Pos.CENTER);
      for (int j = 0; j < 3; j++) {
        Text num = new Text(String.valueOf(i * 3 + j + 1));
        num.setFont(Font.font("Roboto", FontWeight.BOLD, 10));
        numbers.getChildren().add(num);
      }

      HBox leftHbox = new HBox(2.5, numbers, leftVbox);

      VBox rightVbox = new VBox(20);
      rightVbox.setAlignment(Pos.CENTER);
      for (int j = 0; j < 3; j++) {
        Rectangle square = new Rectangle(20, 20, Color.TRANSPARENT);
        square.setStroke(Color.BLACK);
        rightVbox.getChildren().add(square);
      }

      VBox letters = new VBox(30);
      letters.setAlignment(Pos.CENTER);
      for (int j = 0; j < 3; j++) {
        Text let = new Text(CONNECTIONS[j]);
        let.setFont(Font.font("Roboto", FontWeight.BOLD, 10));
        letters.getChildren().add(let);
      }

      HBox rightHbox = new HBox (2.5, rightVbox, letters);

      AnchorPane.setLeftAnchor(pBackground1, 0.0);
      AnchorPane.setTopAnchor(pBackground1, 0.0);
      AnchorPane.setLeftAnchor(pBackground2, 50.0);
      AnchorPane.setTopAnchor(pBackground2, 0.0);
      AnchorPane.setLeftAnchor(pBackground3, 99.0);
      AnchorPane.setTopAnchor(pBackground3, 0.0);
      AnchorPane.setRightAnchor(leftHbox, 107.5);
      AnchorPane.setTopAnchor(leftHbox, 22.5);
      AnchorPane.setLeftAnchor(rightHbox, 107.5);
      AnchorPane.setTopAnchor(rightHbox, 22.5);
      panels[i].getChildren().addAll(pBackground1, pBackground2, pBackground3, leftHbox, rightHbox);
      panels[i].setStyle("-fx-border-color: black;");

      for (int j = 0; j < panels[i].wires.length; j++) {
        placeWire(panels[i].wires[j]);
        panels[i].getChildren().add(panels[i].wires[j]);
      }
    } 

    AnchorPane.setRightAnchor(lightBox, 5.0);
    AnchorPane.setBottomAnchor(lightBox, 10.0);
    AnchorPane.setLeftAnchor(up, 75.0);
    AnchorPane.setTopAnchor(up, 10.0);
    AnchorPane.setLeftAnchor(down, 75.0);
    AnchorPane.setBottomAnchor(down, 10.0);

    for (int i = 0; i < 4; i++) {
      AnchorPane.setLeftAnchor(panels[i], 21.5);
      AnchorPane.setTopAnchor(panels[i], 41.5);
    }

    box.getChildren().addAll(lightBox, up, down, panels[0]);
    this.getChildren().add(box);
  }

  private void updateGUI() {
    box.getChildren().clear();
    box.getChildren().addAll(lightBox, up, down, panels[panelNum]);
  }

  private void close() {
    up.setDisable(true);
    down.setDisable(true);
    box.getChildren().clear();
    Rectangle close = new Rectangle (150, 150, Color.GREY);
    close.setStroke(Color.BLACK);
    AnchorPane.setLeftAnchor(close, 21.5);
    AnchorPane.setTopAnchor(close, 41.5);
    box.getChildren().addAll(lightBox, up, down, close);
  }
  
  private Image imageFromPosition(int position, String connection) {
    return new Image(WireSequencesModule.class.getResourceAsStream("wire_sequences_drawings/wireseq" + position + connection + ".png"));
  }

  private void placeWire(WireSequence wire) {
    if (wire.position == 1) {
      if (wire.connection.equals("A")) {
        AnchorPane.setLeftAnchor(wire, 17.5);
        AnchorPane.setTopAnchor(wire, 20.0);
      } else if (wire.connection.equals("B")) {
        AnchorPane.setLeftAnchor(wire, 25.0);
        AnchorPane.setTopAnchor(wire, 27.5);
      } else {
        AnchorPane.setLeftAnchor(wire, 15.0);
        AnchorPane.setTopAnchor(wire, 25.0);
      }
    } else if (wire.position == 2) {
      if (wire.connection.equals("A")) {
        AnchorPane.setLeftAnchor(wire, 27.5);
        AnchorPane.setTopAnchor(wire, 25.0);
      } else if (wire.connection.equals("B")) {
        AnchorPane.setLeftAnchor(wire, 15.0);
        AnchorPane.setTopAnchor(wire, 60.0);
      } else {
        AnchorPane.setLeftAnchor(wire, 25.0);
        AnchorPane.setTopAnchor(wire, 70.0);
      }
    } else {
      if (wire.connection.equals("A")) {
        AnchorPane.setLeftAnchor(wire, 20.0);
        AnchorPane.setTopAnchor(wire, 25.0);
      } else if (wire.connection.equals("B")) {
        AnchorPane.setLeftAnchor(wire, 27.5);
        AnchorPane.setTopAnchor(wire, 65.0);
      } else {
        AnchorPane.setLeftAnchor(wire, 20.0);
        AnchorPane.setTopAnchor(wire, 100.0);
      }
    }
  }

  public void play() {}

  public void pause() {}
}
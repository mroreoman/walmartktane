package com.example.modules;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
// import javafx.animation.Animation;
// import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import com.example.Bomb;
import com.example.Util;

public class MazesModule extends ModuleBase {
  private static final Random rand = new Random();

  String[][] maze1 = {{"dr","lr","ld","dr","lr","l"}, {"ud*","dr","ul","ur","lr","ld"}, {"ud","ur","ld","dr","lr","uld*"}, {"ud","r","ulr","ul","r","uld"}, {"udr","lr","ld","dr","r","ud"}, {"ur", "l", "ur", "ul", "r", "ul"}};
  String[][] maze2 = {{"r","ldr","l","dr","ldr","l"}, {"dr","ul","dr","ul","ur*","ld"}, {"ud","dr","ul","dr","lr","uld"}, {"udr","ul*","dr","ul","d","ud"}, {"ud","d","ud","dr","ul","ud"}, {"u","ur","ul","ur","lr","ul"}};
  String[][] maze3 = {{"dr","lr","ld","d","dr","ld"}, {"u","d","ud","ur","ul","ud"}, {"dr","uld","ud","dr","ld","ud"}, {"ud","ud","ud","ud*","ud","ud*"}, {"ud","ur","ul","ud","ud","ud"}, {"ur","lr","lr","ul","ur","ul"}};
  String[][] maze4 = {{"dr*","ld","r","lr","lr","ld"}, {"ud","ud","dr","lr","lr","uld"}, {"ud","ur","ul","dr","lr","ud"}, {"ud*","r","lr","ulr","lr","uld"}, {"udr","lr","lr","lr","ld","ud"}, {"ur","lr","l","r","ul","u"}};
  String[][] maze5 = {{"r","lr","lr","lr","ldr","ld"}, {"dr","lr","lr","ldr","ul","u"}, {"udr","ld","r","ul","dr*","ld"}, {"ud","ur","lr","ld","u","ud"}, {"ud","dr","lr","ulr","l","ud"}, {"u","ur","lr","lr*", "lr","ul"}};
  String[][] maze6 = {{"d","dr","ld","r","ldr*","ld"}, {"ud","ud","ud","dr","ul","ud"}, {"udr","ul","u","ud","dr","ul"}, {"ur","ld","dr","uld","ud","d"}, {"dr","ul","u*","ud","ur","uld"}, {"ur","lr","lr","ul","r","ul"}};
  String[][] maze7 = {{"dr","lr*","lr","ld","dr","ld"}, {"ud","dr","l","ur","ul","ud"}, {"ur","ul","dr","l","dr","ul"}, {"dr","ld","udr","lr","ul","d"}, {"ud","u","ur","lr","ld","ud"}, {"ur","lr*","lr","lr","ulr","ul"}};
  String[][] maze8 = {{"d","dr","lr","ld*","dr","ld"}, {"udr","ulr","l","ur","ul","ud"}, {"ud","dr","lr","lr","ld","ud"}, {"ud","ur","ld*","r","ulr","ul"}, {"ud","d","ur","lr","lr","l"}, {"ur","ulr","lr","lr","lr","l"}};
  String[][] maze9 = {{"d","dr","lr","lr","ldr","ld"}, {"ud","ud","dr*","l","ud","ud"}, {"udr","ulr","ul","dr","ul","ud"}, {"ud","d","dr","ul","r","uld"}, {"ud*","ud","ud","dr","ld","u"}, {"ur","ul","ur","ul","ur","l"}};

  private final String[][][] MAZES = {maze1, maze2, maze3, maze4, maze5, maze6, maze7, maze8, maze9};

  private final String[] COLUMNS = {"a","b","c","d","e","f"};

  private int maze;
  private String coordinate = "";
  private String finish = "";
  private Cell[] cells = new Cell[36];

  private AnchorPane box;
  private AnchorPane moduleBox;
  private VBox[] columns =  new VBox[6];
  private Arrow up;
  private Arrow left;
  private Arrow down;
  private Arrow right;
  private Timeline spinner;
  // private RotateTransition spinner;
  
  private class Cell extends StackPane {

    private String coordinate;
    private Rectangle rectangle;
    private boolean isCursor;
    private boolean isCircle = false;
    private boolean isFinish = false;

    public Cell(String coordinate) {
      super();
      this.coordinate = coordinate;

      setMinSize(22.5,22.5);
      setMaxSize(22.5,22.5);
      
      rectangle = new Rectangle(7.5, 7.5, Color.CADETBLUE);

      if (MAZES[maze][(Integer.valueOf(coordinate.substring(1)) - 1)][coordinate.substring(0,1).compareTo("a")].indexOf("*") != -1) {
        Circle circle = new Circle(10, Color.TRANSPARENT);
        circle.setStroke(Color.LIGHTGREEN);
        circle.setStrokeWidth(3.0);

        getChildren().add(circle);
      }
      getChildren().add(rectangle);
    }
  }

  private class Arrow extends Button {

    String direction;

    public Arrow(String direction) {
      super();
      this.direction = direction;
      setOnAction(event -> move());
      setShape(new Polygon(new double[] {
        0.0, 0.0,
        -15.0, 15.0,
        15.0, 15.0
      }));
      setMinSize(30,15);
      setMaxSize(30,15);
      setStyle("-fx-background-color: darkslateblue;");
    }

    private void move() {
      if (MAZES[maze][getRow() - 1][getColumn().compareTo("a")].indexOf(direction) != -1) {
        if (direction.equals("u")) {
          up();
        } else if (direction.equals("l")) {
          left();
        } else if (direction.equals("d")) {
          down();
        } else {
          right();
        }
        checkSolved();
      } else {
        submit(false);
      }
    }

  }

  public MazesModule(Bomb bomb) {
    super("Mazes", bomb);
    initMazes();
    initGUI();
  }

  private void initMazes() {
    maze = rand.nextInt(9);
    coordinate = COLUMNS[rand.nextInt(6)] + String.valueOf(rand.nextInt(6) + 1);;
    finish = coordinate;
    while (finish.equals(coordinate)) {
      finish = COLUMNS[rand.nextInt(6)] + String.valueOf(rand.nextInt(6) + 1);
    }
    for (int i = 0; i < 36; i++) {
      cells[i] = new Cell(COLUMNS[i / 6] + String.valueOf(i % 6 + 1));
    }
    setCursor(getCell(coordinate));
    setFinish(finish);
  }

  private void initGUI() {
    box = new AnchorPane();
    initSubPane(box);

    moduleBox = new AnchorPane();
    moduleBox.setMinSize(150, 150);
    moduleBox.setMaxSize(150, 150);
    moduleBox.setStyle("-fx-background-color: midnightblue; -fx-border-color: black; -fx-border-width: 3");

    for (int i = 0; i < 6; i++) {
      columns[i] = new VBox(0);
      for (int j = 0; j < 6; j++) {
        columns[i].getChildren().add(cells[i * 6 + j]);
      }
      AnchorPane.setLeftAnchor(columns[i], 22.5 * i + 7.5);
      AnchorPane.setTopAnchor(columns[i], 7.5);
      moduleBox.getChildren().add(columns[i]);
    }

    up = new Arrow("u");
    
    left = new Arrow("l");
    left.setRotate(270.0);
    
    down = new Arrow("d");
    down.setRotate(180.0);
    
    right = new Arrow("r");
    right.setRotate(90.0);

    AnchorPane.setLeftAnchor(moduleBox, 41.5);
    AnchorPane.setTopAnchor(moduleBox, 41.5);
    AnchorPane.setLeftAnchor(up, 104.0);
    AnchorPane.setTopAnchor(up, 20.0);
    AnchorPane.setLeftAnchor(left, 10.0);
    AnchorPane.setTopAnchor(left, 104.0);
    AnchorPane.setLeftAnchor(down, 104.0);
    AnchorPane.setBottomAnchor(down, 20.0);
    AnchorPane.setRightAnchor(right, 10.0);
    AnchorPane.setTopAnchor(right, 104.0);
    
    box.getChildren().addAll(moduleBox, up, left, down, right);
    this.getChildren().add(box);
  }

  private void up() {
    Cell cell = getCell(coordinate);
    cell.isCursor = false;
    cell.rectangle.setFill(Color.CADETBLUE);
    setCursor(getCell(cell.coordinate.substring(0,1) + String.valueOf(Integer.valueOf(cell.coordinate.substring(1,2)) - 1)));
  }

  private void left() {
    Cell cell = getCell(coordinate);
    cell.isCursor = false;
    cell.rectangle.setFill(Color.CADETBLUE);
    setCursor(getCell(String.valueOf((char)(cell.coordinate.charAt(0) - 1)) + cell.coordinate.substring(1,2)));
  }

  private void down() {
    Cell cell = getCell(coordinate);
    cell.isCursor = false;
    cell.rectangle.setFill(Color.CADETBLUE);
    setCursor(getCell(cell.coordinate.substring(0,1) + String.valueOf(Integer.valueOf(cell.coordinate.substring(1,2)) + 1)));
  }

  private void right() {
    Cell cell = getCell(coordinate);
    cell.isCursor = false;
    cell.rectangle.setFill(Color.CADETBLUE);
    setCursor(getCell(String.valueOf((char)(cell.coordinate.charAt(0) + 1)) + cell.coordinate.substring(1,2)));
  }

  private void setCursor(Cell cell) {
    cell.isCursor = true;
    cell.rectangle.setFill(Color.WHITE);
    coordinate = cell.coordinate;
  }

  private void setFinish(String coordinate) {
    Cell cell = getCell(coordinate);
    cell.isFinish = true;
    Polygon tringle = new Polygon(new double[] {
      0.0, 0.0,
      -7.5, 12.5,
      7.5, 12.5
    });
    tringle.setFill(Color.RED);

    // spinner = new RotateTransition(Duration.millis(10000), tringle);
    // spinner.setByAngle(360);
    // spinner.setCycleCount(Animation.INDEFINITE);
    // spinner.play();
    spinner = new Timeline();
    for (int i = 0; i < 360; i++) {
      int temp = i;
      spinner.getKeyFrames().add(new KeyFrame(Duration.seconds(10.0/360 * temp), event -> tringle.setRotate(temp)));
    }
    // spinner = new Timeline(new KeyFrame(Duration.seconds(10.0/360), event -> {
    //   tringle.setRotate(counter++);
    //   counter %= 360;
    // }));
    spinner.setCycleCount(Timeline.INDEFINITE);
    spinner.play();
    cell.getChildren().remove(cell.rectangle);
    cell.getChildren().add(tringle);
    
  }

  private int getRow() {
    return Integer.valueOf(coordinate.substring(1,2));
  }

  private String getColumn() {
    return coordinate.substring(0,1);
  }

  private Cell getCell(String coordinate) {
    for (Cell cell: cells) {
      if (cell.coordinate.equals(coordinate)) {
        return cell;
      }
    }
    System.out.println("Cant find cell.");
    return new Cell(null);
  }

  private void checkSolved() {
    if (coordinate.equals(finish)) {
      submit(true);
    }
  }
  
  public void play() {}

  public void pause() {}
}


package main.modules; // possibly deactivate buttons after module solved

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import main.Bomb;

public class MazesModule extends ModuleBase {
  private static final Random rand = new Random();

  private final String[][] maze1 = {{"dr","lr","ld","dr","lr","l"}, {"ud*","dr","ul","ur","lr","ld"}, {"ud","ur","ld","dr","lr","uld*"}, {"ud","r","ulr","ul","r","uld"}, {"udr","lr","ld","dr","r","ud"}, {"ur", "l", "ur", "ul", "r", "ul"}};
  private final String[][] maze2 = {{"r","ldr","l","dr","ldr","l"}, {"dr","ul","dr","ul","ur*","ld"}, {"ud","dr","ul","dr","lr","uld"}, {"udr","ul*","dr","ul","d","ud"}, {"ud","d","ud","dr","ul","ud"}, {"u","ur","ul","ur","lr","ul"}};
  private final String[][] maze3 = {{"dr","lr","ld","d","dr","ld"}, {"u","d","ud","ur","ul","ud"}, {"dr","uld","ud","dr","ld","ud"}, {"ud","ud","ud","ud*","ud","ud*"}, {"ud","ur","ul","ud","ud","ud"}, {"ur","lr","lr","ul","ur","ul"}};
  private final String[][] maze4 = {{"dr*","ld","r","lr","lr","ld"}, {"ud","ud","dr","lr","lr","uld"}, {"ud","ur","ul","dr","lr","ud"}, {"ud*","r","lr","ulr","lr","uld"}, {"udr","lr","lr","lr","ld","ud"}, {"ur","lr","l","r","ul","u"}};
  private final String[][] maze5 = {{"r","lr","lr","lr","ldr","ld"}, {"dr","lr","lr","ldr","ul","u"}, {"udr","ld","r","ul","dr*","ld"}, {"ud","ur","lr","ld","u","ud"}, {"ud","dr","lr","ulr","l","ud"}, {"u","ur","lr","lr*", "lr","ul"}};
  private final String[][] maze6 = {{"d","dr","ld","r","ldr*","ld"}, {"ud","ud","ud","dr","ul","ud"}, {"udr","ul","u","ud","dr","ul"}, {"ur","ld","dr","uld","ud","d"}, {"dr","ul","u*","ud","ur","uld"}, {"ur","lr","lr","ul","r","ul"}};
  private final String[][] maze7 = {{"dr","lr*","lr","ld","dr","ld"}, {"ud","dr","l","ur","ul","ud"}, {"ur","ul","dr","l","dr","ul"}, {"dr","ld","udr","lr","ul","d"}, {"ud","u","ur","lr","ld","ud"}, {"ur","lr*","lr","lr","ulr","ul"}};
  private final String[][] maze8 = {{"d","dr","lr","ld*","dr","ld"}, {"udr","ulr","l","ur","ul","ud"}, {"ud","dr","lr","lr","ld","ud"}, {"ud","ur","ld*","r","ulr","ul"}, {"ud","d","ur","lr","lr","l"}, {"ur","ulr","lr","lr","lr","l"}};
  private final String[][] maze9 = {{"d","dr","lr","lr","ldr","ld"}, {"ud","ud","dr*","l","ud","ud"}, {"udr","ulr","ul","dr","ul","ud"}, {"ud","d","dr","ul","r","uld"}, {"ud*","ud","ud","dr","ld","u"}, {"ur","ul","ur","ul","ur","l"}};

  private final String[][][] MAZES = {maze1, maze2, maze3, maze4, maze5, maze6, maze7, maze8, maze9};

  private final Cell[][] cells = new Cell[6][6];
  private final int mazeIndex;
  private final Cell finish;
  private Timeline spinner;

  private Cell current;
  private int curCol;
  private int curRow;
  
  private abstract class Cell extends StackPane {
    private final String directions;

    public Cell(int col, int row) {
      super();
      this.directions = MAZES[mazeIndex][col][row];

      setMinSize(22.5,22.5);
      setMaxSize(22.5,22.5);

      if (directions.contains("*")) {
        Circle circle = new Circle(10, Color.TRANSPARENT);
        circle.setStroke(Color.LIGHTGREEN);
        circle.setStrokeWidth(3.0);
        getChildren().add(circle);
      }
    }

    public boolean canGo(String direction) {
      return directions.contains(direction);
    }

    public abstract void select();

    public abstract void deselect();
  }

  private class NormalCell extends Cell {
    private final Rectangle rectangle;

    public NormalCell(int col, int row) {
      super(col, row);
      rectangle = new Rectangle(7.5, 7.5, Color.CADETBLUE);
      getChildren().add(rectangle);
    }

    public void select() {
      rectangle.setFill(Color.WHITE);
    }

    public void deselect() {
      rectangle.setFill(Color.CADETBLUE);
    }
  }

  private class FinishCell extends Cell {
    public FinishCell(int col, int row) {
      super(col, row);

      final double height = Math.sqrt(Math.pow(15,2)-Math.pow(7.5,2));
      Polygon tringle = new Polygon(
        0.0, 0.0,
        -7.5, height,
        7.5, height
      );
      tringle.setFill(Color.RED);
      Rotate center = new Rotate(0, 0, height*2/3);
      tringle.setTranslateX(1);
      tringle.setTranslateY(-1*(height*2/3-6.5));
      tringle.getTransforms().add(center);
      spinner = new Timeline();
      for (int i = 0; i < 360; i++) {
        final int j = i;
        spinner.getKeyFrames().add(new KeyFrame(Duration.seconds(10.0/360 * j), event -> center.setAngle(j)));
      }
      spinner.setCycleCount(Timeline.INDEFINITE);
      getChildren().add(tringle);
    }

    public void select() {}

    public void deselect() {}
  }

  private class Arrow extends Button {
    String direction;

    public Arrow(String direction) {
      super();
      this.direction = direction;
      setOnAction(event -> move());
      setShape(new Polygon(
        0.0, 0.0,
        -15.0, 15.0,
        15.0, 15.0
      ));
      setMinSize(30,15);
      setMaxSize(30,15);
      setStyle("-fx-background-color: darkslateblue;");
    }

    private void move() {
      if (current.canGo(direction)) {
        switch (direction) {
          case "u" -> up();
          case "l" -> left();
          case "d" -> down();
          case "r" -> right();
        }
        checkSolved();
      } else {
        submit(false);
      }
    }
  }

  public MazesModule(Bomb bomb) {
    super("Mazes", bomb);

    mazeIndex = rand.nextInt(9);

    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 6; j++) {
        cells[i][j] = new NormalCell(i, j);
      }
    }

    setCursor(rand.nextInt(6), rand.nextInt(6));

    // pick random finCol, finRow that is not the same as curCol, curRow
    int finCol = rand.nextInt(6);
    int finRow;
    if (finCol == curCol) {
      finRow = rand.nextInt(5);
      if (finRow >= curRow) {
        finRow++;
      }
    } else {
      finRow = rand.nextInt(6);
    }

    cells[finCol][finRow] = new FinishCell(finCol, finRow);
    finish = cells[finCol][finRow];

    initGUI();
  }

  private void initGUI() {
    AnchorPane box = new AnchorPane();
    initSubPane(box);

    AnchorPane moduleBox = new AnchorPane();
    moduleBox.setMinSize(150, 150);
    moduleBox.setMaxSize(150, 150);
    moduleBox.setStyle("-fx-background-color: midnightblue; -fx-border-color: black; -fx-border-width: 3");

    VBox[] columns =  new VBox[6];
    for (int i = 0; i < 6; i++) {
      columns[i] = new VBox(0);
      for (int j = 0; j < 6; j++) {
        columns[i].getChildren().add(cells[j][i]);
      }
      AnchorPane.setLeftAnchor(columns[i], 22.5 * i + 7.5);
      AnchorPane.setTopAnchor(columns[i], 7.5);
      moduleBox.getChildren().add(columns[i]);
    }

    Arrow up = new Arrow("u");
    
    Arrow left = new Arrow("l");
    left.setRotate(270.0);
    
    Arrow down = new Arrow("d");
    down.setRotate(180.0);
    
    Arrow right = new Arrow("r");
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
    current.deselect();
    setCursor(curCol - 1, curRow);
  }

  private void left() {
    current.deselect();
    setCursor(curCol, curRow - 1);
  }

  private void down() {
    current.deselect();
    setCursor(curCol + 1, curRow);
  }

  private void right() {
    current.deselect();
    setCursor(curCol, curRow + 1);
  }

  private void setCursor(int col, int row) {
    curCol = col;
    curRow = row;
    current = cells[col][row];
    current.select();
  }

  private void checkSolved() {
    if (current == finish) {
      submit(true);
    }
  }
  
  public void play() {
    spinner.play();
  }

  public void pause() {
    spinner.pause();
  }
}


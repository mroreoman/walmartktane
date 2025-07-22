package com.github.mroreoman.game.modules;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import com.github.mroreoman.game.Bomb;

public class MazesModule extends ModuleBase {
    private static final double CELL_SIZE = 22.5;
    private static final double SQUARE_SIZE = 7;
    private static final double TRIANGLE_HEIGHT = Math.sqrt(Math.pow(15, 2) - Math.pow(7.5, 2));
    private static final double ARROW_SIZE = 30;

    private static final String[][] maze1 = {{"dr", "lr", "ld", "dr", "lr", "l"}, {"ud*", "dr", "ul", "ur", "lr", "ld"}, {"ud", "ur", "ld", "dr", "lr", "uld*"}, {"ud", "r", "ulr", "ul", "r", "uld"}, {"udr", "lr", "ld", "dr", "r", "ud"}, {"ur", "l", "ur", "ul", "r", "ul"}};
    private static final String[][] maze2 = {{"r", "ldr", "l", "dr", "ldr", "l"}, {"dr", "ul", "dr", "ul", "ur*", "ld"}, {"ud", "dr", "ul", "dr", "lr", "uld"}, {"udr", "ul*", "dr", "ul", "d", "ud"}, {"ud", "d", "ud", "dr", "ul", "ud"}, {"u", "ur", "ul", "ur", "lr", "ul"}};
    private static final String[][] maze3 = {{"dr", "lr", "ld", "d", "dr", "ld"}, {"u", "d", "ud", "ur", "ul", "ud"}, {"dr", "uld", "ud", "dr", "ld", "ud"}, {"ud", "ud", "ud", "ud*", "ud", "ud*"}, {"ud", "ur", "ul", "ud", "ud", "ud"}, {"ur", "lr", "lr", "ul", "ur", "ul"}};
    private static final String[][] maze4 = {{"dr*", "ld", "r", "lr", "lr", "ld"}, {"ud", "ud", "dr", "lr", "lr", "uld"}, {"ud", "ur", "ul", "dr", "lr", "ud"}, {"ud*", "r", "lr", "ulr", "lr", "uld"}, {"udr", "lr", "lr", "lr", "ld", "ud"}, {"ur", "lr", "l", "r", "ul", "u"}};
    private static final String[][] maze5 = {{"r", "lr", "lr", "lr", "ldr", "ld"}, {"dr", "lr", "lr", "ldr", "ul", "u"}, {"udr", "ld", "r", "ul", "dr*", "ld"}, {"ud", "ur", "lr", "ld", "u", "ud"}, {"ud", "dr", "lr", "ulr", "l", "ud"}, {"u", "ur", "lr", "lr*", "lr", "ul"}};
    private static final String[][] maze6 = {{"d", "dr", "ld", "r", "ldr*", "ld"}, {"ud", "ud", "ud", "dr", "ul", "ud"}, {"udr", "ul", "u", "ud", "dr", "ul"}, {"ur", "ld", "dr", "uld", "ud", "d"}, {"dr", "ul", "u*", "ud", "ur", "uld"}, {"ur", "lr", "lr", "ul", "r", "ul"}};
    private static final String[][] maze7 = {{"dr", "lr*", "lr", "ld", "dr", "ld"}, {"ud", "dr", "l", "ur", "ul", "ud"}, {"ur", "ul", "dr", "l", "dr", "ul"}, {"dr", "ld", "udr", "lr", "ul", "d"}, {"ud", "u", "ur", "lr", "ld", "ud"}, {"ur", "lr*", "lr", "lr", "ulr", "ul"}};
    private static final String[][] maze8 = {{"d", "dr", "lr", "ld*", "dr", "ld"}, {"udr", "ulr", "l", "ur", "ul", "ud"}, {"ud", "dr", "lr", "lr", "ld", "ud"}, {"ud", "ur", "ld*", "r", "ulr", "ul"}, {"ud", "d", "ur", "lr", "lr", "l"}, {"ur", "ulr", "lr", "lr", "lr", "l"}};
    private static final String[][] maze9 = {{"d", "dr", "lr", "lr", "ldr", "ld"}, {"ud", "ud", "dr*", "l", "ud", "ud"}, {"udr", "ulr", "ul", "dr", "ul", "ud"}, {"ud", "d", "dr", "ul", "r", "uld"}, {"ud*", "ud", "ud", "dr", "ld", "u"}, {"ur", "ul", "ur", "ul", "ur", "l"}};
    private static final String[][][] MAZES = {maze1, maze2, maze3, maze4, maze5, maze6, maze7, maze8, maze9};

    private final Cell[][] cells = new Cell[6][6];
    private final int mazeIndex;
    private final Cell finish;
    private Cell current;

    private abstract class Cell extends StackPane {
        private final String directions;
        private final int col;
        private final int row;

        public Cell(int col, int row) {
            super();
            this.directions = MAZES[mazeIndex][col][row];
            this.col = col;
            this.row = row;

            setMinSize(CELL_SIZE, CELL_SIZE);
            setMaxSize(CELL_SIZE, CELL_SIZE);

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
            rectangle = new Rectangle(SQUARE_SIZE, SQUARE_SIZE, Color.CADETBLUE);
            StackPane.setAlignment(rectangle, Pos.CENTER);
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

            Polygon tringle = new Polygon(
                    0.0, 0.0,
                    -7.5, TRIANGLE_HEIGHT,
                    7.5, TRIANGLE_HEIGHT
            );
            tringle.setFill(Color.RED);
            StackPane.setAlignment(tringle, Pos.CENTER);
            StackPane.setMargin(tringle, new Insets(0, 0, TRIANGLE_HEIGHT / 6 + 0.7, 0));
            getChildren().add(tringle);

            Timeline spinner = new Timeline();
            for (int i = 0; i < 360; i++) {
                final int j = i;
                spinner.getKeyFrames().add(new KeyFrame(Duration.seconds(10.0 / 360 * j), event -> setRotate(j)));
            }
            spinner.setCycleCount(Timeline.INDEFINITE);
            spinner.play();
        }

        public void select() {
        }

        public void deselect() {
        }

    }

    private class Arrow extends Button { //FIXME arrows squish when clicked
        String direction;

        public Arrow(String direction) {
            super();
            this.direction = direction;
            setOnAction(event -> move());
            setShape(new Polygon(
                    0.0, -7.5,
                    -15.0, 7.5,
                    15.0, 7.5
            ));
            setMinSize(30, 15);
            setMaxSize(30, 15);
            setStyle("-fx-background-color: darkslateblue;");
        }

        private void move() {
            if (current == finish) return;

            int targetCol = current.col;
            int targetRow = current.row;
            switch (direction) {
                case "u" -> targetCol--;
                case "l" -> targetRow--;
                case "d" -> targetCol++;
                case "r" -> targetRow++;
            }

            if (current.canGo(direction)) {
                current.deselect();
                moveCursorTo(targetCol, targetRow);
                checkSolved();
            } else if (targetCol >= 0 && targetCol < 6 && targetRow >= 0 && targetRow < 6) {
                submit(false);
            }
        }

    }

    public MazesModule(Bomb bomb, Random rand) {
        super("Mazes", bomb);
        mazeIndex = rand.nextInt(9);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                cells[i][j] = new NormalCell(i, j);
            }
        }
        int finCol = rand.nextInt(6);
        int finRow = rand.nextInt(6);
        cells[finCol][finRow] = new FinishCell(finCol, finRow);
        finish = cells[finCol][finRow];

        // pick random curCol, curRow that is not the same as finCol, finRow
        int curCol = rand.nextInt(6);
        int curRow;
        if (curCol == finCol) {
            curRow = rand.nextInt(5);
            if (curRow >= finRow) {
                curRow++;
            }
        } else {
            curRow = rand.nextInt(6);
        }
        moveCursorTo(curCol, curRow);

        initGUI();
    }

    private void initGUI() {
        GridPane mazeBox = new GridPane();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                GridPane.setConstraints(cells[i][j], j, i);
                mazeBox.getChildren().add(cells[i][j]);
            }
        }
        mazeBox.setStyle("-fx-background-color: midnightblue; -fx-border-color: black; -fx-border-width: 3");
        mazeBox.setMinSize(CELL_SIZE * 6 + 10, CELL_SIZE * 6 + 10); //add double the border width to the width of the cells
        mazeBox.setMaxSize(CELL_SIZE * 6 + 10, CELL_SIZE * 6 + 10);
        StackPane.setAlignment(mazeBox, Pos.CENTER);

        Arrow up = new Arrow("u");
        StackPane upBox = new StackPane(up);
        upBox.setMinSize(ARROW_SIZE, ARROW_SIZE);
        upBox.setMaxSize(ARROW_SIZE, ARROW_SIZE);
        StackPane.setAlignment(upBox, Pos.TOP_CENTER);

        Arrow left = new Arrow("l");
        left.setRotate(270.0);
        StackPane leftBox = new StackPane(left);
        leftBox.setMinSize(ARROW_SIZE, ARROW_SIZE);
        leftBox.setMaxSize(ARROW_SIZE, ARROW_SIZE);
        StackPane.setAlignment(leftBox, Pos.CENTER_LEFT);

        Arrow down = new Arrow("d");
        down.setRotate(180.0);
        StackPane downBox = new StackPane(down);
        downBox.setMinSize(ARROW_SIZE, ARROW_SIZE);
        downBox.setMaxSize(ARROW_SIZE, ARROW_SIZE);
        StackPane.setAlignment(downBox, Pos.BOTTOM_CENTER);

        Arrow right = new Arrow("r");
        right.setRotate(90.0);
        StackPane rightBox = new StackPane(right);
        rightBox.setMinSize(ARROW_SIZE, ARROW_SIZE);
        rightBox.setMaxSize(ARROW_SIZE, ARROW_SIZE);
        StackPane.setAlignment(rightBox, Pos.CENTER_RIGHT);

        StackPane box = new StackPane(mazeBox, upBox, leftBox, downBox, rightBox);
        double SIZE = mazeBox.getMinWidth() + 60;
        box.setMinSize(SIZE, SIZE);
        box.setMaxSize(SIZE, SIZE);
        StackPane root = new StackPane(box);
        initSubPane(root);
        this.getChildren().addAll(root);
    }

    private void moveCursorTo(int col, int row) {
        current = cells[col][row];
        current.select();
    }

    private void checkSolved() {
        if (current == finish) {
            submit(true);
        }
    }

    public void play() {}

    public void pause() {}

}


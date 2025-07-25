package com.github.mroreoman.game.modules;

import java.util.Random;
import java.util.ArrayList;

import java.lang.RuntimeException;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import com.github.mroreoman.Util;
import com.github.mroreoman.game.Bomb;

public class WiresModule extends ModuleBase {
    public static final String NAME = "Wires";
    private static final Color[] COLORS = {Color.RED, Color.WHITE, Color.BLUE, Color.YELLOW, Color.BLACK};

    private final Wire[] wires = new Wire[6];

    private class Wire extends Button {
        private final Color color;
        private boolean isCorrect = false;
        private boolean isCut = false;

        public Wire(Image image, Color color) {
            super("", new ImageView(image));
            this.color = color;
            setClip(new ImageView(image));
            setEffect(new ColorInput(0, 0, image.getWidth(), image.getHeight(), color));
            setOnAction(event -> cut());
        }

        private void cut() {
            if (isCut) {
                System.out.println("Wire already cut.");
            } else {
                isCut = true;
                setDisable(true);
                submit(isCorrect);
            }
        }

        private void setCorrect() {
            isCorrect = true;
        }

        public String toString() {
            return color.toString() + (isCut ? " - Cut" : "") + (isCorrect ? " - Correct " : "");
        }

    }

    public WiresModule(Bomb bomb, Random rand) {
        super(NAME, bomb);
        initWires(rand);
        setSolution();
        initGUI();
    }

    private void initWires(Random rand) {
        int numWires = rand.nextInt(4) + 3;
        int[] positions = Util.goodUniqueIndexes(rand, wires.length, numWires);
        for (int position : positions) {
            wires[position] = new Wire(imageFromPosition(position), COLORS[rand.nextInt(COLORS.length)]);
        }
    }

    private void setSolution() {
        //make list of wires without nulls
        ArrayList<Wire> wires = new ArrayList<>();
        for (Wire wire : this.wires) {
            if (wire != null) {
                wires.add(wire);
            }
        }

        //count each color
        int redWires = 0;
        int whiteWires = 0;
        int blueWires = 0;
        int yellowWires = 0;
        int blackWires = 0;

        for (Wire wire : wires) {
            if (wire.color == Color.RED) {
                redWires++;
            } else if (wire.color == Color.WHITE) {
                whiteWires++;
            } else if (wire.color == Color.BLUE) {
                blueWires++;
            } else if (wire.color == Color.YELLOW) {
                yellowWires++;
            } else if (wire.color == Color.BLACK) {
                blackWires++;
            }
        }

        //logic
        if (wires.size() == 3) {
            if (redWires == 0) {
                wires.get(1).setCorrect();
            } else if (wires.get(2).color == Color.WHITE) {
                wires.get(2).setCorrect();
            } else if (blueWires > 1) {
                lastWire(Color.BLUE).setCorrect();
            } else {
                wires.get(2).setCorrect();
            }
        } else if (wires.size() == 4) {
            if (redWires > 1 && getEdgework().lastSerialDigit() % 2 == 1) {
                lastWire(Color.RED).setCorrect();
            } else if (wires.get(3).color == Color.YELLOW && redWires == 0) {
                wires.get(0).setCorrect();
            } else if (blueWires == 1) {
                wires.get(0).setCorrect();
            } else if (yellowWires > 1) {
                wires.get(3).setCorrect();
            } else {
                wires.get(1).setCorrect();
            }
        } else if (wires.size() == 5) {
            if (wires.get(4).color == Color.BLACK && getEdgework().lastSerialDigit() % 2 == 1) {
                wires.get(3).setCorrect();
            } else if (redWires == 1 && yellowWires > 1) {
                wires.get(0).setCorrect();
            } else if (blackWires == 0) {
                wires.get(1).setCorrect();
            } else {
                wires.get(0).setCorrect();
            }
        } else if (wires.size() == 6) {
            if (yellowWires == 0 && getEdgework().lastSerialDigit() % 2 == 1) {
                wires.get(2).setCorrect();
            } else if (yellowWires == 1 && whiteWires > 1) {
                wires.get(3).setCorrect();
            } else if (redWires == 0) {
                wires.get(5).setCorrect();
            } else {
                wires.get(3).setCorrect();
            }
        } else {
            throw new RuntimeException("Invalid number of wires in Wires module (must be 3-6).");
        }
    }

    private void initGUI() {
        AnchorPane box = new AnchorPane();
        initSubPane(box);

        Rectangle left = new Rectangle(25, 210, Color.TRANSPARENT);
        left.setStroke(Color.BLACK);
        VBox leftVbox = new VBox(15);
        leftVbox.setAlignment(Pos.CENTER);
        for (int i = 0; i < wires.length; i++) {
            Rectangle square = new Rectangle(15, 15, Color.TRANSPARENT);
            square.setStroke(Color.BLACK);
            leftVbox.getChildren().add(square);
        }
        StackPane leftBox = new StackPane(left, leftVbox);
        AnchorPane.setLeftAnchor(leftBox, 19.0);
        AnchorPane.setBottomAnchor(leftBox, 10.0);
        box.getChildren().add(leftBox);

        Rectangle right = new Rectangle(25, 160, Color.TRANSPARENT);
        right.setStroke(Color.BLACK);
        VBox rightVbox = new VBox(8);
        rightVbox.setAlignment(Pos.CENTER);
        for (int i = 0; i < wires.length; i++) {
            Rectangle square = new Rectangle(15, 15, Color.TRANSPARENT);
            square.setStroke(Color.BLACK);
            rightVbox.getChildren().add(square);
        }
        StackPane rightBox = new StackPane(right, rightVbox);
        AnchorPane.setRightAnchor(rightBox, 26.0);
        AnchorPane.setBottomAnchor(rightBox, 10.0);
        box.getChildren().add(rightBox);

        for (int i = 0; i < wires.length; i++) {
            if (wires[i] != null) {
                AnchorPane.setLeftAnchor(wires[i], 25.0);
                AnchorPane.setTopAnchor(wires[i], calculateWireY(i));
                box.getChildren().add(wires[i]);
            }
        }

        getChildren().add(box);
    }

    private double calculateWireY(int i) {
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

    private Wire lastWire(Color color) {
        for (int i = wires.length - 1; i >= 0; i--) {
            if (wires[i] != null && wires[i].color == color) {
                return wires[i];
            }
        }
        return null;
    }

    private Image imageFromPosition(int position) {
        return new Image(WiresModule.class.getResourceAsStream("wire_drawings/wire" + (position + 1) + ".png"));
    }

    public void play() {}

    public void pause() {}

}
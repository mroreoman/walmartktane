package com.github.mroreoman.game.modules;

import java.util.Random;
import java.util.Arrays;

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

import com.github.mroreoman.Util;
import com.github.mroreoman.game.Bomb;

public class ComplicatedWiresModule extends ModuleBase {
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

    public ComplicatedWiresModule(Bomb bomb, Random rand) {
        super("Complicated Wires", bomb);
        do {
            initComplicatedWires(rand);
            setSolution();
        } while (!checkSolvable());
        initGUI();
    }

    private void initComplicatedWires(Random rand) {
        int numWires = rand.nextInt(3) + 4;
        int[] positions = Util.goodUniqueIndexes(rand, wires.length, numWires);
        for (int position : positions) {
            int color = rand.nextInt(6);
            wires[position] = switch(color) {
                case 0 -> new ComplicatedWire(imageFromPosition(position), new Color[]{COLORS[0]});
                case 1 -> new ComplicatedWire(imageFromPosition(position), new Color[]{COLORS[1]});
                case 2 -> new ComplicatedWire(imageFromPosition(position), new Color[]{COLORS[2]});
                case 3 -> new ComplicatedWire(imageFromPosition(position), new Color[]{COLORS[0], COLORS[1]});
                case 4 -> new ComplicatedWire(imageFromPosition(position), new Color[]{COLORS[0], COLORS[2]});
                default -> new ComplicatedWire(imageFromPosition(position), new Color[]{COLORS[1], COLORS[2]});
            };
            
            stars[position] = rand.nextBoolean();
            lights[position] = rand.nextBoolean();
        }
    }

    private void setSolution() {
        for (int i = 0; i < 6; i++) {
            if (wires[i] != null) {
                if (Arrays.equals(wires[i].color, new Color[]{COLORS[0]})) {
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
                } else if (Arrays.equals(wires[i].color, new Color[]{COLORS[1]}) || Arrays.equals(wires[i].color, new Color[]{COLORS[0], COLORS[1]})) {
                    if (lights[i]) {
                        if (getEdgework().numBatteries() >= 2) {
                            setCorrect(wires[i]);
                        }
                    } else if (stars[i]) {
                        setCorrect(wires[i]);
                    } else {
                        if (getEdgework().lastSerialDigit() % 2 == 0) {
                            setCorrect(wires[i]);
                        }
                    }
                } else if (Arrays.equals(wires[i].color, new Color[]{COLORS[2]}) || Arrays.equals(wires[i].color, new Color[]{COLORS[0], COLORS[2]})) {
                    if (lights[i]) {
                        if (getEdgework().hasPort("PAR")) {
                            setCorrect(wires[i]);
                        }
                    } else if (!stars[i]) {
                        if (getEdgework().lastSerialDigit() % 2 == 0) {
                            setCorrect(wires[i]);
                        }
                    }
                } else if (Arrays.equals(wires[i].color, new Color[]{COLORS[1], COLORS[2]})) {
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
        for (int i = 0; i < 6; i++) {
            topHoles[i] = new Rectangle(25, 10, Color.BLACK);
            topHoles[i].setStroke(Color.GRAY);
            topHoles[i].setStrokeType(StrokeType.INSIDE);
        }
        HBox topHoleHbox = new HBox();
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

        HBox bottomHoleHbox = new HBox();
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

        HBox starBox = new HBox(5);
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
                AnchorPane.setLeftAnchor(wires[i], 15.0 + 25 * i);
                AnchorPane.setTopAnchor(wires[i], 50.0);
                box.getChildren().add(wires[i]);
            }
        }

        getChildren().add(box);
    }

    private Image imageFromPosition(int position) {
        return new Image(ComplicatedWiresModule.class.getResourceAsStream("complicated_wire_drawings/compwire" + (position + 1) + ".png"));
    }

    public void play() {}

    public void pause() {}

}
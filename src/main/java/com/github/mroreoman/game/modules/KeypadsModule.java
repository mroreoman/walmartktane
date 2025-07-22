package com.github.mroreoman.game.modules;

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

import com.github.mroreoman.game.Bomb;
import com.github.mroreoman.Util;

public class KeypadsModule extends ModuleBase {
    private static final String[] COLUMN1 = {"keypad_symbols/28-balloon.png", "keypad_symbols/13-at.png", "keypad_symbols/30-upsidedowny.png", "keypad_symbols/12-squigglyn.png", "keypad_symbols/7-squidknife.png", "keypad_symbols/9-hookn.png", "keypad_symbols/23-leftc.png"};
    private static final String[] COLUMN2 = {"keypad_symbols/16-euro.png", "keypad_symbols/28-balloon.png", "keypad_symbols/23-leftc.png", "keypad_symbols/26-cursive.png", "keypad_symbols/3-hollowstar.png", "keypad_symbols/9-hookn.png", "keypad_symbols/20-questionmark.png"};
    private static final String[] COLUMN3 = {"keypad_symbols/1-copyright.png", "keypad_symbols/8-pumpkin.png", "keypad_symbols/26-cursive.png", "keypad_symbols/5-doublek.png", "keypad_symbols/15-meltedthree.png", "keypad_symbols/30-upsidedowny.png", "keypad_symbols/3-hollowstar.png"};
    private static final String[] COLUMN4 = {"keypad_symbols/11-six.png", "keypad_symbols/21-paragraph.png", "keypad_symbols/31-bt.png", "keypad_symbols/7-squidknife.png", "keypad_symbols/5-doublek.png", "keypad_symbols/20-questionmark.png", "keypad_symbols/4-smileyface.png"};
    private static final String[] COLUMN5 = {"keypad_symbols/24-pitchfork.png", "keypad_symbols/4-smileyface.png", "keypad_symbols/31-bt.png", "keypad_symbols/22-rightc.png", "keypad_symbols/21-paragraph.png", "keypad_symbols/19-dragon.png", "keypad_symbols/2-filledstar.png"};
    private static final String[] COLUMN6 = {"keypad_symbols/11-six.png", "keypad_symbols/16-euro.png", "keypad_symbols/27-tracks.png", "keypad_symbols/14-ae.png", "keypad_symbols/24-pitchfork.png", "keypad_symbols/18-nwithhat.png", "keypad_symbols/6-omega.png"};
    private static final String[][] SYMBOLS = {COLUMN1, COLUMN2, COLUMN3, COLUMN4, COLUMN5, COLUMN6};

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
            ImageView img = new ImageView(new Image(KeypadsModule.class.getResourceAsStream(path)));
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

    public KeypadsModule(Bomb bomb, Random rand) {
        super("Keypads", bomb);
        initKeypads(rand);
        initGUI();
    }

    private void initKeypads(Random rand) {
        column = rand.nextInt(6);
        int[] indexes = Util.randomUniqueIndexes(rand, Util.intAsArray(7), 4);
        for (int i = 0; i < 4; i++) {
            keypads[i] = new Keypad(column, indexes[i]);
        }
        setSolution();
    }

    private void setSolution() {
        int i = 0;
        for (String path : SYMBOLS[column]) {
            for (Keypad keypad : keypads) {
                if (keypad.path.equals(path)) {
                    keypad.pressNum = i++;
                }
            }
        }
    }

    private void initGUI() {
        AnchorPane box = new AnchorPane();
        initSubPane(box);

        AnchorPane.setLeftAnchor(keypads[0].buton, PADDING);
        AnchorPane.setBottomAnchor(keypads[0].buton, PADDING + 80);
        AnchorPane.setLeftAnchor(keypads[1].buton, PADDING + 80);
        AnchorPane.setBottomAnchor(keypads[1].buton, PADDING + 80);
        AnchorPane.setLeftAnchor(keypads[2].buton, PADDING);
        AnchorPane.setBottomAnchor(keypads[2].buton, PADDING);
        AnchorPane.setLeftAnchor(keypads[3].buton, PADDING + 80);
        AnchorPane.setBottomAnchor(keypads[3].buton, PADDING);

        for (Keypad keypad : keypads) {
            box.getChildren().add(keypad.buton);
        }
        this.getChildren().add(box);
    }

    public void disable() {
        for (Keypad keypad : keypads) {
            keypad.buton.setDisable(true);
        }
    }

    public void play() {}

    public void pause() {}

}
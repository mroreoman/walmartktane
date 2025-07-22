package com.github.mroreoman.game.modules;

import java.util.Random;
import java.util.HashMap;
import java.util.Map.Entry;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.effect.Glow;

import com.github.mroreoman.game.Bomb;

public class SimonSaysModule extends ModuleBase {
    private static final Color[] COLORS = {Color.web("0x800000"), Color.web("0x000080"), Color.web("0x008000"), Color.web("0x808000")};

    private Color[] flashes;
    private HashMap<Color, Simon> simons = new HashMap<>(4);
    private int stage = 0;
    private int press = 0;

    private Timeline sequence = new Timeline(new KeyFrame(Duration.seconds(1.0 / 3), e -> {
        press = 0;
        simons.get(flashes[0]).flash();
    }));

    private Timeline flasher = new Timeline(new KeyFrame(Duration.seconds(9.0 / 2), e -> sequence.playFromStart()));

    private class Simon extends Button {
        private Timeline flashTimeline;

        private Simon(Color color) {
            super();
            flashTimeline = new Timeline(new KeyFrame(Duration.seconds(1.0 / 3), e -> setEffect(null)));
            setStyle("-fx-background-color: #" + color.toString().substring(2, 8) + "; -fx-min-width: 55px; -fx-min-height: 55px; -fx-max-width: 55px; -fx-max-height: 55px;");
            setRotate(45.0);
            setOnAction(event -> press());
        }

        private void press() {
            sequence.stop();
            flasher.stop();
            flasher.play();
            flash();
            if (this == getSolution(flashes[press])) {
                press++;
                if (press == stage + 1) {
                    press = 0;
                    playNextStage();
                }
            } else {
                press = 0;
                submit(false);
            }
        }

        private void flash() {
            setEffect(new Glow());
            flashTimeline.playFromStart();
        }

        private EventHandler flasher() {
            return new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    flash();
                }
            };
        }

        public String toString() {
            for (Entry<Color, Simon> entry : simons.entrySet()) {
                if (entry.getValue().equals(this)) {
                    return entry.getKey().toString();
                }
            }
            return super.toString();
        }

    }

    public SimonSaysModule(Bomb bomb, Random rand) {
        super("Simon Says", bomb);
        initSimonSays(rand);
        initGUI();
    }

    private void initSimonSays(Random rand) {
        simons.put(COLORS[0], new Simon(COLORS[0]));
        simons.put(COLORS[1], new Simon(COLORS[1]));
        simons.put(COLORS[2], new Simon(COLORS[2]));
        simons.put(COLORS[3], new Simon(COLORS[3]));

        flashes = new Color[rand.nextInt(3) + 3];
        for (int i = 0; i < flashes.length; i++) {
            flashes[i] = COLORS[rand.nextInt(4)];
        }

        flasher.setCycleCount(Timeline.INDEFINITE);
        flasher.play();
    }

    private void initGUI() {
        AnchorPane box = new AnchorPane();
        initSubPane(box);

        Rectangle holder = new Rectangle(130, 130, Color.BLACK);
        holder.setRotate(45.0);

        AnchorPane.setLeftAnchor(holder, 51.5);
        AnchorPane.setBottomAnchor(holder, 51.5);
        AnchorPane.setLeftAnchor(simons.get(COLORS[0]), 116.5 - 5 - 55 / Math.sqrt(2) - 55 / 2.0);
        AnchorPane.setBottomAnchor(simons.get(COLORS[0]), 116.5 - 55 / 2.0);
        AnchorPane.setLeftAnchor(simons.get(COLORS[1]), 116.5 - 55 / 2.0);
        AnchorPane.setBottomAnchor(simons.get(COLORS[1]), 116.5 + 5 + 55 / Math.sqrt(2) - 55 / 2.0);
        AnchorPane.setLeftAnchor(simons.get(COLORS[2]), 116.5 - 55 / 2.0);
        AnchorPane.setBottomAnchor(simons.get(COLORS[2]), 116.5 - 5 - 55 / Math.sqrt(2) - 55 / 2.0);
        AnchorPane.setLeftAnchor(simons.get(COLORS[3]), 116.5 + 5 + 55 / Math.sqrt(2) - 55 / 2.0);
        AnchorPane.setBottomAnchor(simons.get(COLORS[3]), 116.5 - 55 / 2.0);

        box.getChildren().add(holder);
        box.getChildren().addAll(simons.values());
        this.getChildren().add(box);
    }

    public void play() {
        flasher.playFromStart();
    }

    public void pause() {
        flasher.stop();
        sequence.stop();
    }

    private void playNextStage() {
        stage++;

        if (stage >= flashes.length) {
            flasher.stop();
            sequence.stop();
            submit(true);
            return;
        }

        sequence.getKeyFrames().add(new KeyFrame(Duration.seconds((1.0 + stage * 2) / 3), simons.get(flashes[stage]).flasher()));

        flasher.stop();
        sequence.stop();
        flasher.playFrom(Duration.seconds(5.0 / 2));
    }

    private Simon getSolution(Color flash) {
        if (getEdgework().serialHasVowel()) {
            if (getTimer().getStrikes() == 0) {
                if (flash == COLORS[0]) {
                    return simons.get(COLORS[1]);
                } else if (flash == COLORS[1]) {
                    return simons.get(COLORS[0]);
                } else if (flash == COLORS[2]) {
                    return simons.get(COLORS[3]);
                } else {
                    return simons.get(COLORS[2]);
                }
            } else if (getTimer().getStrikes() == 1) {
                if (flash == COLORS[0]) {
                    return simons.get(COLORS[3]);
                } else if (flash == COLORS[1]) {
                    return simons.get(COLORS[2]);
                } else if (flash == COLORS[2]) {
                    return simons.get(COLORS[1]);
                } else {
                    return simons.get(COLORS[0]);
                }
            } else {
                if (flash == COLORS[0]) {
                    return simons.get(COLORS[2]);
                } else if (flash == COLORS[1]) {
                    return simons.get(COLORS[0]);
                } else if (flash == COLORS[2]) {
                    return simons.get(COLORS[3]);
                } else {
                    return simons.get(COLORS[1]);
                }
            }
        } else {
            if (getTimer().getStrikes() == 0) {
                if (flash == COLORS[0]) {
                    return simons.get(COLORS[1]);
                } else if (flash == COLORS[1]) {
                    return simons.get(COLORS[3]);
                } else if (flash == COLORS[2]) {
                    return simons.get(COLORS[2]);
                } else {
                    return simons.get(COLORS[0]);
                }
            } else if (getTimer().getStrikes() == 1) {
                if (flash == COLORS[0]) {
                    return simons.get(COLORS[0]);
                } else if (flash == COLORS[1]) {
                    return simons.get(COLORS[1]);
                } else if (flash == COLORS[2]) {
                    return simons.get(COLORS[3]);
                } else {
                    return simons.get(COLORS[2]);
                }
            } else {
                if (flash == COLORS[0]) {
                    return simons.get(COLORS[3]);
                } else if (flash == COLORS[1]) {
                    return simons.get(COLORS[2]);
                } else if (flash == COLORS[2]) {
                    return simons.get(COLORS[1]);
                } else {
                    return simons.get(COLORS[0]);
                }
            }
        }
    }

}
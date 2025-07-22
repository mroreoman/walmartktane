package com.github.mroreoman.game.modules;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import com.github.mroreoman.game.Bomb;
import com.github.mroreoman.Util;

public class TheButtonModule extends ModuleBase {
    private static final String[] LABELS = {"Abort", "Detonate", "Hold", "Press"};
    private static final Color[] COLORS = {Color.BLUE, Color.RED, Color.WHITE, Color.YELLOW, Color.BLACK};

    private final boolean tapSolve;
    private Color stripColor;
    private int stripSolution;
    private boolean isHeld = false;

    private final String label;
    private final Color color;
    private final Timeline timeline;
    private Rectangle strip;

    public TheButtonModule(Bomb bomb, Random rand) {
        super("The Button", bomb);
        color = COLORS[rand.nextInt(5)];
        label = LABELS[rand.nextInt(4)];
        tapSolve = setSolution();
        timeline = new Timeline(new KeyFrame(Duration.millis(250), e -> holdButton(rand)));
        initGUI();
    }

    private boolean setSolution() {
        if (color == Color.BLUE && label.equals("Abort")) {
            return false;
        } else if (getEdgework().numBatteries() > 1 && label.equals("Detonate")) {
            return true;
        } else if (color == Color.WHITE && getEdgework().hasIndicator("CAR", true)) {
            return false;
        } else if (getEdgework().numBatteries() > 2 && getEdgework().hasIndicator("FRK", true)) {
            return true;
        } else if (color == Color.YELLOW) {
            return false;
        } else if (color == Color.RED && label.equals("Hold")) {
            return true;
        } else {
            return false;
        }
    }

    private void initGUI() {
        AnchorPane box = new AnchorPane();
        initSubPane(box);

        Button buton = new Button(label);
        buton.setTextFill(color == Color.WHITE || color == Color.YELLOW ? Color.BLACK : Color.WHITE);
        buton.setPadding(Insets.EMPTY);
        buton.setStyle("-fx-background-color: #" + color.toString().substring(2, 8) + "; -fx-background-radius: 75em; -fx-min-width: 150px; -fx-min-height: 150px; -fx-max-width: 150px; -fx-max-height: 150px;");
        buton.setFont(Util.bodyFont(25));
        buton.setOnMousePressed(event -> press());
        buton.setOnMouseReleased(event -> release());

        strip = new Rectangle(25, 125, Color.TRANSPARENT);
        strip.setStroke(Color.BLACK);

        AnchorPane.setLeftAnchor(buton, PADDING);
        AnchorPane.setBottomAnchor(buton, PADDING);
        AnchorPane.setRightAnchor(strip, PADDING);
        AnchorPane.setBottomAnchor(strip, PADDING);
        box.getChildren().add(buton);
        box.getChildren().add(strip);
        this.getChildren().add(box);
    }

    public void press() {
        timeline.playFromStart();
    }

    public void release() {
        timeline.pause();
        if (!isHeld) {
            tapButton();
        } else {
            strip.setFill(Color.TRANSPARENT);
            submit(timer.contains(stripSolution));
        }
    }

    public void genStrip(Random rand) {
        stripColor = COLORS[rand.nextInt(4)];
        setStripSolution();
        strip.setFill(stripColor);
    }

    public void setStripSolution() {
        if (stripColor == Color.BLUE)
            stripSolution = 4;
        else if (stripColor == Color.YELLOW)
            stripSolution = 5;
        else
            stripSolution = 1;
    }

    public void tapButton() {
        submit(tapSolve);
    }

    public void holdButton(Random rand) {
        isHeld = true;
        genStrip(rand);
    }

    public void play() {}

    public void pause() {}

}
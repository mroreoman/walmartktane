package com.github.mroreoman.game.widgets;

import com.github.mroreoman.game.BombEvent;
import com.github.mroreoman.game.ModuleEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import com.github.mroreoman.Util;

public class Timer extends BorderPane {
    private static final double SIZE = 80;

    private final Text[] strikeTexts;
    private final Text timerText;
    private final Timeline timeline;
    private final int maxStrikes;

    public final SimpleIntegerProperty strikesRemaining;
    public final SimpleIntegerProperty timeSecs;

    private int tick = 0;

    public Timer(int maxStrikes, int startTimeSecs) {
        this.maxStrikes = maxStrikes;
        strikesRemaining = new SimpleIntegerProperty(maxStrikes);
        timeSecs = new SimpleIntegerProperty(startTimeSecs);

        strikeTexts = new Text[maxStrikes - 1];
        for (int i = 0; i < strikeTexts.length; i++) {
            strikeTexts[i] = new Text("X");
            strikeTexts[i].setFont(Util.bodyFont(15));
            strikeTexts[i].setFill(Color.GRAY);
        }
        HBox hbox = new HBox(10, strikeTexts);
        hbox.setAlignment(Pos.TOP_CENTER);
        timerText = new Text(formattedTime());
        timerText.setFont(Util.bodyFont(30));
        timerText.setFill(Color.BLACK);

        setTop(hbox);
        setCenter(timerText);
        setMinSize(SIZE, SIZE);
        setMaxSize(SIZE, SIZE);
        setBackground(new Background(new BackgroundFill(Color.SILVER, null, null)));

        timeline = new Timeline(new KeyFrame(Duration.millis(250), e -> tick()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        addEventHandler(ModuleEvent.STRIKE, e -> addStrike());
        addEventHandler(BombEvent.PAUSE, e -> timeline.pause());
        addEventHandler(BombEvent.PLAY, e -> timeline.play());
    }

    private void tick() {
        tick++;
        if (tick >= strikesRemaining.get() + 1) {
            incrementTime();
            tick = 0;
        }
    }

    private void incrementTime() {
        timeSecs.set(timeSecs.get() - 1);
        timerText.setText(formattedTime()); //updateBombText();

        if (timeSecs.get() == 0) {
            fireEvent(new BombEvent(BombEvent.EXPLODE));
        }
    }

    private void addStrike() {
        strikesRemaining.set(strikesRemaining.get() - 1);
        if (strikesRemaining.get() > 0) {
            strikeTexts[strikeTexts.length - strikesRemaining.get()].setFill(Color.RED);
        } else if (strikesRemaining.get() == 0) {
            fireEvent(new BombEvent(BombEvent.EXPLODE));
        } else {
            throw new RuntimeException("Attempted to add strike after max strikes");
        }
    }

    public String formattedTime() {
        int mins = timeSecs.get() / 60;
        int secs = timeSecs.get() % 60;
        return mins + ":" + (secs < 10 ? ("0" + secs) : secs);
    }

    public boolean contains(int i) {
        return formattedTime().contains(Integer.toString(i));
    }

    public int getStrikes() {
        return maxStrikes - strikesRemaining.get();
    }

}

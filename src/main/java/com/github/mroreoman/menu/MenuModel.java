package com.github.mroreoman.menu;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

import com.github.mroreoman.game.Bomb;

public class MenuModel {
    private final ListProperty<Bomb> bombHistory = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<Bomb> currentBomb = new SimpleObjectProperty<>();
    private final IntegerProperty menuPage = new SimpleIntegerProperty(0);
    private final IntegerProperty storyModeChapter = new SimpleIntegerProperty(0);
    private final IntegerProperty bombCreationAmount = new SimpleIntegerProperty();
    private final IntegerProperty bombCreationTime = new SimpleIntegerProperty();
    private final IntegerProperty bombCreationStrikes = new SimpleIntegerProperty();

    public ListProperty<Bomb> bombHistoryProperty() {
        return bombHistory;
    }

    public void addBomb(Bomb bomb) {
        if (!bombHistory.contains(bomb))
            bombHistory.add(bomb);
    }

    public ObjectProperty<Bomb> currentBombProperty() {
        return currentBomb;
    }

    public void setCurrentBomb(Bomb bomb) {
        currentBomb.set(bomb);
    }

    public IntegerProperty menuPageProperty() {
        return menuPage;
    }

    public IntegerProperty storyModeChapterProperty() {
        return storyModeChapter;
    }

    public void setStoryModeChapter(int chapter) {
        storyModeChapter.set(chapter);
    }

    public IntegerProperty bombCreationAmountProperty() {
        return bombCreationAmount;
    }

    public int getBombCreationAmount() {
        return bombCreationAmount.get();
    }

    public IntegerProperty bombCreationTimeProperty() {
        return bombCreationTime;
    }

    public int getBombCreationTime() {
        return bombCreationTime.get();
    }

    public IntegerProperty bombCreationStrikesProperty() {
        return bombCreationStrikes;
    }

    public int getBombCreationStrikes() {
        return bombCreationStrikes.get();
    }

}

package com.github.mroreoman.menu;

import jakarta.json.JsonObject;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.github.mroreoman.game.Bomb;
import com.github.mroreoman.game.modules.ModuleBase;

public class MenuModel {

    private final ListProperty<Bomb> bombHistory = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<Bomb> currentBomb = new SimpleObjectProperty<>();
    private final IntegerProperty menuPage = new SimpleIntegerProperty(0);
    private final IntegerProperty storyModeChapter = new SimpleIntegerProperty(0);
    private final IntegerProperty bombCreationAmount = new SimpleIntegerProperty(11);
    private final IntegerProperty bombCreationTime = new SimpleIntegerProperty(300);
    private final IntegerProperty bombCreationStrikes = new SimpleIntegerProperty(3);
    private final ListProperty<ModuleBase.Module> bombCreationModuleList = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final BooleanProperty bombCreationSeeded = new SimpleBooleanProperty(false);
    private final IntegerProperty bombCreationSeed = new SimpleIntegerProperty();
    private final JsonObject saveData;

    public MenuModel (JsonObject saveData) {
        this.saveData = saveData;
    }

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

    public ListProperty<ModuleBase.Module> bombCreationModuleListProperty() {
        return bombCreationModuleList;
    }

    public ObservableList<ModuleBase.Module> getBombCreationModuleList() {
        return bombCreationModuleList.get();
    }

    public BooleanProperty bombCreationSeededProperty() {
        return bombCreationSeeded;
    }

    public boolean getBombCreationSeeded() {
        return bombCreationSeeded.get();
    }

    public IntegerProperty bombCreationSeedProperty() {
        return bombCreationSeed;
    }

    public int getBombCreationSeed() {
        return bombCreationSeed.get();
    }

    public JsonObject getSaveData() { //TODO generate actual data
        return saveData;
    }
}

package com.github.mroreoman.menu;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.github.mroreoman.game.Bomb;
import com.github.mroreoman.game.StoryModeBomb;
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
    private final Map<String, StoryModeBombProgress> storyModeProgress; //TODO update data

    public MenuModel(JsonObject saveData) {
        storyModeProgress = new LinkedHashMap<>();
        for (List<StoryModeBomb> chapter : StoryModeBomb.ALL_CHAPTERS) {
            for (StoryModeBomb bomb : chapter) {
                if (saveData.getJsonObject(bomb.name()) == null) {
                    System.out.println("No data found for " + bomb.name());
                    storyModeProgress.put(bomb.name(), new StoryModeBombProgress(bomb));
                } else {
                    storyModeProgress.put(bomb.name(), new StoryModeBombProgress(saveData.getJsonObject(bomb.name())));
                }
            }
        }
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

    public JsonObject getSaveData() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        storyModeProgress.forEach((k, v) -> builder.add(k, v.toJson()));
        return builder.build();
    }

    private static class StoryModeBombProgress {
        int timeRemaining;
        int strikesRemaining;
        int modulesRemaining;

        StoryModeBombProgress(JsonObject saveData) {
            strikesRemaining = saveData.getJsonNumber("strikesRemaining").intValue();
            timeRemaining = saveData.getJsonNumber("timeRemaining").intValue();
            modulesRemaining = saveData.getJsonNumber("modulesRemaining").intValue();
        }

        StoryModeBombProgress(StoryModeBomb bomb) {
            strikesRemaining = bomb.maxStrikes();
            timeRemaining = bomb.startTimeSecs();
            modulesRemaining = bomb.requiredModules().size() + bomb.pools().size();
        }

        JsonObject toJson() {
            return Json.createObjectBuilder()
                    .add("strikesRemaining", strikesRemaining)
                    .add("timeRemaining", timeRemaining)
                    .add("modulesRemaining", modulesRemaining)
                    .build();
        }
    }
}

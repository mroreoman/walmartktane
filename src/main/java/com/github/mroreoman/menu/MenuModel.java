package com.github.mroreoman.menu;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

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
    private final Map<String, StoryModeBombProgress> storyModeProgress;

    public MenuModel(JsonObject saveData) {
        storyModeProgress = new LinkedHashMap<>();
        for (List<StoryModeBomb> chapter : StoryModeBomb.ALL_CHAPTERS) {
            for (StoryModeBomb bomb : chapter) {
                if (saveData == JsonObject.EMPTY_JSON_OBJECT || saveData.isNull(bomb.name())) {
                    storyModeProgress.put(bomb.name(), new StoryModeBombProgress(null));
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
        if (!bombHistory.contains(bomb)) {
            bombHistory.add(bomb);
            if (storyModeProgress.containsKey(bomb.getName()) && bomb.getState() == Bomb.State.DEFUSED) {
                StoryModeBombProgress solve = new StoryModeBombProgress(bomb.getTimeRemaining(), bomb.getStrikes());
                if (solve.compareTo(storyModeProgress.get(bomb.getName())) > 0) {
                    storyModeProgress.put(bomb.getName(), solve);
                }                
            }
        }
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

    private static class StoryModeBombProgress implements Comparable<StoryModeBombProgress> {
        int timeRemaining;
        int strikes;
        boolean isNull = false;

        StoryModeBombProgress(JsonObject saveData) {
            if (saveData == null) {
                isNull = true;
            } else if (!saveData.containsKey("strikes") || !saveData.containsKey("timeRemaining")) {
                throw new RuntimeException("Invalid save data"); //TODO ask player if they want to close game & fix the file or continue playing & overwrite the file
            } else {
                strikes = saveData.getInt("strikes");
                timeRemaining = saveData.getInt("timeRemaining");
            }
        }

        StoryModeBombProgress(int timeRemaining, int strikes) {
            this.timeRemaining = timeRemaining;
            this.strikes = strikes;
        }

        public int compareTo(StoryModeBombProgress o) {
             if (timeRemaining == o.timeRemaining) {
                return o.strikes - strikes;
             } else {
                return timeRemaining - o.timeRemaining;
             }
        }

        JsonValue toJson() {
            if (isNull) {
                return JsonObject.NULL;
            }
            return Json.createObjectBuilder()
                    .add("strikes", strikes)
                    .add("timeRemaining", timeRemaining)
                    .build();
        }
    }
}

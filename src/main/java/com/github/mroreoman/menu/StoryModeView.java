package com.github.mroreoman.menu;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Builder;

import com.github.mroreoman.game.StoryModeBomb;
import com.github.mroreoman.Util;

public class StoryModeView implements Builder<Region> {
    private final MenuModel model;
    private final Runnable setViewMainMenu;
    private final Runnable bombExitAction;

    public StoryModeView(MenuModel model, Runnable setViewMainMenu, Runnable bombExitAction) {
        this.model = model;
        this.setViewMainMenu = setViewMainMenu;
        this.bombExitAction = bombExitAction;
    }

    @Override
    public Region build() {
        VBox root = new VBox(
                createTitle(),
                createChapterTitle(),
                createChapterButtons(),
                createPageFlipper(),
                createBackButton()
        );
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);
        return root;
    }

    private Node createTitle() {
        Text title = new Text("STORY MODE");
        title.setFont(Util.titleFont(50));
        return title;
    }

    private Node createChapterTitle() {
        Text chapterTitle = new Text("Chapter " + (model.storyModeChapterProperty().getValue() + 1));
        chapterTitle.setFont(Util.titleFont(30));

        model.storyModeChapterProperty().addListener((observable, oldValue, newValue) -> chapterTitle.setText("Chapter " + (newValue.intValue() + 1)));

        return chapterTitle;
    }

    private Node createChapterButtons() {
        List<List<Button>> chapters = new ArrayList<>();
        for (List<StoryModeBomb> chapter : StoryModeBomb.ALL_CHAPTERS) {
            List<Button> buttons = new ArrayList<>();
            for (StoryModeBomb bomb : chapter) {
                buttons.add(createBombButton(bomb));
            }
            chapters.add(buttons);
        }

//        List<List<Button>> chapters = StoryModeBomb.ALL_CHAPTERS.stream().map(chapter -> chapter.stream().map(this::createBombButton).toList()).toList();

        VBox box = new VBox(10);
        box.setMinHeight(235);
        box.setMaxHeight(235);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(chapters.get(model.getStoryModeChapter()));
        model.storyModeChapterProperty().addListener((obs, oldV, newV) -> box.getChildren().setAll(chapters.get(newV.intValue())));
        return box;
    }

    private Button createBombButton(StoryModeBomb bomb) {
        Button button = new Button(createBombButtonText(bomb, model.storyModeBombProgressProperty(bomb.name()).get()));
        button.setOnAction(event -> model.setCurrentBomb(bomb.instantiate(bombExitAction)));

        model.storyModeBombProgressProperty(bomb.name()).addListener((observable, oldValue, newValue) -> {
            button.setText(createBombButtonText(bomb, newValue));
        });

        return button;
    }

    private String createBombButtonText(StoryModeBomb bomb, MenuModel.StoryModeBombProgress progress) {
        if (progress.isNull) {
            return bomb.name();
        }
        int timeRemaining = progress.getTimeRemaining();
        return bomb.name() + " (" + Util.formatTime(timeRemaining) + ")";
    }

    private Node createPageFlipper() {
        Button prevChapter = new Button("Previous");
        prevChapter.setOnAction(event -> model.setStoryModeChapter(model.storyModeChapterProperty().get() - 1));
        prevChapter.disableProperty().bind(model.storyModeChapterProperty().greaterThan(0).not());

        Button nextChapter = new Button("Next");
        nextChapter.setOnAction(event -> model.setStoryModeChapter(model.storyModeChapterProperty().get() + 1));
        nextChapter.disableProperty().bind(model.storyModeChapterProperty().lessThan(StoryModeBomb.ALL_CHAPTERS.size() - 1).not());

        HBox pageFlipper = new HBox(100, prevChapter, nextChapter);
        pageFlipper.setAlignment(Pos.CENTER);
        return pageFlipper;
    }

    private Node createBackButton() {
        Button back = new Button("Back");
        back.setOnAction(event -> setViewMainMenu.run());
        return back;
    }

}
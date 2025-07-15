package main.menus;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Builder;

import main.Bomb;
import main.StoryModeBomb;
import main.Util;

import java.util.function.Consumer;

public class StoryModeBuilder implements Builder<Region> {
    private final KtaneModel model;
    private final Consumer<Bomb> newBomb;
    private final Runnable setViewMainMenu;
    private final Runnable bombExitAction;

    public StoryModeBuilder(KtaneModel model, Consumer<Bomb> newBomb, Runnable setViewMainMenu, Runnable bombExitAction) {
        this.model = model;
        this.newBomb = newBomb;
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
        VBox chapterButtons = new VBox(10);
        chapterButtons.setMinHeight(235);
        chapterButtons.setMaxHeight(235);
        chapterButtons.setAlignment(Pos.CENTER);
        for (StoryModeBomb bomb : StoryModeBomb.ALL_CHAPTERS.get(model.storyModeChapterProperty().get())) {
            chapterButtons.getChildren().add(createStoryModeButton(bomb));
        }

        model.storyModeChapterProperty().addListener((observable, oldValue, newValue) -> {
            chapterButtons.getChildren().clear();
            for (StoryModeBomb bomb : StoryModeBomb.ALL_CHAPTERS.get(newValue.intValue())) {
                chapterButtons.getChildren().add(createStoryModeButton(bomb));
            }
        });

        return chapterButtons;
    }

    private Button createStoryModeButton(StoryModeBomb bomb) {
        Button button = new Button(bomb.name());
        button.setOnAction(event -> newBomb.accept(bomb.initialize(bombExitAction)));
        return button;
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
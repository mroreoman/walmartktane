package com.github.mroreoman.menu;

import java.util.Random;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Builder;
import com.github.mroreoman.game.Bomb;
import com.github.mroreoman.Util;

public class MainMenuView implements Builder<Region> {
    private final MenuModel model;
    private final Runnable setViewStoryMode;
    private final Runnable setViewBombCreation;
    private final Consumer<Bomb> newBomb;
    private final Runnable bombExitAction;

    public MainMenuView(MenuModel model, Runnable setViewStoryMode, Runnable setViewBombCreation, Consumer<Bomb> newBomb, Runnable bombExitAction) {
        this.model = model;
        this.setViewStoryMode = setViewStoryMode;
        this.setViewBombCreation = setViewBombCreation;
        this.newBomb = newBomb;
        this.bombExitAction = bombExitAction;
    }

    public Region build() {
        BorderPane root = new BorderPane();
        root.setTop(createTitle());
        root.setLeft(createMenuButtons());
        root.setRight(createBombScroll());
        root.setPadding(new Insets(10));
        return root;
    }

    private Node createTitle() {
        Text title = new Text("WALMART KTANE");
        title.setFont(Util.titleFont(45));
        BorderPane.setAlignment(title, Pos.TOP_CENTER);
        BorderPane.setMargin(title, new Insets(10, 0, 20, 0));
        return title;
    }

    private Node createMenuButtons() {
        Button storyModeButton = new Button("Story mode");
        storyModeButton.setFont(Util.bodyFont(25));
        storyModeButton.setOnAction(e -> setViewStoryMode.run());

        Button bombCreationButton = new Button("Create bomb");
        bombCreationButton.setFont(Util.bodyFont(25));
        bombCreationButton.setOnAction(e -> setViewBombCreation.run());

        Button quickPlayButton = new Button("Quick play");
        quickPlayButton.setFont(Util.bodyFont(25));
        quickPlayButton.setOnAction(e -> newBomb.accept(new Bomb(new Random(), 5, 300, 3, bombExitAction, "Quick Play")));

        Button exit = new Button("Exit");
        exit.setFont(Util.bodyFont(25));
        exit.setOnAction(event -> Platform.exit());

        return new VBox(25, storyModeButton, bombCreationButton, quickPlayButton, exit);
    }

    private Node createBombScroll() {
        VBox bombButtons = new VBox(10);
        model.bombHistoryProperty().addListener((ListChangeListener<Bomb>) change -> {
            bombButtons.getChildren().clear();
            for (Bomb bomb : change.getList()) {
                bombButtons.getChildren().add(createBombButton(bomb));
            }
        });
        ScrollPane bombButtonsScroll = new ScrollPane(bombButtons);
        bombButtonsScroll.setMinViewportWidth(150);
        return bombButtonsScroll;
    }

    private Node createBombButton(Bomb bomb) {
        Button buton = new Button(bomb.toString());
        buton.setOnAction(event -> model.setCurrentBomb(bomb));
        buton.setFont(Util.bodyFont(15));
        return buton;
    }

}

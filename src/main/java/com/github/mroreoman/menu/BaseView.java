package com.github.mroreoman.menu;

import java.util.function.Consumer;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Builder;

import com.github.mroreoman.game.Bomb;

public class BaseView implements Builder<Region> {
    private final MenuModel model;
    private final Consumer<Bomb> newBomb;

    public BaseView(MenuModel model, Consumer<Bomb> newBomb) {
        this.model = model;
        this.newBomb = newBomb;
    }

    @Override
    public Region build() {
        StackPane root = new StackPane();

        Runnable bombExitAction = () -> {
            model.setCurrentBomb(null);
            model.menuPageProperty().set(0);
        };

        Region storyMode = new StoryModeView(
                model,
                newBomb,
                () -> model.menuPageProperty().set(0),
                bombExitAction
        ).build();
        Region bombCreation = new BombCreationView(
                model,
                newBomb,
                () -> model.menuPageProperty().set(0),
                bombExitAction
        ).build();
        Region mainMenu = new MainMenuView(
                model,
                () -> model.menuPageProperty().set(1),
                () -> model.menuPageProperty().set(2),
                newBomb,
                bombExitAction
        ).build();
        root.getChildren().add(mainMenu);

        model.currentBombProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                model.menuPageProperty().set(-1);
                setView(root, newValue.getView());
            }
        });

        model.menuPageProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.intValue()) {
                case 0 -> setView(root, mainMenu);
                case 1 -> setView(root, storyMode);
                case 2 -> setView(root, bombCreation);
            }
        });

        return root;
    }

    private static void setView(StackPane root, Region view) {
        root.getChildren().setAll(view);
    }

}

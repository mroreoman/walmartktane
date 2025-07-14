package main.menus;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Builder;
import main.Bomb;

import java.util.function.Consumer;

//TODO combine KtaneView and MainMenuView?
public class KtaneBuilder implements Builder<Region> {
    private final KtaneModel model;
    private final Consumer<Bomb> newBomb;

    public KtaneBuilder(KtaneModel model, Consumer<Bomb> newBomb) {
        this.model = model;
        this.newBomb = newBomb;
    }

    @Override
    public Region build() {
        StackPane root = new StackPane();
        Region mainMenu;
        Region storyMode = new StoryModeBuilder(
                model,
                newBomb,
                () -> model.menuPageProperty().set(0)
        ).build();
        Region bombCreation = new BombCreationBuilder(
                model,
                newBomb,
                () -> model.menuPageProperty().set(0)
        ).build();
        mainMenu = new MainMenuBuilder(
                model,
                () -> model.menuPageProperty().set(0),
                () -> model.menuPageProperty().set(1),
                () -> model.menuPageProperty().set(2),
                newBomb
        ).build();
        root.getChildren().add(mainMenu);

        model.currentBomb().addListener((observable, oldValue, newValue) -> {
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

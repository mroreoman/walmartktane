package main;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Builder;

public class KtaneWrapperBuilder implements Builder<Region> {
    private final KtaneModel model;
    private final Runnable quickPlayRunner;

    public KtaneWrapperBuilder(KtaneModel model, Runnable quickPlayRunner) {
        this.model = model;
        this.quickPlayRunner = quickPlayRunner;
    }

    @Override
    public Region build() {
        StackPane root = new StackPane();
        //TODO create StoryModeBuilder and BombCreationBuilder classes
        //  - argument is exitButtonAction
        Region storyMode;
//        storyMode = new StoryModeBuilder(() -> root.getChildren().remove(1)).build();
        Region bombCreation;
//        bombCreation = new BombCreationBuilder(() -> root.getChildren().remove(1)).build();
        MainMenuBuilder mainMenuBuilder = new MainMenuBuilder(
                model,
                () -> root.getChildren().add(1, storyMode),
                () -> root.getChildren().add(1, bombCreation),
                quickPlayRunner
        );
        root.getChildren().add(mainMenuBuilder.build());

        model.currentBomb().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                root.getChildren().add(1, newValue.getView());
            } else if (oldValue != null) {
                root.getChildren().remove(oldValue.getView());
            }
        });
        return root;
    }
}

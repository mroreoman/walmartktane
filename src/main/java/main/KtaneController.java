package main;

import javafx.scene.layout.Region;

public class KtaneController {
    private final KtaneModel model;
    private final KtaneWrapperBuilder viewBuilder;

    public KtaneController() {
        model = new KtaneModel();
        viewBuilder = new KtaneWrapperBuilder(model, this::quickPlay);

        model.currentBomb().addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue) {
                if (oldValue != null) {
                    oldValue.exit();
                }
                if (newValue != null) {
                    newValue.play();
                }
            }
        });
    }

    public Region getView() {
        return viewBuilder.build();
    }

    public void saveData() {
        //TODO save bomb history & story mode progress
        //  - would also need to load bomb history & story mode progress somewhere
    }

    private void quickPlay() {
        Bomb b = new Bomb(5, 300, 3);
        model.addBomb(b);
        model.setCurrentBomb(b);
    }
}

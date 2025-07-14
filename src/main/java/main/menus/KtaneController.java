package main.menus;

import javafx.scene.layout.Region;
import main.Bomb;

public class KtaneController {
    private final KtaneModel model;
    private final KtaneBuilder viewBuilder;

    public KtaneController() {
        model = new KtaneModel();
        viewBuilder = new KtaneBuilder(model, this::newBomb);

        model.currentBomb().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.play();
            }
        });
    }

    public Region getView() {
        return viewBuilder.build();
    }

    public void saveData() {
        //TODO save bomb history & story mode progress
        //  - would also need to load data somewhere
    }

    private void newBomb(Bomb b) {
//        model.addBomb(b);
        model.setCurrentBomb(b);
    }
}

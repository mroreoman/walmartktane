package com.github.mroreoman.menu;

import javafx.scene.layout.Region;

import com.github.mroreoman.game.Bomb;

public class MenuController {
    private final MenuModel model;
    private final BaseView viewBuilder;

    public MenuController() {
        model = new MenuModel();
        viewBuilder = new BaseView(model, this::newBomb);

        model.currentBombProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.play();
            }

            if (oldValue != null && oldValue.getState() != Bomb.State.RUNNING) {
                model.addBomb(oldValue);
            }
        });
    }

    public Region getView() {
        return viewBuilder.build();
    }

    public void saveData() {}

    private void newBomb(Bomb b) {
        model.setCurrentBomb(b);
    }

}

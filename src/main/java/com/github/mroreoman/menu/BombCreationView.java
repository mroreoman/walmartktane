package com.github.mroreoman.menu;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import javafx.beans.property.IntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Builder;

import com.github.mroreoman.game.Bomb;
import com.github.mroreoman.game.modules.ModuleBase;
import com.github.mroreoman.Util;

public class BombCreationView implements Builder<Region> {
    private final MenuModel model;
    private final Consumer<Bomb> newBomb;
    private final Runnable setViewMainMenu;
    private final Runnable bombExitAction;

    public BombCreationView(MenuModel model, Consumer<Bomb> newBomb, Runnable setViewMainMenu, Runnable bombExitAction) {
        this.model = model;
        this.newBomb = newBomb;
        this.setViewMainMenu = setViewMainMenu;
        this.bombExitAction = bombExitAction;
    }

    @Override
    public Region build() {
        VBox box = new VBox(
                createTitle(),
                createTimeBox(),
                createStrikeBox(),
                createModuleAmountBox(),
                createModuleListBox(),
                createSeedBox(),
                createButtonBox()
        );
        box.setSpacing(25);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private Node createTitle() {
        Text title = new Text("BOMB CREATION");
        title.setFont(Util.titleFont(50));
        return title;
    }

    private Node createTimeBox() {
        Text text = new Text("Time (seconds):");
        text.setFont(Util.titleFont(20));
        TextField tf = createIntField(model.bombCreationTimeProperty());
        HBox box = new HBox(10, text, tf);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private Node createStrikeBox() {
        Text text = new Text("Strikes:");
        text.setFont(Util.titleFont(20));
        TextField tf = createIntField(model.bombCreationStrikesProperty());
        HBox box = new HBox(10, text, tf);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private Node createModuleAmountBox() {
        Text text = new Text("Number of modules:");
        text.setFont(Util.titleFont(20));
        TextField tf = createIntField(model.bombCreationAmountProperty());
        HBox box = new HBox(10, text, tf);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private Node createModuleListBox() {
        MenuButton menu = new MenuButton("Module List");
        menu.setFont(Util.titleFont(20));
        for (ModuleBase.Module module : ModuleBase.Module.values()) {
            CheckBox cb = new CheckBox(module.toString());
            cb.setFont(Util.bodyFont(15));
            cb.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    model.bombCreationModuleListProperty().add(module);
                } else {
                    model.bombCreationModuleListProperty().remove(module);
                }
            });
            CustomMenuItem item = new CustomMenuItem(cb);
            item.setHideOnClick(false);
            menu.getItems().add(item);
        }
        return menu;
    }

    private Node createSeedBox() {
        CheckBox checkBox = new CheckBox("Custom seed:");
        checkBox.setFont(Util.titleFont(20));
        checkBox.selectedProperty().bindBidirectional(model.bombCreationSeededProperty());
        TextField tf = createIntField(model.bombCreationSeedProperty());
        tf.disableProperty().bind(checkBox.selectedProperty().not());
        HBox box = new HBox(10, checkBox, tf);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private static TextField createIntField(IntegerProperty modelProperty) {
        TextField tf = new TextField();
        tf.setMaxWidth(400);
        tf.setTextFormatter(new TextFormatter<>(new Util.PositiveIntegerStringConverter()));
        tf.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                tf.getTextFormatter().getValueConverter().fromString(newVal);
                tf.setBorder(null);
            } catch (NumberFormatException e) {
                tf.setBorder(Util.goodBorder(Color.RED));
            }
        });
        tf.setFont(Util.bodyFont(15));
        tf.textProperty().bindBidirectional(modelProperty, new Util.PositiveIntegerStringConverter());
        return tf;
    }

    private Node createButtonBox() {
        Button create = new Button("Create Bomb");
        create.setOnAction(event -> {
            Bomb b = new Bomb(
                    model.getBombCreationSeeded() ? new Random(model.getBombCreationSeed()) : new Random(),
                    model.getBombCreationAmount(),
                    model.getBombCreationTime(),
                    model.getBombCreationStrikes(),
                    model.getBombCreationModuleList().isEmpty() ? List.of(ModuleBase.Module.values()) : model.getBombCreationModuleList(),
                    bombExitAction,
                    "Custom"
            );
            newBomb.accept(b);
        });
        Button back = new Button("Back");
        back.setOnAction(event -> setViewMainMenu.run());
        HBox box = new HBox(10, create, back);
        box.setAlignment(Pos.CENTER);
        return box;
    }

}

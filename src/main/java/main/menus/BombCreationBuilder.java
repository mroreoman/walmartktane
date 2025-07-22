package main.menus;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Builder;

import java.util.Random;
import java.util.function.Consumer;

import main.Bomb;
import main.Util;

public class BombCreationBuilder implements Builder<Region> {
    private final KtaneModel model;
    private final Consumer<Bomb> newBomb;
    private final Runnable setViewMainMenu;
    private final Runnable bombExitAction;

    public BombCreationBuilder(KtaneModel model, Consumer<Bomb> newBomb, Runnable setViewMainMenu, Runnable bombExitAction) {
        this.model = model;
        this.newBomb = newBomb;
        this.setViewMainMenu = setViewMainMenu;
        this.bombExitAction = bombExitAction;
    }

    @Override
    public Region build() {
        VBox box = new VBox(
                createTitle(),
                createAmountBox(),
                createTimeBox(),
                createStrikeBox(),
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

    private Node createAmountBox() {
        Text text = new Text("How many modules?");
        text.setFont(Util.titleFont(20));
        TextField tf = createIntField();
        tf.textProperty().bindBidirectional(model.bombCreationAmountProperty(), new Util.PositiveIntegerStringConverter());
        HBox box = new HBox(10, text, tf);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private Node createTimeBox() {
        Text text = new Text("How much time?");
        text.setFont(Util.titleFont(20));
        TextField tf = createIntField();
        tf.textProperty().bindBidirectional(model.bombCreationTimeProperty(), new Util.PositiveIntegerStringConverter());
        HBox box = new HBox(10, text, tf);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private Node createStrikeBox() {
        Text text = new Text("How many strikes?");
        text.setFont(Util.titleFont(20));
        TextField tf = createIntField();
        tf.textProperty().bindBidirectional(model.bombCreationStrikesProperty(), new Util.PositiveIntegerStringConverter());
        HBox box = new HBox(10, text, tf);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private TextField createIntField() {
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
        return tf;
    }

    private Node createButtonBox() {
        Button create = new Button("Create Bomb");
        create.setOnAction(event -> {
            try {
                Bomb b = new Bomb(
                        new Random(),
                        model.getBombCreationAmount(),
                        model.getBombCreationTime(),
                        model.getBombCreationStrikes(),
                        bombExitAction,
                        "Custom"
                );
                newBomb.accept(b);
            } catch (NumberFormatException e) { System.out.println("Invalid input"); }
        });
        Button back = new Button("Back");
        back.setOnAction(event -> setViewMainMenu.run());
        HBox box = new HBox(10, create, back);
        box.setAlignment(Pos.CENTER);
        return box;
    }
}

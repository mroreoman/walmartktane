package main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.menus.KtaneController;

import java.util.Objects;

// new Application to replace KTANE.java
public class KTANE extends Application {
    private final KtaneController controller;

    public static void main(String[] args) {
        launch(args);
    }

    public KTANE() {
        controller = new KtaneController();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Walmart KTANE");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(KTANE.class.getResourceAsStream("walmartktaneicon.png"))));
        primaryStage.setWidth(750);
        primaryStage.setHeight(475);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(controller.getView()));
        primaryStage.show();
    }

    @Override
    public void stop() {
        controller.saveData();
    }
}

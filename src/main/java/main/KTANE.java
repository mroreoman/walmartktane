package main;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import main.modules.*;

public class KTANE extends Application {
  private static ArrayList<Bomb> bombs = new ArrayList<Bomb>();
  private static VBox bombButtons;
  private static Stage stage;
  private static Scene menuScene;

  private static TextField tf;
  
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    stage.setTitle("Walmart KTANE");
    // stage.getIcons().add(new Image(KTANE.class.getResourceAsStream("ktane.png"))); // TODO images no worky
    stage.setWidth(750);
    stage.setHeight(475);
    KTANE.stage = stage;
    
    Text welcome = new Text("Welcome to Walmart KTANE.");
    welcome.setFont(new Font("Roboto Slab", 50));
    Button buton = new Button("Start");
    buton.setScaleX(5);
    buton.setScaleY(5);
    buton.setOnAction(event -> openMenu());
    VBox root = new VBox(100, welcome, buton, new Text());
    root.setAlignment(Pos.CENTER);
    
    stage.setScene(new Scene(root));
    stage.show();
  }

  @Override
  public void stop() throws Exception {
    for (Bomb bomb: bombs) {
      bomb.stop();
    }
  }
  
  public static void openMenu() {
    if (menuScene == null) {
      initMenu();
    }
    bombButtons.getChildren().clear();
    for (Bomb bomb: bombs) {
      bombButtons.getChildren().add(bomb.getButton());
      bomb.updateButton();
    }
    stage.setScene(menuScene);
  }

  private static void createBomb() {
    Text title = new Text("BOMB CREATION");
    title.setFont(new Font("Roboto Slab", 50));
    Text amtText = new Text("How many modules?");
    amtText.setFont(new Font("Roboto Slab", 20));
    TextField amtField = new TextField();
    amtField.setMaxWidth(400);
    Util.setupIntField(amtField);
    Text timeText = new Text("How much time?");
    timeText.setFont(new Font("Robot Slab", 20));
    TextField timeField = new TextField();
    timeField.setMaxWidth(400);
    Util.setupIntField(timeField);
    Button create = new Button("Create Bomb");
    create.setOnAction(event -> {
      try {
        bombs.add(new Bomb(Integer.parseInt(amtField.getText()), Integer.parseInt(timeField.getText())));
        openMenu();
      } catch (NumberFormatException e) { System.out.println("Invalid input"); }
    });
    Button back = new Button("Back");
    back.setOnAction(event -> openMenu());
    HBox amount = new HBox(10, amtText, amtField);
    amount.setAlignment(Pos.CENTER);
    HBox time = new HBox(10, timeText, timeField);
    time.setAlignment(Pos.CENTER);
    HBox buttons = new HBox(10, create, back);
    buttons.setAlignment(Pos.CENTER);
    VBox box = new VBox(25, title, amount, time, buttons);
    box.setAlignment(Pos.CENTER);
    stage.setScene(new Scene(box));
  }

  private static void playBomb(int bombIndex) {
    try {
      bombs.get(bombIndex).play();
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Invalid bomb number");
    }
  }

  private static void quickPlay() {
    bombs.add(new Bomb());
    playBomb(bombs.size() - 1);
  }

  private static void storyMode() {
    Text title = new Text("STORY MODE");
    title.setFont(new Font("Roboto Slab", 50));

    Button theFirstBomb = new Button("The First Bomb");
    theFirstBomb.setOnAction(event -> {
      Class[] modules = {WiresModule.class, TheButtonModule.class, KeypadsModule.class};
      int[] numModules = {3};
      bombs.add(new Bomb(300, 3, numModules, modules));
      playBomb(bombs.size()-1);
    });
    
    Button back = new Button("Back");
    back.setOnAction(event -> openMenu());
    
    VBox box = new VBox(25, title, theFirstBomb, back);
    box.setAlignment(Pos.CENTER);
    stage.setScene(new Scene(box));
  }

  private static void initMenu() {
    Text title = new Text("WALMART KTANE MENU");
    title.setFont(new Font("Roboto Slab", 30));
    title.setX(200);
    title.setY(100);

    Button story = new Button("Story mode");
    story.setScaleX(1.75);
    story.setScaleY(1.75);
    story.setOnAction(event -> storyMode());
    
    Button create = new Button("Create bomb");
    create.setScaleX(1.75);
    create.setScaleY(1.75);
    create.setOnAction(event -> createBomb());
    
    Button quickPlay = new Button ("Quick play");
    quickPlay.setScaleX(1.75);
    quickPlay.setScaleY(1.75);
    quickPlay.setOnAction(event -> quickPlay());
    
    Button exit = new Button ("Exit");
    exit.setScaleX(1.75);
    exit.setScaleY(1.75) ;
    exit.setOnAction(event -> Platform.exit());
    
    VBox menuButtons = new VBox(25, story, create, quickPlay, exit);
    menuButtons.setAlignment(Pos.CENTER_LEFT);
    menuButtons.setLayoutX(100);
    menuButtons.setLayoutY(150);

    bombButtons = new VBox(10);
    bombButtons.setAlignment(Pos.CENTER_LEFT);
    
    ScrollPane bombButtonsBox = new ScrollPane(bombButtons);
    bombButtonsBox.setMinViewportWidth(150);
    bombButtonsBox.setLayoutX(400);
    bombButtonsBox.setLayoutY(150);
    
    Group root = new Group(title, menuButtons, bombButtonsBox);
    menuScene = new Scene(root);
  }

  public static Stage getStage() {
    return stage;
  }
  
}
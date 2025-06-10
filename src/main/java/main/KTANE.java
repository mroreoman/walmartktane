package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import main.StoryMode.StoryModeBomb;

public class KTANE extends Application {
  private static final List<Bomb> bombs = new ArrayList<>();
  private static VBox bombButtons;
  private static Stage stage;
  private static Scene mainMenuScene;
  private static Scene bombCreationScene;
  private static Scene storyModeScene;

  private static int chapterNum;
  private static Text title;
  private static VBox buttonBox;
  private static HBox pageFlipper;
  private static Button back;
  private static VBox box;
  
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    KTANE.stage = stage;
    initMainMenu();
    initBombCreation();
    initStoryMode();

    stage.setTitle("Walmart KTANE");
    stage.getIcons().add(new Image(Objects.requireNonNull(KTANE.class.getResourceAsStream("walmartktaneicon.png"))));
    stage.setWidth(750);
    stage.setHeight(475);
    stage.setScene(mainMenuScene);
    stage.show();
  }

  @Override
  public void stop() {
    for (Bomb bomb: bombs) {
      bomb.stop();
    }
  }
  
  public static void openMenu() {
    bombButtons.getChildren().clear();
    for (Bomb bomb: bombs) {
      bombButtons.getChildren().add(bomb.getButton(stage));
    }
    stage.setScene(mainMenuScene);
  }

  private static void quickPlay() {
    bombs.add(new Bomb(5, 300, 3));
    bombs.getLast().play(stage);
  }

  private static void initMainMenu() {
    Text title = new Text("WALMART KTANE MENU");
    title.setFont(new Font("Roboto Slab", 30));
    title.setX(200);
    title.setY(100);

    Button story = new Button("Story mode");
    story.setScaleX(1.75);
    story.setScaleY(1.75);
    story.setOnAction(event -> stage.setScene(storyModeScene));
    
    Button create = new Button("Create bomb");
    create.setScaleX(1.75);
    create.setScaleY(1.75);
    create.setOnAction(event -> stage.setScene(bombCreationScene));
    
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
    mainMenuScene = new Scene(root);
  }

  private static void initBombCreation() {
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
        bombs.add(new Bomb(Integer.parseInt(amtField.getText()), Integer.parseInt(timeField.getText()), 2));
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
    bombCreationScene = new Scene(box);
  }

  private static void initStoryMode() {
    title = new Text("STORY MODE");
    title.setFont(new Font("Roboto Slab", 50));
    
    chapterNum = 0;
    Text chapterTitle = new Text("Chapter " + (chapterNum+1));
    chapterTitle.setFont(new Font("Roboto Slab", 30));

    buttonBox = new VBox(10);
    buttonBox.setAlignment(Pos.CENTER);
    List<StoryModeBomb> chapter = StoryMode.ALL_CHAPTERS.get(chapterNum);
    for (StoryModeBomb storyModeBomb : chapter) {
      buttonBox.getChildren().add(createStoryModeButton(storyModeBomb));
    }

    Button previous = new Button("Previous");
    previous.setOnAction(event -> backward());

    Button next = new Button("Next");
    next.setOnAction(event -> forward());
    
    pageFlipper = new HBox(100, previous, next);
    pageFlipper.setAlignment(Pos.CENTER);

    back = new Button("Back");
    back.setOnAction(event -> openMenu());

    box = new VBox(10, title, chapterTitle, buttonBox, pageFlipper, back);
    box.setAlignment(Pos.CENTER);
    storyModeScene = new Scene(box);
  }

  private static void forward() {
    chapterNum++;
    updateGUI();
  }

  private static void backward() {
    chapterNum--;
    updateGUI();
  }

  private static void updateGUI() {
    buttonBox.getChildren().clear();
    box.getChildren().clear();

    Text chapterTitle = new Text("Chapter " + (chapterNum+1));
    chapterTitle.setFont(new Font("Roboto Slab", 30));

    List<StoryModeBomb> chapter = StoryMode.ALL_CHAPTERS.get(chapterNum);
    for (StoryModeBomb storyModeBomb : chapter) {
      buttonBox.getChildren().add(createStoryModeButton(storyModeBomb));
    }

    box.getChildren().addAll(title, chapterTitle, buttonBox, pageFlipper, back);
  }

  private static Button createStoryModeButton(StoryMode.StoryModeBomb storyModeBomb) {
    Button button = new Button(storyModeBomb.name());
    button.setOnAction(event -> {
      bombs.add(storyModeBomb.initialize());
      bombs.getLast().play(stage);
    });
    return button;
  }
  
}
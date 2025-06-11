package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import main.StoryMode.StoryModeBomb;

public class KTANE extends Application {
  private static final List<Bomb> bombs = new ArrayList<>();
  private static VBox bombButtons;
  private static Stage stage;
  private static Scene mainMenuScene;
  private static Scene bombCreationScene;
  private static Scene storyModeScene;

  private static int chapterNum;
  private static Text chapterTitle;
  private static VBox chapterButtons;
  private static Button prevChapter;
  private static Button nextChapter;

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
    stage.setResizable(false);
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
      Button buton = new Button(bomb.toString());
      buton.setOnAction(event -> {
        stage.setScene(bomb);
        bomb.play();
      });
      buton.setFont(Util.bodyFont(15));
      bombButtons.getChildren().add(buton);
    }
    stage.setScene(mainMenuScene);
  }

  private static void quickPlay() {
    bombs.add(new Bomb(5, 300, 3));
    stage.setScene(bombs.getLast());
    bombs.getLast().play();
  }

  private static void initMainMenu() {
    Text title = new Text("WALMART KTANE");
    title.setFont(Util.titleFont(45));

    Button story = new Button("Story mode");
    story.setFont(Util.bodyFont(25));
    story.setOnAction(event -> stage.setScene(storyModeScene));

    Button create = new Button("Create bomb");
    create.setFont(Util.bodyFont(25));
    create.setOnAction(event -> stage.setScene(bombCreationScene));

    Button quickPlay = new Button ("Quick play");
    quickPlay.setFont(Util.bodyFont(25));
    quickPlay.setOnAction(event -> quickPlay());

    VBox menuButtons = new VBox(25, story, create, quickPlay);

    bombButtons = new VBox(10);
    ScrollPane bombButtonsScroll = new ScrollPane(bombButtons);
    bombButtonsScroll.setMinViewportWidth(150);

    BorderPane root = new BorderPane();
    root.setTop(title);
    BorderPane.setAlignment(title, Pos.TOP_CENTER);
    BorderPane.setMargin(title, new Insets(10, 0, 20, 0));
    root.setLeft(menuButtons);
    root.setRight(bombButtonsScroll);
    root.setPadding(new Insets(10));
    mainMenuScene = new Scene(root);
  }

  private static void initBombCreation() {
    Text title = new Text("BOMB CREATION");
    title.setFont(Util.titleFont(50));

    Text amtText = new Text("How many modules?");
    amtText.setFont(Util.titleFont( 20));
    TextField amtField = new TextField();
    amtField.setMaxWidth(400);
    Util.setupIntField(amtField);
    HBox amount = new HBox(10, amtText, amtField);
    amount.setAlignment(Pos.CENTER);

    Text timeText = new Text("How much time?");
    timeText.setFont(Util.titleFont(20));
    TextField timeField = new TextField();
    timeField.setMaxWidth(400);
    Util.setupIntField(timeField);
    HBox time = new HBox(10, timeText, timeField);
    time.setAlignment(Pos.CENTER);

    Text strikeText = new Text("How many strikes?");
    strikeText.setFont(Util.titleFont(20));
    TextField strikeField = new TextField();
    strikeField.setMaxWidth(400);
    Util.setupIntField(strikeField);
    HBox strike = new HBox(10, strikeText, strikeField);
    strike.setAlignment(Pos.CENTER);

    Button create = new Button("Create Bomb");
    create.setOnAction(event -> {
      try {
        bombs.add(new Bomb(Integer.parseInt(amtField.getText()), Integer.parseInt(timeField.getText()), Integer.parseInt(strikeField.getText())));
        openMenu();
      } catch (NumberFormatException e) { System.out.println("Invalid input"); }
    });
    Button back = new Button("Back");
    back.setOnAction(event -> openMenu());
    HBox buttons = new HBox(10, create, back);
    buttons.setAlignment(Pos.CENTER);

    VBox box = new VBox(25, title, amount, time, strike, buttons);
    box.setAlignment(Pos.CENTER);
    bombCreationScene = new Scene(box);
  }

  private static void initStoryMode() {
    Text title = new Text("STORY MODE");
    title.setFont(Util.titleFont(50));

    chapterNum = 0;
    chapterTitle = new Text("Chapter " + (chapterNum+1));
    chapterTitle.setFont(Util.titleFont(30));

    chapterButtons = new VBox(10);
    chapterButtons.setMinHeight(235);
    chapterButtons.setMaxHeight(235);
    chapterButtons.setAlignment(Pos.CENTER);
    for (StoryModeBomb storyModeBomb : StoryMode.ALL_CHAPTERS.get(chapterNum)) {
      chapterButtons.getChildren().add(createStoryModeButton(storyModeBomb));
    }

    prevChapter = new Button("Previous");
    prevChapter.setOnAction(event -> {
      chapterNum--;
      refreshStoryMode();
    });
    prevChapter.setDisable(true);

    nextChapter = new Button("Next");
    nextChapter.setOnAction(event -> {
      chapterNum++;
      refreshStoryMode();
    });

    HBox pageFlipper = new HBox(100, prevChapter, nextChapter);
    pageFlipper.setAlignment(Pos.CENTER);

    Button back = new Button("Back");
    back.setOnAction(event -> openMenu());

    VBox box = new VBox(10, title, chapterTitle, chapterButtons, pageFlipper, back);
    box.setAlignment(Pos.CENTER);
    storyModeScene = new Scene(box);
  }

  private static void refreshStoryMode() {
    chapterTitle.setText("Chapter " + (chapterNum+1));

    chapterButtons.getChildren().clear();
    for (StoryModeBomb storyModeBomb : StoryMode.ALL_CHAPTERS.get(chapterNum)) {
      chapterButtons.getChildren().add(createStoryModeButton(storyModeBomb));
    }

    prevChapter.setDisable(chapterNum == 0);
    nextChapter.setDisable(chapterNum == StoryMode.ALL_CHAPTERS.size() - 1);
  }

  private static Button createStoryModeButton(StoryMode.StoryModeBomb storyModeBomb) {
    Button button = new Button(storyModeBomb.name());
    button.setOnAction(event -> {
      bombs.add(storyModeBomb.initialize());
      stage.setScene(bombs.getLast());
      bombs.getLast().play();
    });
    return button;
  }

}
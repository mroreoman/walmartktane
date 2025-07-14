package main.menus;


import javafx.beans.property.*;
import main.Bomb;

//TODO check if any other properties are needed
public class KtaneModel {
    private final ListProperty<Bomb> bombs = new SimpleListProperty<>();
    private final ObjectProperty<Bomb> currentBomb = new SimpleObjectProperty<>();
    private final IntegerProperty menuPage = new SimpleIntegerProperty(0);
    private final IntegerProperty storyModeChapter = new SimpleIntegerProperty(0);
    private final IntegerProperty bombCreationAmount = new SimpleIntegerProperty();
    private final IntegerProperty bombCreationTime = new SimpleIntegerProperty();
    private final IntegerProperty bombCreationStrikes = new SimpleIntegerProperty();

    public ListProperty<Bomb> bombsProperty() {
        return bombs;
    }

    //TODO fix bomb history (bombs.addList gives error)
    public void addBomb(Bomb bomb) {
        bombs.addLast(bomb);
    }

    //TODO add listener in KtaneController to play/pause the bomb
    public ObjectProperty<Bomb> currentBomb() {
        return currentBomb;
    }

    public void setCurrentBomb(Bomb bomb) {
        currentBomb.set(bomb);
    }

    public IntegerProperty menuPageProperty() {
        return menuPage;
    }

    public IntegerProperty storyModeChapterProperty() {
        return storyModeChapter;
    }

    public void setStoryModeChapter(int chapter) {
        storyModeChapter.set(chapter);
    }

    public IntegerProperty bombCreationAmountProperty() {
        return bombCreationAmount;
    }

    public int getBombCreationAmount() {
        return bombCreationAmount.get();
    }

    public IntegerProperty bombCreationTimeProperty() {
        return bombCreationTime;
    }

    public int getBombCreationTime() {
        return bombCreationTime.get();
    }

    public IntegerProperty bombCreationStrikesProperty() {
        return bombCreationStrikes;
    }

    public int getBombCreationStrikes() {
        return bombCreationStrikes.get();
    }
}

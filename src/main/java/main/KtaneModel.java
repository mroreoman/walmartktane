package main;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

//TODO check if any other properties are needed
public class KtaneModel {
    private final ListProperty<Bomb> bombs = new SimpleListProperty<>();
    private final ObjectProperty<Bomb> currentBomb = new SimpleObjectProperty<>();

    public void addBomb(Bomb bomb) {
        bombs.add(bomb);
    }

    public ObservableList<Bomb> getBombs() {
        return bombs.get();
    }

    //TODO add listener in KtaneController to play/pause the bomb
    public ObjectProperty<Bomb> currentBomb() {
        return currentBomb;
    }

    public void setCurrentBomb(Bomb bomb) {
        currentBomb.set(bomb);
    }
}

package com.github.mroreoman.menu;

import java.io.*;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;
import jakarta.json.stream.JsonParsingException;

import javafx.scene.layout.Region;

import com.github.mroreoman.game.Bomb;

public class MenuController {
    private final File data = new File(System.getenv("LOCALAPPDATA") + "/WalmartKTANE/data.json");
    private final MenuModel model;
    private final BaseView viewBuilder;

    public MenuController() {
        model = new MenuModel(loadData());
        viewBuilder = new BaseView(model);

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

    private JsonObject loadData() {
        if (!data.isFile()) {
            return JsonObject.EMPTY_JSON_OBJECT;
        }
        try (JsonReader reader = Json.createReader(new FileReader(data))) {
            return reader.readObject();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not open file " + data.getAbsolutePath());
        } catch (JsonParsingException e) {
            throw new RuntimeException("Could not parse file " + data.getAbsolutePath()); //TODO ask player if they want to close game & fix the file or continue playing and overwrite
        }
    }

    public void storeData() {
        if (!data.getParentFile().isDirectory()) {
            if (!data.getParentFile().mkdir()) {
                throw new RuntimeException("Could not create directory " + data.getParentFile().getAbsolutePath());
            }
        }
        assert data.getParentFile().isDirectory();

        if (!data.isFile()) {
            try {
                if (!data.createNewFile()) {
                    throw new IOException();
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not create file " + data.getParentFile().getAbsolutePath());
            }
        }
        assert data.isFile();

        try {
            JsonWriter writer = Json.createWriter(new FileWriter(data));
            writer.writeObject(model.getSaveData());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

package com.github.mroreoman.menu;

import java.io.*;

import javafx.scene.layout.Region;

import jakarta.json.Json;
import jakarta.json.JsonReader;
import jakarta.json.JsonStructure;
import jakarta.json.JsonWriter;

import com.github.mroreoman.game.Bomb;

public class MenuController {
    private final File data = new File("../data/test.json");
    private final MenuModel model;
    private final BaseView viewBuilder;

    public MenuController() {
        System.out.println(loadData()); //TODO do something with data
        model = new MenuModel();
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

    private JsonStructure loadData() {
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
                } else {
                    JsonWriter writer = Json.createWriter(new FileWriter(data));
                    writer.write(Json.createObjectBuilder().add("test", "start").build()); //TODO replace with blank data?
                    writer.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not create file " + data.getParentFile().getAbsolutePath());
            }
        }
        assert data.isFile();

        try (JsonReader reader = Json.createReader(new FileReader(data))) {
            return reader.read();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not open file " + data.getAbsolutePath());
        }
    }

    public void saveData() {
        try {
            JsonWriter writer = Json.createWriter(new FileWriter(data));
            writer.write(Json.createObjectBuilder().add("test", "done").build()); //TODO save actual data
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

package com.github.mroreoman.menu;

import java.io.*;

import javafx.scene.layout.Region;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
// import jakarta.json.JsonStructure;
import jakarta.json.JsonWriter;

import com.github.mroreoman.game.Bomb;

public class MenuController {
    private final File data = new File("../data/test.json"); // use this for local
    // private String localAppData = System.getenv("LOCALAPPDATA");
    // private final File data = new File(localAppData+"/WalmartKTANE/data/test.json"); // use this and localAppData for AppData
    private final MenuModel model;
    private final BaseView viewBuilder;

    public MenuController() {
        // System.out.println(loadData()); //TODO do something with data
        // System.out.println(data.getAbsolutePath());
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
            return null;
        }
        try (JsonReader reader = Json.createReader(new FileReader(data))) {
            return (JsonObject)reader.read();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not open file " + data.getAbsolutePath());
        }
    }

    public void storeData() {
        if (!data.getParentFile().isDirectory()) { // use this one to create data locally
            if (!data.getParentFile().mkdir()) {
                throw new RuntimeException("Could not create directory " + data.getParentFile().getAbsolutePath());
            }
        }

        // if (!data.getParentFile().isDirectory()) { // use this one to create data in AppData
        //     if (!data.getParentFile().getParentFile().isDirectory()) {
        //         if (!data.getParentFile().getParentFile().mkdir()) {
        //             throw new RuntimeException("Could not create directory " + data.getParentFile().getParentFile().getAbsolutePath());
        //         }
        //         if (!data.getParentFile().mkdir()) {
        //             throw new RuntimeException("Could not create directory " + data.getParentFile().getAbsolutePath());
        //         }
        //     } else if (!data.getParentFile().mkdir()) {
        //         throw new RuntimeException("Could not create directory " + data.getParentFile().getAbsolutePath());
        //     }
        // }
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

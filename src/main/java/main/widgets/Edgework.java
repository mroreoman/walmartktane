package main.widgets;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import main.Util;

public class Edgework extends VBox {
    private SerialNumber serial;
    private List<BatteryHolder> batteryHolders;
    private List<Indicator> indicators;
    private List<PortPlate> portPlates;

    public Edgework(Random rand) {
        this(rand, 6);
    }

    public Edgework(Random rand, int numWidgets) {
        initWidgets(rand, numWidgets);
        initGUI();
    }

    private void initWidgets(Random rand, int numWidgets) {
        serial = new SerialNumber(rand);
        batteryHolders = new ArrayList<>();
        indicators = new ArrayList<>();
        portPlates = new ArrayList<>();

        List<String> labels = new ArrayList<>(List.of(Indicator.LABELS));
        for (int i = 0; i < numWidgets - 1; i++) {
            switch (rand.nextInt(3)) {
                case 0 -> batteryHolders.add(new BatteryHolder(rand));
                case 1 -> indicators.add(new Indicator(rand, labels));
                case 2 -> portPlates.add(new PortPlate(rand));
            }
        }
    }

    private void initGUI() {
        Text serialText = new Text("Serial Number: " + serial.toString());
        Text batteryHolderText = new Text("Battery Holders: " + batteryHolders.toString());
        Text indicatorText = new Text("Indicators: " + indicators.toString());
        Text portPlateText = new Text("Port Plates: " + portPlates.toString());
        Util.setupTexts(serialText, batteryHolderText, indicatorText, portPlateText);
        getChildren().addAll(serialText, batteryHolderText, indicatorText, portPlateText);
    }

    public int lastSerialDigit() {
        return Integer.parseInt(serial.toString().substring(5));
    }

    public int numBatteries() {
        int temp = 0;
        for (BatteryHolder battery : batteryHolders) {
            temp += battery.getNumBatteries();
        }
        return temp;
    }

    public boolean hasIndicator(String label, boolean isLit) {
        return hasIndicator(new Indicator(label, isLit));
    }

    public boolean hasIndicator(Indicator target) {
        boolean temp = false;
        for (Indicator indicator : indicators) {
            temp |= indicator.equals(target);
        }
        return temp;
    }

    public boolean hasPort(String target) {
        boolean temp = false;
        for (PortPlate portPlate : portPlates) {
            temp |= (portPlate.getPorts().contains(target));
        }
        return temp;
    }

    public boolean serialHasVowel() {
        String vowels = "AEIOU";
        for (char let : vowels.toCharArray()) {
            if (serial.toString().indexOf(let) != -1) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return "Serial Number: " + serial.toString() + "\nBattery Holders: " + batteryHolders.toString() + "\nIndicators: " + indicators.toString() + "\nPort Plates: " + portPlates.toString();
    }

}
package com.github.mroreoman.game.widgets;

import java.util.Random;

public class BatteryHolder {
    private final String batteryType;
    private final int numBatteries;

    public BatteryHolder(Random rand) {
        batteryType = rand.nextBoolean() ? "D" : "AA";
        numBatteries = batteryType.equals("D") ? 1 : 2;
    }

    public int getNumBatteries() {
        return numBatteries;
    }

    public String toString() {
        return batteryType;
    }

}
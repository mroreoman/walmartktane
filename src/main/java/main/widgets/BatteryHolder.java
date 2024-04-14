package main.widgets;

import java.util.Random;

public class BatteryHolder implements Widget {
  private String batteryType;
  private int numBatteries;

  public BatteryHolder() {
    batteryType = new Random().nextBoolean() ? "D" : "AA";
    numBatteries = batteryType.equals("D") ? 1 : 2;
  }

  public int getNumBatteries() {
    return numBatteries;
  }

  public String toString() {
    return batteryType;
  }

}
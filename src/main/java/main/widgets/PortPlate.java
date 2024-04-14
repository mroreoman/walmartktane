package main.widgets;

import java.util.Random;
import java.util.ArrayList;

public class PortPlate implements Widget {
  private ArrayList<String> ports = new ArrayList<String>();

  public PortPlate() {
    Random rand = new Random();
    if (rand.nextBoolean()) {
      if (rand.nextBoolean()) {
        ports.add("PAR");
      }
      if (rand.nextBoolean()) {
        ports.add("SER");
      }
    } else {
      if (rand.nextBoolean()) {
        ports.add("RCA");
      }
      if (rand.nextBoolean()) {
        ports.add("PS2");
      }
      if (rand.nextBoolean()) {
        ports.add("DVI");
      }
      if (rand.nextBoolean()) {
        ports.add("RJ");
      }
    }
  }

  public ArrayList<String> getPortPlate() {
    return ports;
  }

  public String toString() {
    return ports.toString();
  }
  
}
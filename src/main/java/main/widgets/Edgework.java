package main.widgets;

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import main.Util;

public class Edgework extends VBox {
  private static final Random rand = new Random();
  
  private Widget[] widgets;
  
  private SerialNumber serial;
  private BatteryHolder[] batteryHolders;
  private Indicator[] indicators;
  private PortPlate[] portPlates;
  
  private Text serialText;
  private Text batteryHolderText;
  private Text indicatorText;
  private Text portPlateText;

  public Edgework() {
    this(6);
  }
  
  public Edgework(int numWidgets) {
    initWidgets(numWidgets);
    initTexts();
    getChildren().addAll(serialText, batteryHolderText, indicatorText, portPlateText);
  }

  private void initWidgets(int numWidgets) {
    ArrayList<BatteryHolder> batteryHolderAL = new ArrayList<BatteryHolder>();
    ArrayList<Indicator> indicatorAL = new ArrayList<Indicator>();
    ArrayList<PortPlate> portPlateAL = new ArrayList<PortPlate>();
    ArrayList<String> labels = new ArrayList<String>(Arrays.asList(Indicator.LABELS));
    
    widgets = new Widget[numWidgets];
    widgets[0] = new SerialNumber();
    
    for (int i = 1; i < widgets.length; i++) {
      int num = rand.nextInt(3);
      if (num == 0) {
        widgets[i] = new BatteryHolder();
        batteryHolderAL.add((BatteryHolder)widgets[i]);
      } else if (num == 1) {
        widgets[i] = new Indicator(labels);
        indicatorAL.add((Indicator)widgets[i]);
      } else if (num == 2) {
        widgets[i] = new PortPlate();
        portPlateAL.add((PortPlate)widgets[i]);
      }
    }

    serial = (SerialNumber)widgets[0];
    batteryHolders = new BatteryHolder[batteryHolderAL.size()];
    indicators = new Indicator[indicatorAL.size()];
    portPlates = new PortPlate[portPlateAL.size()];
    batteryHolders = batteryHolderAL.toArray(batteryHolders);
    indicators = indicatorAL.toArray(indicators);
    portPlates = portPlateAL.toArray(portPlates);
  }

  private void initTexts() {
    serialText = new Text("Serial Number: " + serial.toString());
    batteryHolderText = new Text("Battery Holders: " + Arrays.toString(batteryHolders));
    indicatorText = new Text("Indicators: " + Arrays.toString(indicators));
    portPlateText = new Text("Port Plates: " + Arrays.toString(portPlates));
    Util.setupTexts(serialText, batteryHolderText, indicatorText, portPlateText);
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
      temp |= (portPlate.getPortPlate().contains(target));
    }
    return temp;
  }

  public boolean serialHasVowel(){
    String vowels = "AEIOU";
    for (char let: vowels.toCharArray()) {
      if (serial.toString().indexOf(let) != -1) {
        return true;
      }
    }
    return false;
  }

  public String toString() {
    return "Serial Number: " + serial.toString() +
           "\nBattery Holders: " + Arrays.toString(batteryHolders) +
           "\nIndicators: " + Arrays.toString(indicators) +
           "\nPort Plates: " + Arrays.toString(portPlates);
  }
  
}
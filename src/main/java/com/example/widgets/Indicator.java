package com.example.widgets;

import java.util.Random;
import java.util.ArrayList;

public class Indicator implements Widget {
  public static final String[] LABELS = {"BOB", "CAR", "CLR", "FRK", "FRQ", "IND", "MSA", "NSA", "SIG", "SND", "TRN"};
  private static final Random rand = new Random();
  private String label;
  private boolean isLit;

  public Indicator() {
    label = LABELS[rand.nextInt(LABELS.length)];
    isLit = rand.nextBoolean();
  }

  public Indicator(ArrayList<String> labels) {
    label = labels.get(rand.nextInt(labels.size()));
    labels.remove(label);
    isLit = rand.nextBoolean();
  }

  public Indicator(String label, boolean isLit) {
    this.label = label;
    this.isLit = isLit;
  }

  public String toString() {
    return label + (isLit ? "*" : "");
  }

  public boolean equals(Indicator other) {
    return toString().equals(other.toString());
  }

  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + (null == label ? 0 : label.hashCode());
    hash = 31 * hash + (isLit ? 1 : 0);
    return hash;
  }

}
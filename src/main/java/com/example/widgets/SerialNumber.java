package com.example.widgets;

import java.util.Random;

public class SerialNumber implements Widget {
  private static final String LETTERS = "0123456789ABCDEFGHIJKLMNPQRTSUVWXZ"; //no O or Y
  private String serial;

  public SerialNumber() {
    Random rand = new Random();
    serial = "";
    serial += LETTERS.charAt(rand.nextInt(34));
    serial += LETTERS.charAt(rand.nextInt(34));
    serial += LETTERS.charAt(rand.nextInt(10));
    serial += LETTERS.charAt(rand.nextInt(24) + 10);
    serial += LETTERS.charAt(rand.nextInt(24) + 10);
    serial += LETTERS.charAt(rand.nextInt(10));
  }

  public SerialNumber(String serial) {
    this.serial = serial;
  }
  
  public String toString() {
    return serial;
  }
  
}
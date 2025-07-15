package main.widgets;

import java.util.Random;

public class SerialNumber implements Widget {
    private static final String CHARS = "0123456789ABCDEFGHIJKLMNPQRTSUVWXZ"; //no O or Y
    private String serial;

    public SerialNumber() {
        Random rand = new Random();
        serial = "";
        serial += CHARS.charAt(rand.nextInt(34));
        serial += CHARS.charAt(rand.nextInt(34));
        serial += CHARS.charAt(rand.nextInt(10));
        serial += CHARS.charAt(rand.nextInt(24) + 10);
        serial += CHARS.charAt(rand.nextInt(24) + 10);
        serial += CHARS.charAt(rand.nextInt(10));
    }

    public SerialNumber(String serial) {
        this.serial = serial;
    }

    public String toString() {
        return serial;
    }

}
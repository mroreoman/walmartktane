package main.widgets;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class PortPlate implements Widget {
    private final List<String> ports = new ArrayList<>();

    public PortPlate(Random rand) {
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

    public List<String> getPorts() {
        return Collections.unmodifiableList(ports);
    }

    public String toString() {
        return ports.toString();
    }

}
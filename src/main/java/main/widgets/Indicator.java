package main.widgets;

import java.util.List;
import java.util.Random;

public class Indicator implements Widget {
    public static final String[] LABELS = {"BOB", "CAR", "CLR", "FRK", "FRQ", "IND", "MSA", "NSA", "SIG", "SND", "TRN"};

    private final String label;
    private final boolean isLit;

    /**
     * Creates an indicator using a random label from the list, then removes the used label from the list
     *
     * @param labels list of possible labels
     */
    public Indicator(Random rand, List<String> labels) {
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
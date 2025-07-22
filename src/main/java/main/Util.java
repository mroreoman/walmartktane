package main;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.util.converter.NumberStringConverter;

public final class Util {
    public static Background simpleBackground(Color color) {
        return new Background(new BackgroundFill(color, null, null));
    }

    public static int[] removeIndex(int[] oldArr, int index) {
        int[] newArr = new int[oldArr.length - 1];
        int oldIt = 0;
        int newIt = 0;
        while (newIt < newArr.length) {
            if (oldIt == index) {
                oldIt++;
            }
            newArr[newIt++] = oldArr[oldIt++];
        }
        return newArr;
    }

    public static int[] goodUniqueIndexes(Random rand, int length, int num) {
        int[] arr = new int[length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        int[] indexes = new int[num];
        for (int i = 0; i < indexes.length; i++) {
            int index = rand.nextInt(arr.length);
            indexes[i] = arr[index];
            arr = removeIndex(arr, index);
        }
        return indexes;
    }

    public static int[] randomUniqueIndexes(Random rand, int[] oldArr, int num) {
        int[] newArr = new int[num];
        int i = 0;
        while (i < num) {
            int index = rand.nextInt(oldArr.length);
            newArr[i++] = oldArr[index];
            oldArr = removeIndex(oldArr, index);
        }
        return newArr;
    }

    public static int[] intAsArray(int num) {
        int[] arr = new int[num];
        for (int i = 0; i < num; i++) {
            arr[i] = i;
        }
        return arr;
    }

    public static Font titleFont(int size) {
        return Font.font("Roboto Slab", size);
    }

    public static Font bodyFont(int size) {
        return Font.font("Roboto Condensed", size);
    }

    public static void setupTexts(Text... texts) {
        for (Text text : texts) {
            text.setFont(bodyFont(15));
            text.setFill(Color.BLACK);
        }
    }

    public static Border goodBorder(Color color) {
        return new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(2), new Insets(-2)));
    }

    public static class PositiveIntegerStringConverter extends NumberStringConverter {
        Integer max;

        public PositiveIntegerStringConverter() {
        }

        public PositiveIntegerStringConverter(int max) {
            this.max = max;
        }

        public Integer fromString(String value) {
            Number newVal = super.fromString(value);
            if (newVal == null) {
                return null;
            }
            if (newVal.intValue() <= 0) {
                throw new NumberFormatException("Value must be positive");
            }
            if (max != null && newVal.intValue() > max) {
                throw new NumberFormatException("Value exceeds max of " + max);
            }
            return newVal.intValue();
        }
    }

}
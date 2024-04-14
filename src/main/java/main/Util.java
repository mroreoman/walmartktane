package main;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;

public final class Util {
  private static Random rand = new Random();
  
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

  public static Class[] removeIndex(Class[] oldArr, int index) {
    Class[] newArr = new Class[oldArr.length - 1];
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

  public static int[] goodUniqueIndexes(int length, int num) {
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
  
  public static int[] randomUniqueIndexes(int[] oldArr, int num){
    int[] newArr = new int[num];
    int i = 0;
    while (i < num){
      int index = rand.nextInt(oldArr.length);
      newArr[i++] = oldArr[index];
      oldArr = removeIndex(oldArr, index);
    }
    return newArr;
  }

  public static int[] intAsArray(int num){
    int[] arr = new int[num];
    for (int i = 0; i < num; i++){
      arr[i] = i;
    }
    return arr;
  }

  public static Class[] randomUniqueIndexes(Class[] oldArr, int num){
    Class[] newArr = new Class[num];
    int i = 0;
    while (i < num){
      int index = rand.nextInt(oldArr.length);
      newArr[i++] = oldArr[index];
      oldArr = removeIndex(oldArr, index);
    }
    return newArr;
  }

  public static void setupText(Text text) {
    text.setFont(Font.font("Roboto Condensed", 15));
    text.setFill(Color.BLACK);
  }

  public static void setupTexts(Text... texts) {
    for (Text text: texts) {
      setupText(text);
    }
  }

  public static String toMinutes(int timeSecs) {
    int mins = timeSecs / 60;
    int secs = timeSecs % 60;
    return mins + ":" + (secs < 10 ? ("0" + secs) : secs);
  }

  public static Border goodBorder(Color color) {
    return new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(2), new Insets(-2)));
  }
  
  public static Border goodBorder() {
    return new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(2), new Insets(-2)));
  }

  public static void setupIntField(TextField tf) {
    tf.setTextFormatter(new TextFormatter<>(new PositiveIntegerStringConverter()));
    tf.textProperty().addListener((obs,oldVal,newVal) -> {
      try {
        tf.getTextFormatter().getValueConverter().fromString(newVal);
        tf.setBorder(null);
      } catch (NumberFormatException e) {
        tf.setBorder(goodBorder());
      }
    });
  }

  //adds a PISC formatter to tf with max val & makes it have a border if invalid
  public static void setupIntField(TextField tf, int max) {
    tf.setTextFormatter(new TextFormatter<>(new PositiveIntegerStringConverter(max)));
    tf.textProperty().addListener((obs,oldVal,newVal) -> {
      try {
        tf.getTextFormatter().getValueConverter().fromString(newVal);
        tf.setBorder(null);
      } catch (NumberFormatException e) {
        tf.setBorder(goodBorder());
      }
    });
  }

  static class PositiveIntegerStringConverter extends StringConverter<Integer> {
    static IntegerStringConverter conv = new IntegerStringConverter();
    Integer max;

    public PositiveIntegerStringConverter() {}
    
    public PositiveIntegerStringConverter(int max) {
      this.max = max;
    }
    
    public Integer fromString(String value) {
      Integer newVal = conv.fromString(value);
      if (newVal == null) {
        return null;
      }
      if (newVal <= 0) {
        throw new NumberFormatException("Value must be positive");
      }
      if (max != null && newVal > max) {
        throw new NumberFormatException("Value exceeds max of " + max);
      }
      return newVal;
    }
    public String toString(Integer value) {
      return conv.toString(value);
    }
  }
  
}
package dev.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.JOptionPane;

public class Utilities {
  public static void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  public static double parseDouble(String value) {
    if (value.contains(",")) {
      value = value.replace(",", ".");
    }

    return Double.parseDouble(value);
  }

  public static String formatDouble(double value) {
    DecimalFormat englishDF = new DecimalFormat("#.#");
    englishDF.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

    return englishDF.format(value);
  }
}

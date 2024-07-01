package dev.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JOptionPane;

public class Utilities {
  private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
  private static final DecimalFormat englishDF = new DecimalFormat();

  static {
    englishDF.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
  }

  public static void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  public static double parseDouble(String value) {
    if (value.contains(",")) {
      value = value.replace(",", ".");
    }

    try {
      return Double.parseDouble(value);
    } catch (Exception e) {
      return -1;
    }
  }

  public static String formatDouble(double value) {
    return englishDF.format(value);
  }

  public static String formatCurrency(double value) {
    return "R$ %.2f".formatted(value);
  }

  public static String formatDate(String date) {
    try {
      return displayFormat.format(df.parse(date));
    } catch (Exception e) {
      return date;
    }
  }

  public static String formatDate(Date date) {
    return displayFormat.format(date);
  }

  public static String unformattedDate(String date) {
    try {
      return df.format(displayFormat.parse(date));
    } catch (Exception e) {
      return date;
    }
  }

  public static boolean isValidDate(String dateText) {
    try {
      displayFormat.parse(dateText);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}

package dev.util;

import javax.swing.JOptionPane;

public class SwingUtil {
  public static void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
  }
}

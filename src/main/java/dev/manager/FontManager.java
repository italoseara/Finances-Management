package dev.manager;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FontManager {
  private static final Map<String, Map<Integer, Map<Integer, Font>>> fontMap = new HashMap<>();

  public static void loadFont(String fontName, String path, int style)
      throws IOException, FontFormatException {
    URL resource = FontManager.class.getClassLoader().getResource(path);
    if (resource == null) {
      throw new IOException("Font not found: " + path);
    }

    InputStream stream = resource.openStream();
    if (stream == null) {
      throw new IOException("Font not found: " + path);
    }

    Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    ge.registerFont(font);

    var styleMap = fontMap.computeIfAbsent(fontName, k -> new HashMap<>());
    var sizeMap = styleMap.computeIfAbsent(style, k -> new HashMap<>());

    for (int size : new int[] {12, 14, 16, 18, 20, 24, 28, 32, 36, 40}) {
      sizeMap.put(size, font.deriveFont(style, size));
    }
  }

  public static Font getFont(String fontName, int style, int size) {
    var styleMap = fontMap.get(fontName);
    if (styleMap != null) {
      var sizeMap = styleMap.get(style);
      if (sizeMap != null) {
        Font font = sizeMap.get(size);
        if (font != null) {
          return font;
        }
      }
    }

    return new Font("Arial", style, size);
  }
}

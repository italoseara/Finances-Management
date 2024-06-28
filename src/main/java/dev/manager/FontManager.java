package dev.manager;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FontManager {
  private final Map<String, Map<Integer, Map<Integer, Font>>> fontMap = new HashMap<>();

  public void loadFont(String fontName, String path, int style)
      throws IOException, FontFormatException {
    InputStream stream =
        Objects.requireNonNull(getClass().getClassLoader().getResource(path)).openStream();

    if (stream == null) {
      throw new IOException("Font not found: " + path);
    }

    Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    ge.registerFont(font);

    Map<Integer, Map<Integer, Font>> styleMap =
        fontMap.computeIfAbsent(fontName, k -> new HashMap<>());
    Map<Integer, Font> sizeMap = styleMap.computeIfAbsent(style, k -> new HashMap<>());

    for (int size : new int[] {12, 14, 16, 18, 20, 24, 28, 32, 36, 40}) {
      sizeMap.put(size, font.deriveFont(style, size));
    }
  }

  public Font getFont(String fontName, int style, int size) {
    Map<Integer, Map<Integer, Font>> styleMap = fontMap.get(fontName);
    if (styleMap != null) {
      Map<Integer, Font> sizeMap = styleMap.get(style);

      if (sizeMap != null) {
        Font font = sizeMap.get(size);

        if (font != null) {
          return font;
        }
      }
    }

    throw new IllegalArgumentException("Font not found: " + fontName + " " + style + " " + size);
  }
}

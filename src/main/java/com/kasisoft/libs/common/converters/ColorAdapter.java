package com.kasisoft.libs.common.converters;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.text.*;

import com.kasisoft.libs.common.*;

import javax.validation.constraints.*;

import java.util.*;

import java.awt.*;

/**
 * Adapter for Color values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ColorAdapter extends AbstractConverter<String, Color> {

  private static final String RGB = "rgb";
  
  private Map<String, Color>   colors;
  
  public ColorAdapter() {
    colors      = new HashMap<>();
    var fields  = Color.class.getFields();
    for (var field : fields) {
      try {
        var color = (Color) field.get(null);
        colors.put(field.getName().toLowerCase(), color);
      } catch (Exception ex) { 
        // uninteresting Field
      }
    }
  }
  
  public ColorAdapter clear() {
    colors.clear();
    return this;
  }
  
  public ColorAdapter withColor(@NotNull String name, @NotNull Color color) {
    colors.put(name, color);
    return this;
  }

  @Override
  protected String encodeImpl(@NotNull Color v) {
    return 
      String.format( 
        "#%02x%02x%02x%02x", 
        Integer.valueOf(v.getAlpha()),
        Integer.valueOf(v.getRed()), 
        Integer.valueOf(v.getGreen()), 
        Integer.valueOf(v.getBlue())
      );
  }

  @Override
  protected Color decodeImpl(@NotNull String v) {
    if (v.startsWith("#")) {
      return unmarshalNumerical(v);
    } else if (v.toLowerCase().startsWith(RGB)) {
      return unmarshalArguments(v);
    } else {
      return symbolicNamed(v);
    }
  }

  /**
   * Returns the Color associated with a specific name. 
   * 
   * @param v   The name of the Color.
   * 
   * @return   The Color associated with the name.
   */
  private @NotNull Color symbolicNamed(@NotNull String v) {
    var lower = v.toLowerCase();
    if (colors.containsKey(lower)) {
      return colors.get( lower );
    }
    throw new KclException(error_invalid_color, v);
  }
  
  /**
   * Transforms an RGB(r,g,b[,a]) expression into a Color instance.
   * 
   * @param v   The RGB(r,g,b[,a]) expression.
   * 
   * @return   The Color if it could be converted.
   */
  private @NotNull Color unmarshalArguments(@NotNull String v) {
    String part = StringFunctions.cleanup(v.substring(RGB.length()));
    if( (part != null) && part.startsWith("(") && part.endsWith(")")) {
      part = StringFunctions.cleanup(part.substring(1, part.length() - 1));
      if (part != null) {
        String[] parts = part.split(",");
        if (parts.length == 3) {
          // rgb only
          return new Color(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        } else if (parts.length == 4) {
          // argb
          return new Color(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
      }
    }
    throw new KclException(error_invalid_color, v);
  }

  /**
   * Converts a numerical Color representatino into a Color instance.
   * 
   * @param v   A numerical Color representation.
   * 
   * @return   The corresponding Color instance.
   */
  private @NotNull Color unmarshalNumerical(@NotNull String v) {
    if (v.length() == 7) {
      // rgb only
      String red    = v.substring(1, 3);
      String green  = v.substring(3, 5);
      String blue   = v.substring(5, 7);
      return new Color(Integer.parseInt(red, 16), Integer.parseInt(green, 16), Integer.parseInt(blue, 16));
    } else if (v.length() == 9) {
      // argb
      String alpha  = v.substring(1, 3);
      String red    = v.substring(3, 5);
      String green  = v.substring(5, 7);
      String blue   = v.substring(7, 9);
      return new Color(Integer.parseInt(red, 16), Integer.parseInt(green, 16), Integer.parseInt(blue, 16), Integer.parseInt(alpha, 16));
    }
    throw new KclException(error_invalid_color, v);
  }
  
} /* ENDCLASS */

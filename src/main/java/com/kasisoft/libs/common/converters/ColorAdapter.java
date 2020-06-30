package com.kasisoft.libs.common.converters;

import static com.kasisoft.libs.common.internal.Messages.error_invalid_color;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.text.StringFunctions;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.HashMap;
import java.util.Map;

import java.awt.Color;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Adapter for Color values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColorAdapter extends AbstractConverter<String, Color> {

  private static final String RGB = "rgb";
  
  Map<String, Color>   colors;
  
  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
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
  protected @Null String encodeImpl(@NotNull Color v) {
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
  protected @Null Color decodeImpl(@NotNull String v) {
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
   * @param v   The name of the Color. Neither <code>null</code> nor empty.
   * 
   * @return   The Color associated with the name. Not <code>null</code>.
   * 
   * @throws Exception   The name could not be recognized.
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
   * @param v   The RGB(r,g,b[,a]) expression. Neither <code>null</code> nor empty.
   * 
   * @return   The Color if it could be converted. Not <code>null</code>.
   * 
   * @throws KclException   The conversion failed for some reason.
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
   * @param v   A numerical Color representation. Neither <code>null</code> nor empty.
   * 
   * @return   The corresponding Color instance. Not <code>null</code>.
   * 
   * @throws KclException   The conversion failed for some reason.
   */
  private Color unmarshalNumerical(@NotNull String v) {
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

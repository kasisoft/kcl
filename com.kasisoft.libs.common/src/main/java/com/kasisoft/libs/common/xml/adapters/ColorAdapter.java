package com.kasisoft.libs.common.xml.adapters;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.util.*;

import com.kasisoft.libs.common.base.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.*;

import java.lang.reflect.*;

import java.awt.*;

/**
 * Adapter used to convert a String into a Color and vice versa.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColorAdapter extends TypeAdapter<String,Color> {

  static final String RGB = "rgb";
  
  Map<String,Color>   colors;
  
  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   */
  public ColorAdapter() {
    this( null, null, null );
  }
  
  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public ColorAdapter( BiConsumer<Object,Exception> handler, String defval1, Color defval2 ) {
    super( handler, defval1, defval2 );
    colors          = new Hashtable<>();
    Field[] fields  = Color.class.getFields();
    for( Field field : fields ) {
      try {
        Color color = (Color) field.get( null );
        colors.put( field.getName().toLowerCase(), color );
      } catch( IllegalArgumentException | IllegalAccessException | ClassCastException ex ) { 
        // uninteresting Field
      }
    }
  }

  @Override
  protected String marshalImpl( @NonNull Color v ) {
    return 
      String.format( 
        "#%02x%02x%02x%02x", 
        Integer.valueOf( v.getAlpha() ),
        Integer.valueOf( v.getRed() ), 
        Integer.valueOf( v.getGreen() ), 
        Integer.valueOf( v.getBlue() )
      );
  }

  @Override
  protected Color unmarshalImpl( @NonNull String v ) throws Exception {
    if( v.startsWith( "#" ) ) {
      return unmarshalNumerical( v );
    } else if( v.toLowerCase().startsWith( RGB ) ) {
      return unmarshalArguments( v );
    } else {
      return symbolicNamed( v );
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
  private Color symbolicNamed( String v ) throws Exception {
    String lower = v.toLowerCase();
    if( colors.containsKey( lower ) ) {
      return colors.get( lower );
    }
    throw FailureCode.ConversionFailure.newException( invalid_color.format( v ) );
  }
  
  /**
   * Transforms an RGB(r,g,b[,a]) expression into a Color instance.
   * 
   * @param v   The RGB(r,g,b[,a]) expression. Neither <code>null</code> nor empty.
   * 
   * @return   The Color if it could be converted. Not <code>null</code>.
   * 
   * @throws FailureException   The conversion failed for some reason.
   */
  private Color unmarshalArguments( String v ) throws Exception {
    String part = StringFunctions.cleanup( v.substring( RGB.length() ) );
    if( (part != null) && part.startsWith( "(" ) && part.endsWith( ")" ) ) {
      part = StringFunctions.cleanup( part.substring( 1, part.length() - 1 ) );
      if( part != null ) {
        String[] parts = part.split( "," );
        if( parts.length == 3 ) {
          // rgb only
          return new Color( Integer.parseInt( parts[0] ), Integer.parseInt( parts[1] ), Integer.parseInt( parts[2] ) );
        } else if( parts.length == 4 ) {
          // argb
          return new Color( Integer.parseInt( parts[0] ), Integer.parseInt( parts[1] ), Integer.parseInt( parts[2] ), Integer.parseInt( parts[3] ) );
        }
      }
    }
    throw FailureCode.ConversionFailure.newException( invalid_color.format( v ) );
  }

  /**
   * Converts a numerical Color representatino into a Color instance.
   * 
   * @param v   A numerical Color representation. Neither <code>null</code> nor empty.
   * 
   * @return   The corresponding Color instance. Not <code>null</code>.
   * 
   * @throws FailureException   The conversion failed for some reason.
   */
  private Color unmarshalNumerical( String v ) throws Exception {
    if( v.length() == 7 ) {
      // rgb only
      String red    = v.substring( 1, 3 );
      String green  = v.substring( 3, 5 );
      String blue   = v.substring( 5, 7 );
      return new Color( Integer.parseInt( red, 16 ), Integer.parseInt( green, 16 ), Integer.parseInt( blue, 16 ) );
    } else if( v.length() == 9 ) {
      // argb
      String alpha  = v.substring( 1, 3 );
      String red    = v.substring( 3, 5 );
      String green  = v.substring( 5, 7 );
      String blue   = v.substring( 7, 9 );
      return new Color( Integer.parseInt( red, 16 ), Integer.parseInt( green, 16 ), Integer.parseInt( blue, 16 ), Integer.parseInt( alpha, 16 ) );
    } else {
      throw FailureCode.ConversionFailure.newException( invalid_color.format( v ) );
    }
  }
  
} /* ENDCLASS */

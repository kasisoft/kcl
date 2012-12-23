/**
 * Name........: ColorAdapter
 * Description.: Adapter used to convert a String into a Color and vice versa. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;


import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.util.*;

import java.util.*;

import java.awt.*;

import java.lang.reflect.*;

/**
 * Adapter used to convert a String into a Color and vice versa.
 */
public class ColorAdapter extends NullSafeAdapter<String,Color> {

  private static final String MSG_INVALIDCOLOR  = "%s is not a valid Color";

  private static final String RGB = "rgb";
  
  private Map<String,Color>   colors;
  
  /**
   * Initialises this adapter used to convert a Color.
   */
  public ColorAdapter() {
    colors          = new Hashtable<String,Color>();
    Field[] fields  = Color.class.getFields();
    for( Field field : fields ) {
      try {
        Color color = (Color) field.get( null );
        colors.put( field.getName().toLowerCase(), color );
      } catch( IllegalArgumentException ex ) { // uninteresting Field
      } catch( IllegalAccessException   ex ) { // uninteresting Field
      } catch( ClassCastException       ex ) { // uninteresting Field
      }
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected String marshalImpl( Color v ) {
    return 
      String.format( 
        "#%02x%02x%02x%02x", 
        Integer.valueOf( v.getAlpha() ),
        Integer.valueOf( v.getRed() ), 
        Integer.valueOf( v.getGreen() ), 
        Integer.valueOf( v.getBlue() )
      );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Color unmarshalImpl( String v ) throws Exception {
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
    throw new FailureException( FailureCode.ConversionFailure, String.format( MSG_INVALIDCOLOR, v ) );
  }
  
  /**
   * Transforms an RGB(r,g,b[,a]) expression into a Color instance.
   * 
   * @param v   The RGB(r,g,b[,a]) expression. Neither <code>null</code> nor empty.
   * 
   * @return   The Color if it could be converted. Not <code>null</code>.
   * 
   * @throws Exception   The conversion failed for some reason.
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
    throw new FailureException( FailureCode.ConversionFailure, String.format( MSG_INVALIDCOLOR, v ) );
  }

  /**
   * Converts a numerical Color representatino into a Color instance.
   * 
   * @param v   A numerical Color representation. Neither <code>null</code> nor empty.
   * 
   * @return   The corresponding Color instance. Not <code>null</code>.
   * 
   * @throws Exception   The conversion failed for some reason.
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
      throw new FailureException( FailureCode.ConversionFailure, String.format( MSG_INVALIDCOLOR, v ) );
    }
  }
  
} /* ENDCLASS */
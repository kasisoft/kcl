/**
 * Name........: RectangleAdapter
 * Description.: Adapter used to convert a String into a Rectangle and vice versa. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml.adapters;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.libs.common.util.*;

import com.kasisoft.lgpl.libs.common.base.*;
import com.kasisoft.lgpl.tools.diagnostic.*;

import java.util.regex.*;

import java.awt.*;

/**
 * Adapter used to convert a String into a Point and vice versa.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class RectangleAdapter extends NullSafeAdapter<String,Rectangle> {

  private static final String MSG_INVALIDRECTANGLE  = "%s is not a valid Rectangle";
  
  private String   delimiter;

  /**
   * Initialises this adapter with the default delimiter ','.
   */
  public RectangleAdapter() {
    this( null );
  }

  /**
   * Initialises this adapter with the supplied delimiter.
   * 
   * @param delim   The delimiter to be used for the textual representation.
   *                If <code>null</code> or empty the default ',' is used.
   */
  public RectangleAdapter( String delim ) {
    delimiter = StringFunctions.cleanup( delim );
    if( delimiter == null ) {
      delimiter = ",";
    }
  }
  
  /**
   * {@inheritDoc}
   */
  protected String marshalImpl( Rectangle v ) throws Exception {
    return String.format( "%d%s%d%s%d%s%d", Integer.valueOf( v.x ), delimiter, Integer.valueOf( v.y ), delimiter, Integer.valueOf( v.width ), delimiter, Integer.valueOf( v.height ) );
  }

  /**
   * {@inheritDoc}
   */
  protected Rectangle unmarshalImpl( String v ) throws Exception {
    String[] parts = v.split( Pattern.quote( delimiter ) );
    if( (parts == null) || (parts.length != 4) ) {
      throw new FailureException( FailureCode.ConversionFailure, String.format( MSG_INVALIDRECTANGLE, v ) );
    }
    int x = Integer.parseInt( parts[0] );
    int y = Integer.parseInt( parts[1] );
    int w = Integer.parseInt( parts[2] );
    int h = Integer.parseInt( parts[3] );
    return new Rectangle( x, y, w, h );
  }

} /* ENDCLASS */

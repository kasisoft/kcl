/**
 * Name........: PointAdapter
 * Description.: Adapter used to convert a String into a Point and vice versa. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;


import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.util.*;

import java.util.regex.*;

import java.awt.*;

/**
 * Adapter used to convert a String into a Point and vice versa.
 */
public class PointAdapter extends NullSafeAdapter<String,Point> {

  private static final String MSG_INVALIDPOINT  = "%s is not a valid Point";
  
  private String   delimiter;

  /**
   * Initialises this adapter with the default delimiter ','.
   */
  public PointAdapter() {
    this( null );
  }

  /**
   * Initialises this adapter with the supplied delimiter.
   * 
   * @param delim   The delimiter to be used for the textual representation. If <code>null</code> or empty the default 
   *                ',' is used.
   */
  public PointAdapter( String delim ) {
    delimiter = StringFunctions.cleanup( delim );
    if( delimiter == null ) {
      delimiter = ",";
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected String marshalImpl( Point v ) throws Exception {
    return String.format( "%d%s%d", Integer.valueOf( v.x ), delimiter, Integer.valueOf( v.y ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Point unmarshalImpl( String v ) throws Exception {
    String[] parts = v.split( Pattern.quote( delimiter ) );
    if( (parts == null) || (parts.length != 2) ) {
      throw new FailureException( FailureCode.ConversionFailure, String.format( MSG_INVALIDPOINT, v ) );
    }
    int x = Integer.parseInt( parts[0] );
    int y = Integer.parseInt( parts[1] );
    return new Point( x, y );
  }

} /* ENDCLASS */

/**
 * Name........: InsetsAdapter
 * Description.: Adapter used to convert a String into a Insets and vice versa. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml.adapters;

import com.kasisoft.lgpl.libs.common.util.*;
import com.kasisoft.lgpl.libs.common.base.*;

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.util.regex.*;

import java.awt.*;

/**
 * Adapter used to convert a String into a Insets and vice versa.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class InsetsAdapter extends NullSafeAdapter<String,Insets> {

  private static final String MSG_INVALIDINSETS  = "%s is not a valid Insets";
  
  private String   delimiter;

  /**
   * Initialises this adapter with the default delimiter ','.
   */
  public InsetsAdapter() {
    this( null );
  }

  /**
   * Initialises this adapter with the supplied delimiter.
   * 
   * @param delim   The delimiter to be used for the textual representation.
   *                If <code>null</code> or empty the default ',' is used.
   */
  public InsetsAdapter( String delim ) {
    delimiter = StringFunctions.cleanup( delim );
    if( delimiter == null ) {
      delimiter = ",";
    }
  }
  
  /**
   * {@inheritDoc}
   */
  protected String marshalImpl( Insets v ) throws Exception {
    return String.format( "%d%s%d%s%d%s%d", Integer.valueOf( v.top ), delimiter, Integer.valueOf( v.left ), delimiter, Integer.valueOf( v.bottom ), delimiter, Integer.valueOf( v.right ) );
  }

  /**
   * {@inheritDoc}
   */
  protected Insets unmarshalImpl( String v ) throws Exception {
    String[] parts = v.split( Pattern.quote( delimiter ) );
    if( (parts == null) || (parts.length != 4) ) {
      throw new FailureException( FailureCode.ConversionFailure, String.format( MSG_INVALIDINSETS, v ) );
    }
    int top     = Integer.parseInt( parts[0] );
    int left    = Integer.parseInt( parts[1] );
    int bottom  = Integer.parseInt( parts[2] );
    int right   = Integer.parseInt( parts[3] );
    return new Insets( top, left, bottom, right );
  }

} /* ENDCLASS */

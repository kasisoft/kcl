/**
 * Name........: ListTypeAdapter
 * Description.: Adapter used to convert a String into a data structure which consists of a delimited list. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.util.*;

import java.util.regex.*;

/**
 * Adapter used to convert a String into a data structure which consists of a delimited list.
 */
public abstract class ListTypeAdapter<T> extends TypeAdapter<String,T> {

  private String   delimiter;
  private String   quoted;
  private int      count;
  
  /**
   * Initialises this adapter with the default delimiter ','.
   */
  public ListTypeAdapter( int size ) {
    this( null, null, null, size, null );
  }

  /**
   * Initialises this adapter with the supplied delimiter.
   * 
   * @param delim   The delimiter to be used for the textual representation. If <code>null</code> or empty the default 
   *                ',' is used.
   */
  public ListTypeAdapter( int size, String delim ) {
    this( null, null, null, size, delim );
  }

  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public ListTypeAdapter( SimpleErrorHandler handler, String defval1, T defval2, int size ) {
    this( handler, defval1, defval2, size, null );
  }

  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   * @param delim     The delimiter to be used for the textual representation. If <code>null</code> or empty the default 
   *                  ',' is used.
   */
  public ListTypeAdapter( SimpleErrorHandler handler, String defval1, T defval2, int size, String delim ) {
    super( handler, defval1, defval2 );
    delimiter = StringFunctions.cleanup( delim );
    if( delimiter == null ) {
      delimiter = ",";
    }
    quoted    = Pattern.quote( delimiter );
    count     = size;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected T unmarshalImpl( String v ) throws Exception {
    String[] parts = v.split( quoted );
    if( (parts == null) || (parts.length != count) ) {
      throw new FailureException( FailureCode.ConversionFailure, v );
    }
    return unmarshalListImpl( parts );
  }

  protected String marshalListImpl( Object ... elements ) throws Exception {
    if( elements.length > 0 ) {
      StringBuffer buffer = new StringBuffer();
      for( int i = 0; i < elements.length; i++ ) {
        buffer.append( delimiter );
        buffer.append( elements[i] );
      }
      buffer.delete( 0, delimiter.length() );
      return buffer.toString();
    } else {
      return "";
    }
  }

  protected abstract T unmarshalListImpl( String[] v ) throws Exception;

} /* ENDCLASS */

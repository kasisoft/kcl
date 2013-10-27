/**
 * Name........: StructuralTypeAdapter
 * Description.: Adapter used to convert a String into a data structure which consists of a delimited list. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.util.*;

/**
 * Adapter used to convert a String into a data structure which consists of a delimited list.
 */
@SuppressWarnings("deprecation")
public abstract class StructuralTypeAdapter<T> extends ListTypeAdapter<T> {

  /**
   * Initialises this adapter with the default delimiter ','.
   */
  public StructuralTypeAdapter( int size ) {
    super( size );
  }

  /**
   * Initialises this adapter with the supplied delimiter.
   * 
   * @param delim   The delimiter to be used for the textual representation. If <code>null</code> or empty the default 
   *                ',' is used.
   */
  public StructuralTypeAdapter( int size, String delim ) {
    super( size, delim );
  }

  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public StructuralTypeAdapter( SimpleErrorHandler handler, String defval1, T defval2, int size ) {
    super( handler, defval1, defval2, size );
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
  public StructuralTypeAdapter( SimpleErrorHandler handler, String defval1, T defval2, int size, String delim ) {
    super( handler, defval1, defval2, size, delim );
  }

} /* ENDCLASS */

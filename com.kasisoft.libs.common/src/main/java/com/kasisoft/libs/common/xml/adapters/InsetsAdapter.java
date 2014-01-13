/**
 * Name........: InsetsAdapter
 * Description.: Adapter used to convert a String into a Insets and vice versa. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.util.*;

import java.awt.*;

import lombok.*;

/**
 * Adapter used to convert a String into a Insets and vice versa.
 */
public class InsetsAdapter extends StructuralTypeAdapter<Insets> {

  /**
   * Initialises this adapter with the default delimiter ','.
   */
  public InsetsAdapter() {
    this( null, null, null, null );
  }

  /**
   * Initialises this adapter with the supplied delimiter.
   * 
   * @param delim   The delimiter to be used for the textual representation. If <code>null</code> or empty the default 
   *                ',' is used.
   */
  public InsetsAdapter( String delim ) {
    this( null, null, null, delim );
  }

  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public InsetsAdapter( SimpleErrorHandler handler, String defval1, Insets defval2 ) {
    this( handler, defval1, defval2, null );
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
  public InsetsAdapter( SimpleErrorHandler handler, String defval1, Insets defval2, String delim ) {
    super( handler, defval1, defval2, 4, delim );
  }

  @Override
  @SuppressWarnings("deprecation")
  protected Insets unmarshalListImpl( @NonNull String[] v ) throws Exception {
    int top     = Integer.parseInt( v[0] );
    int left    = Integer.parseInt( v[1] );
    int bottom  = Integer.parseInt( v[2] );
    int right   = Integer.parseInt( v[3] );
    return new Insets( top, left, bottom, right );
  }

  @Override
  @SuppressWarnings("deprecation")
  protected String marshalImpl( @NonNull Insets v ) throws Exception {
    return marshalListImpl( 
      Integer.valueOf( v.top    ),
      Integer.valueOf( v.left   ),
      Integer.valueOf( v.bottom ),
      Integer.valueOf( v.right  )
    );
  }

} /* ENDCLASS */

package com.kasisoft.libs.common.xml.adapters;

import lombok.*;

import java.util.function.*;

import java.awt.*;

/**
 * Adapter used to convert a String into a Point and vice versa.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class RectangleAdapter extends StructuralTypeAdapter<Rectangle> {

  /**
   * Initialises this adapter with the default delimiter ','.
   */
  public RectangleAdapter() {
    this( null, null, null, null );
  }

  /**
   * Initialises this adapter with the supplied delimiter.
   * 
   * @param delim   The delimiter to be used for the textual representation. If <code>null</code> or empty the default 
   *                ',' is used.
   */
  public RectangleAdapter( String delim ) {
    this( null, null, null, delim );
  }

  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public RectangleAdapter( BiConsumer<Object,Exception> handler, String defval1, Rectangle defval2 ) {
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
  public RectangleAdapter( BiConsumer<Object,Exception> handler, String defval1, Rectangle defval2, String delim ) {
    super( handler, defval1, defval2, 4, delim );
  }

  @Override
  protected Rectangle unmarshalListImpl( @NonNull String[] v ) throws Exception {
    int x = Integer.parseInt( v[0] );
    int y = Integer.parseInt( v[1] );
    int w = Integer.parseInt( v[2] );
    int h = Integer.parseInt( v[3] );
    return new Rectangle( x, y, w, h );
  }

  @Override
  protected String marshalImpl( @NonNull Rectangle v ) throws Exception {
    return marshalListImpl(
      Integer.valueOf( v.x      ),
      Integer.valueOf( v.y      ),
      Integer.valueOf( v.width  ),
      Integer.valueOf( v.height )
    );
  }

} /* ENDCLASS */

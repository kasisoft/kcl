package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.util.*;

import java.awt.*;

import lombok.*;

/**
 * Adapter used to convert a String into a Point and vice versa.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PointAdapter extends StructuralTypeAdapter<Point> {

  /**
   * Initialises this adapter with the default delimiter ','.
   */
  public PointAdapter() {
    this( null, null, null, null );
  }

  /**
   * Initialises this adapter with the supplied delimiter.
   * 
   * @param delim   The delimiter to be used for the textual representation. If <code>null</code> or empty the default 
   *                ',' is used.
   */
  public PointAdapter( String delim ) {
    this( null, null, null, delim );
  }

  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public PointAdapter( SimpleErrorHandler handler, String defval1, Point defval2 ) {
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
  public PointAdapter( SimpleErrorHandler handler, String defval1, Point defval2, String delim ) {
    super( handler, defval1, defval2, 2, delim );
  }

  @Override
  protected Point unmarshalListImpl( @NonNull String[] v ) throws Exception {
    int x = Integer.parseInt( v[0] );
    int y = Integer.parseInt( v[1] );
    return new Point( x, y );
  }

  @Override
  protected String marshalImpl( @NonNull Point v ) throws Exception {
    return marshalListImpl(
      Integer.valueOf( v.x ),
      Integer.valueOf( v.y )
    );
  }
  
} /* ENDCLASS */

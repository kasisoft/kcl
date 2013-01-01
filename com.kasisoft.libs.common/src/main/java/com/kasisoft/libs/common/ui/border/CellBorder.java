/**
 * Name........: CellBorder
 * Description.: The CellBorder is similar to the borders known from spreadsheets where each edge can be selectively 
 *               enabled. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.ui.border;

import javax.swing.border.*;

import java.awt.*;

/**
 * The CellBorder is similar to the borders known from spreadsheets where each edge can be selectively enabled.
 */
public class CellBorder extends LineBorder {

  private boolean   left;
  private boolean   right;
  private boolean   top;
  private boolean   bottom;

  /**
   * Initialises this border with selectively enabled edges.
   *  
   * @param color   The Color to be used.
   * @param edges   The edges, where the order is: top, left, bottom, right. Any additional value is ignored.
   */
  public CellBorder( Color color, boolean ... edges ) {
    super( color );
    setEdges( edges );
  }

  /**
   * Initialises this border with selectively enabled edges.
   * 
   * @param color       The Color to be used.
   * @param thickness   The thickness of the line. Must be positive.
   * @param edges       The edges, where the order is: top, left, bottom, right. Any additional value is ignored.
   */
  public CellBorder( Color color, int thickness, boolean ... edges ) {
    super( color, thickness );
    setEdges( edges );
  }

  /**
   * Enables/disables the edges which have to be painted.
   * 
   * @param edges   The edges, where the order is: top, left, bottom, right. Any additional value is ignored.
   */
  private void setEdges( boolean ... edges ) {
    left   = false;
    right  = false;
    top    = false;
    bottom = false;
    if( edges != null ) {
      if( edges.length > 0 ) {
        top  = edges[0]; 
      }
      if( edges.length > 1 ) {
        left = edges[1];
      }
      if( edges.length > 2 ) {
        bottom = edges[2];
      }
      if( edges.length > 3 ) {
        right = edges[3];
      }
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void paintBorder( Component c, Graphics g, int x, int y, int width, int height ) {
    Color oldcolor = g.getColor();
    g.setColor( getLineColor() );
    for( int i = 0; i < getThickness(); i++ ) {
      int x1 = x + i;
      int y1 = y + i;
      int x2 = x1 + width  - 2 * i - 2;
      int y2 = y1 + height - 2 * i - 2;
      if( top ) {
        g.drawLine( x1 - i, y1, x2 + i, y1 );
      }
      if( left ) {
        g.drawLine( x1, y1 - i, x1, y2 + i );
      }
      if( bottom ) {
        g.drawLine( x1 - i, y2, x2 + i, y2 );
      }
      if( right ) {
        g.drawLine( x2, y1 - i, x2, y2 + i );
      }
    }
    g.setColor( oldcolor );
  }

} /* ENDCLASS */
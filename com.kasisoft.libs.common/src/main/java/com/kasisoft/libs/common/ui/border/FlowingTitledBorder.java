package com.kasisoft.libs.common.ui.border;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.geom.*;

import lombok.*;

/**
 * The FlowingTitledBorder also allows to display a title where this title is backgrounded by a flow changing one color 
 * to another one.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FlowingTitledBorder extends TitledBorder {

  private static final String KEY_FROMCOLOR = "FlowingTitledBorder.fromColor" ;
  private static final String KEY_TOCOLOR   = "FlowingTitledBorder.toColor"   ;

  private Color   fromcolor;
  private Color   tocolor;

  /**
   * Creates a FlowingTitledBorder instance without some textual content.
   */
  public FlowingTitledBorder() {
    this( "", LEADING, null, Color.black, Color.cyan, Color.white );
  }

  /**
   * Creates a FlowingTitledBorder instance with the specified title.
   *
   * @param title   The title the border should display. Neither <code>null</code> nor empty.
   */
  public FlowingTitledBorder( @NonNull String title ) {
    this( title, LEADING, null, Color.black, Color.cyan, Color.white  );
  }

  /**
   * Creates a FlowingTitledBorder instance with the specified title and title-justification.
   *
   * @param title           The title the border should display. Neither <code>null</code> nor empty.
   * @param justification   The justification for the title.
   */
  public FlowingTitledBorder( @NonNull String title, int justification ) {
    this( title, justification, null, Color.black, Color.cyan, Color.white  );
  }

  /**
   * Creates a FlowingTitledBorder instance with the specified title, title-justification and title-font.
   *
   * @param title           The title the border should display. Neither <code>null</code> nor empty.
   * @param justification   The justification for the title.
   * @param font            The font for rendering the title.
   */
  public FlowingTitledBorder( @NonNull String title, int justification, Font font ) {
    this( title, justification, font, Color.black, Color.cyan, Color.white );
  }

  /**
   * Creates a FlowingTitledBorder instance with the specified title, title-justification, title-font, title-color, 
   * start-color and destination-color.
   *
   * @param title           The title the border should display. Neither <code>null</code> nor empty.
   * @param justification   The justification for the title.
   * @param font            The font of the title.
   * @param titlecol        The color of the title.
   * @param fromcol         The starting color for the flow.
   * @param tocol           The end color for the flow.
   */
  public FlowingTitledBorder( @NonNull String title, int justification, Font font, Color titlecol, Color fromcol, Color tocol ) {
    super( new EmptyBorder( 1, 1, 1, 1 ), title, justification, TOP, font, Color.black );
    setFromColor( fromcol );
    setToColor( tocol );
  }

  /**
   * Returns the starting color for the background flow.
   * 
   * @return   The starting color for the background flow. Not <code>null</code>.
   */
  public Color getFromColor() {
    return fromcolor;
  }

  /**
   * Returns the ending color for the background flow.
   * 
   * @return   The ending color for the background flow. Not <code>null</code>.
   */
  public Color getToColor() {
    return tocolor;
  }

  /**
   * Sets the ending color for the background flow.
   *
   * @param tocol   The color the flow should end with.
   */
  public void setToColor( Color tocol ) {
    tocolor = tocol;
    if( tocol == null ) {
      tocolor = UIManager.getColor( KEY_TOCOLOR );
    }
  }

  /**
   * Sets the starting color for the background flow.
   *
   * @param fromcol   The color the flow should start with.
   */
  public void setFromColor( Color fromcol ) {
    fromcolor = fromcol;
    if( fromcol == null ) {
      fromcolor = UIManager.getColor( KEY_FROMCOLOR );
    }
  }

  @Override
  public void paintBorder( @NonNull Component c, @NonNull Graphics g, int x, int y, int width, int height ) {

    if( g instanceof Graphics2D ) {

      int   mh  = c.getFontMetrics( getFont(c) ).getHeight();
      float fx1 = x;
      float fy1 = y;
      float fx2 = x + width - 1;
      float fy2 = y + mh    - 1;
      float fw  = width;
      float fh  = mh;

      // Setup the rectangle to be filled
      Rectangle2D   rect  = new Rectangle2D.Float( fx1, fy1, fw, fh );

      // This Paint makes the flow
      GradientPaint gp    = new GradientPaint( fx1, fy1, fromcolor, fx2, fy2, tocolor, false );

      // The paint requires such a Graphics instance
      Graphics2D    g2d   = (Graphics2D) g;

      // Make our painting
      g2d.setPaint( gp );
      g2d.fill( rect );

    }

    // Let the superclass render the title
    super.paintBorder( c, g, x, y, width, height );

  }

} /* ENDCLASS */

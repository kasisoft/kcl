/**
 * Name........: MultiSplitPane
 * Description.: A JSplitPane like variety that supports more than two parts. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.swing;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.tools.diagnostic.*;

import javax.swing.*;

import java.awt.*;

/**
 * A JSplitPane like variety that supports more than two parts.
 */
public class MultiSplitPane extends JPanel {

  private JSplitPane[]   chain;

  /**
   * Initialises this instance without continuous layouting.
   * 
   * @param orientation   The orientation for the contained fields. Not <code>null</code>.
   * @param count         The number of fields to be provided. Minimum allowed value is 2.
   */
  public MultiSplitPane( 
      @KNotNull(name="orientation")   Orientation   orientation, 
      @KIRange(name="count",min=2)    int           count
  ) {
    this( orientation, count, false );
  }
  
  /**
   * Initialises this instance with configurable continuous layouting.
   * 
   * @param orientation        The orientation for the contained fields. Not <code>null</code>.
   * @param count              The number of fields to be provided. Minimum allowed value is 2.
   * @param continuouslayout   <code>true</code> <=> Enable continuous layouting while the divider location
   *                                                 is still being changed.
   */
  public MultiSplitPane( 
    @KNotNull(name="orientation")   Orientation   orientation, 
    @KIRange(name="count",min=2)    int           count, 
                                    boolean       continuouslayout 
  ) {
    super( new BorderLayout() );
    chain = new JSplitPane[ count - 1 ];
    for( int i = 0; i < chain.length; i++ ) {
      chain[i] = new JSplitPane( orientation.getSplitPaneOrientation(), continuouslayout );
    }
    for( int i = 1; i < chain.length; i++ ) {
      chain[ i - 1 ].setBottomComponent( chain[i] );
    }
    add( chain[0], BorderLayout.CENTER );
  }
  
  /**
   * Returns the number of fields available within this component.
   * 
   * @return   The number of fields available within this component. 
   */
  public int getFieldCount() {
    return chain.length + 1;
  }

  /**
   * Returns the component currently stored at the supplied index.
   * 
   * @param index   The position of the desired field. The value must be within the range {0, {@link #getFieldCount()} - 1} .
   * 
   * @return   The field which was stored at the desired position. Maybe <code>null</code>.
   */
  public Component getField( @KIPositive(name="index") int index ) {
    if( index >= 0 ) {
      if( index < chain.length ) {
        return chain[ index ].getTopComponent();
      } else if( index == chain.length ) {
        return chain[ index - 1 ].getBottomComponent();
      }
    }
    return null;
  }

  /**
   * Changes the component currently stored at the supplied index.
   * 
   * @param index       The position of the desired field. The value must be within the range {0, {@link #getFieldCount()} - 1} .
   * @param component   The Component which has to be set. Not <code>null</code>.
   */
  public void setField( @KIPositive(name="index") int index, @KNotNull(name="component") Component component ) {
    if( index >= 0 ) {
      if( index < chain.length ) {
        chain[ index ].setTopComponent( component );
      } else if( index == chain.length ) {
        chain[ index - 1 ].setBottomComponent( component );
      }
    }
  }
  
} /* ENDCLASS */
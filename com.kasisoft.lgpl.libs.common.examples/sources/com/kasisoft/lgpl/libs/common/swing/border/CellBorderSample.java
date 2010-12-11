/**
 * Name........: CellBorderSample
 * Description.: Simple demonstration of a CellBorder . 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.swing.border;

import com.kasisoft.lgpl.libs.common.swing.*;

import javax.swing.*;

import java.awt.*;

/**
 * Simple demonstration of a CellBorder .
 */
public class CellBorderSample extends JFrame {

  /**
   * Initialises this frame.
   */
  public CellBorderSample() {
    initGUI();
    pack();
    setSize( 640, 480 );
    SwingFunctions.center( this );
  }
  
  /**
   * Create and layout GUI components.
   */
  private void initGUI() {
    getContentPane().setLayout( new GridLayout( 2, 2, 4, 4 ) );
    getContentPane().add( newLabel( "None" ) );
    getContentPane().add( newLabel( "Top"    , true  ) );
    getContentPane().add( newLabel( "Left"   , false , true ) );
    getContentPane().add( newLabel( "Bottom" , false , false , true  ) );
    getContentPane().add( newLabel( "Right"  , false , false , false , true ) );
    getContentPane().add( newLabel( "All"    , true  , true  , true  , true ) );
  }
  
  /**
   * Create a new label with the CellBorder around it.
   * 
   * @param text    The text to display.
   * @param edges   The edges to be enabled.
   * 
   * @return   The newly created label.
   */
  private JLabel newLabel( String text, boolean ... edges ) {
    JLabel      result  = new JLabel( text );
    CellBorder  border  = new CellBorder( Color.blue, 4, edges );
    result.setBorder( border );
    return result;
  }
  
  public static final void main( String[] args ) throws Exception {
    SwingUtilities.invokeLater( new Runnable() {
      public void run() {
        new CellBorderSample().setVisible( true );
      }
    });
  }
  
} /* ENDCLASS */

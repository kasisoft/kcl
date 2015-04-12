package com.kasisoft.libs.common.ui.component;

import com.kasisoft.libs.common.workspace.*;

import javax.swing.*;

import java.awt.event.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class KTextField extends JTextField {

  /**
   * Sets up this text field.
   */
  public KTextField() {
    super();
  }
  
  /**
   * Sets up this text field with the supplied content.
   * 
   * @param content   The content to display initially. Not <code>null</code>.
   */
  public KTextField( String content ) {
    super( content );
  }

  @Override
  public synchronized void addFocusListener( FocusListener l ) {
    if( l instanceof WSListener ) {
      ((WSListener) l).configure( this );
    }
    super.addFocusListener( l );
  }

} /* ENDCLASS */

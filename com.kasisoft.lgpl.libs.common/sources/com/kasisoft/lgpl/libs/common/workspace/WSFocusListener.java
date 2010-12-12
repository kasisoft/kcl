/**
 * Name........: WSFocusListener
 * Description.: A FocusListener implementation which stores the setting of a widget when it looses
 *               it's focus.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.workspace;

import com.kasisoft.lgpl.tools.diagnostic.*;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

/**
 * A FocusListener implementation which stores the setting of a widget when it looses it's focus.
 */
public class WSFocusListener extends FocusAdapter {

  private String   wsproperty;
  
  /**
   * Sets up this listener using the supplied key allowing to access the Workspace.
   * 
   * @param key   The key used to persist the settings. Neither <code>null</code> nor empty.
   */
  public WSFocusListener( @KNotEmpty(name="key") String key ) {
    wsproperty = key;
  }
  
  /**
   * {@inheritDoc}
   */
  public void focusLost( FocusEvent evt ) {
    Component component = evt.getComponent();
    if( component instanceof JTextField ) {
      Workspace.getInstance().setString( wsproperty, ((JTextField) component).getText() );
    }
  }

} /* ENDCLASS */
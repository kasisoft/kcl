package com.kasisoft.libs.common.workspace;

import javax.swing.*;

import java.awt.event.*;

import java.awt.*;

import lombok.*;
import lombok.experimental.*;

/**
 * A FocusListener implementation which stores the setting of a widget when it looses it's focus.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WSFocusListener extends FocusAdapter {

  String   wsproperty;
  
  /**
   * Sets up this listener using the supplied key allowing to access the Workspace.
   * 
   * @param key   The key used to persist the settings. Neither <code>null</code> nor empty.
   */
  public WSFocusListener( @NonNull String key ) {
    wsproperty = key;
  }
  
  @Override
  public void focusLost( @NonNull FocusEvent evt ) {
    Component component = evt.getComponent();
    if( component instanceof JTextField ) {
      Workspace.getInstance().setString( wsproperty, ((JTextField) component).getText() );
    }
  }

} /* ENDCLASS */

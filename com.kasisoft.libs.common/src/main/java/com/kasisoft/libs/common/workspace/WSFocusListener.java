package com.kasisoft.libs.common.workspace;

import com.kasisoft.libs.common.config.*;

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
public class WSFocusListener<T> extends FocusAdapter implements WSListener<Component> {

  String              wsproperty;
  SimpleProperty<T>   property;
  
  /**
   * Sets up this listener using the supplied key allowing to access the Workspace.
   * 
   * @param key   The key used to persist the settings. Neither <code>null</code> nor empty.
   * 
   * @deprecated [12-Apr-2015:KASI]    This constructor will be removed with version 1.8.
   */
  @Deprecated
  public WSFocusListener( @NonNull String key ) {
    wsproperty = key;
  }

  /**
   * Sets up this listener using the supplied property allowing to access the Workspace.
   * 
   * @param newproperty   The property used to persist the settings. Not <code>null</code>.
   */
  public WSFocusListener( @NonNull SimpleProperty<T> newproperty ) {
    property = newproperty;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void focusLost( @NonNull FocusEvent evt ) {
    Component component = evt.getComponent();
    if( component instanceof JTextField ) {
      if( wsproperty != null ) {
        Workspace.getInstance().setString( wsproperty, ((JTextField) component).getText() );
      } else {
        ((SimpleProperty<String>) property).setValue( Workspace.getInstance().getProperties(), ((JTextField) component).getText() );
      }
    }
  }

  @Override
  public void configure( Component component ) {
    if( property != null ) {
      T value = property.getValue( Workspace.getInstance().getProperties() );
      if( (value != null) && (component instanceof JTextField) ) {
        ((JTextField) component).setText( (String) value );
      }
    }
  }

} /* ENDCLASS */

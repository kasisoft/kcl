package com.kasisoft.libs.common.workspace;

import com.kasisoft.libs.common.config.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.*;

import java.awt.event.*;

import java.awt.*;

/**
 * A FocusListener implementation which stores the setting of a widget when it looses it's focus.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated [08-Sep-2016:KASI]   This type will be removed with version 2.5. Use {@link WorkspacePersistent}
 *                                  instead.
 */
@Deprecated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WSFocusListener<T> extends FocusAdapter implements WSListener<Component> {

  SimpleProperty<T>   property;
  
  /**
   * Sets up this listener using the supplied property allowing to access the Workspace.
   * 
   * @param newproperty   The property used to persist the settings. Not <code>null</code>.
   */
  public WSFocusListener( @NonNull SimpleProperty<T> newproperty ) {
    property = newproperty;
  }

  @Override
  public void focusLost( @NonNull FocusEvent evt ) {
    Component component = evt.getComponent();
    if( component instanceof JTextField ) {
      ((SimpleProperty<String>) property).setValue( Workspace.getInstance().getProperties(), ((JTextField) component).getText() );
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

package com.kasisoft.libs.common.workspace;

import com.kasisoft.libs.common.config.*;

import javax.swing.event.*;

import lombok.*;
import lombok.experimental.*;

/**
 * A basic ChangeListener implementation which stores the settings of a source object to the Workspace.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class WSChangeListener<T> implements ChangeListener {

  SimpleProperty<T>   property;
  
  /**
   * Sets up this listener using the supplied property allowing to access the Workspace.
   * 
   * @param newproperty   The property used to persist the settings. Not <code>null</code>.
   */
  public WSChangeListener( @NonNull SimpleProperty<T> newproperty ) {
    property = newproperty;
  }

  @Override
  public void stateChanged( @NonNull ChangeEvent evt ) {
    T source = (T) evt.getSource();
    property.setValue( Workspace.getInstance().getProperties(), source );
  }
  
} /* ENDCLASS */

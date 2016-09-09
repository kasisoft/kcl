package com.kasisoft.libs.common.workspace;

import com.kasisoft.libs.common.config.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.event.*;

/**
 * A basic ChangeListener implementation which stores the settings of a source object to the Workspace.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated [08-Sep-2016:KASI]   This type will be removed with version 2.5. Use {@link WorkspacePersistent}
 *                                  instead.
 */
@Deprecated
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

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
  public WSChangeListener( @NonNull String key ) {
    wsproperty = key;
  }

  /**
   * Sets up this listener using the supplied property allowing to access the Workspace.
   * 
   * @param newproperty   The property used to persist the settings. Not <code>null</code>.
   */
  public WSChangeListener( @NonNull SimpleProperty<T> newproperty ) {
    property = newproperty;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void stateChanged( @NonNull ChangeEvent evt ) {
    T source = (T) evt.getSource();
    if( wsproperty != null ) {
      Workspace.getInstance().setString( wsproperty, asString( source ) );
    } else {
      property.setValue( Workspace.getInstance().getProperties(), source );
    }
  }
  
  /**
   * Converts the source setting into a String.
   * 
   * @param source   The source which setting is desired.
   * 
   * @return   The String representation of the source. Not <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]    This method will be removed with version 1.8.
   */
  @Deprecated
  protected abstract String asString( T source );

} /* ENDCLASS */

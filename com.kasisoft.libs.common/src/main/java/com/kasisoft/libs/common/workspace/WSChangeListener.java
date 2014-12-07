package com.kasisoft.libs.common.workspace;

import javax.swing.event.*;

import lombok.*;

/**
 * A basic ChangeListener implementation which stores the settings of a source object to the Workspace.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public abstract class WSChangeListener<T> implements ChangeListener {

  private String   wsproperty;
  
  /**
   * Sets up this listener using the supplied key allowing to access the Workspace.
   * 
   * @param key   The key used to persist the settings. Neither <code>null</code> nor empty.
   */
  public WSChangeListener( @NonNull String key ) {
    wsproperty = key;
  }
  
  @Override
  public void stateChanged( @NonNull ChangeEvent evt ) {
    T source = (T) evt.getSource();
    Workspace.getInstance().setString( wsproperty, asString( source ) );
  }
  
  /**
   * Converts the source setting into a String.
   * 
   * @param source   The source which setting is desired.
   * 
   * @return   The String representation of the source. Not <code>null</code>.
   */
  protected abstract String asString( T source );

} /* ENDCLASS */

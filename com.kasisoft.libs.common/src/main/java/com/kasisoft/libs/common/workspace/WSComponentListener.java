package com.kasisoft.libs.common.workspace;

import com.kasisoft.libs.common.config.*;

import java.awt.event.*;

import java.awt.*;

import lombok.*;
import lombok.experimental.*;

/**
 * A ComponentListener implementation which stores the settings of a Component to the Workspace.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WSComponentListener extends ComponentAdapter {

  String                      wsproperty;
  SimpleProperty<Rectangle>   property;
  
  /**
   * Sets up this listener using the supplied key allowing to access the Workspace.
   * 
   * @param key   The key used to persist the settings. Neither <code>null</code> nor empty.
   * 
   * @deprecated [12-Apr-2015:KASI]    This constructor will be removed with version 1.8.
   */
  @Deprecated
  public WSComponentListener( @NonNull String key ) {
    wsproperty = key;
  }

  /**
   * Sets up this listener using the supplied key allowing to access the Workspace.
   * 
   * @param newproperty   The newproperty used to persist the settings. Not <code>null</code>.
   */
  public WSComponentListener( @NonNull SimpleProperty<Rectangle> newproperty ) {
    property = newproperty;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void componentMoved( @NonNull ComponentEvent evt ) {
    Component component = evt.getComponent();
    if( wsproperty != null ) {
      Workspace.getInstance().setRectangle( wsproperty, component.getBounds() );
    } else {
      property.setValue( Workspace.getInstance().getProperties(), component.getBounds() );
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void componentResized( @NonNull ComponentEvent evt ) {
    Component component = evt.getComponent();
    if( wsproperty != null ) {
      Workspace.getInstance().setRectangle( wsproperty, component.getBounds() );
    } else {
      property.setValue( Workspace.getInstance().getProperties(), component.getBounds() );
    }
  }

} /* ENDCLASS */

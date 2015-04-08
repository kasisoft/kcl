package com.kasisoft.libs.common.workspace;

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

  String   wsproperty;
  
  /**
   * Sets up this listener using the supplied key allowing to access the Workspace.
   * 
   * @param key   The key used to persist the settings. Neither <code>null</code> nor empty.
   */
  public WSComponentListener( @NonNull String key ) {
    wsproperty = key;
  }
  
  @Override
  public void componentMoved( @NonNull ComponentEvent evt ) {
    Component component = evt.getComponent();
    Workspace.getInstance().setRectangle( wsproperty, component.getBounds() );
  }

  @Override
  public void componentResized( @NonNull ComponentEvent evt ) {
    Component component = evt.getComponent();
    Workspace.getInstance().setRectangle( wsproperty, component.getBounds() );
  }

} /* ENDCLASS */

/**
 * Name........: WSComponentListener
 * Description.: A ComponentListener implementation which stores the settings of a Component
 *               to the Workspace.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.workspace;

import java.awt.event.*;

import java.awt.*;

/**
 * A ComponentListener implementation which stores the settings of a Component to the Workspace.
 */
public class WSComponentListener extends ComponentAdapter {

  private String   wsproperty;
  
  /**
   * Sets up this listener using the supplied key allowing to access the Workspace.
   * 
   * @param key   The key used to persist the settings. Neither <code>null</code> nor empty.
   */
  public WSComponentListener( String key ) {
    wsproperty = key;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void componentMoved( ComponentEvent evt ) {
    Component component = evt.getComponent();
    Workspace.getInstance().setRectangle( wsproperty, component.getBounds() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void componentResized( ComponentEvent evt ) {
    Component component = evt.getComponent();
    Workspace.getInstance().setRectangle( wsproperty, component.getBounds() );
  }

} /* ENDCLASS */

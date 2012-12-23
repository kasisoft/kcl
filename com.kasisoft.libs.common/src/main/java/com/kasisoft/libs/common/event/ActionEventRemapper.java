/**
 * Name........: ActionEventRemapper
 * Description.: Specialisation of a dispatcher which allows to change the 'source' of an ActionEvent. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.event;

import javax.swing.*;

import java.awt.event.*;

/**
 * Specialisation of a dispatcher which allows to change the 'source' of an ActionEvent. This listener implementation 
 * is typically used for composite widgets. 
 */
public class ActionEventRemapper extends ActionEventDispatcher implements ActionListener {

  private JComponent   newsource;

  /**
   * Initialises this mapper for the supplied alternative source.
   * 
   * @param source   The new source to be used while firing events. Not <code>null</code>.
   */
  public ActionEventRemapper( JComponent source ) {
    newsource = source;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed( ActionEvent evt ) {
    fireEvent( new ActionEvent( newsource, evt.getID(), evt.getActionCommand(), evt.getWhen(), evt.getModifiers() ) );
  }
  
} /* ENDCLASS */
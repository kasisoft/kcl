/**
 * Name........: ActionEventRemapper
 * Description.: Specialisation of a dispatcher which allows to change the 'source' of an ActionEvent. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.event;

import com.kasisoft.lgpl.tools.diagnostic.*;

import javax.swing.*;

import java.awt.event.*;

/**
 * Specialisation of a dispatcher which allows to change the 'source' of an ActionEvent. 
 * This listener implementation is typically used for composite widgets. 
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class ActionEventRemapper extends ActionEventDispatcher implements ActionListener {

  private JComponent   newsource;

  /**
   * Initialises this mapper for the supplied alternative source.
   * 
   * @param source   The new source to be used while firing events. Not <code>null</code>.
   */
  public ActionEventRemapper(
    @KNotNull(name="source") JComponent source 
  ) {
    super();
    newsource = source;
  }
  
  /**
   * {@inheritDoc}
   */
  public void actionPerformed( ActionEvent evt ) {
    fireActionEvent( new ActionEvent( newsource, evt.getID(), evt.getActionCommand(), evt.getWhen(), evt.getModifiers() ) );
  }
  
} /* ENDCLASS */

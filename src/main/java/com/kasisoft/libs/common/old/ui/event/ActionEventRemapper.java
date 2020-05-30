package com.kasisoft.libs.common.old.ui.event;

import lombok.experimental.*;

import lombok.*;

import javax.swing.*;

import java.awt.event.*;

/**
 * Specialisation of a dispatcher which allows to change the 'source' of an ActionEvent. This listener implementation 
 * is typically used for composite widgets. 
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActionEventRemapper extends ActionEventDispatcher implements ActionListener {

  JComponent   newsource;

  /**
   * Initialises this mapper for the supplied alternative source.
   * 
   * @param source   The new source to be used while firing events. Not <code>null</code>.
   */
  public ActionEventRemapper( @NonNull JComponent source ) {
    newsource = source;
  }
  
  @Override
  public void actionPerformed( @NonNull ActionEvent evt ) {
    fireEvent( new ActionEvent( newsource, evt.getID(), evt.getActionCommand(), evt.getWhen(), evt.getModifiers() ) );
  }
  
} /* ENDCLASS */

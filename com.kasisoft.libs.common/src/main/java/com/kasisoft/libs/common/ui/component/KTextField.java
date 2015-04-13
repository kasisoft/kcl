package com.kasisoft.libs.common.ui.component;

import com.kasisoft.libs.common.workspace.*;

import javax.swing.*;

import java.awt.event.*;

import java.awt.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Initializes this component.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KTextField extends JTextField {

  static final Color DEFAULT_PLACEHOLDER = Color.lightGray;
  
  @Getter @Setter
  String   placeHolder;
  
  @Getter @Setter
  Color    placeHolderColor;
  
  /**
   * Sets up this text field.
   */
  public KTextField() {
    super();
    setColumns(6);
    placeHolderColor  = DEFAULT_PLACEHOLDER;
  }
  
  /**
   * Sets up this text field with the supplied content.
   * 
   * @param content   The content to display initially. Not <code>null</code>.
   */
  public KTextField( String content ) {
    super( content );
    setColumns(6);
    placeHolderColor  = DEFAULT_PLACEHOLDER;
  }

  @Override
  public synchronized void addFocusListener( FocusListener l ) {
    if( l instanceof WSListener ) {
      ((WSListener) l).configure( this );
    }
    super.addFocusListener( l );
  }

  @Override
  protected void paintComponent( Graphics g ) {
    super.paintComponent( g );
    if( placeHolder != null ) {
      if( getText().isEmpty() ) {
        Graphics2D  g2d = (Graphics2D) g.create();
        FontMetrics fm  = g2d.getFontMetrics();
        g2d.setColor( placeHolderColor != null ? placeHolderColor : DEFAULT_PLACEHOLDER );
        g2d.drawString( placeHolder, 2, fm.getHeight() - 1 );
        g2d.dispose();
      }
    }
  }

} /* ENDCLASS */

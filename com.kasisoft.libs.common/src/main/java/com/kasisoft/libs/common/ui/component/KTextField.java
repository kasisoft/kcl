package com.kasisoft.libs.common.ui.component;

import com.kasisoft.libs.common.workspace.*;

import com.kasisoft.libs.common.config.*;

import com.kasisoft.libs.common.xml.adapters.*;

import com.kasisoft.libs.common.text.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.*;

import java.awt.*;

/**
 * Initializes this component.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KTextField extends JTextField implements WorkspacePersistent {

  private static final Color DEFAULT_PLACEHOLDER = Color.lightGray;
  
  @Getter @Setter
  String                   placeHolder;
  
  @Getter @Setter
  Color                    placeHolderColor;
  
  SimpleProperty<String>   property;
  
  /**
   * Sets up this text field.
   */
  public KTextField() {
    this( null, null, null );
  }

  /**
   * Sets up this text field with the supplied content.
   * 
   * @param content   The content to display initially. Not <code>null</code>.
   */
  public KTextField( @NonNull String content ) {
    this( content, null, null );
  }

  /**
   * Sets up this text field with the supplied content.
   * 
   * @param content   The content to display initially. Not <code>null</code>.
   * @param wsprop    A key used to store the data behind this field. Maybe <code>null</code>.
   */
  public KTextField( @NonNull String content, String wsprop ) {
    this( content, wsprop, null );
  }

  /**
   * Sets up this text field with the supplied content.
   * 
   * @param content   The content to display initially. Maybe <code>null</code>.
   * @param wsprop    A key used to store the data behind this field. Maybe <code>null</code>.
   * @param phColor   The color to be used for the placeholder. Maybe <code>null</code>.
   */
  protected KTextField( String content, String wsprop, Color phColor ) {
    super();
    wsprop = StringFunctions.cleanup( wsprop );
    if( wsprop != null ) {
      property = new SimpleProperty<>( wsprop, new StringAdapter() );
    }
    if( content != null ) {
      super.setText( content );
    }
    setColumns(6);
    placeHolderColor = phColor != null ? phColor : DEFAULT_PLACEHOLDER;
  }

  @Override
  protected void paintComponent( Graphics g ) {
    super.paintComponent( g );
    if( placeHolder != null ) {
      if( getText().isEmpty() ) {
        Graphics2D  g2d    = (Graphics2D) g.create();
        FontMetrics fm     = g2d.getFontMetrics();
        Insets      insets = getInsets();
        g2d.setColor( placeHolderColor != null ? placeHolderColor : DEFAULT_PLACEHOLDER );
        g2d.drawString( placeHolder, insets.left, fm.getHeight() - insets.bottom + insets.top );
        g2d.dispose();
      }
    }
  }

  @Override
  public String getPersistentProperty() {
    return property != null ? property.getKey() : null;
  }

  @Override
  public void loadPersistentSettings() {
    if( property != null ) {
      setText( property.getValue( Workspace.getInstance().getProperties() ) );
    }
  }

  @Override
  public void savePersistentSettings() {
    if( property != null ) {
      property.setValue( Workspace.getInstance().getProperties(), getText() );
    }
  }

} /* ENDCLASS */

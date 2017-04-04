package com.kasisoft.libs.common.ui.component;

import com.kasisoft.libs.common.ui.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.*;

import java.awt.*;

/**
 * A small extension to the original {@link JLabel} which provides the possibility to preconfigure the sizes easily. 
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Setter @Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KLabel extends JLabel {

  int   minCharacters;
  
  public KLabel() {
    super();
  }

  public KLabel( Icon image, int horizontalAlignment ) {
    super( image, horizontalAlignment );
  }

  public KLabel( Icon image ) {
    super( image );
  }

  public KLabel( String text, Icon icon, int horizontalAlignment ) {
    super( text, icon, horizontalAlignment );
  }

  public KLabel( String text, int horizontalAlignment ) {
    super( text, horizontalAlignment );
  }

  public KLabel( String text ) {
    super( text );
  }

  @Override
  public Dimension getPreferredSize() {
    return SwingFunctions.getAdjustedPreferredSize( super.getPreferredSize(), this, minCharacters );
  }

  @Override
  public Dimension getMinimumSize() {
    return SwingFunctions.getAdjustedMinimumSize( super.getMinimumSize(), this );
  }
  
  @Override
  public Dimension getMaximumSize() {
    return SwingFunctions.getAdjustedMaximumSize( super.getMaximumSize(), this );
  }
  
} /* ENDCLASS */

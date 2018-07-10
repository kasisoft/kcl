package com.kasisoft.libs.common.ui.component;

import com.kasisoft.libs.common.i18n.*;
import com.kasisoft.libs.common.ui.*;

import javax.swing.*;

import java.util.*;

import java.awt.*;

import lombok.experimental.*;

import lombok.*;

/**
 * A small extension to the original {@link JLabel} which provides the possibility to preconfigure the sizes easily. 
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Setter @Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KLabel extends JLabel implements I18NSensitive {

  int           minCharacters;
  I18NString    i18n;
  
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

  public KLabel( I18NString text, Icon icon, int horizontalAlignment ) {
    super( text.toString(), icon, horizontalAlignment );
    i18n = text;
  }

  public KLabel( I18NString text, int horizontalAlignment ) {
    super( text.toString(), horizontalAlignment );
    i18n = text;
  }

  public KLabel( I18NString text ) {
    super( text.toString() );
    i18n = text;
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

  public void setText( I18NString text ) {
    if( text != null ) {
      setText( text.toString() );
    }
    i18n = text;
  }
  
  @Override
  public void onLocaleChange( Locale newLocale ) {
    if( i18n != null ) {
      setText( i18n.toString() );
    }
  }
  
} /* ENDCLASS */

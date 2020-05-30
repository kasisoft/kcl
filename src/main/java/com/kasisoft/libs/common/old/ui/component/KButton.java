package com.kasisoft.libs.common.old.ui.component;

import static com.kasisoft.libs.common.old.function.Functions.*;

import com.kasisoft.libs.common.old.i18n.*;

import javax.swing.*;

import java.util.function.*;

import java.util.*;

import java.awt.event.*;

import lombok.experimental.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KButton extends JButton implements I18NSensitive {

  I18NString    i18n;
  Object[]      args;
  
  public KButton() {
    super();
  }

  public KButton( Action a ) {
    super( a );
  }

  public KButton( Icon icon ) {
    super( icon );
  }

  public KButton( String text, Icon icon ) {
    super( text, icon );
  }

  public KButton( String text ) {
    super( text );
  }

  public KButton( I18NString text, Icon icon, Object ... arguments ) {
    super( text.toString(), icon );
    i18n = text;
    args = arguments;
  }

  public KButton( I18NString text, Object ... arguments ) {
    super( text.toString() );
    i18n = text;
    args = arguments;
  }
  
  public void addActionListener( Consumer<ActionEvent> listener ) {
    addActionListener( adaptToActionListener( listener ) );
  }
  
  public void setI18N( I18NString text, Object ... arguments ) {
    args = arguments;
    i18n = text != null ? text : i18n;
    if( i18n != null ) {
      setText( i18n.format( args ) );
    }
  }

  @Override
  public void onLocaleChange( Locale newLocale ) {
    if( i18n != null ) {
      setText( i18n.format( args ) );
    }
  }

} /* ENDCLASS */

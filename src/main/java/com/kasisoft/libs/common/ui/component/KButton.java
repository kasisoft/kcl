package com.kasisoft.libs.common.ui.component;

import com.kasisoft.libs.common.i18n.*;

import javax.swing.*;

import java.util.*;

import lombok.experimental.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KButton extends JButton implements I18NSensitive {

  I18NString    i18n;
  
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

  public KButton( I18NString text, Icon icon ) {
    super( text.toString(), icon );
    i18n = text;
  }

  public KButton( I18NString text ) {
    super( text.toString() );
    i18n = text;
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

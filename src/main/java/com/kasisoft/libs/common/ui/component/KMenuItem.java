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
public class KMenuItem extends JMenuItem implements I18NSensitive {

  I18NString   i18n;
  
  public KMenuItem() {
    super();
  }

  public KMenuItem( Action a ) {
    super( a );
  }

  public KMenuItem( Icon icon ) {
    super( icon );
  }

  public KMenuItem( I18NString text, Icon icon ) {
    super( text.toString(), icon );
    i18n = text;
  }

  public KMenuItem( I18NString text, int mnemonic ) {
    super( text.toString(), mnemonic );
    i18n = text;
  }

  public KMenuItem( I18NString text ) {
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

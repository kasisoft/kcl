package com.kasisoft.libs.common.ui.border;

import com.kasisoft.libs.common.i18n.*;

import javax.swing.border.*;

import java.util.*;

import java.awt.*;

import lombok.experimental.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class I18NTitledBorder extends TitledBorder implements I18NSensitive {

  I18NString    i18n;
  
  public I18NTitledBorder( Border border, I18NString title, int titleJustification, int titlePosition, Font titleFont, Color titleColor ) {
    super( border, title.toString(), titleJustification, titlePosition, titleFont, titleColor );
    i18n = title;
  }

  public I18NTitledBorder( Border border, I18NString title, int titleJustification, int titlePosition, Font titleFont ) {
    super( border, title.toString(), titleJustification, titlePosition, titleFont );
    i18n = title;
  }

  public I18NTitledBorder( Border border, I18NString title, int titleJustification, int titlePosition ) {
    super( border, title.toString(), titleJustification, titlePosition );
    i18n = title;
  }

  public I18NTitledBorder( Border border, I18NString title ) {
    super( border, title.toString() );
    i18n = title;
  }

  public I18NTitledBorder( I18NString title ) {
    super( title.toString() );
    i18n = title;
  }

  @Override
  public void onLocaleChange( Locale newLocale ) {
    setTitle( i18n.toString() );
  }

} /* ENDCLASS */

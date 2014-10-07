package com.kasisoft.libs.common.internal;

import com.kasisoft.libs.common.i18n.*;

/**
 * Collection of messages that could be overridden if necessary.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Messages {

  @I18N("(default=%s)")
  public static I18NFormatter   format_default;
  
  @I18N("%s is not a valid Color")
  public static I18NFormatter   invalid_color;
  
  @I18N("mandatory")
  public static String          label_mandatory;

  @I18N("optional")
  public static String          label_optional;

  static {
    I18NSupport.initialize( Messages.class );
  }
  
} /* ENDCLASS */

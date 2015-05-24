package com.kasisoft.libs.common.internal;

import com.kasisoft.libs.common.i18n.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Collection of messages that could be overridden if necessary.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PUBLIC)
public class Messages {

  @I18N("(default=%s)")
  static I18NFormatter   format_default;
  
  @I18N("%s is not a valid Color")
  static I18NFormatter   invalid_color;
  
  @I18N("mandatory")
  static String          label_mandatory;

  @I18N("optional")
  static String          label_optional;
  
  @I18N("Unsupported CharSequence type '%s'")
  static I18NFormatter   unsupported_charsequence;

  static {
    I18NSupport.initialize( Messages.class );
  }
  
} /* ENDCLASS */

package com.kasisoft.libs.common.i18n;

import lombok.*;
import lombok.experimental.*;

@FieldDefaults(level = AccessLevel.PUBLIC)
public class Messages1 {

  @I18N("Default.0")
  static String m0;

  @I18N("Default.1")
  static String m1;
  
  @I18N("Default.2")
  static String m2;
  
  @I18N("Default.3")
  static String m3;

  @I18N("Default.4")
  static String m4;

  static {
    I18NSupport.initialize( Messages1.class );
  }
  
} /* ENDCLASS */

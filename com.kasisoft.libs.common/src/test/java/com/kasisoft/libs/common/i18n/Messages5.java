package com.kasisoft.libs.common.i18n;

import lombok.experimental.*;

import lombok.*;

import java.util.*;

@FieldDefaults(level = AccessLevel.PUBLIC)
@I18NBasename(resource="messages5",prefix="pre.")
public class Messages5 {

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
  
  @I18N("The text was '%s'")
  static I18NFormatter  m5;

  @I18N(value="DEFAULT", key="overridden.key")
  static String m6;
  
  static {
    I18NSupport.initialize( Locale.GERMANY, Messages5.class );
  }
  
} /* ENDCLASS */

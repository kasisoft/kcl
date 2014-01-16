package com.kasisoft.libs.common.i18n;

import java.util.*;

@I18NBasename(resource="messages5",prefix="pre.")
public class Messages5 {

  @I18N("Default.0")
  public static String m0;

  @I18N("Default.1")
  public static String m1;
  
  @I18N("Default.2")
  public static String m2;
  
  @I18N("Default.3")
  public static String m3;

  @I18N("Default.4")
  public static String m4;
  
  @I18N("The text was '%s'")
  public static I18NFormatter  m5;

  static {
    I18NSupport.initialize( Locale.GERMANY, Messages5.class );
  }
  
} /* ENDCLASS */

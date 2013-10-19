package com.kasisoft.libs.common.i18n;

public class Messages1 {

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

  static {
    I18NSupport.initialize( Messages1.class );
  }
  
} /* ENDCLASS */

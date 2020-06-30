package com.kasisoft.libs.common;

import com.kasisoft.libs.common.constants.Encoding;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * This class provides the defaults for this library. Change them if necessary. 
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class KclConfig {

  public static String          LINE_ENDING         = "\n";

  public static Encoding        DEFAULT_ENCODING    = Encoding.UTF8;
  
  public static Locale          DEFAULT_LOCALE      = Locale.getDefault();
  
  public static List<String>    TRUE_VALUES         = Arrays.asList("yes", "ja", "j", "y", "on", "ein", "1", "-1", "an", "true");

  public static List<String>    FALSE_VALUES        = Arrays.asList("no", "nein", "n", "off", "aus", "0",  "aus", "false");

} /* ENDCLASS */

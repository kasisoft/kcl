package com.kasisoft.libs.common.internal;

import com.kasisoft.libs.common.i18n.*;

/**
 * Collection of messages that could be overridden if necessary.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Messages {

  @I18N("The number of columns for each line isn't consistent !")
  public static String          error_csv_inconsistent_column_count;

  @I18N("Cannot add row %d (data = %s). Error: %s !")
  public static I18NFormatter   error_csv_invalid_add_row;

  @I18N("The cell value '%s' at column %d cannot be parsed !")
  public static I18NFormatter   error_csv_invalid_cell_value;

  @I18N("Misconfigured CsvOptions. The column %d does not provide an adapter !")
  public static I18NFormatter   error_csv_missing_adapter;
  
  @I18N("The closing quote for content '%s' is missing !")
  public static I18NFormatter   error_csv_missing_closing_quote;
  
  @I18N("The filesystem watching dispatcher has already been started.")
  public static String          error_fswatcher_already_started;

  @I18N("Start the configuration of an argument first (flag, single, many) !")
  public static String          error_missing_argument;
  
  @I18N("Missing required command line option '%s' !")
  public static I18NFormatter   error_missing_required_option;

  @I18N("(default=%s)")
  public static I18NFormatter   format_default;
  
  @I18N("%s is not a valid Color")
  public static I18NFormatter   invalid_color;
  
  @I18N("mandatory")
  public static String          label_mandatory;

  @I18N("optional")
  public static String          label_optional;
  
  @I18N("Unsupported CharSequence type '%s'")
  public static I18NFormatter   unsupported_charsequence;

  @I18N("The argument '%s' had not been used !")
  public static I18NFormatter   unused_argument;

  static {
    I18NSupport.initialize( Messages.class );
  }
  
} /* ENDCLASS */

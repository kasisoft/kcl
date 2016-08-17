package com.kasisoft.libs.common.internal;

import com.kasisoft.libs.common.i18n.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Collection of messages that could be overridden if necessary.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PUBLIC)
public class Messages {

  @I18N("The number of columns for each line isn't consistent !")
  static String          error_csv_inconsistent_column_count;

  @I18N("Cannot add row %d (data = %s). Error: %s !")
  static I18NFormatter   error_csv_invalid_add_row;

  @I18N("The cell value '%s' at column %d cannot be parsed !")
  static I18NFormatter   error_csv_invalid_cell_value;

  @I18N("Misconfigured CsvOptions. The column %d does not provide an adapter !")
  static I18NFormatter   error_csv_missing_adapter;
  
  @I18N("The closing quote for content '%s' is missing !")
  static I18NFormatter   error_csv_missing_closing_quote;
  
  @I18N("The filesystem watching dispatcher has already been started.")
  static String          error_fswatcher_already_started;

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

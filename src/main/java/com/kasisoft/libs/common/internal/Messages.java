package com.kasisoft.libs.common.internal;

import com.kasisoft.libs.common.i18n.*;

import com.kasisoft.libs.common.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Messages {

  @I18N("API misuse. Buffer must have at least one element.")
  public static String                          error_api_misuse_empty_buffer;

  @I18N("Failed to load blacklist !")
  public static String                          error_blacklist_loading_failure;

  @I18N("Cannot insert at position %d into buffer of length %d")
  public static String                          error_buffer_insertion;

  @I18N("Cannot determine canonical file of '%s'!")
  public static String                          error_cannot_determine_canonical_file;

  @I18N("Couldn't connect to database with url '%s'")
  public static String                          error_cannot_connect_to_database;

  @I18N("Cannot add row %d (data = %s). Error: %s !")
  public static I18NString                      error_csv_cannot_add_row;

  @I18N("The cell value '%s' at column %d cannot be parsed !")
  public static I18NString                      error_csv_cannot_parse_cell_value;

  @I18N("The number of columns for each line isn't consistent !")
  public static String                          error_csv_inconsistent_column_counts;

  @I18N("The closing quote for content '%s' is missing !")
  public static String                          error_csv_missing_closing_quote;

  @I18N("The directory '%s' does not exist!")
  public static String                          error_directory_does_not_exist;

  @I18N("DOM Implementation doesn't support LS !")
  public static String                          error_dom_impl_without_ls;

  @I18N("Failed to activate driver '%s' !")
  public static String                          error_failed_to_activate_jdbc_driver;

  @I18N("Failed to copy '%s' to '%s'!")
  public static String                          error_failed_to_copy;

  @I18N("Failed to create directory '%s' !")
  public static String                          error_failed_to_create_directory;

  @I18N("Could not create temporary file. Prefix: '%s', Suffix: '%s'")
  public static String                          error_failed_to_create_temporary_file;

  @I18N("Failed to delete directory '%s' !")
  public static String                          error_failed_to_delete_directory;

  @I18N("Failed to delete file '%s' !")
  public static String                          error_failed_to_delete_file;

  @I18N("Could not gzip '%s' (destination: '%s')")
  public static String                          error_failed_to_gzip;

  @I18N("Failed to load gzipped '%s'")
  public static String                          error_failed_to_load_gzipped;

  @I18N("Failed to load properties!")
  public static String                          error_failed_to_load_properties;

  @I18N("Failed to process zip file '%s' !")
  public static String                          error_failed_to_process_zip;

  @I18N("Failed to scan dir '%s' !")
  public static String                          error_failed_to_scan_dir;

  @I18N("Failed to ungzip '%s'")
  public static String                          error_failed_to_ungzip;

  @I18N("Failed to unzip to file '%s' (zip: %s, file: %s) !")
  public static String                          error_failed_to_unzip;

  @I18N("Failed to zip to '%s' !")
  public static String                          error_failed_to_zip;

  @I18N("Failed to read from '%s'!")
  public static String                          error_failed_to_read_from;

  @I18N("Failed to read image from '%s'!")
  public static String                          error_failed_to_read_image;

  @I18N("Failed to write image to '%s' (type: %s)")
  public static String                          error_failed_to_write_image;

  @I18N("Failed to write to '%s'!")
  public static String                          error_failed_to_write_to;

  @I18N("Failed to write text to '%s'!")
  public static String                          error_failed_to_write_text_to;

  @I18N("The file '%s' does not exist!")
  public static String                          error_file_does_not_exist;

  @I18N("Invalid boolean value: '%s'")
  public static String                          error_invalid_boolean_value;

  @I18N("%s is not a valid Color")
  public static String                          error_invalid_color;

  @I18N("Invalid date value '%s'")
  public static String                          error_invalid_date_value;

  @I18N("Invalid decoded value: '%s'")
  public static String                          error_invalid_decoded_value;

  @I18N("Invalid encoded value: '%s'")
  public static String                          error_invalid_encoded_value;

  @I18N("%s is not supported. Allowed values: %s")
  public static String                          error_invalid_enumeration_value;

  @I18N("Invalid URI '%s'")
  public static String                          error_invalid_uri;

  @I18N("Invalid URL '%s'")
  public static String                          error_invalid_url;

  @I18N("Missing CSV Adapter for column %d")
  public static I18NString                      error_missing_csv_adapter;

  @I18N("The resource '%s' is missing !")
  public static String                          error_missing_resource;

  @I18N("There's no write support for URLs (%s) !")
  public static String                          error_no_write_support_for_urls;

  @I18N("Unknown digest algorithm '%s'!")
  public static String                          error_unknown_digest_algorithm;

  @I18N("Cannot parse version '%s'")
  public static String                          error_version_cannot_parse_version;

  @I18N("Missing qualifier")
  public static String                          error_version_missing_qualifier;

  static {
    I18NSupport.initialize(Locale.getDefault(), Messages.class);
  }

} /* ENDCLASS */

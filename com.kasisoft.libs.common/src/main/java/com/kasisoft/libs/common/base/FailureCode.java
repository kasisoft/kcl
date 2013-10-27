/**
 * Name........: FailureCode
 * Description.: Collection of failure codes.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.base;

/**
 * Collection of failure codes.
 */
public enum FailureCode {

  /** everythings okay. */
  Success                                             (   0 ),
  
  /** an unknown failure has been launched. */
  Unexpected                                          (  -1 ),
  
  /** closing a resource failed. */
  Close                                               (  -2 ),
  
  /** reading or writing failed. */
  IO                                                  (  -3 ),
  
  /** skipping within a resource failed. */
  Skip                                                (  -4 ),
  
  /** the encoding that has been used is not supported. */
  InvalidEncoding                                     (  -5 ),

  /** a timeout has been recognized. */
  Timeout                                             (  -6 ),

  /** a listener implementation caused a failure. */
  ListenerFailure                                     (  -7 ),
  
  /** a reflections based functionality failed. */
  Reflections                                         (  -8 ),
  
  /** indicates a xml related failure. */
  XmlFailure                                          (  -9 ),
  
  /** a requested resource could not be found. */
  MissingResource                                     ( -10 ),
  
  /** a conversion failed. */
  ConversionFailure                                   ( -11 ),
  
  /** a directory creation failed. */
  CreateDirectory                                     ( -12 ),
  
  /** a file could not be found. */
  FileNotFound                                        ( -13 ),

  /** accessing a resource failed. */
  ResourceIO                                          ( -14 );
  
  private int      code;
  
  FailureCode( int value ) {
    code = value;
  }
  
  /**
   * Returns the numeric failure code associated with this constant.
   * 
   * @return   The numeric failure code associated with this constant.
   */
  public int getCode() {
    return code;
  }
  
  @Override
  public String toString() {
    return String.format( "%s[%d]", name(), Integer.valueOf( code ) );
  }
  
} /* ENDENUM */

/**
 * Name........: FailureCode
 * Description.: Collection of failure codes.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.constants;

import com.kasisoft.lgpl.tools.diagnostic.*;

/**
 * Collection of failure codes.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public enum FailureCode {

  /** everythings okay. */
  Success                                             (   0, "Success"                                              ),
  
  /** an unknown failure has been launched. */
  Unexpected                                          (  -1, "An unexpected error occured."                         ),
  
  /** closing a resource failed. */
  Close                                               (  -2, "Closing a resource failed."                           ),
  
  /** reading or writing failed. */
  IO                                                  (  -3, "There was an error while reading/writing a resource." ),
  
  /** skipping within a resource failed. */
  Skip                                                (  -4, "Skipping failed for some reason."                     ),
  
  /** the encoding that has been used is not supported. */
  InvalidEncoding                                     (  -5, "The encoding is invalid."                             ),

  /** a timeout has been recognized. */
  Timeout                                             (  -6, "A timeout occured."                                   ),

  /** a listener implementation caused a failure. */
  ListenerFailure                                     (  -7, "A listener implementation failed."                    ),
  
  /** a reflections based functionality failed. */
  Reflections                                         (  -8, "Error on Java reflections."                           ),
  
  /** indicates a xml related failure. */
  XmlFailure                                          (  -9, "There was an xml related error."                      ),
  
  /** a requested resource could not be found. */
  MissingResource                                     ( -10, "A resource could not be found."                       ),
  
  /** a conversion failed. */
  ConversionFailure                                   ( -11, "Some conversion failed."                              ),
  
  /** a directory creation failed. */
  CreateDirectory                                     ( -12, "The creation of a directory failed."                  );

  private int      code;
  private String   message;
  
  FailureCode( int value, String msg ) {
    code    = value;
    message = msg;
  }
  
  /**
   * Returns the message associated with this code.
   * 
   * @return   The message associated with this code. Neither <code>null</code> nor empty.
   */
  public String getMessage() {
    return message;
  }
  
  /**
   * Returns the numeric failure code associated with this constant.
   * 
   * @return   The numeric failure code associated with this constant.
   */
  public int getCode() {
    return code;
  }
  
  /**
   * {@inheritDoc}
   */
  public String toString() {
    return String.format( "%s[%d]", name(), Integer.valueOf( code ) );
  }
  
} /* ENDENUM */

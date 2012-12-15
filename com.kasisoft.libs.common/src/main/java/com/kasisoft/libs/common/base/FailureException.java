/**
 * Name........: FailureException
 * Description.: Specialisation of the RuntimeException which provides a numerical code which allows to handle this 
 *               exception in a more apropriate way than checking it's message.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.base;

/**
 * Specialisation of the RuntimeException which provides a numerical code which allows to handle this exception in a 
 * more apropriate way than checking it's message.
 */
public class FailureException extends RuntimeException {

  private FailureCode   failurecode;
  
  /**
   * Initialises this exception with the appropriate failure information.
   * 
   * @param code    A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param cause   The causing exception.
   */
  public FailureException( FailureCode code, Throwable cause ) {
    super( String.valueOf( code ), cause );
    failurecode = code;
  }

  /**
   * Initialises this exception with the appropriate failure information.
   * 
   * @param code   A failure code as described in {@link FailureCode}. Not <code>null</code>.
   */
  public FailureException( FailureCode code ) {
    super( String.valueOf( code ) );
    failurecode = code;
  }

  /**
   * Initialises this exception with the appropriate failure information.
   * 
   * @param code      A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param message   The error message. Neither <code>null</code> nor empty.
   */
  public FailureException( FailureCode code, String message ) {
    super( String.format( "%s: %s", String.valueOf( code ), message ) );
    failurecode = code;
  }

  /**
   * Returns the failurecode used to produce this exception.
   * 
   * @return   The failurecode used to produce this exception. Not <code>null</code>.
   */
  public FailureCode getFailureCode() {
    return failurecode;
  }
  
} /* ENDCLASS */

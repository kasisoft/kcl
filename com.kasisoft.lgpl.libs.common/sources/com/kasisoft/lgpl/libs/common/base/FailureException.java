/**
 * Name........: FailureException
 * Description.: Specialisation of the RuntimeException which provides a numerical code which allows 
 *               to handle this exception in a more apropriate way than checking it's message.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.base;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.tools.diagnostic.*;

/**
 * Specialisation of the RuntimeException which provides a numerical code which allows to handle 
 * this exception in a more apropriate way than checking it's message.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class FailureException extends RuntimeException {

  private FailureCode   failurecode;
  
  /**
   * Initialises this exception with the appropriate failure information.
   * 
   * @param code   A failure code as described in {@link FailureCode}. Not <code>null</code>.
   */
  public FailureException( FailureCode code ) {
    super( code.toString() );
    failurecode = code;
  }

  /**
   * Initialises this exception with the appropriate failure information.
   * 
   * @param code    A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param msg     The message of this failue.
   * @param cause   The causing exception.
   */
  public FailureException( FailureCode code, String msg, Throwable cause ) {
    super( String.format( "%s:%s", code.toString(), msg ), cause );
    failurecode = code;
  }

  /**
   * Initialises this exception with the appropriate failure information.
   * 
   * @param code   A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param msg    The message of this failue.
   */
  public FailureException( FailureCode code, String msg ) {
    super( String.format( "%s:%s", code.toString(), msg ) );
    failurecode = code;
  }

  /**
   * Initialises this exception with the appropriate failure information.
   * 
   * @param code    A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param cause   The causing exception.
   */
  public FailureException( FailureCode code, Throwable cause ) {
    super( String.format( "%s:%s", code.toString(), cause.getMessage() ), cause );
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

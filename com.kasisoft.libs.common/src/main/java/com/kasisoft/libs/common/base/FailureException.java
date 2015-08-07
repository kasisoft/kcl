package com.kasisoft.libs.common.base;

import lombok.*;
import lombok.experimental.*;

/**
 * Specialisation of the RuntimeException which provides a numerical code which allows to handle this exception in a 
 * more apropriate way than checking it's message.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Getter @FieldDefaults(level = AccessLevel.PRIVATE)
public class FailureException extends RuntimeException {

  FailureCode   failurecode;
  Object[]      params;

  private FailureException( String message, FailureCode code, Throwable cause, Object[] parameters ) {
    super( message, cause );
    failurecode = code;
    params      = parameters;
  }
  
  /**
   * Initialises this exception with the appropriate failure information.
   * 
   * @param code    A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param cause   The causing exception.
   */
  public static FailureException newFailureException( @NonNull FailureCode code, Throwable cause ) {
    return new FailureException( createMessage( code, null, null ), code, cause, null );
  }

  /**
   * Initialises this exception with the appropriate failure information.
   * 
   * @param code   A failure code as described in {@link FailureCode}. Not <code>null</code>.
   */
  public static FailureException newFailureException( @NonNull FailureCode code ) {
    return new FailureException( createMessage( code, null, null ), code, null, null );
  }

  /**
   * Initialises this exception with the appropriate failure information.
   * 
   * @param code      A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param message   The error message. Maybe <code>null</code>.
   */
  public static FailureException newFailureException( @NonNull FailureCode code, String message ) {
    return new FailureException( createMessage( code, message, null ), code, null, null );
  }
 
  /**
   * Initialises this exception with the appropriate failure information.
   * 
   * @param code      A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param message   The error message. Maybe <code>null</code>.
   * @param cause     The causing exception.
   */
  public static FailureException newFailureException( @NonNull FailureCode code, String message, Throwable cause ) {
    return new FailureException( createMessage( code, message, null ), code, cause, null );
  }

  /**
   * Initialises this exception with the appropriate failure information.
   * 
   * @param code      A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param message   The error message. Maybe <code>null</code>.
   * @param cause     The causing exception.
   * @param params    Optional parameters which have been involved in the cause of the exception. Maybe <code>null</code>.
   */
  public static FailureException newFailureException( @NonNull FailureCode code, String message, Throwable cause, Object ... params ) {
    return new FailureException( createMessage( code, message, params ), code, cause, params );
  }

  /**
   * Causes an exception if that's desired.
   * 
   * @param enabled   <code>true</code> <=> Raise the exception.
   * @param result    The result that shall be returned. Maybe <code>null</code>.
   * @param code      A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param cause     The causing exception. Maybe <code>null</code>.
   * 
   * @deprecated [07-Aug-2015:KASI]   Conditional exception raising will no longer be supported. Exceptions will be
   *                                  raised by default. This function will be removed with 1.9 .
   */             
  @Deprecated
  public static <T> T raiseIf( boolean enabled, T result, @NonNull FailureCode code, Throwable cause ) {
    return raiseIf( enabled, result, code, null, cause, (Object[]) null );
  }

  /**
   * Causes an exception if that's desired.
   * 
   * @param enabled   <code>true</code> <=> Raise the exception.
   * @param code      A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param cause     The causing exception. Maybe <code>null</code>.
   * 
   * @deprecated [07-Aug-2015:KASI]   Conditional exception raising will no longer be supported. Exceptions will be
   *                                  raised by default. This function will be removed with 1.9 .
   */
  @Deprecated
  public static <T> T raiseIf( boolean enabled, @NonNull FailureCode code, Throwable cause ) {
    return raiseIf( enabled, null, code, null, cause, (Object[]) null );
  }

  /**
   * Causes an exception if that's desired.
   * 
   * @param enabled   <code>true</code> <=> Raise the exception.
   * @param result    The result that shall be returned. Maybe <code>null</code>.
   * @param code      A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param cause     The causing exception. Maybe <code>null</code>.
   * @param params    Optional parameters which have been involved in the cause of the exception. Maybe <code>null</code>.
   * 
   * @deprecated [07-Aug-2015:KASI]   Conditional exception raising will no longer be supported. Exceptions will be
   *                                  raised by default. This function will be removed with 1.9 .
   */
  @Deprecated
  public static <T> T raiseIf( boolean enabled, T result, @NonNull FailureCode code, Throwable cause, Object ... params ) {
    return raiseIf( enabled, result, code, null, cause, params );
  }

  /**
   * Causes an exception if that's desired.
   * 
   * @param enabled   <code>true</code> <=> Raise the exception.
   * @param code      A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param cause     The causing exception. Maybe <code>null</code>.
   * @param params    Optional parameters which have been involved in the cause of the exception. Maybe <code>null</code>.
   * 
   * @deprecated [07-Aug-2015:KASI]   Conditional exception raising will no longer be supported. Exceptions will be
   *                                  raised by default. This function will be removed with 1.9 .
   */
  @Deprecated
  public static <T> T raiseIf( boolean enabled, @NonNull FailureCode code, Throwable cause, Object ... params ) {
    return raiseIf( enabled, null, code, null, cause, params );
  }

  /**
   * Causes an exception if that's desired.
   * 
   * @param enabled   <code>true</code> <=> Raise the exception.
   * @param result    The result that shall be returned. Maybe <code>null</code>.
   * @param code      A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * 
   * @deprecated [07-Aug-2015:KASI]   Conditional exception raising will no longer be supported. Exceptions will be
   *                                  raised by default. This function will be removed with 1.9 .
   */
  @Deprecated
  public static <T> T raiseIf( boolean enabled, T result, @NonNull FailureCode code ) {
    return raiseIf( enabled, result, code, null, null, (Object[]) null );
  }

  /**
   * Causes an exception if that's desired.
   * 
   * @param enabled   <code>true</code> <=> Raise the exception.
   * @param code      A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * 
   * @deprecated [07-Aug-2015:KASI]   Conditional exception raising will no longer be supported. Exceptions will be
   *                                  raised by default. This function will be removed with 1.9 .
   */
  @Deprecated
  public static <T> T raiseIf( boolean enabled, @NonNull FailureCode code ) {
    return raiseIf( enabled, null, code, null, null, (Object[]) null );
  }

  /**
   * Causes an exception if that's desired.
   * 
   * @param enabled   <code>true</code> <=> Raise the exception.
   * @param result    The result that shall be returned. Maybe <code>null</code>.
   * @param code      A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param params    Optional parameters which have been involved in the cause of the exception. Maybe <code>null</code>.
   * 
   * @deprecated [07-Aug-2015:KASI]   Conditional exception raising will no longer be supported. Exceptions will be
   *                                  raised by default. This function will be removed with 1.9 .
   */
  @Deprecated
  public static <T> T raiseIf( boolean enabled, T result, @NonNull FailureCode code, Object ... params ) {
    return raiseIf( enabled, result, code, null, null, params );
  }

  /**
   * Causes an exception if that's desired.
   * 
   * @param enabled   <code>true</code> <=> Raise the exception.
   * @param code      A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param params    Optional parameters which have been involved in the cause of the exception. Maybe <code>null</code>.
   * 
   * @deprecated [07-Aug-2015:KASI]   Conditional exception raising will no longer be supported. Exceptions will be
   *                                  raised by default. This function will be removed with 1.9 .
   */
  @Deprecated
  public static <T> T raiseIf( boolean enabled, @NonNull FailureCode code, Object ... params ) {
    return raiseIf( enabled, null, code, null, null, params );
  }

  /**
   * Causes an exception if that's desired.
   * 
   * @param enabled   <code>true</code> <=> Raise the exception.
   * @param result    The result that shall be returned. Maybe <code>null</code>.
   * @param code      A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param message   The error message. Maybe <code>null</code>.
   * 
   * @deprecated [07-Aug-2015:KASI]   Conditional exception raising will no longer be supported. Exceptions will be
   *                                  raised by default. This function will be removed with 1.9 .
   */
  @Deprecated
  public static <T> T raiseIf( boolean enabled, T result, @NonNull FailureCode code, String message ) {
    return raiseIf( enabled, result, code, message, null, (Object[]) null );
  }

  /**
   * Causes an exception if that's desired.
   * 
   * @param enabled   <code>true</code> <=> Raise the exception.
   * @param code      A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param message   The error message. Maybe <code>null</code>.
   * 
   * @deprecated [07-Aug-2015:KASI]   Conditional exception raising will no longer be supported. Exceptions will be
   *                                  raised by default. This function will be removed with 1.9 .
   */
  @Deprecated
  public static <T> T raiseIf( boolean enabled, @NonNull FailureCode code, String message ) {
    return raiseIf( enabled, null, code, message, null, (Object[]) null );
  }

  /**
   * Causes an exception if that's desired.
   * 
   * @param enabled   <code>true</code> <=> Raise the exception.
   * @param result    The result that shall be returned. Maybe <code>null</code>.
   * @param code      A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param message   The error message. Maybe <code>null</code>.
   * @param params    Optional parameters which have been involved in the cause of the exception. Maybe <code>null</code>.
   * 
   * @deprecated [07-Aug-2015:KASI]   Conditional exception raising will no longer be supported. Exceptions will be
   *                                  raised by default. This function will be removed with 1.9 .
   */
  @Deprecated
  public static <T> T raiseIf( boolean enabled, T result, @NonNull FailureCode code, String message, Object ... params ) {
    return raiseIf( enabled, result, code, message, null, params );
  }

  /**
   * Causes an exception if that's desired.
   * 
   * @param enabled   <code>true</code> <=> Raise the exception.
   * @param code      A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param message   The error message. Maybe <code>null</code>.
   * @param params    Optional parameters which have been involved in the cause of the exception. Maybe <code>null</code>.
   * 
   * @deprecated [07-Aug-2015:KASI]   Conditional exception raising will no longer be supported. Exceptions will be
   *                                  raised by default. This function will be removed with 1.9 .
   */
  @Deprecated
  public static <T> T raiseIf( boolean enabled, @NonNull FailureCode code, String message, Object ... params ) {
    return raiseIf( enabled, null, code, message, null, params );
  }

  /**
   * Causes an exception if that's desired.
   * 
   * @param enabled   <code>true</code> <=> Raise the exception.
   * @param code      A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param message   The error message. Maybe <code>null</code>.
   * @param cause     The causing exception.
   * @param params    Optional parameters which have been involved in the cause of the exception. Maybe <code>null</code>.
   * 
   * @deprecated [07-Aug-2015:KASI]   Conditional exception raising will no longer be supported. Exceptions will be
   *                                  raised by default. This function will be removed with 1.9 .
   */
  @Deprecated
  public static <T> T raiseIf( boolean enabled, @NonNull FailureCode code, String message, Throwable cause, Object ... params ) {
    return raiseIf( enabled, null, code, message, cause, params );
  }
  
  /**
   * Causes an exception if that's desired.
   * 
   * @param enabled   <code>true</code> <=> Raise the exception.
   * @param result    The result that shall be returned. Maybe <code>null</code>.
   * @param code      A failure code as described in {@link FailureCode}. Not <code>null</code>.
   * @param message   The error message. Maybe <code>null</code>.
   * @param cause     The causing exception.
   * @param params    Optional parameters which have been involved in the cause of the exception. Maybe <code>null</code>.
   * 
   * @deprecated [07-Aug-2015:KASI]   Conditional exception raising will no longer be supported. Exceptions will be
   *                                  raised by default. This function will be removed with 1.9 .
   */
  @Deprecated
  public static <T> T raiseIf( boolean enabled, T result, @NonNull FailureCode code, String message, Throwable cause, Object ... params ) {
    if( enabled ) {
      throw new FailureException( createMessage( code, message, params ), code, cause, params );
    } else {
      return result;
    }
  }

  private static String createMessage( FailureCode code, String message, Object[] params ) {
    StringBuilder builder = new StringBuilder();
    builder.append( String.valueOf( code ) );
    if( message != null ) {
      builder.append( ": " );
      builder.append( message );
    }
    if( (params != null) && (params.length > 0) ) {
      builder.append( "{" );
      builder.append( params[0] );
      for( int i = 1; i < params.length; i++ ) {
        builder.append( ", " );
        builder.append( params[i] );
      }
      builder.append( "}" );
    }
    return builder.toString();
  }
  
} /* ENDCLASS */

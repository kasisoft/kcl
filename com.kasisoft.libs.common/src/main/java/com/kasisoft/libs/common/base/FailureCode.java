package com.kasisoft.libs.common.base;

import lombok.experimental.*;

import lombok.*;

/**
 * Collection of failure codes.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum FailureCode {

  /** everythings okay. */
  Success                                             (   0 ),
  
  /** an unknown failure has been launched. */
  Unexpected                                          (  -1 ),
  
  /** reading or writing failed. */
  IO                                                  (  -3 ),
  
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
  
  /** a conversion failed. */
  ConversionFailure                                   ( -11 ),
  
  /** a jdbc operation failed */
  SqlFailure                                          ( -12 );
  
  int      code;
  
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
  
  /**
   * Initialises this exception with the appropriate failure information.
   */
  public FailureException newException() {
    return new FailureException( createMessage( null, null ), this, null, null );
  }
  
  /**
   * Initialises this exception with the appropriate failure information.
   * 
   * @param cause   The causing exception.
   */
  public FailureException newException( Throwable cause ) {
    if( cause instanceof FailureException ) {
      return (FailureException) cause;
    } else {
      return new FailureException( createMessage( null, null ), this, cause, null );
    }
  }
  
  /**
   * Initialises this exception with the appropriate failure information.
   * 
   * @param message   The error message. Maybe <code>null</code>.
   */
  public FailureException newException( String message ) {
    return new FailureException( createMessage( message, null ), this, null, null );
  }
 
  /**
   * Initialises this exception with the appropriate failure information.
   * 
   * @param message   The error message. Maybe <code>null</code>.
   * @param cause     The causing exception.
   */
  public FailureException newException( String message, Throwable cause ) {
    if( cause instanceof FailureException ) {
      return (FailureException) cause;
    } else {
      return new FailureException( createMessage( message, null ), this, cause, null );
    }
  }

  /**
   * Initialises this exception with the appropriate failure information.
   * 
   * @param message   The error message. Maybe <code>null</code>.
   * @param cause     The causing exception.
   * @param params    Optional parameters which have been involved in the cause of the exception. Maybe <code>null</code>.
   */
  public FailureException newException( String message, Throwable cause, Object ... params ) {
    if( cause instanceof FailureException ) {
      return (FailureException) cause;
    } else {
      return new FailureException( createMessage( message, params ), this, cause, params );
    }
  }
  
  private String createMessage( String message, Object[] params ) {
    StringBuilder builder = new StringBuilder();
    builder.append( String.valueOf( this ) );
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

  @Override
  public String toString() {
    return String.format( "%s[%d]", name(), Integer.valueOf( code ) );
  }
  
} /* ENDENUM */

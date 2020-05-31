package com.kasisoft.libs.common;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Specialisation of the RuntimeException which is commonly used within this library.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class KclException extends RuntimeException {

  public KclException() {
    super();
  }
  
  public KclException(@NotNull Exception ex) {
    super(ex);
  }
  
  public KclException(@NotNull String fmt, Object ... args) {
    super(formatString(fmt, args));
  }
  
  KclException(@NotNull Exception ex, @NotNull String fmt, Object ... args) {
    super(formatString(fmt, args), ex);
  }
  
  private static @NotNull String formatString(String fmt, Object ... args) {
    var result = fmt;
    if ((args != null) && (args.length > 0)) {
      result = String.format(fmt, args);
    }
    return result;
  }
  
  /**
   * This function makes sure that an exception is always wrapped as a {@link KclException} without
   * unnecessary wrappings.
   * 
   * @param ex   The exception that might need to be wrapped.
   * 
   * @return   A KclException instance.
   */
  public static @NotNull KclException wrap(@NotNull Exception ex) {
    if (ex instanceof KclException) {
      return (KclException) ex;
    } else {
      return new KclException(ex);
    }
  }

  /**
   * Looks for the causing KclException instance.
   * 
   * @param ex   The exception used to start to look for a causing KclException.
   * 
   * @return   The causing KclException instance or <code>null</code>.
   */
  public static @Null KclException unwrap(@Null Exception ex) {
    if (ex != null) {
      if (ex instanceof KclException) {
        return (KclException) ex;
      } else if (ex.getCause() instanceof Exception) {
        return unwrap((Exception) ex.getCause());
      }
    }
    return null;
  }
  
} /* ENDCLASS */

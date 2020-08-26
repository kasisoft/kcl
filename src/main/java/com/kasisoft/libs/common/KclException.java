package com.kasisoft.libs.common;

import javax.validation.constraints.NotNull;
import java.util.function.Supplier;

/**
 * Specialisation of the RuntimeException which is commonly used within this library.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class KclException extends RuntimeException {

  private static final long serialVersionUID = -7284302896487719569L;

  public KclException() {
    super();
  }
  
  public KclException(@NotNull Exception ex) {
    super(ex);
  }
  
  public KclException(String fmt, Object ... args) {
    super(formatString(fmt, args));
  }
  
  public KclException(@NotNull Exception ex, String fmt, Object ... args) {
    super(formatString(fmt, args), ex);
  }
  
  private static String formatString(String fmt, Object ... args) {
    var result = fmt;
    if ((args != null) && (args.length > 0)) {
      result = String.format(fmt, args);
    }
    return result;
  }
  
  public static <R> R execute(@NotNull Supplier<R> supplier, String fmt, Object ... args) {
    try {
      return supplier.get();
    } catch (Exception ex) {
      throw KclException.wrap(ex, fmt, args);
    }
  }
  
  public static @NotNull KclException wrap(@NotNull Exception ex) {
    if (ex instanceof KclException) {
      return (KclException) ex;
    } else {
      return new KclException(ex);
    }
  }

  public static @NotNull KclException wrap(@NotNull Exception ex, String fmt, Object ... args) {
    if (ex instanceof KclException) {
      return (KclException) ex;
    } else {
      return new KclException(ex, fmt, args);
    }
  }

  public static KclException unwrap(Exception ex) {
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

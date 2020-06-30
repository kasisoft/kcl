package com.kasisoft.libs.common;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

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
  
  public KclException(@Null String fmt, @Null Object ... args) {
    super(formatString(fmt, args));
  }
  
  public KclException(@NotNull Exception ex, @Null String fmt, @Null Object ... args) {
    super(formatString(fmt, args), ex);
  }
  
  private static @Null String formatString(@Null String fmt, @Null Object ... args) {
    var result = fmt;
    if ((args != null) && (args.length > 0)) {
      result = String.format(fmt, args);
    }
    return result;
  }
  
  public static <R> R execute(@NotNull Supplier<R> supplier, @Null String fmt, @Null Object ... args) {
    try {
      return supplier.get();
    } catch (KclException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new KclException(ex, fmt, args);
    }
  }
  
  public static @NotNull KclException wrap(@NotNull Exception ex) {
    if (ex instanceof KclException) {
      return (KclException) ex;
    } else {
      return new KclException(ex);
    }
  }

  public static @NotNull KclException wrap(@NotNull Exception ex, @Null String fmt, @Null Object ... args) {
    if (ex instanceof KclException) {
      return (KclException) ex;
    } else {
      return new KclException(ex, fmt, args);
    }
  }

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

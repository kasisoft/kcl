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

  FailureException( String message, FailureCode code, Throwable cause, Object[] parameters ) {
    super( message, cause );
    failurecode = code;
    params      = parameters;
  }
  
} /* ENDCLASS */

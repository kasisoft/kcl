package com.kasisoft.libs.common.config;

import lombok.experimental.*;

import lombok.*;

/**
 * Exception type that indicates that a required property is missing.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MissingPropertyException extends RuntimeException {

  /** Neither <code>null</code> nor empty. */
  @Getter String   property;

  /**
   * Initializes this exception with some information about the missing property.
   *  
   * @param propertykey   The property that is missing. Neither <code>null</code> nor empty.
   */
  public MissingPropertyException( @NonNull String propertykey ) {
    super( propertykey );
    property  = propertykey;
  }

} /* ENDCLASS */

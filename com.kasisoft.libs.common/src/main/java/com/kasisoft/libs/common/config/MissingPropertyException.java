package com.kasisoft.libs.common.config;

import lombok.*;

/**
 * Exception type that indicates that a required property is missing.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class MissingPropertyException extends RuntimeException {

  private String   property;

  /**
   * Initializes this exception with some information about the missing property.
   *  
   * @param propertykey   The property that is missing. Neither <code>null</code> nor empty.
   */
  public MissingPropertyException( @NonNull String propertykey ) {
    property  = propertykey;
  }

  /**
   * Returns the property that is missing.
   * 
   * @return   The property that is missing. Neither <code>null</code> nor empty.
   */
  public String getProperty() {
    return property;
  }
  
} /* ENDCLASS */

/**
 * Name........: EvalPropertyValueFactory
 * Description.: PropertyValueFactory implementation for evaluating properties. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util.properties;

import com.kasisoft.lgpl.tools.diagnostic.*;

/**
 * PropertyValueFactory implementation for evaluating properties.
 */
public class PropertyValueFactory {

  private ExtProperties.EvalType   evaltype;
  
  public PropertyValueFactory( ExtProperties.EvalType type ) {
    evaltype = type;
  }
    
  /**
   * {@inheritDoc}
   */
  public PropertyValue newPropertyValue( 
    @KNotNull(name="owner")   ExtProperties   owner,
    @KNotEmpty(name="key")    String          key,
                              String          value 
  ) {
    if( value == null ) {
      return null;
    } else {
      if( evaltype.isVariableValue( value ) ) {
        System.err.println( "@@@: '" + key + "'" );
        return new EvalPropertyValue( owner, key, value );
      } else {
        System.err.println( "+++: '" + key + "'" );
        return new StringPropertyValue( value );
      }
    }
  }
  
} /* ENDCLASS */

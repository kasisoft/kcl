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
public class EvalPropertyValueFactory extends PropertyValueFactory {

  private EvaluatingProperties.EvalType   evaltype;
  
  public EvalPropertyValueFactory( EvaluatingProperties.EvalType type ) {
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
        return new EvalPropertyValue( owner, key, value );
      } else {
        return new StringPropertyValue( value );
      }
    }
  }
  
} /* ENDCLASS */

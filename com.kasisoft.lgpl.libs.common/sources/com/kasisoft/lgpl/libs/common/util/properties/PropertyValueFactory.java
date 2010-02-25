/**
 * Name........: PropertyValueFactory
 * Description.: A factory class which can be used to create PropertyValue instances. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util.properties;

import com.kasisoft.lgpl.tools.diagnostic.*;

/**
 * A factory class which can be used to create PropertyValue instances.
 */
public class PropertyValueFactory {

  /**
   * Creates a new PropertyValue instance.
   * 
   * @param owner   The properties map causing the creation. Not <code>null</code>.
   * @param value   The textual property value itself. Maybe <code>null</code>.
   * 
   * @return   The PropertyValue instance or <code>null</code>.
   */
  public PropertyValue newPropertyValue( 
    @KNotNull(name="owner")   ExtProperties   owner, 
                              String          value 
  ) {
    if( value == null ) {
      return null;
    } else {
      return new StringPropertyValue( value );
    }
  }
  
} /* ENDCLASS */

/**
 * Name........: EvalPropertyValue
 * Description.: 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util.properties;

import com.kasisoft.lgpl.tools.diagnostic.*;

public class EvalPropertyValue implements PropertyValue {

  private EvaluatingProperties   properties;
  private String                 propkey;
  private String                 content;
  
  /**
   * Initialises this value using the supplied value.
   * 
   * @param val   The string value used to be stored within this type. Neither <code>null</code> nor empty.
   */
  public EvalPropertyValue( @KNotNull(name="fac") ExtProperties fac, @KNotEmpty(name="key") String key, @KNotEmpty(name="val") String val ) {
    content     = val;
    propkey     = key;
    properties  = (EvaluatingProperties) fac;
  }
  
  /**
   * {@inheritDoc}
   */
  public int hashCode() {
    return content.hashCode();
  }
  
  /**
   * {@inheritDoc}
   */
  public String toString() {
    return properties.evaluate( propkey, content );
  }

} /* ENDCLASS */

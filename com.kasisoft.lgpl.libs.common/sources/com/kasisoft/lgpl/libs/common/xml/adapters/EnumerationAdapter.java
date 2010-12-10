/**
 * Name........: EnumerationAdapter
 * Description.: An enumeration adapter allows to bind literals against an enumeration type. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml.adapters;

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.util.*;

/**
 * An enumeration adapter allows to bind literals against an enumeration type. Each descendent is
 * supposed to realise the following constraints:
 * 
 * <ul>
 *   <li>The parameter type must be an enumeration.</li>
 *   <li>The (un)marshalling is based upon the textual representation of an enumeration which
 *   means that the result of the toString() implementation is relevant here.</li>
 * </ul>
 * 
 */
public class EnumerationAdapter<T> extends NullSafeAdapter<String,T> {

  private Class<T>        enumtype;
  private Map<String,T>   values;
  private boolean         ignorecase;

  /**
   * Initializes this adapter using the supplied enumeration type.
   * 
   * @param type   The class of the enumeration which shall be adapted. Not <code>null</code>.
   */
  public EnumerationAdapter( @KNotNull(name="type") Class<T> type ) {
    this( type, false );
  }
  
  /**
   * Initializes this adapter using the supplied enumeration type.
   * 
   * @param type              The class of the enumeration which shall be adapted. Not <code>null</code>.
   * @param caseinsensitive   <code>true</code> <=> Disable case sensitivity.
   */
  public EnumerationAdapter( @KNotNull(name="type") Class<T> type, boolean caseinsensitive ) {
    enumtype    = type;
    ignorecase  = caseinsensitive;
    values      = new Hashtable<String,T>();
    for( T value : enumtype.getEnumConstants() ) {
      String text = String.valueOf( value );
      if( ignorecase ) {
        text = text.toLowerCase();
      }
      values.put( text, value );
    }
  }
  
  /**
   * {@inheritDoc}
   */
  public String marshalImpl( T v ) {
    return v.toString();
  }

  /**
   * {@inheritDoc}
   */
  public T unmarshalImpl( String v ) {
    if( ignorecase ) {
      v = v.toLowerCase();
    }
    return values.get( v );
  }
  
} /* ENDCLASS */

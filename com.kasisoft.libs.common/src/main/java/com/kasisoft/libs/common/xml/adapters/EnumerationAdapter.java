/**
 * Name........: EnumerationAdapter
 * Description.: An enumeration adapter allows to bind literals against an enumeration type. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.util.*;

import java.util.*;

/**
 * An enumeration adapter allows to bind literals against an enumeration type. Each descendent is supposed to realise 
 * the following constraints:
 * 
 * <ul>
 *   <li>The parameter type must be an enumeration.</li>
 *   <li>The (un)marshalling is based upon the textual representation of an enumeration which
 *   means that the result of the toString() implementation is relevant here.</li>
 * </ul>
 * 
 */
public class EnumerationAdapter<T> extends TypeAdapter<String,T> {

  private Class<T>        enumtype;
  private Map<String,T>   values;
  private boolean         ignorecase;
  private StringBuffer    allowed;

  /**
   * Initializes this adapter using the supplied enumeration type.
   * 
   * @param type   The class of the enumeration which shall be adapted. Not <code>null</code>.
   */
  public EnumerationAdapter( Class<T> type ) {
    this( null, null, null, type, false );
  }

  /**
   * Initializes this adapter using the supplied enumeration type.
   * 
   * @param type              The class of the enumeration which shall be adapted. Not <code>null</code>.
   * @param caseinsensitive   <code>true</code> <=> Disable case sensitivity.
   */
  public EnumerationAdapter( Class<T> type, boolean caseinsensitive ) {
    this( null, null, null, type, caseinsensitive );
  }

  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler           A custom error handler. Maybe <code>null</code>.
   * @param defval1           A default value for the source type. Maybe <code>null</code>.
   * @param defval2           A default value for the target type. Maybe <code>null</code>.
   * @param type              The class of the enumeration which shall be adapted. Not <code>null</code>.
   */
  public EnumerationAdapter( SimpleErrorHandler handler, String defval1, T defval2, Class<T> type ) {
    this( handler, defval1, defval2, type, false );
  }
  
  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler           A custom error handler. Maybe <code>null</code>.
   * @param defval1           A default value for the source type. Maybe <code>null</code>.
   * @param defval2           A default value for the target type. Maybe <code>null</code>.
   * @param type              The class of the enumeration which shall be adapted. Not <code>null</code>.
   * @param caseinsensitive   <code>true</code> <=> Disable case sensitivity.
   */
  public EnumerationAdapter( SimpleErrorHandler handler, String defval1, T defval2, Class<T> type, boolean caseinsensitive ) {
    super( handler, defval1, defval2 );
    enumtype    = type;
    ignorecase  = caseinsensitive;
    allowed     = new StringBuffer();
    values      = new Hashtable<String,T>();
    for( T value : enumtype.getEnumConstants() ) {
      String text = String.valueOf( value );
      if( allowed.length() > 0 ) {
        allowed.append( "," );
      }
      allowed.append( text );
      if( ignorecase ) {
        text = text.toLowerCase();
      }
      values.put( text, value );
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String marshalImpl( T v ) {
    return v.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T unmarshalImpl( String v ) {
    if( ignorecase ) {
      v = v.toLowerCase();
    }
    if( ! values.containsKey( v ) ) {
      throw new FailureException( FailureCode.ConversionFailure, String.format( "%s != {%s}", v, allowed ) );
    }
    return values.get( v );
  }
  
} /* ENDCLASS */

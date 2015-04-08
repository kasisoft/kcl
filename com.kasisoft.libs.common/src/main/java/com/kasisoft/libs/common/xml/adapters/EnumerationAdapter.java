package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.util.*;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

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
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnumerationAdapter<T> extends TypeAdapter<String,T> {

  Class<T>        enumtype;
  Map<String,T>   values;
  boolean         ignorecase;
  String          allowed;

  /**
   * Initializes this adapter using the supplied enumeration type.
   * 
   * @param type   The class of the enumeration which shall be adapted. Not <code>null</code>.
   */
  public EnumerationAdapter( @NonNull Class<T> type ) {
    this( null, null, null, type, false );
  }

  /**
   * Initializes this adapter using the supplied enumeration type.
   * 
   * @param type              The class of the enumeration which shall be adapted. Not <code>null</code>.
   * @param caseinsensitive   <code>true</code> <=> Disable case sensitivity.
   */
  public EnumerationAdapter( @NonNull Class<T> type, boolean caseinsensitive ) {
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
  public EnumerationAdapter( SimpleErrorHandler handler, String defval1, T defval2, @NonNull Class<T> type ) {
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
  public EnumerationAdapter( SimpleErrorHandler handler, String defval1, T defval2, @NonNull Class<T> type, boolean caseinsensitive ) {
    super( handler, defval1, defval2 );
    enumtype    = type;
    ignorecase  = caseinsensitive;
    values      = new Hashtable<>();
    allowed     = "";
    T[] enums   = enumtype.getEnumConstants();
    if( (enums != null) && (enums.length > 0) ) {
      StringBuilder builder = new StringBuilder();
      for( int i = 0; i < enums.length; i++ ) {
        builder.append(',');
        String text = String.valueOf( enums[i] );
        builder.append( text );
        if( ignorecase ) {
          text = text.toLowerCase();
        }
        values.put( text, enums[i] ); 
      }
      builder.deleteCharAt(0);
      allowed = builder.toString();
    }
  }
  
  @Override
  public String marshalImpl( @NonNull T v ) {
    return v.toString();
  }

  @Override
  public T unmarshalImpl( @NonNull String v ) {
    if( ignorecase ) {
      v = v.toLowerCase();
    }
    if( ! values.containsKey( v ) ) {
      throw FailureException.newFailureException( FailureCode.ConversionFailure, String.format( "%s != {%s}", v, allowed ) );
    }
    return values.get( v );
  }
  
} /* ENDCLASS */

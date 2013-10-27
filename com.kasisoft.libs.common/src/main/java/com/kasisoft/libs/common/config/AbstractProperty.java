/**
 * Name........: AbstractProperty
 * Description.: Base type which allows to realize typed property values.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.config;

import com.kasisoft.libs.common.util.*;
import com.kasisoft.libs.common.xml.adapters.*;

import java.util.*;

import lombok.*;

/**
 * Base type which allows to realize typed property values.
 */
@ToString
@EqualsAndHashCode(of={"key"})
public abstract class AbstractProperty<T,V,C extends AbstractProperty> {

  @Getter private String                  key;
  @Getter private TypeAdapter<String,T>   adapter;
  @Getter private boolean                 required;
  @Getter private String                  description;
  
  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion. This constructor
   * creates optional properties.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   */
  public AbstractProperty( String property, TypeAdapter<String,T> typeadapter ) {
    this( property, typeadapter, false );
  }
  
  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   * @param req           <code>true</code> <=> The property must be available which means it's value is not allowed
   *                                            to be <code>null</code>.
   */
  public AbstractProperty( String property, TypeAdapter<String,T> typeadapter, boolean req ) {
    key       = property;
    required  = req;
    adapter   = typeadapter;
  }
  
  /**
   * Configures the description for this property.
   * 
   * @param newdescription   The new description for this property. Maybe <code>null</code>.
   * 
   * @return   this
   */
  public C withDescription( String newdescription ) {
    description = StringFunctions.cleanup( newdescription );
    return (C) this;
  }
  
  /**
   * Delivers the typed value provided by it's textual representation.
   * 
   * @param value             The current textual presentation. Maybe <code>null</code>.
   * @param instancedefault   The default value which is supposed to be used if the value isn't valid. Maybe <code>null</code>.
   * 
   * @return   The typed value. Maybe <code>null</code>.
   */
  protected T getTypedValue( String value, T instancedefault ) {
    T result = null;
    if( value != null ) {
      // conversion errors will only be filed if the adapter comes with a specific handling
      result = adapter.unmarshal( value );
    }
    if( result == null ) {
      // use the default value provided by this property
      result = instancedefault;
    }
    return result;
  }
  
  /**
   * Returns the value of a property.
   * 
   * @param props        The properties instance. Maybe <code>null</code>.
   * @param properties   <code>true</code> <=> We're dealing with a Properties type here. 
   * @param key          The key used to access the value. Neither <code>null</code> nor empty.
   * 
   * @return   The property value. Maybe <code>null</code>.
   */
  protected String getProperty( Object props, boolean properties, String key ) {
    String result = null;
    if( props != null ) {
      if( properties ) {
        result = ((Properties) props).getProperty( key );
      } else {
        result = ((Map<String,String>) props).get( key );
      }
      result = cleanup( result );
    }
    return result;
  }
  
  /**
   * Makes sure that the supplied String is either <code>null</code> or not empty. The text will be trimmed so there 
   * won't be any whitespace at the beginning or the end (except for line delimiters).
   * 
   * @param input   The String that has to be altered.
   * 
   * @return   <code>null</code> or a non-empty String.
   */
  private String cleanup( String input ) {
    if( input != null ) {
      input = trim( input );
      if( input.length() == 0 ) {
        input = null;
      }
    }
    return input;
  }

  /**
   * Like {@link String#trim()} with the difference that line delimiters will be execluded here.
   * 
   * @param input   The input that is supposed to be trimmed. Not <code>null</code>.
   * 
   * @return   The trimmed input. Not <code>null</code>.
   */
  private String trim( String input ) {
    int    len = input.length();
    int    st  = 0;
    char[] val = input.toCharArray();
    while( (st < len) && (val[st] <= ' ') && (val[st] != '\r') && (val[st] != '\n') ) {
      st++;
    }
    while( (st < len) && (val[len - 1] <= ' ') && (val[st] != '\r') && (val[st] != '\n') ) {
      len--;
    }
    return ((st > 0) || (len < input.length())) ? input.substring( st, len ) : input;
  }
  
  /**
   * Changes the value of a property.
   * 
   * @param props        The properties instance. Maybe <code>null</code>.
   * @param properties   <code>true</code> <=> We're dealing with a Properties type here. 
   * @param key          The key used to access the value. Neither <code>null</code> nor empty.
   * @param value        The new value for the property. Maybe <code>null</code>.
   */
  protected void setProperty( Object props, boolean properties, String key, T value ) {
    if( props != null ) {
      if( value == null ) {
        if( properties ) {
          ((Properties) props).remove( key );
        } else {
          ((Map<String,String>) props).remove( key );
        }
      } else {
        if( properties ) {
          ((Properties) props).setProperty( key, getAdapter().marshal( value ) );
        } else {
          ((Map<String,String>) props).put( key, getAdapter().marshal( value ) );
        }
      }
    }
  }

} /* ENDCLASS */

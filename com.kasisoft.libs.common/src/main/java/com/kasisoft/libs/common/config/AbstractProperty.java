package com.kasisoft.libs.common.config;

import com.kasisoft.libs.common.util.*;
import com.kasisoft.libs.common.xml.adapters.*;

import java.util.*;

import lombok.*;

/**
 * Base type which allows to realize typed property values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@ToString
@EqualsAndHashCode(of={"key"})
public abstract class AbstractProperty<T,V,C extends AbstractProperty> {

  @Getter private String                  key;
  @Getter private TypeAdapter<String,T>   adapter;
  @Getter private boolean                 required;
  @Getter private String                  description;
  
  /**
   * @deprecated [06-Oct-2014:KASI]   This key abbreviation will be removed without a substitute starting with version 1.5 .
   */
  @Deprecated
  @Getter private String                  shortkey;
  
  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion. This constructor
   * creates optional properties.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   */
  public AbstractProperty( @NonNull String property, @NonNull TypeAdapter<String,T> typeadapter ) {
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
  public AbstractProperty( @NonNull String property, @NonNull TypeAdapter<String,T> typeadapter, boolean req ) {
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
   * Configures the shortkey for this property.
   * 
   * @param newshortkey   The new shortkey for this property. Maybe <code>null</code>.
   * 
   * @return   this
   * 
   * @deprecated [06-Oct-2014:KASI]   Will be removed without a substitute beginning with version 1.5.
   */
  @Deprecated
  public C withShortkey( String newshortkey ) {
    shortkey = StringFunctions.cleanup( newshortkey );
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
   * 
   * @deprecated [06-Oct-2014:KASI]   Will be removed with version 1.5.
   */
  @Deprecated
  protected String getProperty( Object props, boolean properties, @NonNull String key ) {
    return getProperty( (Map<?,?>) props, key );
  }

  /**
   * Returns the value of a property.
   * 
   * @param props        The properties instance. Maybe <code>null</code>.
   * @param key          The key used to access the value. Neither <code>null</code> nor empty.
   * 
   * @return   The property value. Maybe <code>null</code>.
   */
  protected String getProperty( Map<?,?> props, @NonNull String key ) {
    String result = null;
    if( props != null ) {
      result = cleanup( (String) props.get( key ) );
    }
    return result;
  }

  /**
   * Makes sure that the supplied String is either <code>null</code> or not empty. The text will be trimmed so there 
   * won't be any whitespace at the beginning or the end (except for line delimiters).
   * 
   * @param input   The String that has to be altered. Maybe <code>null</code>.
   * 
   * @return   <code>null</code> or a non-empty String.
   */
  protected String cleanup( String input ) {
    if( input != null ) {
      input = StringFunctions.trim( input, " \t", null );
      if( input.length() == 0 ) {
        input = null;
      }
    }
    return input;
  }

  /**
   * Changes the value of a property.
   * 
   * @param props        The properties instance. Maybe <code>null</code>.
   * @param properties   <code>true</code> <=> We're dealing with a Properties type here. 
   * @param key          The key used to access the value. Neither <code>null</code> nor empty.
   * @param value        The new value for the property. Maybe <code>null</code>.
   * 
   * @deprecated [06-Oct-2014:KASI]   Will be removed with version 1.5+.
   */
  @Deprecated
  protected void setProperty( Object props, boolean properties, @NonNull String key, T value ) {
    setProperty( (Map) props, key, value );
  }

  /**
   * Changes the value of a property.
   * 
   * @param props   The properties instance. Maybe <code>null</code>.
   * @param key     The key used to access the value. Neither <code>null</code> nor empty.
   * @param value   The new value for the property. Maybe <code>null</code>.
   */
  protected void setProperty( Map props, @NonNull String key, T value ) {
    if( props != null ) {
      if( value == null ) {
        props.remove( key );
      } else {
        props.put( key, getAdapter().marshal( value ) );
      }
    }
  }

} /* ENDCLASS */

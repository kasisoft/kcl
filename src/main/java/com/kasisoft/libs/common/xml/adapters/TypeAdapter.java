package com.kasisoft.libs.common.xml.adapters;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.*;

/**
 * Simple adapter implementation which is <code>null</code> safe and allows to convert datatypes if possible. 
 * This adapter is similar to the {@link XmlToTypeAdapter} with the following differences:
 * 
 * <ul>
 * <li>An exception will only be thrown if a {@link SimpleErrorHandler} has been passed which throws it. In all
 * other cases default values will be delivered.</li>
 * <li>This adapter assumes that <code>null</code> will always be mapped to <code>null</code> and vice versa.</li>
 * </ul>
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class TypeAdapter<F, T> implements Function<F, T> {

  BiConsumer<Object,Exception>   errhandler;
  F                              defvalue1;
  T                              defvalue2;

  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   */
  protected TypeAdapter() {
    this( null, null, null );
  }
  
  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  protected TypeAdapter( BiConsumer<Object, Exception> handler, F defval1, T defval2 ) {
    errhandler  = handler;
    defvalue1   = defval1;
    defvalue2   = defval2;
  }
  
  /**
   * Will be invoked whenever the binding of a value didn't succeed.
   * 
   * @param value   The value which has to be bound. Not <code>null</code>.
   * @param cause   The {@link Exception} indicating the possible cause of error. Not <code>null</code>.
   */
  protected void failure( @NonNull Object value, @NonNull Exception cause ) {
    if( errhandler != null ) {
      errhandler.accept( value, cause );
    }
  }
  
  /**
   * Converts the supplied To-Type into the expected From-Type.
   * 
   * @param v   The To-Type instance to convert. Maybe <code>null</code>.
   * 
   * @return   The From-Type instance. Maybe <code>null</code>.
   */
  public final F marshal( T v ) {
    if( v != null ) {
      try {
        return marshalImpl( v );
      } catch( Exception ex ) {
        failure( v, ex );
        return defvalue1;
      }
    }
    return null;
  }
  
  /**
   * Like {@link #marshal(Object)} with the difference that this function provides the cast implicitly. Therefore you 
   * need to be cautious as the supplied object might cause a ClassCastException.
   * 
   * @param v   The To-Type instance to convert. Maybe <code>null</code>.
   * 
   * @return   The From-Type instance. Maybe <code>null</code>.
   */
  public final F marshalObject( Object v ) {
    return marshal( (T) v );
  }

  /**
   * @see #marshal(Object)
   */
  public List<F> marshal( List<T> v ) {
    if( v != null ) {
      List<F> result = new ArrayList<>();
      for( int i = 0; i < v.size(); i++ ) {
        result.add( marshal( v.get(i) ) );
      }
      return result;
    }
    return null;
  }
  
  /**
   * Converts the supplied From-Type into the expected To-Type.
   * 
   * @param v   The From-Type instance to convert. Maybe <code>null</code>.
   * 
   * @return   The To-Type instance. Maybe <code>null</code>.
   */
  public final T unmarshal( F v ) {
    if( v != null ) {
      try {
        return unmarshalImpl( v );
      } catch( Exception ex ) {
        failure( v, ex );
        return defvalue2;
      }
    }
    return null;
  }

  /**
   * @see #unmarshal(Object)
   */
  public List<T> unmarshal( List<F> v ) {
    if( v != null ) {
      List<T> result = new ArrayList<>( v.size() );
      for( int i = 0; i < v.size(); i++ ) {
        result.add( apply( v.get(i) ) );
      }
      return result;
    }
    return null;
  }
  
  @Override
  public final T apply( F input ) {
    return unmarshal( input );
  }
  
  /**
   * @see #map(List)
   */
  public List<T> map( List<F> input ) {
    return unmarshal( input );
  }

  /**
   * @see #marshal(Object)
   * 
   * @param v   The value is guarantueed to be not <code>null</code>.
   */
  protected abstract F marshalImpl( T v ) throws Exception;

  /**
   * @see #unmarshal(Object) 
   * 
   * @param v   The value is guarantueed to be not <code>null</code>.
   */
  protected abstract T unmarshalImpl( F v ) throws Exception;

} /* ENDCLASS */
package com.kasisoft.libs.common.old.wrapper;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class WrapperFactory {

  public static Map<String, Object> toMap( Properties properties ) {
    Map<String, Object> result = null;
    if( properties != null ) {
      result = new PropertiesMap( properties );
    }
    return result;
  }

  public static <T> Function<T, T> toFunction( Consumer<T> consumer ) {
    Function<T,T> result = null;
    if( consumer != null ) {
      result = new IdentityAndApply( consumer );
    }
    return result;
  }
  
  @FieldDefaults(level = AccessLevel.PRIVATE)
  @AllArgsConstructor
  private static class IdentityAndApply<T> implements Function<T, T> {

    Consumer<T>   consumer;
    
    @Override
    public T apply( T t ) {
      consumer.accept(t);
      return t;
    }
    
  } /* ENDCLASS */

  @FieldDefaults(level = AccessLevel.PRIVATE)
  @AllArgsConstructor
  private static class PropertiesMap implements Map<String, Object> {

    Properties   properties;

    @Override
    public int size() {
      return properties.size();
    }

    @Override
    public boolean isEmpty() {
      return properties.isEmpty();
    }

    @Override
    public boolean containsKey( Object key ) {
      return properties.containsKey( key );
    }

    @Override
    public boolean containsValue( Object value ) {
      return properties.containsValue( value );
    }

    @Override
    public Object get( Object key ) {
      return properties.get( key );
    }

    @Override
    public Object put( String key, Object value ) {
      return properties.put( key, value );
    }

    @Override
    public Object remove( Object key ) {
      return properties.remove( key );
    }

    @Override
    public void putAll( Map<? extends String, ? extends Object> m ) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
      properties.clear();
    }

    @Override
    public Set<String> keySet() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Object> values() {
      return properties.values();
    }

    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
      throw new UnsupportedOperationException();
    }
    
  } /* ENDCLASS */

} /* ENDCLASS */

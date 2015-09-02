package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.base.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.lang.reflect.*;

/**
 * Default implementation of a BucketFactory.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultBucketFactory<T,P> implements BucketFactory<T> {

  Supplier<T>      create;
  BiConsumer<T,P>  reset;
  P                param;
  
  /**
   * Initializes this factory using the supplied creation and reset methods.
   * 
   * @param   The method used for the creation of an instance. Not <code>null</code>.
   * @param   The method used to perform a reset. Not <code>null</code>.
   * 
   * @throws FailureException   There's neither a default constructor nor a method named <code>reset</code> or
   *                            <code>clear</code>.
   */
  public DefaultBucketFactory( @NonNull Supplier<T> creator, @NonNull Consumer<T> resetter ) {
    this( creator, (x,y) -> resetter.accept(x), null );
  }
  
  /**
   * Initializes this factory using the supplied creation and reset methods.
   * 
   * @param   The method used for the creation of an instance. Not <code>null</code>.
   * @param   The method used to perform a reset. Not <code>null</code>.
   * @param   The reset value to be used if we're dealing with a parameterized reset method. Maybe <code>null</code>.
   * 
   * @throws FailureException   There's neither a default constructor nor a method named <code>reset</code> or
   *                            <code>clear</code>.
   */
  public DefaultBucketFactory( @NonNull Supplier<T> creator, @NonNull BiConsumer<T,P> resetter, P resetval ) {
    create = creator;
    reset  = resetter;
    param  = resetval;
  }
  
  /**
   * Initializes this factory using the supplied type.
   * 
   * @param type   The type that is used to create entries. Not <code>null</code>.
   * 
   * @throws FailureException   There's neither a default constructor nor a method named <code>reset</code> or
   *                            <code>clear</code>.
   *                            
   * @deprecated [03-Sep-2015:KASI]   Will be removed with version 2.1. Use {@link DefaultBucketFactory} instead.
   */
  @Deprecated
  public DefaultBucketFactory( @NonNull Class<? extends T> type ) {
    
    create = new Supplier<T>() {

      Constructor constructor;
      
      {
        constructor = MiscFunctions.getConstructor( type );
        if( constructor == null ) {
          throw FailureCode.Reflections.newException();
        }
      }
      
      @Override
      public T get() {
        try {
          return (T) constructor.newInstance();
        } catch( Exception ex ) {
          // won't happen as we've checked that within the constructor
          return null;
        }
      }
      
    };
    
    reset = new BiConsumer<T,P>() {
      
      Method method;
      
      {
        method = MiscFunctions.getMethod( type, "reset" );
        if( method == null ) {
          method = MiscFunctions.getMethod( type, "clear" );
        }
        if( method == null ) {
          throw FailureCode.Reflections.newException();
        }
      }

      @Override
      public void accept( T t, P p ) {
        try {
          method.invoke(t);
        } catch( Exception ex ) {
          // won't happen as we've checked that within the constructor
        }
      }
      
    };
    
    
    try {
      reset( create.get() );
    } catch( Exception ex ) {
      throw FailureCode.Reflections.newException( ex );
    }
    
  }
  
  @Override
  public <R extends T> R create() {
    return (R) create.get();
  }

  @Override
  public <R extends T> R reset( @NonNull T object ) {
    reset.accept( object, param );
    return (R) object;
  }

} /* ENDCLASS */

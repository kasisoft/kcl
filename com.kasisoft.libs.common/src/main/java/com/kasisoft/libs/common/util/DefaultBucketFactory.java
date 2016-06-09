package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.base.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

/**
 * Default implementation of a BucketFactory.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultBucketFactory<T, P> implements BucketFactory<T> {

  Supplier<T>       create;
  BiConsumer<T, P>  reset;
  P                 param;
  
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
  public DefaultBucketFactory( @NonNull Supplier<T> creator, @NonNull BiConsumer<T, P> resetter, P resetval ) {
    create = creator;
    reset  = resetter;
    param  = resetval;
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

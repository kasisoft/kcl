package com.kasisoft.libs.common.buckets;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import java.util.LinkedList;
import java.util.List;

import java.lang.ref.SoftReference;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
 
/**
 * Collector for often used objects like collections, maps etc. .
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bucket<T> {

  List<SoftReference<T>>   references;
  Supplier<T>              creator;
  Consumer<T>              reset;

  /**
   * Initializes this bucket.
   * 
   * @param producer   Supplier for new elements.
   * @param resetter   Cleaning function for elements.
   */
  public Bucket(@NotNull Supplier<T> producer, @NotNull Consumer<T> resetter) {
    references  = new LinkedList<>();
    creator     = producer;
    reset       = resetter;
  }
  
  /**
   * Returns the number of references currently stored.
   * 
   * @return   The number of references currently stored.
   */
  public int getSize() {
    return references.size();
  }
  
  /**
   * Creates a new object (potentially a reused one).
   * 
   * @return   A new object.
   */
  public @NotNull T allocate() {
    T result = null;
    synchronized (references) {
      while ((result == null) && (!references.isEmpty())) {
        SoftReference<T> reference = references.remove(0);
        result                     = reference.get();
        reference.clear();
      }
    }
    if (result == null) {
      result = creator.get();
    }
    return result;
  }

  /**
   * Frees the supplied object, so it's allowed to be reused.
   * 
   * @param object   The object that shall be freed. Maybe <code>null</code>.
   */
  public <R extends T> void free(@NotNull R object) {
    if (object != null) {
      synchronized (references) {
        reset.accept(object);
        references.add(new SoftReference<>(object));
      }
    }
  }

  /**
   * Executes the supplied function with the desired instance.
   * 
   * @param function   The function that is supposed to be executed. Not <code>null</code>.
   * 
   * @return   The return value of the supplied function. Maybe <code>null<code>.
   */
  public <R> @Null R forInstance(@NotNull Function<T, R> function) {
    T instance = allocate();
    try {
      return function.apply(instance);
    } finally {
      free(instance);
    }
  }

  /**
   * Executes the supplied function with the desired instance.
   * 
   * @param function   The function that is supposed to be executed. Not <code>null</code>.
   * @param param      An additional parameter for the function.
   * 
   * @return   The return value of the supplied function. Maybe <code>null<code>.
   */
  public <R, P> @Null R forInstance(@NotNull BiFunction<T, P, R> function, @Null P param) {
    T instance = allocate();
    try {
      return function.apply(instance, param);
    } finally {
      free(instance);
    }
  }

  /**
   * Executes the supplied consumer with the desired instance.
   * 
   * @param consumer   The consumer that is supposed to be executed. Not <code>null</code>.
   */
  public void forInstanceDo(@NotNull Consumer<T> consumer) {
    forInstance($ -> {consumer.accept($); return null;});
  }

  /**
   * Executes the supplied consumer with the desired instance.
   * 
   * @param consumer   The consumer that is supposed to be executed. Not <code>null</code>.
   * @param param      An additional parameter for the function.
   */
  public <P> void forInstanceDo(@NotNull BiConsumer<T, P> consumer, @Null P param) {
    forInstance($ -> {consumer.accept($, param); return null;});
  }

} /* ENDCLASS */

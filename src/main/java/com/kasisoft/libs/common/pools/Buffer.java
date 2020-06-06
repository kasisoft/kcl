package com.kasisoft.libs.common.pools;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import java.util.Collections;
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
public class Buffer<T> {

  List<SoftReference<T>>   references;
  List<Integer>            lengths;
  
  Function<Integer, T>     creator;
  Function<T, Integer>     getLength;
  Consumer<T>              reset;

  /**
   * Initializes this bucket.
   * 
   * @param producer   Supplier for new elements.
   * @param resetter   Cleaning function for elements.
   */
  public Buffer(@NotNull Function<Integer, T> producer, @NotNull Consumer<T> resetter, @NotNull Function<T, Integer> getLength) {
    references      = new LinkedList<>();
    lengths         = new LinkedList<>();
    creator         = producer;
    reset           = resetter;
    this.getLength  = getLength;
  }
  
  public void compact() {
    synchronized (references) {
      for (int i = references.size() - 1; i >= 0; i--) {
        var reference = references.get(i);
        var content   = reference.get();
        if (content == null) {
          references.remove(i);
          lengths.remove(i);
        }
      }
    }
  }
  
  /**
   * Creates a new object (potentially a reused one).
   * 
   * @return   A new object. The returned object might be larget than requested.
   */
  public @NotNull T allocate(int size) {
    T result = null;
    synchronized (references) {
      while ((result == null) && (!references.isEmpty())) {
        // look for a buffer with a sufficient size
        int idx = findSizableBuffer(size);
        if (idx != -1) {
          SoftReference<T> reference = references.remove(0);
          lengths.remove(0);
          result                     = reference.get();
          reference.clear();
        }
      }
    }
    if (result == null) {
      result = creator.apply(size);
    }
    return result;
  }

  private int findSizableBuffer(int size) {
    int idx = Collections.binarySearch(lengths, size);
    synchronized (references) {
      // find an index with a size equal or higher the requested size
      if (idx < 0) {
        idx = -idx + 1;
      }
      while (idx < lengths.size()) {
        // look for a buffer that's still exist
        if (references.get(idx).get() != null) {
          return idx;
        }
        idx++;
      }
      return -1;
    }
  }

  /**
   * Frees the supplied object, so it's allowed to be reused.
   * 
   * @param object   The object that shall be freed. Maybe <code>null</code>.
   */
  public void free(@Null T object) {
    if (object != null) {
      synchronized (references) {
        
        // clear the buffer
        reset.accept(object);
        
        // get the length of the buffer
        var length = getLength.apply(object);
        var idx    = Collections.binarySearch(lengths, length);
        if (idx < 0) {
          idx = -idx + 1;
        }
        references.add(idx, new SoftReference<>(object));
        lengths.add(idx, length);
        
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
  public <R> @Null R forInstance(int size, @NotNull Function<T, R> function) {
    T instance = allocate(size);
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
  public <R, P> @Null R forInstance(int size, @NotNull BiFunction<T, P, R> function, @Null P param) {
    T instance = allocate(size);
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
  public void forInstanceDo(int size, @NotNull Consumer<T> consumer) {
    forInstance(size, $ -> {consumer.accept($); return null;});
  }

  /**
   * Executes the supplied consumer with the desired instance.
   * 
   * @param consumer   The consumer that is supposed to be executed. Not <code>null</code>.
   * @param param      An additional parameter for the function.
   */
  public <P> void forInstanceDo(int size, @NotNull BiConsumer<T, P> consumer, @Null P param) {
    forInstance(size, $ -> {consumer.accept($, param); return null;});
  }

} /* ENDCLASS */

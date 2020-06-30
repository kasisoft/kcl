package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.KclException;

import javax.validation.constraints.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Collection of useful adaptation functions.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Functions {

  private Functions() {
  }
  
  public static @NotNull ActionListener adaptConsumerToActionListener(@NotNull Consumer<ActionEvent> handler) {
    return new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        handler.accept(evt);
      }
    };
  }
  
  /**
   * Protects the supplied function while testing for a null input.
   *  
   * @param delegate   The original function. Not <code>null</code>.
   * 
   * @return   The <code>null</code> aware function. <code>null</code> values will be passed through.
   *           Not <code>null</code>.
   */
  public static <I, O> @NotNull Function<I, O> nullSafeToFunction(@NotNull Function<I, O> delegate) {
    return $ -> $ != null ? delegate.apply($) : null;
  }
  
  public static <A, B, C, R> TriFunction<A, B, C, R> adaptFunctionToTriFunction(@NotNull Function<A, R> function) {
    return ($a, $b, $c) -> function.apply($a);
  }

  public static <A, B, C, R> TriFunction<A, B, C, R> adaptBiFunctionToTriFunction(@NotNull BiFunction<A, B, R> function) {
    return ($a, $b, $c) -> function.apply($a, $b);
  }

  public static <A, B, C> TriConsumer<A, B, C> adaptConsumerToTriConsumer(@NotNull Consumer<A> consumer) {
    return ($a, $b, $c) -> consumer.accept($a);
  }

  public static <A, B, C> TriConsumer<A, B, C> adaptBiConsumerToTriConsumer(@NotNull BiConsumer<A, B> consumer) {
    return ($a, $b, $c) -> consumer.accept($a, $b);
  }

  public static <A, B, R> @NotNull BiFunction<A, B, R> adaptFunctionToBiFunction(@NotNull Function<A, R> function) {
    return ($a, $b) -> function.apply($a);
  }

  public static <A, B> @NotNull BiConsumer<A, B> adaptConsumerToBiConsumer(@NotNull Consumer<A> consumer) {
    return ($a, $b) -> consumer.accept($a);
  }

  public static <A, B, R> @NotNull BiFunction<A, B, R> adaptTriFunctionToBiFunction(TriFunction<A, B, ?, R> function) {
    return ($a, $b) -> function.apply($a, $b, null);
  }

  public static <A, B> @NotNull BiConsumer<A, B> adaptTriConsumerToBiConsumer(@NotNull TriConsumer<A, B, ?> consumer) {
    return ($a, $b) -> consumer.accept($a, $b, null);
  }
  
  public static <A, R> @NotNull Function<A, R> adaptBiFunctionToFunction(@NotNull BiFunction<A, ?, R> function) {
    return $ -> function.apply($, null);
  }

  public static <A, B> @NotNull Consumer<A> adaptBiConsumerToConsumer(@NotNull BiConsumer<A, ?> consumer) {
    return $ -> consumer.accept($, null);
  }

  public static <A, R> @NotNull Function<A, R> adaptTriFunctionToFunction(@NotNull TriFunction<A, ?, ?, R> function) {
    return $ -> function.apply($, null, null);
  }

  public static <A> @NotNull Consumer<A> adaptTriConsumerToConsumer(@NotNull TriConsumer<A, ?, ?> consumer) {
    return $ -> consumer.accept($, null, null);
  }
  
  public static <A> @NotNull SimpleFunction<A> adaptConsumerToSimpleFunction(@NotNull Consumer<A> consumer) {
    return $ -> {
      consumer.accept($);
      return $;
    };
  }

  public static void run(@NotNull KRunnable inv) {
    try {
      inv.run();
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static <R> R get(@NotNull KSupplier<R> inv) {
    try {
      return inv.get();
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static <I, O> O apply(@NotNull KFunction<I, O> inv, I value) {
    try {
      return inv.apply(value);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static <I> void accept(@NotNull KConsumer<I> inv, I value) {
    try {
      inv.accept(value);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }
  
} /* ENDCLASS */

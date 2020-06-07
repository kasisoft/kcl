package com.kasisoft.libs.common.functional;

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
  
  public static @NotNull ActionListener adaptToActionListener(@NotNull Consumer<ActionEvent> handler) {
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
  public static <T> @NotNull Function<T, T> nullSafe(@NotNull Function<T, T> delegate) {
    return $ -> $ != null ? delegate.apply($) : null;
  }
  
  public static <A, B, C, R> com.kasisoft.libs.common.functional.TriFunction<A, B, C, R> adaptToTri(@NotNull Function<A, R> function) {
    return ($a, $b, $c) -> function.apply($a);
  }

  public static <A, B, C, R> com.kasisoft.libs.common.functional.TriFunction<A, B, C, R> adaptToTri(@NotNull BiFunction<A, B, R> function) {
    return ($a, $b, $c) -> function.apply($a, $b);
  }

  public static <A, B, C> com.kasisoft.libs.common.functional.TriConsumer<A, B, C> adaptToTri(@NotNull Consumer<A> consumer) {
    return ($a, $b, $c) -> consumer.accept($a);
  }

  public static <A, B, C> com.kasisoft.libs.common.functional.TriConsumer<A, B, C> adaptToTri(@NotNull BiConsumer<A, B> consumer) {
    return ($a, $b, $c) -> consumer.accept($a, $b);
  }

  public static <A, B, R> @NotNull BiFunction<A, B, R> adaptToBi(@NotNull Function<A, R> function) {
    return ($a, $b) -> function.apply($a);
  }

  public static <A, B> @NotNull BiConsumer<A, B> adaptToBi(@NotNull Consumer<A> consumer) {
    return ($a, $b) -> consumer.accept($a);
  }

  public static <A, B, R> @NotNull BiFunction<A, B, R> adaptToBi(TriFunction<A, B, ?, R> function) {
    return ($a, $b) -> function.apply($a, $b, null);
  }

  public static <A, B> @NotNull BiConsumer<A, B> adaptToBi(@NotNull TriConsumer<A, B, ?> consumer) {
    return ($a, $b) -> consumer.accept($a, $b, null);
  }
  
  public static <A, R> @NotNull Function<A, R> adapt(@NotNull BiFunction<A, ?, R> function) {
    return $ -> function.apply($, null);
  }

  public static <A, B> @NotNull Consumer<A> adapt(@NotNull BiConsumer<A, ?> consumer) {
    return $ -> consumer.accept($, null);
  }

  public static <A, R> @NotNull Function<A, R> adapt(@NotNull TriFunction<A, ?, ?, R> function) {
    return $ -> function.apply($, null, null);
  }

  public static <A> @NotNull Consumer<A> adapt(@NotNull TriConsumer<A, ?, ?> consumer) {
    return $ -> consumer.accept($, null, null);
  }
  
  public static <A> @NotNull SimpleFunction<A> adapt(@NotNull Consumer<A> consumer) {
    return $ -> {
      consumer.accept($);
      return $;
    };
  }

  
} /* ENDCLASS */

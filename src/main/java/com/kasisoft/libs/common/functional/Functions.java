package com.kasisoft.libs.common.functional;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.awt.event.*;

/**
 * Collection of useful adaptation functions.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class Functions {

    private Functions() {
    }

    @NotNull
    public static ActionListener adaptConsumerToActionListener(@NotNull Consumer<ActionEvent> handler) {
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
     * @param delegate
     *            The original function.
     * @return A null aware function. null values will be passed through.
     */
    @NotNull
    public static <I, O> Function<I, O> nullSafeToFunction(@NotNull Function<I, O> delegate) {
        return $ -> $ != null ? delegate.apply($) : null;
    }

    @NotNull
    public static <A, B, R> BiFunction<A, B, R> adaptFunctionToBiFunction(@NotNull Function<A, R> function) {
        return ($a, $b) -> function.apply($a);
    }

    @NotNull
    public static <A, B> BiConsumer<A, B> adaptConsumerToBiConsumer(@NotNull Consumer<A> consumer) {
        return ($a, $b) -> consumer.accept($a);
    }

    @NotNull
    public static <A, R> Function<A, R> adaptBiFunctionToFunction(@NotNull BiFunction<A, ?, R> function) {
        return $ -> function.apply($, null);
    }

    @NotNull
    public static <A, B> Consumer<A> adaptBiConsumerToConsumer(@NotNull BiConsumer<A, ?> consumer) {
        return $ -> consumer.accept($, null);
    }

    @NotNull
    public static <A> SimpleFunction<A> adaptConsumerToSimpleFunction(@NotNull Consumer<A> consumer) {
        return $ -> {
            consumer.accept($);
            return $;
        };
    }

    public static void run(@NotNull KRunnable inv) {
        inv.protect().run();
    }

    public static <R> R get(@NotNull KSupplier<R> inv) {
        return inv.protect().get();
    }

    public static <I, O> O apply(@NotNull KFunction<I, O> inv, I value) {
        return inv.protect().apply(value);
    }

    public static <I> void accept(@NotNull KConsumer<I> inv, I value) {
        inv.protect().accept(value);
    }

} /* ENDCLASS */

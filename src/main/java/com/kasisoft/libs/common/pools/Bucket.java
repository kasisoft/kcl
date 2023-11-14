package com.kasisoft.libs.common.pools;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.util.*;

import java.lang.ref.*;

/**
 * Collector for often used objects like collections, maps etc. .
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Bucket<T> {

    private List<SoftReference<T>> references;

    private Supplier<T>            creator;

    private Consumer<T>            reset;

    /**
     * Initializes this bucket.
     *
     * @param producer
     *            Supplier for new elements.
     * @param resetter
     *            Cleaning function for elements.
     */
    public Bucket(@NotNull Supplier<T> producer, @NotNull Consumer<T> resetter) {
        references = new LinkedList<>();
        creator    = producer;
        reset      = resetter;
    }

    public void reset() {
        synchronized (references) {
            references.forEach(SoftReference::clear);
            references.clear();
        }
    }

    /**
     * Returns the number of references currently stored.
     *
     * @return The number of references currently stored.
     */
    public int getSize() {
        return references.size();
    }

    /**
     * Creates a new object (potentially a reused one).
     *
     * @return A new object.
     */
    public @NotNull T allocate() {
        T result = null;
        synchronized (references) {
            while ((result == null) && (!references.isEmpty())) {
                var reference = references.remove(0);
                result = reference.get();
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
     * @param object
     *            The object that shall be freed.
     */
    public <R extends T> void free(R object) {
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
     * @param function
     *            The function that is supposed to be executed.
     * @return The return value of the supplied function.
     */
    public <R> R forInstance(@NotNull Function<T, R> function) {
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
     * @param function
     *            The function that is supposed to be executed.
     * @param param
     *            An additional parameter for the function.
     * @return The return value of the supplied function.
     */
    public <R, P> R forInstance(@NotNull BiFunction<T, P, R> function, P param) {
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
     * @param consumer
     *            The consumer that is supposed to be executed.
     */
    public void forInstanceDo(@NotNull Consumer<T> consumer) {
        forInstance($ -> {
            consumer.accept($);
            return null;
        });
    }

    /**
     * Executes the supplied consumer with the desired instance.
     *
     * @param consumer
     *            The consumer that is supposed to be executed.
     * @param param
     *            An additional parameter for the function.
     */
    public <P> void forInstanceDo(@NotNull BiConsumer<T, P> consumer, P param) {
        forInstance($ -> {
            consumer.accept($, param);
            return null;
        });
    }

} /* ENDCLASS */

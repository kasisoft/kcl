package com.kasisoft.libs.common.pools;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.util.*;

import java.lang.ref.*;

/**
 * Collector for often used objects like collections, maps etc. .
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class Buffer<T> {

    private List<SoftReference<T>> references;
    private List<Integer>          lengths;
    private Function<Integer, T>   creator;
    private Function<T, Integer>   getLength;
    private Consumer<T>            reset;

    /**
     * Initializes this bucket.
     *
     * @param producer
     *            Supplier for new elements.
     * @param resetter
     *            Cleaning function for elements.
     * @param getLength
     *            A function that provides the length per record.
     */
    public Buffer(@NotNull Function<Integer, T> producer, @NotNull Consumer<T> resetter, @NotNull Function<T, Integer> getLength) {
        references     = new LinkedList<>();
        lengths        = new LinkedList<>();
        creator        = producer;
        reset          = resetter;
        this.getLength = getLength;
    }

    public void compact() {
        synchronized (references) {
            for (var i = references.size() - 1; i >= 0; i--) {
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
     * @return A new object. The returned object might be larget than requested.
     */
    @NotNull
    public T allocate(int size) {
        T result = null;
        synchronized (references) {
            while ((result == null) && (!references.isEmpty())) {
                // look for a buffer with a sufficient size
                var idx = findSizableBuffer(size);
                if (idx != -1) {
                    var reference = references.remove(0);
                    lengths.remove(0);
                    result = reference.get();
                    reference.clear();
                } else {
                    break;
                }
            }
        }
        if (result == null) {
            result = creator.apply(size);
        }
        return result;
    }

    private int findSizableBuffer(int size) {
        synchronized (references) {
            var idx = Collections.binarySearch(lengths, size);
            // find an index with a size equal or higher the requested size
            if (idx < 0) {
                idx = -idx - 1;
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
     * @param object
     *            The object that shall be freed.
     */
    public void free(T object) {
        if (object != null) {
            synchronized (references) {

                // clear the buffer
                reset.accept(object);

                // get the length of the buffer
                var length = getLength.apply(object);
                var idx    = Collections.binarySearch(lengths, length);
                if (idx < 0) {
                    idx = -idx - 1;
                }
                references.add(idx, new SoftReference<>(object));
                lengths.add(idx, length);

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
    @NotNull
    public <R> R forInstance(int size, @NotNull Function<T, R> function) {
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
     * @param function
     *            The function that is supposed to be executed.
     * @param param
     *            An additional parameter for the function.
     * @return The return value of the supplied function.
     */
    @NotNull
    public <R, P> R forInstance(int size, @NotNull BiFunction<T, P, R> function, P param) {
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
     * @param consumer
     *            The consumer that is supposed to be executed.
     */
    public void forInstanceDo(int size, @NotNull Consumer<T> consumer) {
        forInstance(size, $ -> {
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
    public <P> void forInstanceDo(int size, @NotNull BiConsumer<T, P> consumer, P param) {
        forInstance(size, $ -> {
            consumer.accept($, param);
            return null;
        });
    }

} /* ENDCLASS */

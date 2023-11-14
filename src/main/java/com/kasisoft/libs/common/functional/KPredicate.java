package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface KPredicate<T> {

    boolean test(T arg) throws Exception;

    default KPredicate<T> and(@NotNull KPredicate<? super T> other) {
        return (t) -> test(t) && other.test(t);
    }

    default @NotNull KPredicate<T> negate() {
        return (t) -> !test(t);
    }

    default @NotNull KPredicate<T> or(@NotNull KPredicate<? super T> other) {
        return (t) -> test(t) || other.test(t);
    }

    default @NotNull Predicate<T> protect() {
        return (T t) -> {
            try {
                return test(t);
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        };
    }

    static <T> @NotNull KPredicate<T> isEqual(Object targetRef) {
        return (null == targetRef) ? Objects::isNull : object -> targetRef.equals(object);
    }

    @SuppressWarnings("unchecked")
    static <T> @NotNull KPredicate<T> not(KPredicate<? super T> target) {
        return (KPredicate<T>) target.negate();
    }

} /* ENDINTERFACE */

package com.kasisoft.libs.common.utils;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.types.*;

import com.kasisoft.libs.common.text.*;

import jakarta.validation.constraints.*;

import java.time.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
@SuppressWarnings("unchecked")
public class MiscFunctions {

    @NotNull
    public static <R> List<R> trimLeading(@NotNull List<R> input) {
        while ((!input.isEmpty()) && (input.get(0) == null)) {
            input.remove(0);
        }
        return input;
    }

    @NotNull
    public static <R> List<R> trimTrailing(@NotNull List<R> input) {
        while ((!input.isEmpty()) && (input.get(input.size() - 1) == null)) {
            input.remove(input.size() - 1);
        }
        return input;
    }

    @NotNull
    public static <R> List<R> trim(@NotNull List<R> input) {
        trimLeading(input);
        trimTrailing(input);
        return input;
    }

    public static String getGravatarLink(String email, Integer size) {
        /** @spec [22-JUN-2020:KASI] https://en.gravatar.com/site/implement/hash/ */
        String result = null;
        if (email != null) {
            result = StringFunctions.cleanup(email.toLowerCase());
            result = Digest.MD5.digestToString(result.getBytes());
            result = "https://www.gravatar.com/avatar/%s".formatted(result);
            if (size != null) {
                result += "?s=%d".formatted(size);
            }
        }
        return result;
    }

    /**
     * Returns <code>true</code> if the supplied year is a leap year.
     *
     * @param year
     *            The year which has to be tested.
     * @return <code>true</code> <=> The supplied year is a leap year.
     */
    public static boolean isLeapYear(int year) {
        if ((year % 400) == 0) {
            return true;
        } else if ((year % 100) == 0) {
            return false;
        } else {
            return (year % 4) == 0;
        }
    }

    /**
     * Returns <code>true</code> if the supplied date is a leap year.
     *
     * @param date
     *            The date which has to be tested.
     * @return <code>true</code> <=> The supplied date is a leap year.
     */
    @SuppressWarnings("deprecation")
    public static boolean isLeapYear(@NotNull Date date) {
        return isLeapYear(date.getYear() + 1900);
    }

    public static boolean isLeapYear(@NotNull OffsetDateTime date) {
        return isLeapYear(date.getYear());
    }

    public static boolean isLeapYear(@NotNull LocalDateTime date) {
        return isLeapYear(date.getYear());
    }

    /**
     * Calculates the biggest common divisor.
     *
     * @param a
     *            One number.
     * @param b
     *            Another number.
     * @return The biggest common divisor.
     */
    public static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return gcd(b, a % b);
        }
    }

    @NotNull
    public static <R> List<R> toUniqueList(List<R> list) {
        if ((list != null) && (!list.isEmpty())) {
            return new ArrayList<>(new TreeSet<R>(list));
        }
        return Collections.emptyList();
    }

    /**
     * Creates a list of pairs from the supplied entries.
     *
     * @param entries
     *            The entries that will be returned as list of pairs.
     * @return A list of pairs.
     */
    @NotNull
    public static <R> List<Pair<R, R>> toPairs(R ... entries) {
        if ((entries != null) && (entries.length > 0) && (entries.length % 2 == 0)) {
            var count  = entries.length / 2;
            var result = new ArrayList<Pair<R, R>>(count);
            for (int i = 0, j = 1; i < entries.length; i+=2, j+=2) {
                result.add(new Pair<>(entries[i], entries[j]));
            }
            return result;
        }
        return Collections.emptyList();
    }

    /**
     * Creates a map from the supplied entries.
     *
     * @param entries
     *            The entries that will be returned as a map.
     * @return A map providing all entries (unless the list length is odd).
     */
    @NotNull
    public static <K, V> Map<K, V> toMap(@NotNull Object ... entries) {
        if ((entries != null) && (entries.length > 0) && (entries.length % 2 == 0)) {
            var count  = entries.length / 2;
            var result = new HashMap<K, V>(count);
            for (int i = 0, j = 1; i < entries.length; i+=2, j+=2) {
                result.put((K) entries[i], (V) entries[j]);
            }
            return result;
        }
        return Collections.emptyMap();
    }

    @NotNull
    public static Map<@NotNull String, String> propertiesToMap(Properties properties) {
        var result = new HashMap<String, String>(100);
        if (properties != null) {
            var names = (Enumeration<String>) properties.propertyNames();
            while (names.hasMoreElements()) {
                var name = names.nextElement();
                result.put(name, StringFunctions.cleanup(properties.getProperty(name)));
            }
        }
        return result;
    }

} /* ENDCLASS */

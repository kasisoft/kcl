package com.kasisoft.libs.common.utils;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.types.*;

import com.kasisoft.libs.common.text.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.util.*;

import java.time.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@SuppressWarnings("unchecked")
public class MiscFunctions {

    public static <R> @NotNull List<R> trimLeading(@NotNull List<R> input) {
        while ((!input.isEmpty()) && (input.get(0) == null)) {
            input.remove(0);
        }
        return input;
    }

    public static <R> @NotNull List<R> trimTrailing(@NotNull List<R> input) {
        while ((!input.isEmpty()) && (input.get(input.size() - 1) == null)) {
            input.remove(input.size() - 1);
        }
        return input;
    }

    public static <R> @NotNull List<R> trim(@NotNull List<R> input) {
        trimLeading(input);
        trimTrailing(input);
        return input;
    }

    /**
     * Returns an adjusted index since this extension supports negative indices as well.
     *
     * @param length
     *            The length to be used for the calculation.
     * @param index
     *            The index supplied by the user.
     * @return The index to use for the original implementation.
     */
    public static int adjustIndex(int length, int index, boolean isEnd) {
        if (index < 0) {
            return length + index;
        } else if ((index == 0) && isEnd) {
            return length;
        }
        return index;
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
     * Creates a set from the supplied elements.
     *
     * @param elements
     *            The elements that shall be collected within a set.
     * @return The set created from the supplied elements.
     */
    public static <T> @NotNull Set<@NotNull T> toSet(T ... elements) {
        var result = new HashSet<T>();
        if (elements != null) {
            for (var i = 0; i < elements.length; i++) {
                result.add(elements[i]);
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

    public static <R> @NotNull List<R> toUniqueList(List<R> list) {
        var result = Collections.<R> emptyList();
        if (list != null) {
            result = new ArrayList<>(new TreeSet<R>(list));
        }
        return result;
    }

    /**
     * Creates a list of pairs from the supplied entries.
     *
     * @param entries
     *            The entries that will be returned as list of pairs.
     * @return A list of pairs.
     */
    public static <R> @NotNull List<Pair<R, R>> toPairs(R ... entries) {
        var result = Collections.<Pair<R, R>> emptyList();
        if (entries != null) {
            var count = entries.length / 2;
            result = new ArrayList<>(count);
            for (var i = 0; i < count; i++) {
                var offset = i * 2;
                result.add(new Pair<>(entries[offset], entries[offset + 1]));
            }
        }
        return result;
    }

    /**
     * Creates a map from the supplied entries.
     *
     * @param entries
     *            The entries that will be returned as a map.
     * @return A map providing all entries (unless the list length is odd).
     */
    public static <R1, R2> @NotNull Map<R1, R2> toMap(@NotNull Object ... entries) {
        var result = Collections.<R1, R2> emptyMap();
        if (entries != null) {
            var count = entries.length / 2;
            result = new HashMap<>(count);
            for (var i = 0; i < count; i++) {
                var offset = i * 2;
                result.put((R1) entries[offset], (R2) entries[offset + 1]);
            }
        }
        return result;
    }

    /**
     * Convenience function which waits until the supplied Thread finishes his task or will be
     * interrupted.
     *
     * @param thread
     *            The Thread that will be executed.
     */
    public static void joinThread(Thread thread) {
        if (thread != null) {
            try {
                thread.join(30_000);
            } catch (InterruptedException ex) {
            }
        }
    }

    public static void sleep(long millis) {
        while (millis > 0) {
            var before = System.currentTimeMillis();
            try {
                Thread.sleep(millis);
            } catch (InterruptedException ex) {
            }
            var after = System.currentTimeMillis();
            var done  = after - before;
            millis -= done;
        }
    }

    /**
     * Interpretes a value as a boolean. Causes an exception if the value isn't recognized.
     *
     * @param value
     *            The value which has to be parsed.
     * @return A boolean value if recognized or null if the argument is null as well.
     */
    public static Boolean parseBoolean(String value) {
        return parse(value, PrimitiveFunctions::parseBoolean);
    }

    /**
     * Interpretes a value as a byte. Causes an exception if the value isn't recognized.
     *
     * @param value
     *            The value which has to be parsed.
     * @return A byte value if recognized or null if the argument is null as well.
     */
    public static Byte parseByte(String value) {
        return parse(value, Byte::parseByte);
    }

    /**
     * Interpretes a value as a short. Causes an exception if the value isn't recognized.
     *
     * @param value
     *            The value which has to be parsed.
     * @return A short value if recognized or null if the argument is null as well.
     */
    public static Short parseShort(String value) {
        return parse(value, Short::parseShort);
    }

    /**
     * Interpretes a value as a integer. Causes an exception if the value isn't recognized.
     *
     * @param value
     *            The value which has to be parsed.
     * @return An int value if recognized or null if the argument is null as well.
     */
    public static Integer parseInt(String value) {
        return parse(value, Integer::parseInt);
    }

    /**
     * Interpretes a value as a long. Causes an exception if the value isn't recognized.
     *
     * @param value
     *            The value which has to be parsed.
     * @return A long value if recognized or null if the argument is null as well.
     */
    public static Long parseLong(String value) {
        return parse(value, Long::parseLong);
    }

    /**
     * Interpretes a value as a float. Causes an exception if the value isn't recognized.
     *
     * @param value
     *            The value which has to be parsed.
     * @return A float value if recognized or null if the argument is null as well.
     */
    public static Float parseFloat(String value) {
        return parse(value, Float::parseFloat);
    }

    /**
     * Interpretes a value as a double. Causes an exception if the value isn't recognized.
     *
     * @param value
     *            The value which has to be parsed.
     * @return A double value if recognized or null if the argument is null as well.
     */
    public static Double parseDouble(String value) {
        return parse(value, Double::parseDouble);
    }

    /**
     * Parses a number and returns null if the value is invalid.
     *
     * @param value
     *            The value shall be parsed.
     * @param parse
     *            The function used to parse the value.
     * @return The parsed value or null.
     */
    private static <T> T parse(String value, @NotNull Function<String, T> parse) {
        if (value != null) {
            try {
                return parse.apply(value);
            } catch (Exception ex) {
                // cause to return null
            }
        }
        return null;
    }

    /**
     * Creates a list of <param>count</param> elements while repeating the supplied one.
     *
     * @param count
     *            The number of elements that shall be created.
     * @param element
     *            The element that shall be repeated.
     * @return A list with the supplied amount of elements.
     */
    public static <T> @NotNull List<T> repeat(int count, T element) {
        var result = new ArrayList<T>(count);
        for (var i = 0; i < count; i++) {
            result.add(element);
        }
        return result;
    }

    public static @NotNull Map<@NotNull String, String> propertiesToMap(Properties properties) {
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

    public static <R> @NotNull ExtendedList<R> wrapToExtendedList(List<R> source) {
        if (source == null) {
            return new ExtendedList<>(new ArrayList<>());
        } else {
            return new ExtendedList<>(source);
        }
    }

} /* ENDCLASS */

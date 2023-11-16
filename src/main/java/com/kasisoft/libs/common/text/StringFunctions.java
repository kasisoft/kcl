package com.kasisoft.libs.common.text;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.pools.*;

import com.kasisoft.libs.common.utils.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.util.regex.Pattern;

import java.util.*;

import java.io.*;

/**
 * Collection of functions used for String processing.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class StringFunctions {

    private StringFunctions() {
    }

    /**
     * Returns the supplied string without it's prefix if there was any.
     *
     * @param name
     *            The name which might contain a suffix.
     * @return The string without the suffix (the dot will removed as well).
     */
    @NotNull
    public static String removeSuffix(@NotNull String name) {
        var lidx = name.lastIndexOf('.');
        if (lidx == -1) {
            return name;
        }
        return name.substring(0, lidx);
    }

    /**
     * Returns the suffix of the supplied string
     *
     * @param name
     *            The name which might contain a suffix.
     * @return The suffix without dot or null.
     */
    @NotNull
    public static String getSuffix(@NotNull String name) {
        var lidx = name.lastIndexOf('.');
        if (lidx == -1) {
            return null;
        }
        return name.substring(lidx + 1);
    }

    /**
     * Changes the suffix for the supplied name. If the name doesn't provide a suffix it will be
     * appended.
     *
     * @param name
     *            The name which might be altered.
     * @param suffix
     *            The suffix which has to be added (without '.').
     * @return The name with the updated suffix.
     */
    @NotNull
    public static String changeSuffix(@NotNull String name, @NotNull String suffix) {
        return "%s.%s".formatted(removeSuffix(name), suffix);
    }

    /**
     * Makes sure that the supplied String is either null or not empty. The text will be trimmed so
     * there won't be any whitespace at the beginning or the end.
     *
     * @param input
     *            The String that has to be altered.
     * @return null or a non-empty String.
     */
    public static String cleanup(String input) {
        if (input != null) {
            input = input.trim();
            if (input.length() == 0) {
                input = null;
            }
        }
        return input;
    }

    /**
     * Makes the first character upper case if there's one.
     *
     * @param input
     *            The String where the first character has to be altered.
     * @return A possibly in-place altered input.
     */
    @NotNull
    public static String firstUp(@NotNull String input) {
        if (input.length() == 1) {
            return input.toUpperCase();
        }
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }

    /**
     * Makes the first character lower case if there's one.
     *
     * @param input
     *            The CharSequence where the first character has to be altered.
     * @return A possibly in-place altered input.
     */
    @NotNull
    public static String firstDown(@NotNull String input) {
        if (input.length() == 1) {
            return input.toLowerCase();
        }
        return Character.toLowerCase(input.charAt(0)) + input.substring(1);
    }

    /**
     * Transforms the supplied value into a camelcase representation.
     *
     * @param input
     *            The object which has to be changed.
     * @return The supplied sequence if possible. The content is altered to a camelcase variety.
     */
    @NotNull
    public static String camelCase(@NotNull String input) {
        return Buckets.bucketStringBuilder().forInstance($ -> {
            $.append(input);
            int len = $.length();
            for (int i = len - 2, j = len - 1; i >= 0; i--, j--) {
                char current = $.charAt(i);
                char next    = $.charAt(j);
                if (!Character.isLetter(current)) {
                    $.setCharAt(j, Character.toUpperCase(next));
                    $.deleteCharAt(i);
                }
            }
            if ($.length() > 0) {
                $.setCharAt(0, Character.toLowerCase($.charAt(0)));
            }
            return $.toString();
        });
    }

    /**
     * Replaces all occurrences of a regular expression with a specified replacement.
     *
     * @param input
     *            The text that needs to be replaced.
     * @param search
     *            The term that should be replaced.
     * @param replacement
     *            The replacement which has to be used instead.
     * @return This buffer.
     */
    @NotNull
    public static String replaceLiterallyAll(@NotNull String input, @NotNull String search, @NotNull String replacement) {
        return replaceAll(input, Pattern.compile(Pattern.quote(search)), $ -> replacement);
    }

    /**
     * Replaces regions within some text.
     *
     * @param input
     *            The text which might be altered.
     * @param sep
     *            The opening/closing of a region (f.e. "(*").
     * @param replacement
     *            The replacement value.
     * @return The altered text.
     */
    @NotNull
    public static String replaceRegions(@NotNull String input, @NotNull String sep, @NotNull CharSequence replacement) {
        return replaceRegions(input, sep, sep, replacement);
    }

    /**
     * Replaces regions within some text.
     *
     * @param input
     *            The text which might be altered.
     * @param open
     *            The opening of a region (f.e. "(*").
     * @param close
     *            The closing of a region (f.e. "*)").
     * @param replacement
     *            The replacement value.
     * @return The altered text.
     */
    @NotNull
    public static String replaceRegions(@NotNull String input, @NotNull String open, String close, @NotNull CharSequence replacement) {
        return replaceRegions(input, open, close, $ -> replacement);
    }

    /**
     * Replaces regions within some text.
     *
     * @param input
     *            The text which might be altered.
     * @param delimiter
     *            The delimiter of a region (f.e. "(*").
     * @param replacement
     *            The replacement value.
     * @return The altered text.
     */
    @NotNull
    public static String replaceRegions(@NotNull String input, @NotNull String delimiter, @NotNull Function<String, CharSequence> replacement) {
        return replaceRegions(input, delimiter, delimiter, replacement);
    }

    /**
     * Replaces regions within some text.
     *
     * @param input
     *            The text which might be altered.
     * @param open
     *            The opening of a region (f.e. "(*").
     * @param close
     *            The closing of a region (f.e. "*)").
     * @param replacement
     *            The replacement value.
     * @return The altered text.
     */
    @NotNull
    public static String replaceRegions(@NotNull String input, @NotNull String open, String close, @NotNull Function<String, CharSequence> replacement) {
        if (close == null) {
            close = open;
        }
        var sb       = new StringBuilder(input);
        var start    = 0;
        var idxOpen  = sb.indexOf(open, start);
        var idxClose = sb.indexOf(close, idxOpen + 1);
        while ((idxOpen != -1) && (idxClose != -1)) {
            var inner = sb.substring(idxOpen + open.length(), idxClose);
            var value = replacement.apply(inner);
            sb.delete(idxOpen, idxClose + close.length());
            if (value != null) {
                sb.insert(idxOpen, value);
                start = idxOpen + value.length();
            } else {
                start = idxOpen;
            }
            idxOpen  = sb.indexOf(open, start);
            idxClose = sb.indexOf(close, idxOpen + 1);
        }
        return sb.toString();
    }

    public static String startsWithMany(@NotNull String input, @NotNull String ... candidates) {
        return startsWithMany(input, true, candidates);
    }

    public static String startsWithMany(@NotNull String input, boolean casesensitive, @NotNull String ... candidates) {
        if ((candidates == null) || (candidates.length == 0)) {
            return null;
        }
        BiPredicate<String, String> predicate = casesensitive ? String::startsWith : ($a, $b) -> $a.toLowerCase().startsWith($b.toLowerCase());
        for (String seq : candidates) {
            if (predicate.test(input, seq)) {
                return seq;
            }
        }
        return null;
    }

    public static String endsWithMany(@NotNull String input, @NotNull String ... candidates) {
        return endsWithMany(input, true, candidates);
    }

    public static String endsWithMany(@NotNull String input, boolean casesensitive, @NotNull String ... candidates) {
        if ((candidates == null) || (candidates.length == 0)) {
            return null;
        }
        BiPredicate<String, String> predicate = casesensitive ? String::endsWith : ($a, $b) -> $a.toLowerCase().endsWith($b.toLowerCase());
        for (String seq : candidates) {
            if (predicate.test(input, seq)) {
                return seq;
            }
        }
        return null;
    }

    /**
     * This function removes leading whitespace from this buffer.
     */
    @NotNull
    public static String trimLeading(@NotNull String input) {
        var sb = new StringBuilder(input);
        while (sb.length() > 0) {
            var codePoint = sb.codePointAt(0);
            if (!Character.isWhitespace(codePoint)) {
                break;
            }
            var charCount = Character.charCount(codePoint);
            sb.delete(0, charCount);
        }
        return sb.toString();
    }

    /**
     * This function removes trailing whitespace from this buffer.
     */
    @NotNull
    public static String trimTrailing(@NotNull String input) {
        var sb = new StringBuilder(input);
        while (sb.length() > 0) {
            var length    = sb.length();
            var codePoint = sb.codePointAt(length - 1);
            if (!Character.isWhitespace(codePoint)) {
                break;
            }
            var charCount = Character.charCount(codePoint);
            sb.delete(length - charCount, length);
        }
        return sb.toString();
    }

    /**
     * This function removes leading and trailing whitespace from this buffer.
     */
    @NotNull
    public static String trim(@NotNull String input) {
        return trimTrailing(trimLeading(input));
    }

    /**
     * This function removes leading whitespace from this buffer.
     *
     * @param chars
     *            The whitespace characters.
     */
    @NotNull
    public static String trimLeading(@NotNull String input, @NotBlank String chars) {
        var sb = new StringBuilder(input);
        while (sb.length() > 0) {
            var ch = sb.charAt(0);
            if (chars.indexOf(ch) == -1) {
                break;
            }
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    /**
     * This function removes trailing whitespace from this buffer.
     *
     * @param chars
     *            The whitespace characters.
     */
    @NotNull
    public static String trimTrailing(@NotNull String input, @NotBlank String chars) {
        var sb = new StringBuilder(input);
        while (sb.length() > 0) {
            var length = sb.length();
            var ch     = sb.charAt(length - 1);
            if (chars.indexOf(ch) == -1) {
                break;
            }
            sb.deleteCharAt(length - 1);
        }
        return sb.toString();
    }

    /**
     * Trims this instance depending on the provided settings.
     *
     * @param chars
     *            The whitespace characters.
     * @param left
     *            <code>null</code> <=> Trim left and right, <code>true</code> <=> Trim left,
     *            <code>false</code> <=> Trim right.
     */
    @NotNull
    public static String trim(@NotNull String input, @NotNull String chars, Boolean left) {
        if ((left == null) || left.booleanValue()) {
            input = trimLeading(input, chars);
        }
        if ((left == null) || (!left.booleanValue())) {
            input = trimTrailing(input, chars);
        }
        return input;
    }

    /**
     * Creates a concatenation of the supplied Strings. This function allows elements to be null which
     * means that they're just be ignored.
     *
     * @param delimiter
     *            A delimiter which might be used.
     * @param args
     *            The list of Strings that has to be concatenated.
     * @return The concatenated String
     */
    @NotNull
    public static String concatenate(String delimiter, CharSequence ... args) {
        return concatenate(delimiter, Arrays.asList(args));
    }

    /**
     * Creates a concatenation of the supplied Strings. This function allows elements to be null which
     * means that they're just be ignored.
     *
     * @param delimiter
     *            A delimiter which might be used.
     * @param args
     *            The collection of Strings that has to be concatenated.
     * @return The concatenated String.
     */
    @NotNull
    public static <C extends CharSequence, L extends Collection<C>> String concatenate(String delimiter, L args) {
        if ((args == null) || args.isEmpty()) {
            return Empty.NO_STRING;
        }
        var del = delimiter == null ? Empty.NO_STRING : delimiter;
        return Buckets.bucketStringBuilder().forInstance($ -> {
            var iterator = args.iterator();
            while (iterator.hasNext()) {
                var object = iterator.next();
                if ((object != null) && (object.length() > 0)) {
                    $.append(del);
                    $.append(object);
                }
            }
            if (($.length() > 0) && (del.length() > 0)) {
                $.delete(0, del.length());
            }
            return $.toString();
        });
    }

    /**
     * Repeats the supplied text <param>n</param> times.
     *
     * @param n
     *            The number of concatenations that have to be performed.
     * @param text
     *            The text that has to be repeated.
     * @return The concatenated reproduction string.
     */
    @NotNull
    public static String repeat(@Min(0) int n, CharSequence text) {
        if ((n > 0) && (text != null) && (text.length() > 0)) {
            return Buckets.bucketStringBuilder().forInstance($ -> {
                var c = n;
                while (c > 0) {
                    $.append(text);
                    c--;
                }
                return $.toString();
            });
        }
        return Empty.NO_STRING;
    }

    /**
     * Returns a textual representation of the supplied object.
     *
     * @param obj
     *            The object which textual representation is desired.
     * @return The textual representation of the supplied object.
     */
    @NotNull
    public static String objectToString(Object obj) {
        return Buckets.bucketStringBuilder().forInstance($ -> {
            appendToString($, obj);
            return $.toString();
        });
    }

    /**
     * Returns a textual representation of the supplied object.
     *
     * @param obj
     *            The object which textual representation is desired.
     */
    private static void appendToString(@NotNull StringBuilder receiver, Object obj) {
        if (obj == null) {
            receiver.append("null");
        } else if (obj instanceof boolean[] value) {
            appendToStringBooleanArray(receiver, value);
        } else if (obj instanceof char[] value) {
            appendToStringCharArray(receiver, value);
        } else if (obj instanceof byte[] value) {
            appendToStringByteArray(receiver, value);
        } else if (obj instanceof short[] value) {
            appendToStringShortArray(receiver, value);
        } else if (obj instanceof int[] value) {
            appendToStringIntArray(receiver, value);
        } else if (obj instanceof long[] value) {
            appendToStringLongArray(receiver, value);
        } else if (obj instanceof float[] value) {
            appendToStringFloatArray(receiver, value);
        } else if (obj instanceof double[] value) {
            appendToStringDoubleArray(receiver, value);
        } else if (obj.getClass().isArray()) {
            appendToStringObjectArray(receiver, (Object[]) obj);
        } else if (obj instanceof Throwable value) {
            appendToStringThrowable(receiver, value);
        } else {
            receiver.append(String.valueOf(obj));
        }
    }

    private static void appendToStringThrowable(@NotNull StringBuilder receiver, @NotNull Throwable throwable) {
        Buckets.bucketStringWriter().forInstanceDo($ -> {
            try (var writer = new PrintWriter($)) {
                throwable.printStackTrace(writer);
            }
            receiver.append($.toString());
        });
    }

    private static void appendToStringObjectArray(@NotNull StringBuilder receiver, @NotNull Object[] array) {
        receiver.append('[');
        if (array.length > 0) {
            appendToString(receiver, array[0]);
            for (var i = 1; i < array.length; i++) {
                receiver.append(',');
                appendToString(receiver, array[i]);
            }
        }
        receiver.append(']');
    }

    private static void appendToStringBooleanArray(@NotNull StringBuilder receiver, @NotNull boolean[] array) {
        receiver.append('[');
        if (array.length > 0) {
            receiver.append(array[0]);
            for (var i = 1; i < array.length; i++) {
                receiver.append(',').append(array[i]);
            }
        }
        receiver.append(']');
    }

    private static void appendToStringCharArray(@NotNull StringBuilder receiver, @NotNull char[] array) {
        receiver.append('[');
        if (array.length > 0) {
            receiver.append('\'').append(array[0]).append('\'');
            for (var i = 1; i < array.length; i++) {
                receiver.append(',').append('\'').append(array[i]).append('\'');
            }
        }
        receiver.append(']');
    }

    private static void appendToStringByteArray(@NotNull StringBuilder receiver, @NotNull byte[] array) {
        receiver.append('[');
        if (array.length > 0) {
            receiver.append("(byte)").append(array[0]);
            for (var i = 1; i < array.length; i++) {
                receiver.append(',').append("(byte)").append(array[i]);
            }
        }
        receiver.append(']');
    }

    private static void appendToStringShortArray(@NotNull StringBuilder receiver, @NotNull short[] array) {
        receiver.append('[');
        if (array.length > 0) {
            receiver.append("(short)").append(array[0]);
            for (var i = 1; i < array.length; i++) {
                receiver.append(',').append("(short)").append(array[i]);
            }
        }
        receiver.append(']');
    }

    private static void appendToStringIntArray(@NotNull StringBuilder receiver, @NotNull int[] array) {
        receiver.append('[');
        if (array.length > 0) {
            receiver.append(array[0]);
            for (var i = 1; i < array.length; i++) {
                receiver.append(',').append(array[i]);
            }
        }
        receiver.append(']');
    }

    private static void appendToStringLongArray(@NotNull StringBuilder receiver, @NotNull long[] array) {
        receiver.append('[');
        if (array.length > 0) {
            receiver.append(array[0]).append('l');
            for (var i = 1; i < array.length; i++) {
                receiver.append(',').append(array[i]).append('l');
            }
        }
        receiver.append(']');
    }

    private static void appendToStringFloatArray(@NotNull StringBuilder receiver, @NotNull float[] array) {
        receiver.append('[');
        if (array.length > 0) {
            receiver.append(array[0]).append('f');
            for (var i = 1; i < array.length; i++) {
                receiver.append(',').append(array[i]).append('f');
            }
        }
        receiver.append(']');
    }

    private static void appendToStringDoubleArray(@NotNull StringBuilder receiver, @NotNull double[] array) {
        receiver.append('[');
        if (array.length > 0) {
            receiver.append(array[0]);
            for (var i = 1; i < array.length; i++) {
                receiver.append(',').append(array[i]);
            }
        }
        receiver.append(']');
    }

    public static int indexOf(@NotNull String input, char ... characters) {
        return indexOf(input, 0, characters);
    }

    public static int indexOf(@NotNull String input, int index, char ... characters) {
        var result = -1;
        if ((characters != null) && (characters.length > 0)) {
            var str = input.toString();
            for (char ch : characters) {
                var idx = str.indexOf(ch, index);
                if (idx != -1) {
                    if (result == -1) {
                        result = idx;
                    } else {
                        result = Math.min(idx, result);
                    }
                }
                if (result == 0) {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Performs a search & replace operation on the supplied input.
     *
     * @param input
     *            The input which has to be modified.
     * @param replacements
     *            A Map of String's used to run the search replace operation.
     * @return The modified String.
     */
    @NotNull
    public static String replaceAll(@NotNull String input, @NotNull Map<String, String> replacements) {
        return replaceAll(input, replacements, null);
    }

    /**
     * Performs a search & replace operation on the supplied input.
     *
     * @param input
     *            The input which has to be modified.
     * @param replacements
     *            A Map of String's used to run the search replace operation.
     * @param fmt
     *            A key formatter. Default: '%s' (alternativ: '${%s}' which means that keys will be
     *            ${fredo}, ${dodo}, ...)
     * @return The modified String.
     */
    @NotNull
    public static String replaceAll(@NotNull String input, @NotNull Map<String, String> replacements, String fmt) {
        return Buckets.<String, String> bucketHashMap().forInstance($substitutions -> {

          // setup the substitution map
          if ((fmt != null) && (!"%s".equals(fmt))) {
              replacements.forEach(($k, $v) -> $substitutions.put(fmt.formatted($k), $v));
          } else {
            $substitutions.putAll(replacements);
          }

          // build a big OR of all keys
          var pattern = Pattern.compile(RegExpressions.oredKeywords($substitutions.keySet()));
          return replaceAll(input, pattern, $substitutions::get);

        });
    }

    /**
     * Replaces all occurrences of a regular expression with a specified replacement.
     *
     * @param pattern
     *            The Pattern providing the regular expression for the substitution.
     * @param replacementSupplier
     *            The replacement which has to be used instead.
     * @return This buffer.
     */
    @NotNull
    public static String replaceAll(@NotNull String input, @NotNull Pattern pattern, @NotNull Function<String, String> replacementSupplier) {
        return Buckets.<Integer> bucketArrayList().forInstance($ranges -> {

            var matcher = pattern.matcher(input);

            // we're collecting the substitutions from left to right, so the supplying function can assume this and use an
            // internal state if desired
            while (matcher.find()) {
                $ranges.add(matcher.start());
                $ranges.add(matcher.end());
            }

            return substitute(input, $ranges, replacementSupplier);

        });
    }

    private static String substitute(@NotNull String input, List<Integer> ranges, @NotNull Function<String, String> mapper) {
        return Buckets.bucketStringBuilder().forInstance($sb -> {
            $sb.append(input);
            for (int i = ranges.size() - 2; i >= 0; i -= 2) {
                int start = ranges.get(i);
                int end   = ranges.get(i + 1);
                var key   = input.substring(start, end);
                $sb.delete(start, end);
                $sb.insert(start, mapper.apply(key));
            }
            return $sb.toString();
        });
    }

} /* ENDCLASS */

package com.kasisoft.libs.common.text;

import com.kasisoft.libs.common.constants.Empty;

import com.kasisoft.libs.common.pools.Buckets;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.function.Function;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import java.io.PrintWriter;

/**
 * Collection of functions used for String processing.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class StringFunctions {

  private StringFunctions() {
  }
  
  /**
   * Returns the supplied string without it's prefix if there was any.
   * 
   * @param name   The name which might contain a suffix.
   * 
   * @return   The string without the suffix (the dot will removed as well).
   */
  public static @NotNull String removeSuffix(@NotNull String name) {
   var lidx = name.lastIndexOf('.');
   if (lidx == -1) {
     return name;
   } else {
     return name.substring(0, lidx);
   }
  }
  
  /**
   * Changes the suffix for the supplied name. If the name doesn't provide a suffix it will be appended.
   * 
   * @param name     The name which might be altered.
   * @param suffix   The suffix which has to be added (without '.').
   * 
   * @return   The name with the updated suffix.
   */
  public static @NotNull String changeSuffix(@NotNull String name, @NotNull String suffix) {
    return String.format("%s.%s", removeSuffix(name), suffix);
  }
  
  /**
   * Makes sure that the supplied String is either null or not empty. The text will be trimmed so there 
   * won't be any whitespace at the beginning or the end.
   * 
   * @param input   The String that has to be altered.
   * 
   * @return   null or a non-empty String.
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
   * @param input   The String where the first character has to be altered.
   * 
   * @return   A possibly in-place altered input.
   */
  public static @NotNull String firstUp(@NotNull String input) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).firstUp().toString());
  }
  
  /**
   * Makes the first character lower case if there's one.
   * 
   * @param input   The CharSequence where the first character has to be altered.
   * 
   * @return   A possibly in-place altered input.
   */
  public static @NotNull String firstDown(@NotNull String input) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).firstDown().toString());
  }
  
  /**
   * Replaces all occurrences of a regular expression with a specified replacement.
   * 
   * @param input         The text that needs to be replaced.
   * @param search        The term that should be replaced.
   * @param replacement   The replacement which has to be used instead.
   * 
   * @return   This buffer.
   */
  public static @NotNull String replaceLiterallyAll(@NotNull String input, @NotNull String search, @NotNull String replacement) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).replaceLiterallyAll(search, replacement).toString());
  }
  
  /**
   * Performs a search & replace operation on the supplied input.
   * 
   * @param input          The input which has to be modified.
   * @param replacements   A Map of String's used to run the search replace operation.
   * 
   * @return   The modified String.
   */
  public static @NotNull String replaceAll(@NotNull String input, @NotNull Map<String,String> replacements) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).replaceAll(replacements).toString());
  }
  
  /**
   * Performs a search & replace operation on the supplied input.
   * 
   * @param input          The input which has to be modified.
   * @param replacements   A Map of String's used to run the search replace operation.
   * @param fmt            A key formatter. Default: '%s' (alternativ: '${%s}' which means that keys will be ${fredo}, ${dodo}, ...)
   * 
   * @return   The modified String.
   */
  public static @NotNull String replaceAll(@NotNull String input, @NotNull Map<String,String> replacements, String fmt) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).replaceAll(replacements, fmt).toString());
  }

  /**
   * Transforms the supplied value into a camelcase representation.
   * 
   * @param input   The object which has to be changed.
   *                      
   * @return   The supplied sequence if possible. The content is altered to a camelcase variety. 
   */
  public static @NotNull String camelCase(@NotNull String input) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).camelCase().toString());
  }
  
  /**
   * Replaces regions within some text.
   * 
   * @param input         The text which might be altered.
   * @param sep           The opening/closing of a region (f.e. "(*").
   * @param replacement   The replacement value.
   *
   * @return   The altered text.
   */
  public static @NotNull String replaceRegions(@NotNull String input, @NotNull String sep, @NotNull CharSequence replacement) {
    return replaceRegions(input, sep, sep, replacement);
  }

  /**
   * Replaces regions within some text.
   * 
   * @param input         The text which might be altered.
   * @param open          The opening of a region (f.e. "(*").
   * @param close         The closing of a region (f.e. "*)").
   * @param replacement   The replacement value.
   *
   * @return   The altered text.
   */
  public static @NotNull String replaceRegions(@NotNull String input, @NotNull String open, String close, @NotNull CharSequence replacement) {
    return replaceRegions(input, open, close, $ -> replacement);
  }

  /**
   * Replaces regions within some text.
   * 
   * @param input         The text which might be altered.
   * @param open          The opening of a region (f.e. "(*").
   * @param close         The closing of a region (f.e. "*)").
   * @param replacement   The replacement value.
   *
   * @return   The altered text.
   */
  public static @NotNull String replaceRegions(@NotNull String input, @NotNull String open, @NotNull Function<String, CharSequence> replacement) {
    return replaceRegions(input, open, open, replacement);
  }

  /**
   * Replaces regions within some text.
   * 
   * @param input         The text which might be altered.
   * @param open          The opening of a region (f.e. "(*").
   * @param close         The closing of a region (f.e. "*)").
   * @param replacement   The replacement value.
   *
   * @return   The altered text.
   */
  public static @NotNull String replaceRegions(@NotNull String input, @NotNull String open, String close, @NotNull Function<String, CharSequence> replacement) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).replaceRegions(open, close, replacement).toString());
  }

  public static <T extends CharSequence> T startsWithMany(@NotNull String input, @NotNull T ... candidates) {
    return startsWithMany(input, true, candidates);
  }
  
  public static <T extends CharSequence> T startsWithMany(@NotNull String input, boolean casesensitive, @NotNull T ... candidates) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).startsWithMany(casesensitive, candidates));
  }

  public static <T extends CharSequence> T endsWithMany(@NotNull String input, @NotNull T ... candidates) {
    return endsWithMany(input, true, candidates);
  }

  public static <T extends CharSequence> T endsWithMany(@NotNull String input, boolean casesensitive, @NotNull T ... candidates) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).endsWithMany(casesensitive, candidates));
  }
  
  public static @NotNull String trim(@NotNull String input, @NotNull String chars, Boolean left) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).trim(chars, left).toString());
  }

  /**
   * Creates a concatenation of the supplied Strings. This function allows elements to be null which means
   * that they're just be ignored.
   * 
   * @param delimiter   A delimiter which might be used.
   * @param args        The list of Strings that has to be concatenated.
   * 
   * @return   The concatenated String
   */
  public static @NotNull String concatenate(String delimiter, CharSequence ... args) {
    return concatenate(delimiter, Arrays.asList(args));
  }

  /**
   * Creates a concatenation of the supplied Strings. This function allows elements to be null which means
   * that they're just be ignored.
   * 
   * @param delimiter   A delimiter which might be used.
   * @param args        The collection of Strings that has to be concatenated.
   * 
   * @return   The concatenated String.
   */
  public static <C extends CharSequence, L extends Collection<C>> @NotNull String concatenate(String delimiter, L args) {
    if ((args == null) || args.isEmpty()) {
      return Empty.NO_STRING;
    }
    var del = delimiter == null ? Empty.NO_STRING : delimiter;
    return Buckets.bucketStringFBuilder().forInstance($ -> {
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
   * @param n      The number of concatenations that have to be performed.
   * @param text   The text that has to be repeated.
   * 
   * @return   The concatenated reproduction string.
   */
  public static @NotNull String repeat(@Min(0) int n, CharSequence text ) {
    if ((n > 0) && (text != null) && (text.length() > 0)) {
      return Buckets.bucketStringFBuilder().forInstance($ -> {
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
   * Creates a textual presentation with a padding using the space character.
   * 
   * @param text      The text that is supposed to be filled with padding.
   * @param limit     The maximum number of characters allowed.
   * @param left      <code>true</code> <=> Use left padding.
   * 
   * @return   The text that is padded.
   */
  public static @NotNull String padding(String text, @Min(1) int limit, boolean left) {
    return padding(text, limit, ' ', left);
  }
  
  public static @NotNull String fillString(int count, char ch) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.appendFilling(count, ch).toString());
  }

  /**
   * Creates a textual presentation with a padding.
   * 
   * @param text      The text that is supposed to be filled with padding.
   * @param limit     The maximum number of characters allowed.
   * @param padding   The padding character.
   * @param left      <code>true</code> <=> Use left padding.
   * 
   * @return   The text that is padded.
   */
  public static @NotNull String padding(String text, @Min(1) int limit, char padding, boolean left) {
    if (text == null) {
      return fillString(limit, padding);
    }
    if (text.length() >= limit) {
      return text;
    }
    return Buckets.bucketStringFBuilder().forInstance($ -> {
      var diff   = limit - text.length();
      var padStr = fillString(diff, padding);
      if (left) {
        $.append(padStr);
      }
      $.append(text);
      if (!left) {
        $.append(padStr);
      }
      return $.toString();
    });
  }

  /**
   * Returns a textual representation of the supplied object.
   * 
   * @param obj    The object which textual representation is desired.
   * 
   * @return   The textual representation of the supplied object.
   */
  public static @NotNull String objectToString(Object obj) {
    return Buckets.bucketStringFBuilder().forInstance($ -> {
      appendToString($, obj);
      return $.toString();
    });
  }
  
  /**
   * Returns a textual representation of the supplied object.
   * 
   * @param obj    The object which textual representation is desired.
   * 
   * @return   The textual representation of the supplied object.
   */
  private static <S extends StringLike> void appendToString(@NotNull S receiver, Object obj) {
    if (obj == null) {
      receiver.append("null");
    } else if (obj instanceof boolean[]) {
      appendToStringBooleanArray(receiver, (boolean[]) obj);
    } else if (obj instanceof char[]) {
      appendToStringCharArray(receiver, (char[]) obj);
    } else if (obj instanceof byte[]) {
      appendToStringByteArray(receiver, (byte[]) obj);
    } else if (obj instanceof short[]) {
      appendToStringShortArray(receiver, (short[]) obj);
    } else if (obj instanceof int[]) {
      appendToStringIntArray(receiver, (int[]) obj);
    } else if (obj instanceof long[]) {
      appendToStringLongArray(receiver, (long[]) obj);
    } else if (obj instanceof float[]) {
      appendToStringFloatArray(receiver, (float[]) obj);
    } else if (obj instanceof double[]) {
      appendToStringDoubleArray(receiver, (double[]) obj);
    } else if (obj.getClass().isArray()) {
      appendToStringObjectArray(receiver, (Object[]) obj);
    } else if (obj instanceof Throwable) {
      appendToStringThrowable(receiver, (Throwable) obj);
    } else {
      receiver.append(String.valueOf(obj));
    }
  }

  private static <S extends StringLike> void appendToStringThrowable(@NotNull S receiver, @NotNull Throwable throwable) {
    Buckets.bucketStringWriter().forInstanceDo($ -> {
      try (var writer = new PrintWriter($)) {
        throwable.printStackTrace(writer);
      }
      receiver.append($.toString());
    });
  }
  
  private static <S extends StringLike> void appendToStringObjectArray(@NotNull S receiver, @NotNull Object[] array) {
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
  
  private static <S extends StringLike> void appendToStringBooleanArray(@NotNull S receiver, @NotNull boolean[] array) {
    receiver.append('[');
    if (array.length > 0) {
      receiver.append(array[0]);
      for (var i = 1; i < array.length; i++) {
        receiver.append(',').append(array[i]);
      }
    }
    receiver.append(']');
  }
  
  private static <S extends StringLike> void appendToStringCharArray(@NotNull S receiver, @NotNull char[] array) {
    receiver.append('[');
    if (array.length > 0) {
      receiver.append('\'').append(array[0]).append('\'');
      for (var i = 1; i < array.length; i++) {
        receiver.append(',').append('\'').append(array[i]).append('\'');
      }
    }
    receiver.append(']');
  }

  private static <S extends StringLike> void appendToStringByteArray(@NotNull S receiver, @NotNull byte[] array) {
    receiver.append('[');
    if (array.length > 0) {
      receiver.append("(byte)").append(array[0]);
      for (var i = 1; i < array.length; i++) {
        receiver.append(',').append("(byte)").append(array[i]);
      }
    }
    receiver.append(']');
  }

  private static <S extends StringLike> void appendToStringShortArray(@NotNull S receiver, @NotNull short[] array) {
    receiver.append('[');
    if (array.length > 0) {
      receiver.append("(short)").append(array[0]);
      for (var i = 1; i < array.length; i++) {
        receiver.append(',').append("(short)").append(array[i]);
      }
    }
    receiver.append(']');
  }

  private static <S extends StringLike> void appendToStringIntArray(@NotNull S receiver, @NotNull int[] array) {
    receiver.append('[');
    if (array.length > 0) {
      receiver.append(array[0]);
      for (var i = 1; i < array.length; i++) {
        receiver.append(',').append(array[i]);
      }
    }
    receiver.append(']');
  }

  private static <S extends StringLike> void appendToStringLongArray(@NotNull S receiver, @NotNull long[] array) {
    receiver.append('[');
    if (array.length > 0) {
      receiver.append(array[0]).append('l');
      for (var i = 1; i < array.length; i++) {
        receiver.append(',').append(array[i]).append('l');
      }
    }
    receiver.append(']');
  }

  private static <S extends StringLike> void appendToStringFloatArray(@NotNull S receiver, @NotNull float[] array) {
    receiver.append('[');
    if (array.length > 0) {
      receiver.append(array[0]).append('f');
      for (var i = 1; i < array.length; i++) {
        receiver.append(',').append(array[i]).append('f');
      }
    }
    receiver.append(']');
  }

  private static <S extends StringLike> void appendToStringDoubleArray(@NotNull S receiver, @NotNull double[] array) {
    receiver.append('[');
    if (array.length > 0) {
      receiver.append(array[0]);
      for (var i = 1; i < array.length; i++) {
        receiver.append(',').append(array[i]);
      }
    }
    receiver.append(']');
  }

} /* ENDCLASS */

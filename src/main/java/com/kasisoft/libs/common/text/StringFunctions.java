package com.kasisoft.libs.common.text;

import com.kasisoft.libs.common.pools.Buckets;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.function.Function;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Collection of functions used for String processing.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class StringFunctions {

  private StringFunctions() {
  }
  
  /**
   * Returns the basename for the supplied string which means to strip away the suffix if there's one.
   * 
   * @param name   The name which might contain a suffix. Not <code>null</code>.
   * 
   * @return   The basename without the suffix. Not <code>null</code>.
   */
  public static @NotNull String getBasename(@NotNull String name) {
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
   * @param name     The name which might be altered. Not <code>null</code>.
   * @param suffix   The suffix which has to be added (without '.'). Neither <code>null</code> nor empty.
   * 
   * @return   The name with the updated suffix. Neither <code>null</code> nor empty.
   */
  public static @NotNull String changeSuffix(@NotNull String name, @NotNull String suffix) {
    return String.format("%s.%s", getBasename(name), suffix);
  }
  
  /**
   * Makes sure that the supplied String is either <code>null</code> or not empty. The text will be trimmed so there 
   * won't be any whitespace at the beginning or the end.
   * 
   * @param input   The String that has to be altered. Maybe <code>null</code>.
   * 
   * @return   <code>null</code> or a non-empty String.
   */
  public static @Null String cleanup(@Null String input) {
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
   * @param input   The String where the first character has to be altered. Neither <code>null</code> nor empty.
   * 
   * @return   A possibly in-place altered input. Not <code>null</code>.
   */
  public static @NotNull String firstUp(@NotNull String input) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).firstUp().toString());
  }
  
  /**
   * Makes the first character lower case if there's one.
   * 
   * @param input   The CharSequence where the first character has to be altered. Neither <code>null</code> nor empty.
   * 
   * @return   A possibly in-place altered input. Not <code>null</code>.
   */
  public static @NotNull String firstDown(@NotNull String input) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).firstDown().toString());
  }
  
  /**
   * Performs a search & replace operation on the supplied input.
   * 
   * @param input          The input which has to be modified. Not <code>null</code>.
   * @param replacements   A Map of String's used to run the search replace operation. Not <code>null</code>.
   * 
   * @return   The modified String. Not <code>null</code>.
   */
  public static @NotNull String replaceAll(@NotNull String input, @NotNull Map<String,String> replacements) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).replaceAll(replacements).toString());
  }
  
  /**
   * Performs a search & replace operation on the supplied input.
   * 
   * @param input          The input which has to be modified. Not <code>null</code>.
   * @param replacements   A Map of String's used to run the search replace operation. Not <code>null</code>.
   * @param fmt            A key formatter. Default: '%s' (alternativ: '${%s}' which means that keys will be ${fredo}, ${dodo}, ...)
   * 
   * @return   The modified String. Not <code>null</code>.
   */
  public static @NotNull String replaceAll(@NotNull String input, @NotNull Map<String,String> replacements, @Null String fmt) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).replaceAll(replacements, fmt).toString());
  }

  /**
   * Transforms the supplied value into a camelcase representation.
   * 
   * @param input   The object which has to be changed. Not <code>null</code>.
   *                      
   * @return   The supplied sequence if possible. The content is altered to a camelcase variety. 
   *           Not <code>null</code>.
   */
  public static @NotNull String camelCase(@NotNull String input) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).camelCase().toString());
  }
  
  /**
   * Replaces regions within some text.
   * 
   * @param input         The text which might be altered. Not <code>null</code>.
   * @param sep           The opening/closing of a region (f.e. "(*"). Not <code>null</code>.
   * @param replacement   The replacement value. Not <code>null</code>.
   *
   * @return   The altered text. Not <code>null</code>.
   */
  public static @NotNull String replaceRegions(@NotNull String input, @NotNull String sep, CharSequence replacement) {
    return replaceRegions(input, sep, sep, replacement);
  }

  /**
   * Replaces regions within some text.
   * 
   * @param input         The text which might be altered. Not <code>null</code>.
   * @param open          The opening of a region (f.e. "(*"). Not <code>null</code>.
   * @param close         The closing of a region (f.e. "*)"). Not <code>null</code>.
   * @param replacement   The replacement value. Not <code>null</code>.
   *
   * @return   The altered text. Not <code>null</code>.
   */
  public static @NotNull String replaceRegions(@NotNull String input, @NotNull String open, @Null String close, @NotNull CharSequence replacement) {
    return replaceRegions(input, open, close, $ -> replacement);
  }

  /**
   * Replaces regions within some text.
   * 
   * @param input         The text which might be altered. Not <code>null</code>.
   * @param open          The opening of a region (f.e. "(*"). Not <code>null</code>.
   * @param close         The closing of a region (f.e. "*)"). Not <code>null</code>.
   * @param replacement   The replacement value. Not <code>null</code>.
   *
   * @return   The altered text. Not <code>null</code>.
   */
  public static @NotNull String replaceRegions(@NotNull String input, @NotNull String open, @NotNull Function<String, CharSequence> replacement) {
    return replaceRegions(input, open, open, replacement);
  }

  /**
   * Replaces regions within some text.
   * 
   * @param input         The text which might be altered. Not <code>null</code>.
   * @param open          The opening of a region (f.e. "(*"). Not <code>null</code>.
   * @param close         The closing of a region (f.e. "*)"). Not <code>null</code>.
   * @param replacement   The replacement value. Not <code>null</code>.
   *
   * @return   The altered text. Not <code>null</code>.
   */
  public static @NotNull String replaceRegions(@NotNull String input, @NotNull String open, @Null String close, @NotNull Function<String, CharSequence> replacement) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).replaceRegions(open, close, replacement).toString());
  }

  public static @NotNull boolean startsWith(@NotNull String input, boolean casesensitive) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).startsWith(casesensitive, input));
  }

  public static <T extends CharSequence> @Null T startsWithMany(@NotNull String input, @NotNull T ... candidates) {
    return startsWithMany(input, true, candidates);
  }
  
  public static <T extends CharSequence> @Null T startsWithMany(@NotNull String input, boolean casesensitive, @NotNull T ... candidates) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).startsWithMany(casesensitive, candidates));
  }

  public static @NotNull boolean endsWith(@NotNull String input, boolean casesensitive) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).endsWith(casesensitive, input));
  }

  public static <T extends CharSequence> @Null T endsWithMany(@NotNull String input, @NotNull T ... candidates) {
    return endsWithMany(input, true, candidates);
  }

  public static <T extends CharSequence> @Null T endsWithMany(@NotNull String input, boolean casesensitive, @NotNull T ... candidates) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.append(input).endsWithMany(casesensitive, candidates));
  }

  /**
   * Creates a concatenation of the supplied Strings. This function allows elements to be <code>null</code> which means
   * that they're just be ignored.
   * 
   * @param delimiter   A delimiter which might be used. Maybe <code>null</code>.
   * @param args        The list of Strings that has to be concatenated. Maybe <code>null</code>.
   * 
   * @return   The concatenated String. Not <code>null</code>.
   */
  public static @NotNull String concatenate(@Null String delimiter, @Null CharSequence ... args) {
    return concatenate(delimiter, Arrays.asList(args));
  }

  /**
   * Creates a concatenation of the supplied Strings. This function allows elements to be <code>null</code> which means
   * that they're just be ignored.
   * 
   * @param delimiter   A delimiter which might be used. Maybe <code>null</code>.
   * @param args        The collection of Strings that has to be concatenated. Maybe <code>null</code>.
   * 
   * @return   The concatenated String. Not <code>null</code>.
   */
  public static <C extends CharSequence, L extends Collection<C>> @NotNull String concatenate(@Null String delimiter, @Null L args) {
    if ((args == null) || args.isEmpty()) {
      return "";
    }
    var del = delimiter == null ? "" : delimiter;
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
   * @param text   The text that has to be repeated. Maybe <code>null</code>.
   * 
   * @return   The concatenated reproduction string. Not <code>null</code>.
   */
  public static String repeat(@Min(0) int n, @Null CharSequence text ) {
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
    return "";
  }

  /**
   * Creates a textual presentation with a padding using the space character.
   * 
   * @param text      The text that is supposed to be filled with padding. Maybe <code>null</code>.
   * @param limit     The maximum number of characters allowed.
   * @param left      <code>true</code> <=> Use left padding.
   * 
   * @return   The text that is padded. Not <code>null</code>.
   */
  public static @NotNull String padding(@Null String text, @Min(1) int limit, boolean left) {
    return padding(text, limit, ' ', left);
  }
  
  public static @NotNull String fillString(int count, char ch) {
    return Buckets.bucketStringFBuilder().forInstance($ -> $.appendFilling(count, ch).toString());
  }

  /**
   * Creates a textual presentation with a padding.
   * 
   * @param text      The text that is supposed to be filled with padding. Maybe <code>null</code>.
   * @param limit     The maximum number of characters allowed.
   * @param padding   The padding character.
   * @param left      <code>true</code> <=> Use left padding.
   * 
   * @return   The text that is padded. Not <code>null</code>.
   */
  public static @NotNull String padding(@Null String text, @Min(1) int limit, char padding, boolean left) {
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

} /* ENDCLASS */

package com.kasisoft.libs.common.text;

import com.kasisoft.libs.common.buckets.Buckets;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.function.BiFunction;
import java.util.function.Function;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Arrays;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface StringLike<T extends StringLike> extends CharSequence, Comparable<T> {

  /**
   * @see StringBuilder#capacity()
   */
  @Min(1) int capacity();

  /**
   * @see StringBuilder#ensureCapacity(int)
   */
  T ensureCapacity(@Min(1) int minimum);

  /**
   * @see StringBuilder#trimToSize()
   */
  T trimToSize();

  /**
   * @see StringBuilder#setLength(int)
   */
  T setLength(@Min(0) int newlength);

  /**
   * @see StringBuilder#codePointAt(int)
   */
  int codePointAt(int index);

  /**
   * @see StringBuilder#codePointBefore(int)
   */
  int codePointBefore(int index);

  /**
   * @see StringBuilder#codePointCount(int, int)
   */
  int codePointCount(int begin, int end);

  /**
   * @see StringBuilder#offsetByCodePoints(int, int)
   */
  int offsetByCodePoints(int index, int codepointoffset);

  /**
   * @see StringBuilder#getChars(int, int, char[], int)
   */
  T getChars(int start, int end, @NotNull char[] destination, int destbegin);

  /**
   * @see StringBuilder#setCharAt(int, char)
   */
  T setCharAt(int index, char ch);

  T setCodepointAt(int index, int codepoint);
  
  /**
   * @see StringBuilder#append(Object)
   */
  @NotNull T append(@NotNull Object obj);

  /**
   * @see StringBuilder#append(CharSequence)
   */
  @NotNull T append(@NotNull CharSequence sequence);

  /**
   * @see StringBuilder#append(CharSequence, int, int)
   */
  @NotNull T append(@NotNull CharSequence sequence, int start, int end);

  /**
   * @see StringBuilder#append(char[])
   */
  @NotNull T append(@NotNull char[] charray);

  /**
   * @see StringBuilder#append(char[], int, int)
   */
  @NotNull T append(@NotNull char[] charray, int offset, int length);
  
  /**
   * @see StringBuilder#append(boolean)
   */
  @NotNull T append(boolean value);

  /**
   * @see StringBuilder#append(char)
   */
  @NotNull T append(char value);

  /**
   * @see StringBuilder#append(int)
   */
  @NotNull T append(int value);

  /**
   * Appends some values using a specific format pattern.
   * 
   * @param format   The pattern to use. Neither <code>null</code> nor empty.
   * @param args     The arguments for this pattern.
   * 
   * @return   The current buffer.
   */
  default @NotNull T appendF(@NotNull String fmt, Object ... args) {
    var toAdd = fmt;
    if ((args != null) && (args.length > 0)) {
      toAdd = String.format(fmt, args);
    }
    return append(toAdd);
  }
  
  /**
   * @see StringBuilder#appendCodePoint(int)
   */
  @NotNull T appendCodePoint(int codepoint);

  /**
   * @see StringBuilder#append(long)
   */
  @NotNull T append(long value);

  /**
   * @see StringBuilder#append(float)
   */
  @NotNull T append(float value);

  /**
   * @see StringBuilder#append(double)
   */
  @NotNull T append(double value);

  /**
   * @see StringBuilder#delete(int, int)
   */
  @NotNull T delete(int start, int end);

  /**
   * @see StringBuilder#deleteCharAt(int)
   */
  @NotNull T deleteCharAt(int index);

  /**
   * @see StringBuilder#replace(int, int, String)
   */
  @NotNull T replace(int start, int end, @NotNull String str);

  /**
   * @see StringBuilder#substring(int)
   */
  @NotNull String substring(int start);

  /**
   * @see StringBuilder#substring(int, int)
   */
  @NotNull String substring(int start, int end);

  /**
   * @see StringBuilder#insert(int, char[], int, int)
   */
  @NotNull T insert(int index, @NotNull char[] charray, int offset, int length);

  /**
   * @see StringBuilder#insert(int, Object)
   */
  @NotNull T insert(int offset, @NotNull Object obj);

  /**
   * Inserts some values using a specific format pattern.
   * 
   * @param offset   The location where to insert the formatted content.
   * @param format   The pattern to use. Neither <code>null</code> nor empty.
   * @param args     The arguments for this pattern.
   * 
   * @return   The current buffer.
   */
  default @NotNull T insertF(int offset, @NotNull String fmt, Object ... args) {
    var toAdd = fmt;
    if ((args != null) && (args.length > 0)) {
      toAdd = String.format(fmt, args);
    }
    return insert(offset, toAdd);
    
  }

  /**
   * @see StringBuilder#insert(int, char[])
   */
  @NotNull T insert(int offset, @NotNull char[] value);

  /**
   * @see StringBuilder#insert(int, CharSequence)
   */
  @NotNull T insert(int offset, @NotNull CharSequence value);

  /**
   * @see StringBuilder#insert(int, CharSequence, int, int)
   */
  @NotNull T insert(int offset, @NotNull CharSequence value, int start, int end);

  /**
   * @see StringBuilder#insert(int, boolean)
   */
  @NotNull T insert(int offset, boolean value);

  /**
   * @see StringBuilder#insert(int, char)
   */
  @NotNull T insert(int offset, char value);

  /**
   * @see StringBuilder#insert(int, int)
   */
  @NotNull T insert(int offset, int value);

  /**
   * @see StringBuilder#insert(int, long)
   */
  @NotNull T insert(int offset, long value);

  /**
   * @see StringBuilder#insert(int, float)
   */
  @NotNull T insert(int offset, float value);

  /**
   * @see StringBuilder#insert(int, double)
   */
  @NotNull T insert(int offset, double value);

  /**
   * @see StringBuilder#indexOf(String)
   */
  int indexOf(@NotNull String str);

  /**
   * @see StringBuilder#indexOf(String, int)
   */
  int indexOf(@NotNull String str, int index);

  default int indexOf(char ... characters) {
    return indexOf(0, characters);
  }

  default int indexOf(int index, char ... characters) {
    var result = -1;
    if ((characters != null) && (characters.length > 0)) {
      var str = toString();
      for (char ch : characters) {
        var idx = str.indexOf(ch);
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
   * Like {@link StringBuilder#indexOf(String)} with the difference that this function provides the position of the
   * leftmost literal which could be found.
   * 
   * @param literals   A list of literals that will be checked. Maybe <code>null</code>.
   * 
   * @return   The index of the leftmost found literal or -1 if none matched.
   */
  default int indexOf(@Null String ... literals) {
    return indexOf(0, literals);
  }
  
  /**
   * Like {@link StringBuilder#indexOf(String)} with the difference that this function provides the position of the
   * leftmost literal which could be found.
   * 
   * @param index      The index used as the starting point for the lookup.
   * @param literals   A list of literals that will be checked. Maybe <code>null</code>.
   * 
   * @return   The index of the leftmost found literal or -1 if none matched.
   */
  default int indexOf(int index, @NotNull String ... literals) {
    var result = -1;
    if (literals != null) {
      for (var literal : literals) {
        var pos = indexOf(literal, index);
        if (pos != -1) {
          if (result == -1) {
            result = pos;
          } else {
            result = Math.min(result, pos);
          }
        }
      }
    }
    return result;
  }
  
  /**
   * @see StringBuilder#lastIndexOf(String)
   */
  int lastIndexOf(@NotNull String str);

  /**
   * @see StringBuilder#lastIndexOf(String, int)
   */
  int lastIndexOf(@NotNull String str, int index);

  default int lastIndexOf(char ... characters) {
    return lastIndexOf(0, true, characters);
  }

  default int lastIndexOf(boolean leftmost, char ... characters) {
    return lastIndexOf(0, leftmost, characters);
  }

  default int lastIndexOf(int index, boolean leftmost, char ... characters) {
    int result = -1;
    if ((characters != null) && (characters.length > 0)) {
      BiFunction<Integer, Integer, Integer> func = leftmost ? Math::min : Math::max;
      var str = toString();
      for (var ch : characters) {
        var idx = str.lastIndexOf(ch);
        if (idx != -1) {
          result = func.apply(idx, result);
        }
      }
    }
    return result;
  }

  /**
   * Like {@link StringBuilder#lastIndexOf(String,int)} with the difference that this function provides the position of 
   * the rightmost literal which could be found.
   * 
   * @param literals   A list of literals that will be checked. Maybe <code>null</code>.
   * 
   * @return   The index of the rightmost found literal or -1 if none matched.
   */
  default int lastIndexOf(@Null String ... literals) {
    return lastIndexOf(-1, literals);
  }
  
  /**
   * Like {@link StringBuilder#lastIndexOf(String,int)} with the difference that this function provides the position of 
   * the rightmost literal which could be found.
   * 
   * @param index      The index used as the starting point for the lookup.
   * @param literals   A list of literals that will be checked. Maybe <code>null</code>.
   * 
   * @return   The index of the rightmost found literal or -1 if none matched.
   */
  default int lastIndexOf(int index, @Null String ... literals) {
    var result = -1;
    if (literals != null) {
      for (var literal : literals) {
        var pos = lastIndexOf(literal, index);
        if (pos != -1) {
          if (result == -1) {
            result = pos;
          } else {
            result = Math.max(result, pos);
          }
        }
      }
    }
    return result;
  }
  
  /**
   * @see StringBuilder#reverse()
   */
  @NotNull T reverse();

  /**
   * This function removes leading whitespace from this buffer.
   */
  default @NotNull T trimLeading() {
    while (length() > 0) {
      int codePoint = codePointAt(0);
      if (!Character.isWhitespace(codePoint)) {
        break;
      }
      int charCount = Character.charCount(codePoint);
      delete(0, charCount);
    }
    return (T) this;
  }

  /**
   * This function removes trailing whitespace from this buffer.
   */
  default @NotNull T trimTrailing() {
    while (length() > 0) {
      int length    = length();
      int codePoint = codePointAt(length - 1);
      if (!Character.isWhitespace(codePoint)) {
        break;
      }
      int charCount = Character.charCount(codePoint);
      delete(length - charCount, length);
    }
    return (T) this;
  }

  /**
   * This function removes leading and trailing whitespace from this buffer.
   */
  default @NotNull T trim() {
    return (T) trimLeading().trimTrailing();
  }

  /**
   * Returns <code>true</code> if the content of this buffer starts with the supplied literal.
   *  
   * @param totest   The text used for the comparison. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The literal starts with the supplied literal.
   */
  default boolean startsWith(@NotNull CharSequence totest) {
    return startsWith(true, totest);
  }
  
  /**
   * Returns <code>true</code> if the content of this buffer starts with the supplied literal.
   *  
   * @param casesensitive   <code>true</code> <=> Performs a case sensitive comparison.
   * @param totest          The text used for the comparison. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The literal starts with the supplied literal.
   */
  default boolean startsWith(boolean casesensitive, @NotNull CharSequence seq) {
    if (seq.length() > length()) {
      return false;
    }
    var totest = seq.toString();
    var part   = substring(0, totest.length());
    if (casesensitive) {
      return part.equals(totest);
    } else {
      return part.equalsIgnoreCase(totest);
    }
  }

  /**
   * Returns <code>true</code> if the content of this buffer ends with the supplied literal.
   *  
   * @param casesensitive   <code>true</code> <=> Performs a case sensitive comparison.
   * @param candidates      The candidates to be tested at the end.
   * 
   * @return   The sequence that's at the start or null.
   */
  default <R extends CharSequence> @Null R startsWithMany(@Null R ... candidates) {
    return startsWithMany(true, candidates);
  }
  
  /**
   * Returns <code>true</code> if the content of this buffer ends with the supplied literal.
   *  
   * @param casesensitive   <code>true</code> <=> Performs a case sensitive comparison.
   * @param candidates      The candidates to be tested at the end.
   * 
   * @return   The sequence that's at start or null.
   */
  default <R extends CharSequence> @Null R startsWithMany(boolean casesensitive, @Null R ... candidates) {
    if ((candidates == null) || (candidates.length == 0)) {
      return null;
    }
    for (R seq : candidates) {
      if (startsWith(casesensitive, seq)) {
        return seq;
      }
    }
    return null;
  }

  /**
   * Returns <code>true</code> if the content of this buffer ends with the supplied literal.
   *  
   * @param totest   The text used for the comparison. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The literal ends with the supplied literal.
   */
  default boolean endsWith(@NotNull String totest) {
    return endsWith(true, totest);
  }

  /**
   * Returns <code>true</code> if the content of this buffer ends with the supplied literal.
   *  
   * @param casesensitive   <code>true</code> <=> Performs a case sensitive comparison.
   * @param seq             The text used for the comparison. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The literal ends with the supplied literal.
   */
  default boolean endsWith(boolean casesensitive, @NotNull CharSequence seq) {
    if (seq.length() > length()) {
      return false;
    }
    var totest = seq.toString();
    var part   = substring(length() - seq.length());
    if (casesensitive) {
      return part.equals(totest);
    } else {
      return part.equalsIgnoreCase(totest);
    }
  }
  
  /**
   * Returns <code>true</code> if the content of this buffer ends with the supplied literal.
   *  
   * @param casesensitive   <code>true</code> <=> Performs a case sensitive comparison.
   * @param candidates      The candidates to be tested at the end.
   * 
   * @return   The sequence that's at the end or null.
   */
  default <R extends CharSequence> @Null R endsWithMany(@Null R ... candidates) {
    return endsWithMany(true, candidates);
  }
  
  /**
   * Returns <code>true</code> if the content of this buffer ends with the supplied literal.
   *  
   * @param casesensitive   <code>true</code> <=> Performs a case sensitive comparison.
   * @param candidates      The candidates to be tested at the end.
   * 
   * @return   The sequence that's at the end or null.
   */
  default <R extends CharSequence> @Null R endsWithMany(boolean casesensitive, @Null R ... candidates) {
    if ((candidates == null) || (candidates.length == 0)) {
      return null;
    }
    for (R seq : candidates) {
      if (endsWith(casesensitive, seq)) {
        return seq;
      }
    }
    return null;
  }

  /**
   * Returns <code>true</code> if the content of this buffer equals the supplied literal.
   *  
   * @param totest   The text used for the comparison. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The literal is equal.
   */
  default boolean equals(@NotNull String totest) {
    return equals(true, totest);
  }
  
  /**
   * Returns <code>true</code> if the content of this buffer equals the supplied literal.
   *  
   * @param casesensitive   <code>true</code> <=> Performs a case sensitive comparison.
   * @param totest          The text used for the comparison. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The literal is equal.
   */
  default boolean equals(boolean casesensitive, @NotNull String totest) {
    if (casesensitive) {
      return toString().equals(totest);
    } else {
      return toString().equalsIgnoreCase(totest);
    }
  }

  /**
   * Removes a collection of characters from this buffer.
   * 
   * @param toremove   A list of characters which have to be removed. Neither <code>null</code> nor empty.
   * 
   * @return   The altered input. Not <code>null</code>.
   */
  default @NotNull T remove(@NotNull String toremove) {
    for (var i = length() - 1; i >= 0; i--) {
      if (toremove.indexOf(charAt(i)) != -1) {
        deleteCharAt(i);
      }
    }
    return (T) this;
  }

  /**
   * Returns a splitted representation of this buffer except the delimiting characters. In difference to the function
   * {@link String#split(String)} this one doesn't use a regular expression.
   * 
   * @param delimiters   A list of characters providing the delimiters for the splitting. 
   *                     Neither <code>null</code> nor empty.
   *                     
   * @return   A splitted list without the delimiting character. Not <code>null</code>.
   */
  default @NotNull String[] split(@NotNull String delimiters) {
    return Buckets.bucketArrayList().forInstance($ -> {
      var tokenizer = new StringTokenizer(toString(), delimiters, false);
      while (tokenizer.hasMoreTokens()) {
        $.add(tokenizer.nextToken());
      }
      return $.toArray(new String[$.size()]);
    });
  }
  
  /**
   * Like {@link #split(String)} with the difference that this function accepts a regular expression for the splitting.
   * 
   * @param regex   A regular expression used for the splitting. Neither <code>null</code> nor empty.
   *                     
   * @return   A splitted list without fragments matching the supplied regular expression. Not <code>null</code>.
   */
  default @NotNull String[] splitRegex(@NotNull String regex) {
    return splitRegex(Pattern.compile(regex));
  }
  
  /**
   * Like {@link #split(String)} with the difference that this function accepts a regular expression for the splitting.
   * 
   * @param pattern   A pattern providing the regular expression used for the splitting. Not <code>null</code>.
   *                     
   * @return   A splitted list without fragments matching the supplied regular expression. Not <code>null</code>.
   */
  default @NotNull String[] splitRegex(@NotNull Pattern pattern) {
     return Buckets.<String>bucketArrayList().forInstance($ -> {
      var matcher = pattern.matcher(this);
      var last    = 0;
      var match   = false;
      while (matcher.find()) {
        match = true;
        if (matcher.start() > last) {
          $.add(substring(last, matcher.start()));
        }
        last = matcher.end();
      }
      if (match && (last < length())) {
        $.add(substring(last));
      }
      if (!match) {
        // there was not at least one match
        $.add(toString());
      }
      return $.toArray(new String[$.size()]);
    });
  }

  /**
   * @see String#replace(char, char)
   * 
   * @param from   The character which has to be replaced.
   * @param to     The character which has to be used instead.
   * 
   * @return   This buffer without <code>from</code> characters. Not <code>null</code>.
   */
  default @NotNull T replace(char from, char to) {
    for (var i = 0; i < length(); i++) {
      if (charAt(i) == from) {
        setCharAt(i, to);
      }
    }
    return (T) this;
  }

  /**
   * @see String#replace(int, int)
   * 
   * @param fromCodepoint   The codepoint which has to be replaced.
   * @param toCodepoint     The codepoint which has to be used instead.
   * 
   * @return   This buffer without <code>from</code> characters. Not <code>null</code>.
   */
  default @NotNull T replace(int fromCodepoint, int toCodepoint) {
    for (var i = 0; i < length(); i++) {
      if (codePointAt(i) == fromCodepoint) {
        setCodepointAt(i, toCodepoint);
      }
    }
    return (T) this;
  }

  /**
   * Replaces all occurrences of a regular expression with a specified replacement.
   * 
   * @param regex         The regular expression used to select the fragments that will be replaced. 
   *                      Neither <code>null</code> nor empty.
   * @param replacement   The replacement which has to be used instead. Not <code>null</code>.
   * 
   * @return   This buffer. Not <code>null</code>.
   */
  default @NotNull T replaceAll(@NotNull String regex, @NotNull String replacement) {
    return replaceAll(Pattern.compile(regex), replacement);
  }
  
  /**
   * Replaces all occurrences of a regular expression with a specified replacement.
   * 
   * @param pattern       The Pattern providing the regular expression for the substitution.
   *                      Not <code>null</code>.
   * @param replacement   The replacement which has to be used instead. Not <code>null</code>.
   * 
   * @return   This buffer. Not <code>null</code>.
   */
  default @NotNull T replaceAll(@NotNull Pattern pattern, @NotNull String replacement) {
    Buckets.<Integer>bucketArrayList().forInstanceDo($ -> {
      var matcher = pattern.matcher(this);
      while (matcher.find()) {
        $.add(matcher.start());
        $.add(matcher.end());
      }
      for (var i = $.size() - 2; i >= 0; i -= 2) {
        var start = $.get(i);
        var end   = $.get(i + 1);
        delete(start, end);
        insert(start, replacement);
      }
    });
    return (T) this;
  }

  /**
   * Replaces all occurrences of the supplied keys with the corresponding values.
   * 
   * @param replacements   The substitution map. Not <code>null</code>.
   * 
   * @return   This buffer. Not <code>null</code>.
   */
  default @NotNull T replaceAll(@NotNull Map<String, String> replacements) {
    return replaceAll(replacements, null);
  }
  
  /**
   * Replaces all occurrences of a regular expression with a specified replacement.
   * 
   * @param replacements   The substitution map. Not <code>null</code>.
   * @param fmt            A formatting string to tweak the key literal. Default is '%s' (an alternative example: '${%s}')
   * 
   * @return   This buffer. Not <code>null</code>.
   */
  default @NotNull T replaceAll(@NotNull Map<String, String> replacements, @Null String fmt) {
    
    var substitutions = Buckets.<String, String>bucketHashMap().allocate();
    var builder       = Buckets.bucketStringFBuilder().allocate();
    var regions       = Buckets.<Integer>bucketArrayList().allocate();

    // setup the substitution map
    if ((fmt != null) && (!"%s".equals(fmt))) {
      replacements.forEach(($k, $v) -> substitutions.put(String.format(fmt, $k), $v));
    } else {
      substitutions.putAll(replacements);
    }
    
    // build a big OR of all keys
    substitutions.keySet().forEach($ -> builder.append('|').append(Pattern.quote($)));
    builder.setCharAt(0, '(');
    builder.append(')');
    
    // collect regions of matches
    Pattern pattern = Pattern.compile(builder.toString());
    Matcher matcher = pattern.matcher(this);
    while (matcher.find()) {
      regions.add(matcher.start());
      regions.add(matcher.end());
    }

    // substitute matches
    for (var i = regions.size() - 2; i >= 0; i -= 2) {
      var start = regions.get(i);
      var end   = regions.get(i + 1);
      var key   = substring(start, end);
      delete(start, end);
      insert(start, substitutions.get(key));
    }

    Buckets.<String, String>bucketHashMap().free(substitutions);
    Buckets.bucketStringFBuilder().free(builder);
    Buckets.<Integer>bucketArrayList().free(regions);
    
    return (T) this;
  }

  /**
   * Like {@link #replaceAll(String, String)} but only the first occurrence of the regular expression will be replaced. 
   * 
   * @param regex         The regular expression used to select the fragments that will be replaced.
   *                      Neither <code>null</code> nor empty.
   * @param replacement   The replacement which has to be used instead. Not <code>null</code>.
   * 
   * @return   This buffer. Not <code>null</code>.
   */
  default @NotNull T replaceFirst(@NotNull String regex, @NotNull String replacement) {
    return replaceFirst(Pattern.compile(regex), replacement);
  }
  
  /**
   * Like {@link #replaceAll(String, String)} but only the first occurrence of the regular expression will be replaced. 
   * 
   * @param pattern       The Pattern providing the regular expression for the substitution. Not <code>null</code>.
   * @param replacement   The replacement which has to be used instead. Not <code>null</code>.
   * 
   * @return   This buffer. Not <code>null</code>.
   */
  default @NotNull T replaceFirst(@NotNull Pattern pattern, @NotNull String replacement) {
    var matcher = pattern.matcher(this);
    if (matcher.find()) {
      delete(matcher.start(), matcher.end());
      insert(matcher.start(), replacement);
    }
    return (T) this;
  }

  /**
   * Like {@link #replaceAll(String, String)} but only the last occurrence of the regular expression will be replaced. 
   * 
   * @param regex         The regular expression used to select the fragments that will be replaced.
   *                      Neither <code>null</code> nor empty.
   * @param replacement   The replacement which has to be used instead. Not <code>null</code>.
   * 
   * @return   This buffer. Not <code>null</code>.
   */
  default @NotNull T replaceLast(@NotNull String regex, @NotNull String replacement) {
    return replaceLast(Pattern.compile(regex), replacement);
  }
  
  /**
   * Like {@link #replaceAll(String, String)} but only the last occurrence of the regular expression will be replaced. 
   * 
   * @param pattern       The Pattern providing the regular expression for the substitution. Not <code>null</code>.
   * @param replacement   The replacement which has to be used instead. Not <code>null</code>.
   * 
   * @return   This buffer. Not <code>null</code>.
   */
  default @NotNull T replaceLast(@NotNull Pattern pattern, @NotNull String replacement) {
    var matcher = pattern.matcher(this);
    var start   = -1;
    var end     = -1;
    while (matcher.find()) {
      start = matcher.start();
      end   = matcher.end();
    }
    if (start != -1) {
      delete(start, end);
      insert(start, replacement);
    }
    return (T) this;
  }
  
  default @NotNull T firstUp() {
    int len = length();
    if (len > 0) {
      char first = charAt(0);
      char upper = Character.toUpperCase(first);
      if (first != upper) {
        setCharAt(0, upper);
      }
    }
    return (T) this;
  }

  default @NotNull T firstDown() {
    int len = length();
    if (len > 0) {
      char first = charAt(0);
      char lower = Character.toLowerCase(first);
      if (first != lower) {
        setCharAt(0, lower);
      }
    }
    return (T) this;
  }

  /**
   * Creates a camelcase representation of the supplied sequence.
   * 
   * @param sequence   The sequence which has to be tested. Not <code>null</code>.
   * 
   * @return   The camelcase representation. Not <code>null</code>
   */
  default @NotNull T camelCase() {
    int len = length();
    for (int i = len - 2, j = len - 1; i >= 0; i--,j--) {
      char current = charAt(i);
      char next    = charAt(j);
      if (!Character.isLetter(current)) {
        setCharAt(j, Character.toUpperCase(next));
        deleteCharAt(i);
      }
    }
    if (length() > 0) {
      firstDown();
    }
    return (T) this;
  }
  
  default @NotNull T toLowerCase() {
    String lower = toString().toLowerCase();
    setLength(0);
    append(lower);
    return (T) this;
  }

  default @NotNull T toUpperCase() {
    String upper = toString().toUpperCase();
    setLength(0);
    append(upper);
    return (T) this;
  }

  default @NotNull T appendIfMissing(@NotNull CharSequence seq) {
    return appendIfMissing(true, seq);
  }
  
  default @NotNull T appendIfMissing(boolean casesensitive, @NotNull CharSequence seq) {
    if (!endsWith(casesensitive, seq)) {
      append(seq);
    }
    return (T) this;
  }

  default @NotNull T prependIfMissing(@NotNull CharSequence seq) {
    return prependIfMissing(true, seq);
  }
  
  default @NotNull T prependIfMissing(boolean casesensitive, @NotNull CharSequence seq) {
    if (!startsWith(casesensitive, seq)) {
      insert(0, seq);
    }
    return (T) this;
  }
  
  default @NotNull T removeEnd(@NotNull CharSequence seq) {
    return removeEnd(true, seq);
  }

  default @NotNull T removeEnd(boolean casesensitive, @NotNull CharSequence seq) {
    if (endsWith(casesensitive, seq)) {
      delete(-seq.length(), 0);
    }
    return (T) this;
  }

  default @NotNull T removeStart(@NotNull CharSequence seq) {
    return removeStart(true, seq);
  }

  default @NotNull T removeStart(boolean casesensitive, @NotNull CharSequence seq) {
    if (startsWith(casesensitive, seq)) {
      delete(0, seq.length());
    }
    return (T) this;
  }

  default @NotNull T replaceRegions(@NotNull String open, @NotNull String replacement) {
    return (T) replaceRegions(open, open, $ -> replacement);
  }

  default @NotNull T replaceRegions(@NotNull String open, @Null String close, @NotNull String replacement) {
    return (T) replaceRegions(open, close, $ -> replacement);
  }
  
  default @NotNull T replaceRegions(@NotNull String open, @NotNull Function<String, CharSequence> replacement) {
    return replaceRegions(open, open, replacement);
  }
  
  default @NotNull T replaceRegions(@NotNull String open, @Null String close, @NotNull Function<String, CharSequence> replacement) {
    if (close == null) {
      close = open;
    }
    var start    = 0;
    var idxOpen  = indexOf(open, start);
    var idxClose = indexOf(close, idxOpen + 1);
    while ((idxOpen != -1) && (idxClose != -1)) {
      var inner = substring(idxOpen + open.length(), idxClose);
      var value = replacement.apply(inner);
      delete(idxOpen, idxClose + close.length());
      if (value != null) {
        insert(idxOpen, value);
        start = idxOpen + value.length();
      } else {
        start = idxOpen;
      }
      idxOpen  = indexOf(open, start);
      idxClose = indexOf(close, idxOpen + 1);
    }
    return (T) this;
  }
  
  default @NotNull T appendFilling(@Min(1) int count, char ch) {
    char[] charray = new char[count];
    Arrays.fill(charray, ch);
    return append(charray);
  }
  
} /* ENDINTERFACE */

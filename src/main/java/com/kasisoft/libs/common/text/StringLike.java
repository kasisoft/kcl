package com.kasisoft.libs.common.text;

import com.kasisoft.libs.common.buckets.Buckets;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.regex.Pattern;

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
  void ensureCapacity(@Min(1) int minimum);

  /**
   * @see StringBuilder#trimToSize()
   */
  void trimToSize();

  /**
   * @see StringBuilder#setLength(int)
   */
  void setLength(@Min(0) int newlength);

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
  void getChars(int start, int end, @NotNull char[] destination, int destbegin);

  /**
   * @see StringBuilder#setCharAt(int, char)
   */
  void setCharAt(int index, char ch);

  /**
   * @see StringBuilder#append(Object)
   */
  T append(@NotNull Object obj);

  /**
   * @see StringBuilder#append(CharSequence)
   */
  T append(@NotNull CharSequence sequence);

  /**
   * @see StringBuilder#append(CharSequence, int, int)
   */
  T append(@NotNull CharSequence sequence, int start, int end);

  /**
   * @see StringBuilder#append(char[])
   */
  T append(@NotNull char[] charray);

  /**
   * @see StringBuilder#append(char[], int, int)
   */
  T append(@NotNull char[] charray, int offset, int length);
  
  /**
   * @see StringBuilder#append(boolean)
   */
  T append(boolean value);

  /**
   * @see StringBuilder#append(char)
   */
  T append(char value);

  /**
   * @see StringBuilder#append(int)
   */
  T append(int value);

  /**
   * Appends some values using a specific format pattern.
   * 
   * @param format   The pattern to use. Neither <code>null</code> nor empty.
   * @param args     The arguments for this pattern.
   * 
   * @return   The current buffer.
   */
  default T appendF(@NotNull String fmt, Object ... args) {
    var toAdd = fmt;
    if ((args != null) && (args.length > 0)) {
      toAdd = String.format(fmt, args);
    }
    return append(toAdd);
  }
  
  /**
   * @see StringBuilder#appendCodePoint(int)
   */
  T appendCodePoint(int codepoint);

  /**
   * @see StringBuilder#append(long)
   */
  T append(long value);

  /**
   * @see StringBuilder#append(float)
   */
  T append(float value);

  /**
   * @see StringBuilder#append(double)
   */
  T append(double value);

  /**
   * @see StringBuilder#delete(int, int)
   */
  T delete(int start, int end);

  /**
   * @see StringBuilder#deleteCharAt(int)
   */
  T deleteCharAt(int index);

  /**
   * @see StringBuilder#replace(int, int, String)
   */
  T replace(int start, int end, @NotNull String str);

  /**
   * @see StringBuilder#substring(int)
   */
  String substring(int start);

  /**
   * @see StringBuilder#substring(int, int)
   */
  String substring(int start, int end);

  /**
   * @see StringBuilder#insert(int, char[], int, int)
   */
  T insert(int index, @NotNull char[] charray, int offset, int length);

  /**
   * @see StringBuilder#insert(int, Object)
   */
  T insert(int offset, @NotNull Object obj);

  /**
   * @see StringBuilder#insert(int, String)
   */
  T insert(int offset, @NotNull String value);

  /**
   * Inserts some values using a specific format pattern.
   * 
   * @param offset   The location where to insert the formatted content.
   * @param format   The pattern to use. Neither <code>null</code> nor empty.
   * @param args     The arguments for this pattern.
   * 
   * @return   The current buffer.
   */
  default T insertF(int offset, @NotNull String fmt, Object ... args) {
    var toAdd = fmt;
    if ((args != null) && (args.length > 0)) {
      toAdd = String.format(fmt, args);
    }
    return insert(offset, toAdd);
    
  }

  /**
   * @see StringBuilder#insert(int, char[])
   */
  T insert(int offset, @NotNull char[] value);

  /**
   * @see StringBuilder#insert(int, CharSequence)
   */
  T insert(int offset, @NotNull CharSequence value);

  /**
   * @see StringBuilder#insert(int, CharSequence, int, int)
   */
  T insert(int offset, @NotNull CharSequence value, int start, int end);

  /**
   * @see StringBuilder#insert(int, boolean)
   */
  T insert(int offset, boolean value);

  /**
   * @see StringBuilder#insert(int, char)
   */
  T insert(int offset, char value);

  /**
   * @see StringBuilder#insert(int, int)
   */
  T insert(int offset, int value);

  /**
   * @see StringBuilder#insert(int, long)
   */
  T insert(int offset, long value);

  /**
   * @see StringBuilder#insert(int, float)
   */
  T insert(int offset, float value);

  /**
   * @see StringBuilder#insert(int, double)
   */
  T insert(int offset, double value);

  /**
   * @see StringBuilder#indexOf(String)
   */
  int indexOf(@NotNull String str);

  /**
   * @see StringBuilder#indexOf(String, int)
   */
  int indexOf(@NotNull String str, int index);

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
  T reverse();

  /**
   * This function removes leading whitespace from this buffer.
   */
  default void trimLeading() {
    while ((length() > 0) && Character.isWhitespace(charAt(0))) {
      deleteCharAt(0);
    }
  }

  /**
   * This function removes trailing whitespace from this buffer.
   */
  default void trimTrailing() {
    while ((length() > 0) && Character.isWhitespace(charAt(-1))) {
      deleteCharAt(-1);
    }
  }

  /**
   * This function removes leading and trailing whitespace from this buffer.
   */
  default void trim() {
    trimLeading();
    trimTrailing();
  }

  /**
   * Returns <code>true</code> if the content of this buffer starts with the supplied literal.
   *  
   * @param totest   The text used for the comparison. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The literal starts with the supplied literal.
   */
  default boolean startsWith(@NotNull String totest) {
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
  default boolean startsWith(boolean casesensitive, @NotNull String totest) {
    if (totest.length() > length()) {
      return false;
    }
    var part = substring(0, totest.length());
    if (casesensitive) {
      return part.equals(totest);
    } else {
      return part.equalsIgnoreCase(totest);
    }
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
   * @param totest          The text used for the comparison. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The literal ends with the supplied literal.
   */
  default boolean endsWith(boolean casesensitive, @NotNull String totest) {
    if (totest.length() > length()) {
      return false;
    }
    var part = substring(length() - totest.length());
    if (casesensitive) {
      return part.equals(totest);
    } else {
      return part.equalsIgnoreCase(totest);
    }
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
  default T remove(@NotNull String toremove) {
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
  default String[] split(@NotNull String delimiters) {
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
  default String[] splitRegex(@NotNull String regex) {
    return splitRegex(Pattern.compile(regex));
  }
  
  /**
   * Like {@link #split(String)} with the difference that this function accepts a regular expression for the splitting.
   * 
   * @param pattern   A pattern providing the regular expression used for the splitting. Not <code>null</code>.
   *                     
   * @return   A splitted list without fragments matching the supplied regular expression. Not <code>null</code>.
   */
  default String[] splitRegex(@NotNull Pattern pattern) {
    return Buckets.bucketArrayList().forInstance($ -> {
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
      if (match && (last < length() - 1)) {
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
  default T replace(char from, char to) {
    for (var i = 0; i < length(); i++) {
      if (charAt(i) == from) {
        setCharAt(i, to);
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
  default T replaceAll(@NotNull String regex, @NotNull String replacement) {
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
  default T replaceAll(@NotNull Pattern pattern, @NotNull String replacement) {
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
   * Like {@link #replaceAll(String, String)} but only the first occurrence of the regular expression will be replaced. 
   * 
   * @param regex         The regular expression used to select the fragments that will be replaced.
   *                      Neither <code>null</code> nor empty.
   * @param replacement   The replacement which has to be used instead. Not <code>null</code>.
   * 
   * @return   This buffer. Not <code>null</code>.
   */
  default T replaceFirst(@NotNull String regex, @NotNull String replacement) {
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
  default T replaceFirst(@NotNull Pattern pattern, @NotNull String replacement) {
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
  default T replaceLast(@NotNull String regex, @NotNull String replacement) {
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
  default T replaceLast(@NotNull Pattern pattern, @NotNull String replacement) {
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
  
} /* ENDINTERFACE */

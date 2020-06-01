package com.kasisoft.libs.common.text;

import com.kasisoft.libs.common.buckets.Buckets;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.stream.IntStream;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.StringTokenizer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.NonNull;

/**
 * StringF(ormatting)Builder  equivalent which supports formatting. This builder also supports negative indices which 
 * means that the original index is calculated beginning from the end of the buffer.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StringFBuilder implements Serializable, CharSequence, Comparable<StringFBuilder> {

  private static final long serialVersionUID = 1050795857819832439L;
  
  // the original implementation
  StringBuilder  origin;
  
  /**
   * @see StringBuilder#StringBuilder()
   */
  public StringFBuilder() {
    origin = new StringBuilder();
  }

  /**
   * @see StringBuilder#StringBuilder(int)
   */
  public StringFBuilder(@Min(1) int capacity) {
    origin = new StringBuilder( capacity );
  }

  /**
   * @see StringBuilder#StringBuilder(String)
   */
  public StringFBuilder(@NotNull String str) {
    origin = new StringBuilder(str);
  }

  /**
   * @see StringBuilder#StringBuilder(CharSequence)
   */
  public StringFBuilder(@NotNull CharSequence seq) {
    origin = new StringBuilder(seq);
  }

  /**
   * @see StringBuilder#length() 
   */
  @Override
  public @Min(0) int length() {
    return origin.length();
  }

  /**
   * @see StringBuilder#capacity()
   */
  public @Min(1) int capacity() {
    return origin.capacity();
  }

  /**
   * @see StringBuilder#ensureCapacity(int)
   */
  public void ensureCapacity(@Min(1) int minimum) {
    origin.ensureCapacity(minimum);
  }

  /**
   * @see StringBuilder#trimToSize()
   */
  public void trimToSize() {
    origin.trimToSize();
  }

  /**
   * @see StringBuilder#setLength(int)
   */
  public void setLength(@Min(0) int newlength) {
    origin.setLength(newlength);
  }

  /**
   * @see StringBuilder#charAt(int)
   */
  @Override
  public char charAt(int index) {
    return origin.charAt(adjustIndex(index));
  }

  /**
   * @see StringBuilder#codePointAt(int)
   */
  public int codePointAt(int index) {
    return origin.codePointAt(adjustIndex(index));
  }

  /**
   * @see StringBuilder#codePointBefore(int)
   */
  public int codePointBefore(int index) {
    return origin.codePointBefore(adjustIndex(index));
  }

  /**
   * @see StringBuilder#codePointCount(int, int)
   */
  public int codePointCount(int begin, int end) {
    return origin.codePointCount(adjustIndex(begin), adjustIndex(end));
  }

  /**
   * @see StringBuilder#offsetByCodePoints(int, int)
   */
  public int offsetByCodePoints(int index, int codepointoffset) {
    return origin.offsetByCodePoints( adjustIndex(index), codepointoffset);
  }

  /**
   * @see StringBuilder#getChars(int, int, char[], int)
   */
  public void getChars(int start, int end, @NotNull char[] destination, int destbegin) {
    origin.getChars(adjustIndex(start), adjustIndex(end), destination, adjustIndex(destination.length, destbegin));
  }

  /**
   * @see StringBuilder#setCharAt(int, char)
   */
  public void setCharAt(int index, char ch) {
    origin.setCharAt(adjustIndex( index ), ch);
  }

  /**
   * @see StringBuilder#append(Object)
   */
  public StringFBuilder append(@NotNull Object obj) {
    origin = origin.append(obj);
    return this;
  }

  /**
   * @see StringBuilder#append(String) 
   */
  public StringFBuilder append(@NotNull String str) {
    origin = origin.append(str);
    return this;
  }

  /**
   * @see StringBuilder#append(StringBuilder)
   */
  public StringFBuilder append(@NotNull StringBuffer buffer) {
    origin = origin.append(buffer);
    return this;
  }

  /**
   * @see StringBuilder#append(StringBuilder)
   */
  public StringFBuilder append(@NotNull StringFBuilder buffer) {
    origin = origin.append(buffer.origin);
    return this;
  }

  /**
   * @see StringBuilder#append(CharSequence)
   */
  public StringFBuilder append(@NotNull CharSequence sequence) {
    origin = origin.append(sequence);
    return this;
  }

  /**
   * @see StringBuilder#append(CharSequence, int, int)
   */
  public StringFBuilder append(@NotNull CharSequence sequence, int start, int end) {
    origin = origin.append(sequence, adjustIndex(sequence.length(), start), adjustIndex(sequence.length(), end));
    return this;
  }

  /**
   * @see StringBuilder#append(char[])
   */
  public StringFBuilder append(@NotNull char[] charray) {
    origin = origin.append(charray);
    return this;
  }

  /**
   * @see StringBuilder#append(char[], int, int)
   */
  public StringFBuilder append(@NotNull char[] charray, int offset, int length) {
    origin = origin.append(charray, adjustIndex(charray.length, offset), length);
    return this;
  }
  
  /**
   * @see StringBuilder#append(boolean)
   */
  public StringFBuilder append(boolean value) {
    origin = origin.append(value);
    return this;
  }

  /**
   * @see StringBuilder#append(char)
   */
  public StringFBuilder append(char value) {
    origin = origin.append(value);
    return this;
  }

  /**
   * @see StringBuilder#append(int)
   */
  public StringFBuilder append(int value) {
    origin = origin.append(value);
    return this;
  }

  /**
   * Appends some values using a specific format pattern.
   * 
   * @param format   The pattern to use. Neither <code>null</code> nor empty.
   * @param args     The arguments for this pattern.
   * 
   * @return   The current buffer.
   */
  public StringFBuilder appendF(@NonNull String fmt, Object ... args) {
    var toAdd = fmt;
    if ((args != null) && (args.length > 0)) {
      toAdd = String.format(fmt, args);
    }
    return append(toAdd);
  }
  
  /**
   * @see StringBuilder#appendCodePoint(int)
   */
  public StringFBuilder appendCodePoint(int codepoint) {
    origin = origin.appendCodePoint(codepoint);
    return this;
  }

  /**
   * @see StringBuilder#append(long)
   */
  public StringFBuilder append(long value) {
    origin = origin.append(value);
    return this;
  }

  /**
   * @see StringBuilder#append(float)
   */
  public StringFBuilder append(float value) {
    origin = origin.append(value);
    return this;
  }

  /**
   * @see StringBuilder#append(double)
   */
  public StringFBuilder append(double value) {
    origin = origin.append(value);
    return this;
  }

  /**
   * @see StringBuilder#delete(int, int)
   */
  public StringFBuilder delete(int start, int end) {
    origin = origin.delete(adjustIndex(start), adjustIndex(end));
    return this;
  }

  /**
   * @see StringBuilder#deleteCharAt(int)
   */
  public StringFBuilder deleteCharAt(int index) {
    origin = origin.deleteCharAt(adjustIndex(index));
    return this;
  }

  /**
   * @see StringBuilder#replace(int, int, String)
   */
  public StringFBuilder replace(int start, int end, @NotNull String str) {
    origin = origin.replace(adjustIndex(start), adjustIndex(end), str );
    return this;
  }

  /**
   * @see StringBuilder#substring(int)
   */
  public String substring(int start) {
    return origin.substring(adjustIndex(start));
  }

  /**
   * @see StringBuilder#subSequence(int, int)
   */
  @Override
  public CharSequence subSequence(int start, int end) {
    return origin.subSequence(adjustIndex(start), adjustIndex(end));
  }

  /**
   * @see StringBuilder#substring(int, int)
   */
  public String substring(int start, int end) {
    return origin.substring(adjustIndex(start), adjustIndex(end));
  }

  /**
   * @see StringBuilder#insert(int, char[], int, int)
   */
  public StringFBuilder insert(int index, @NotNull char[] charray, int offset, int length) {
    origin = origin.insert(adjustIndex(index), charray, adjustIndex(charray.length, offset), length);
    return this;
  }

  /**
   * @see StringBuilder#insert(int, Object)
   */
  public StringFBuilder insert(int offset, @NotNull Object obj) {
    origin = origin.insert(adjustIndex(offset), obj);
    return this;
  }

  /**
   * @see StringBuilder#insert(int, String)
   */
  public StringFBuilder insert(int offset, @NotNull String value) {
    origin = origin.insert(adjustIndex(offset), value);
    return this;
  }

  /**
   * Inserts some values using a specific format pattern.
   * 
   * @param offset   The location where to insert the formatted content.
   * @param format   The pattern to use. Neither <code>null</code> nor empty.
   * @param args     The arguments for this pattern.
   * 
   * @return   The current buffer.
   */
  public StringFBuilder insertF(int offset, @NotNull String fmt, Object ... args) {
    var toAdd = fmt;
    if ((args != null) && (args.length > 0)) {
      toAdd = String.format(fmt, args);
    }
    return insert(adjustIndex(offset), toAdd);
  }

  /**
   * @see StringBuilder#insert(int, char[])
   */
  public StringFBuilder insert(int offset, @NotNull char[] value) {
    origin = origin.insert(adjustIndex(offset), value);
    return this;
  }

  /**
   * @see StringBuilder#insert(int, CharSequence)
   */
  public StringFBuilder insert(int offset, @NotNull CharSequence value) {
    origin = origin.insert(adjustIndex(offset), value);
    return this;
  }

  /**
   * @see StringBuilder#insert(int, CharSequence, int, int)
   */
  public StringFBuilder insert(int offset, @NotNull CharSequence value, int start, int end) {
    origin = origin.insert(adjustIndex(offset), value, adjustIndex(value.length(), start), adjustIndex(value.length(), end));
    return this;
  }

  /**
   * @see StringBuilder#insert(int, boolean)
   */
  public StringFBuilder insert(int offset, boolean value) {
    origin = origin.insert(adjustIndex(offset), value);
    return this;
  }

  /**
   * @see StringBuilder#insert(int, char)
   */
  public StringFBuilder insert(int offset, char value) {
    origin = origin.insert(adjustIndex(offset), value);
    return this;
  }

  /**
   * @see StringBuilder#insert(int, int)
   */
  public StringFBuilder insert(int offset, int value) {
    origin = origin.insert(adjustIndex(offset), value);
    return this;
  }

  /**
   * @see StringBuilder#insert(int, long)
   */
  public StringFBuilder insert(int offset, long value) {
    origin = origin.insert(adjustIndex(offset), value);
    return this;
  }

  /**
   * @see StringBuilder#insert(int, float)
   */
  public StringFBuilder insert(int offset, float value) {
    origin = origin.insert(adjustIndex(offset), value);
    return this;
  }

  /**
   * @see StringBuilder#insert(int, double)
   */
  public StringFBuilder insert(int offset, double value) {
    origin = origin.insert(adjustIndex(offset), value);
    return this;
  }

  /**
   * @see StringBuilder#indexOf(String)
   */
  public int indexOf(@NotNull String str) {
    return origin.indexOf(str);
  }

  /**
   * @see StringBuilder#indexOf(String, int)
   */
  public int indexOf(@NotNull String str, int index) {
    return origin.indexOf(str, adjustIndex(index));
  }

  /**
   * Like {@link StringBuilder#indexOf(String)} with the difference that this function provides the position of the
   * leftmost literal which could be found.
   * 
   * @param literals   A list of literals that will be checked. Maybe <code>null</code>.
   * 
   * @return   The index of the leftmost found literal or -1 if none matched.
   */
  public int indexOf(@NotNull String ... literals) {
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
  public int indexOf(int index, @NotNull String ... literals) {
    index      = adjustIndex(index);
    var result = -1;
    if (literals != null) {
      for (String literal : literals) {
        var pos = origin.indexOf(literal, index);
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
  public int lastIndexOf(@NotNull String str) {
    return origin.lastIndexOf(str);
  }

  /**
   * @see StringBuilder#lastIndexOf(String, int)
   */
  public int lastIndexOf(@NotNull String str, int index) {
    return origin.lastIndexOf(str, adjustIndex(index));
  }

  /**
   * Like {@link StringBuilder#lastIndexOf(String,int)} with the difference that this function provides the position of 
   * the rightmost literal which could be found.
   * 
   * @param literals   A list of literals that will be checked. Maybe <code>null</code>.
   * 
   * @return   The index of the rightmost found literal or -1 if none matched.
   */
  public int lastIndexOf(@NotNull String ... literals) {
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
  public int lastIndexOf(int index, @NotNull String ... literals) {
    index      = adjustIndex(index);
    var result = -1;
    if (literals != null) {
      for (String literal : literals) {
        var pos = origin.lastIndexOf(literal, index);
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
  public StringFBuilder reverse() {
    origin = origin.reverse();
    return this;
  }

  @Override
  public String toString() {
    return origin.toString();
  }
  
  /**
   * This function removes leading whitespace from this buffer.
   */
  public void trimLeading() {
    while ((length() > 0) && Character.isWhitespace(charAt(0))) {
      deleteCharAt(0);
    }
  }

  /**
   * This function removes trailing whitespace from this buffer.
   */
  public void trimTrailing() {
    while ((length() > 0) && Character.isWhitespace(charAt(-1))) {
      deleteCharAt(-1);
    }
  }

  /**
   * This function removes leading and trailing whitespace from this buffer.
   */
  public void trim() {
    trimLeading();
    trimTrailing();
  }

  /**
   * Returns an adjusted index since this extension supports negative indices as well.
   * 
   * @param index   The index supplied by the user.
   * 
   * @return  The index to use for the original implementation.
   */
  private int adjustIndex(int index) {
    return adjustIndex(origin.length(), index);
  }

  /**
   * Returns an adjusted index since this extension supports negative indices as well.
   *
   * @param length  The length to be used for the calculation.
   * @param index   The index supplied by the user.
   * 
   * @return  The index to use for the original implementation.
   */
  private int adjustIndex(int length, int index) {
    if (index < 0) {
      return length + index;
    }
    return index;
  }
  
  /**
   * Returns <code>true</code> if the content of this buffer starts with the supplied literal.
   *  
   * @param totest   The text used for the comparison. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The literal starts with the supplied literal.
   */
  public boolean startsWith(@NotNull String totest) {
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
  public boolean startsWith(boolean casesensitive, @NotNull String totest) {
    if (totest.length() > origin.length()) {
      return false;
    }
    String part = origin.substring(0, totest.length());
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
  public boolean endsWith(@NotNull String totest) {
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
  public boolean endsWith(boolean casesensitive, @NotNull String totest) {
    if( totest.length() > origin.length() ) {
      return false;
    }
    String part = origin.substring(origin.length() - totest.length());
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
  public boolean equals(@NotNull String totest) {
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
  public boolean equals(boolean casesensitive, @NotNull String totest) {
    if (casesensitive) {
      return origin.toString().equals(totest);
    } else {
      return origin.toString().equalsIgnoreCase(totest);
    }
  }

  /**
   * Removes a collection of characters from this buffer.
   * 
   * @param toremove   A list of characters which have to be removed. Neither <code>null</code> nor empty.
   * 
   * @return   The altered input. Not <code>null</code>.
   */
  public StringFBuilder remove(@NotNull String toremove) {
    for (var i = length() - 1; i >= 0; i--) {
      if (toremove.indexOf(charAt(i)) != -1) {
        deleteCharAt(i);
      }
    }
    return this;
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
  public String[] split(@NotNull String delimiters) {
    return Buckets.bucketArrayList().forInstance($ -> {
      StringTokenizer tokenizer = new StringTokenizer(origin.toString(), delimiters, false);
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
  public String[] splitRegex(@NotNull String regex) {
    return splitRegex(Pattern.compile(regex));
  }
  
  /**
   * Like {@link #split(String)} with the difference that this function accepts a regular expression for the splitting.
   * 
   * @param pattern   A pattern providing the regular expression used for the splitting. Not <code>null</code>.
   *                     
   * @return   A splitted list without fragments matching the supplied regular expression. Not <code>null</code>.
   */
  public String[] splitRegex(@NotNull Pattern pattern) {
    return Buckets.bucketArrayList().forInstance($ -> {
      var matcher = pattern.matcher(origin);
      var last    = 0;
      var match   = false;
      while (matcher.find()) {
        match = true;
        if (matcher.start() > last) {
          $.add(origin.substring(last, matcher.start()));
        }
        last = matcher.end();
      }
      if (match && (last < origin.length() - 1)) {
        $.add(origin.substring(last));
      }
      if (!match) {
        // there was not at least one match
        $.add(origin.toString());
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
  public StringFBuilder replace(char from, char to) {
    for (var i = 0; i < origin.length(); i++) {
      if (origin.charAt(i) == from) {
        origin.setCharAt(i, to);
      }
    }
    return this;
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
  public StringFBuilder replaceAll(@NotNull String regex, @NotNull String replacement) {
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
  public StringFBuilder replaceAll(@NotNull Pattern pattern, @NotNull String replacement) {
    Buckets.<Integer>bucketArrayList().forInstanceDo($ -> {
      var matcher = pattern.matcher(origin);
      while (matcher.find()) {
        $.add(matcher.start());
        $.add(matcher.end());
      }
      for (var i = $.size() - 2; i >= 0; i -= 2) {
        int start = $.get(i);
        int end   = $.get(i + 1);
        origin.delete(start, end        );
        origin.insert(start, replacement);
      }
    });
    return this;
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
  public StringFBuilder replaceFirst(@NotNull String regex, @NotNull String replacement) {
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
  public StringFBuilder replaceFirst(@NotNull Pattern pattern, @NotNull String replacement) {
    Matcher matcher = pattern.matcher( origin );
    if (matcher.find()) {
      origin.delete(matcher.start(), matcher.end());
      origin.insert(matcher.start(), replacement);
    }
    return this;
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
  public StringFBuilder replaceLast(@NotNull String regex, @NotNull String replacement) {
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
  public StringFBuilder replaceLast(@NotNull Pattern pattern, @NotNull String replacement) {
    Matcher matcher   = pattern.matcher(origin);
    int     start     = -1;
    int     end       = -1;
    while (matcher.find()) {
      start = matcher.start();
      end   = matcher.end();
    }
    if (start != -1) {
      origin.delete(start, end);
      origin.insert(start, replacement);
    }
    return this;
  }
  
  @Override
  public IntStream chars() {
    return origin.chars();
  }
  
  @Override
  public IntStream codePoints() {
    return origin.codePoints();
  }
  
  @Override
  public int compareTo(@NotNull StringFBuilder another) {
    if (this == another) {
      return 0;
    }
    return origin.compareTo(another.origin);
  }
  
  private void writeObject(ObjectOutputStream s) throws IOException {
      s.writeObject(origin);
  }

  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    origin = (StringBuilder) s.readObject();
  }

} /* ENDCLASS */

package com.kasisoft.libs.common.text;

import com.kasisoft.libs.common.buckets.Buckets;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.stream.IntStream;

import java.util.regex.Pattern;

import java.util.StringTokenizer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * StringF(ormatting)Buffer equivalent which supports formatting. This buffer also supports negative indices which 
 * means that the original index is calculated beginning from the end of the buffer.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StringFBuffer implements Serializable, CharSequence, Comparable<StringFBuffer> {
  
  private static final long serialVersionUID = 6094891463351971217L;
  
  // the original implementation
  StringBuffer   origin;
  
  /**
   * @see StringBuffer#StringBuffer()
   */
  public StringFBuffer() {
    origin    = new StringBuffer();
  }

  /**
   * @see StringBuffer#StringBuffer(int)
   */
  public StringFBuffer(@Min(1) int capacity) {
    origin = new StringBuffer(capacity);
  }

  /**
   * @see StringBuffer#StringBuffer(String)
   */
  public StringFBuffer(@NotNull String str) {
    origin = new StringBuffer(str);
  }

  /**
   * @see StringBuffer#StringBuffer(CharSequence)
   */
  public StringFBuffer(@NotNull CharSequence seq) {
    origin = new StringBuffer(seq);
  }

  /**
   * @see StringBuffer#length() 
   */
  @Override
  public synchronized @Min(0) int length() {
    return origin.length();
  }

  /**
   * @see StringBuffer#capacity()
   */
  public synchronized @Min(1) int capacity() {
    return origin.capacity();
  }

  /**
   * @see StringBuffer#ensureCapacity(int)
   */
  public synchronized void ensureCapacity(@Min(1) int minimum) {
    origin.ensureCapacity(minimum);
  }

  /**
   * @see StringBuffer#trimToSize()
   */
  public synchronized void trimToSize() {
    origin.trimToSize();
  }

  /**
   * @see StringBuffer#setLength(int)
   */
  public synchronized void setLength(@Min(0) int newlength) {
    origin.setLength(newlength);
  }

  /**
   * @see StringBuffer#charAt(int)
   */
  @Override
  public synchronized char charAt(int index) {
    return origin.charAt(adjustIndex(index));
  }

  /**
   * @see StringBuffer#codePointAt(int)
   */
  public synchronized int codePointAt(int index) {
    return origin.codePointAt(adjustIndex(index));
  }

  /**
   * @see StringBuffer#codePointBefore(int)
   */
  public synchronized int codePointBefore(int index) {
    return origin.codePointBefore(adjustIndex(index));
  }

  /**
   * @see StringBuffer#codePointCount(int, int)
   */
  public synchronized int codePointCount(int begin, int end) {
    return origin.codePointCount(adjustIndex(begin), adjustIndex(end));
  }

  /**
   * @see StringBuffer#offsetByCodePoints(int, int)
   */
  public synchronized int offsetByCodePoints(int index, int codepointoffset) {
    return origin.offsetByCodePoints(adjustIndex(index), codepointoffset);
  }

  /**
   * @see StringBuffer#getChars(int, int, char[], int)
   */
  public synchronized void getChars(int start, int end, @NotNull char[] destination, int destbegin) {
    origin.getChars(adjustIndex(start), adjustIndex(end), destination, adjustIndex(destination.length, destbegin));
  }

  /**
   * @see StringBuffer#setCharAt(int, char)
   */
  public synchronized void setCharAt(int index, char ch) {
    origin.setCharAt(adjustIndex(index), ch);
  }

  /**
   * @see StringBuffer#append(Object)
   */
  public synchronized StringFBuffer append(@NotNull Object obj) {
    origin.append(obj);
    return this;
  }

  /**
   * @see StringBuffer#append(String) 
   */
  public synchronized StringFBuffer append(@NotNull String str) {
    origin.append(str);
    return this;
  }

  /**
   * @see StringBuffer#append(StringBuffer)
   */
  public synchronized StringFBuffer append(@NotNull StringBuffer buffer) {
    origin.append(buffer);
    return this;
  }

  /**
   * @see StringBuffer#append(StringBuffer)
   */
  public synchronized StringFBuffer append(@NotNull StringFBuffer buffer) {
    origin.append(buffer.origin);
    return this;
  }

  /**
   * @see StringBuffer#append(CharSequence)
   */
  public synchronized StringFBuffer append(@NotNull CharSequence sequence) {
    origin.append(sequence);
    return this;
  }

  /**
   * @see StringBuffer#append(CharSequence, int, int)
   */
  public synchronized StringFBuffer append(@NotNull CharSequence sequence, int start, int end) {
    origin.append(sequence, adjustIndex(sequence.length(), start), adjustIndex(sequence.length(), end));
    return this;
  }

  /**
   * @see StringBuffer#append(char[])
   */
  public synchronized StringFBuffer append(@NotNull char[] charray) {
    origin.append(charray);
    return this;
  }

  /**
   * @see StringBuffer#append(char[], int, int)
   */
  public synchronized StringFBuffer append(@NotNull char[] charray, int offset, int length) {
    origin.append(charray, offset, length);
    return this;
  }
  
  /**
   * @see StringBuffer#append(boolean)
   */
  public synchronized StringFBuffer append(boolean value) {
    origin.append(value);
    return this;
  }

  /**
   * @see StringBuffer#append(char)
   */
  public synchronized StringFBuffer append(char value) {
    origin.append(value);
    return this;
  }

  /**
   * @see StringBuffer#append(int)
   */
  public synchronized StringFBuffer append(int value) {
    origin.append(value);
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
  public synchronized StringFBuffer appendF(@NotNull String fmt, Object ... args) {
    var toAdd = fmt;
    if ((args != null) && (args.length > 0)) {
      toAdd = String.format(fmt, args);
    }
    return append(toAdd);
  }
  
  /**
   * @see StringBuffer#appendCodePoint(int)
   */
  public synchronized StringFBuffer appendCodePoint(int codepoint) {
    origin.appendCodePoint( codepoint);
    return this;
  }

  /**
   * @see StringBuffer#append(long)
   */
  public synchronized StringFBuffer append(long value) {
    origin.append(value);
    return this;
  }

  /**
   * @see StringBuffer#append(float)
   */
  public synchronized StringFBuffer append(float value) {
    origin.append(value);
    return this;
  }

  /**
   * @see StringBuffer#append(double)
   */
  public synchronized StringFBuffer append(double value) {
    origin.append(value);
    return this;
  }

  /**
   * @see StringBuffer#delete(int, int)
   */
  public synchronized StringFBuffer delete(int start, int end) {
    origin.delete(adjustIndex(start), adjustIndex(end));
    return this;
  }

  /**
   * @see StringBuffer#deleteCharAt(int)
   */
  public synchronized StringFBuffer deleteCharAt(int index) {
    origin.deleteCharAt(adjustIndex(index));
    return this;
  }

  /**
   * @see StringBuffer#replace(int, int, String)
   */
  public synchronized StringFBuffer replace(int start, int end, @NotNull String str) {
    origin.replace(adjustIndex(start), adjustIndex(end), str);
    return this;
  }

  /**
   * @see StringBuffer#substring(int)
   */
  public synchronized String substring(int start) {
    return origin.substring(adjustIndex(start));
  }

  /**
   * @see StringBuffer#subSequence(int, int)
   */
  @Override
  public synchronized CharSequence subSequence(int start, int end) {
    return origin.subSequence(adjustIndex(start), adjustIndex(end));
  }

  /**
   * @see StringBuffer#substring(int, int)
   */
  public synchronized String substring( int start, int end ) {
    return origin.substring(adjustIndex(start), adjustIndex(end));
  }

  /**
   * @see StringBuffer#insert(int, char[], int, int)
   */
  public synchronized StringFBuffer insert(int index, @NotNull char[] charray, int offset, int length) {
    origin.insert(adjustIndex(index), charray, adjustIndex(charray.length, offset), length);
    return this;
  }

  /**
   * @see StringBuffer#insert(int, Object)
   */
  public synchronized StringFBuffer insert(int offset, @NotNull Object obj) {
    origin.insert(adjustIndex(offset), obj);
    return this;
  }

  /**
   * @see StringBuffer#insert(int, String)
   */
  public synchronized StringFBuffer insert(int offset, @NotNull String value) {
    origin.insert(adjustIndex(offset), value);
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
  public synchronized StringFBuffer insertF(int offset, @NotNull String fmt, Object ... args) {
    var toAdd = fmt;
    if ((args != null) && (args.length > 0)) {
      toAdd = String.format(fmt, args);
    }
    return insert(adjustIndex(offset), toAdd);
  }

  /**
   * @see StringBuffer#insert(int, char[])
   */
  public synchronized StringFBuffer insert(int offset, @NotNull char[] value) {
    origin.insert(adjustIndex(offset), value);
    return this;
  }

  /**
   * @see StringBuffer#insert(int, CharSequence)
   */
  public synchronized StringFBuffer insert(int offset, @NotNull CharSequence value) {
    origin.insert(adjustIndex(offset), value);
    return this;
  }

  /**
   * @see StringBuffer#insert(int, CharSequence, int, int)
   */
  public synchronized StringFBuffer insert(int offset, @NotNull CharSequence value, int start, int end) {
    origin.insert(adjustIndex(offset), value, adjustIndex(value.length(), start), adjustIndex(value.length(), end));
    return this;
  }

  /**
   * @see StringBuffer#insert(int, boolean)
   */
  public synchronized StringFBuffer insert(int offset, boolean value) {
    origin.insert(adjustIndex(offset), value);
    return this;
  }

  /**
   * @see StringBuffer#insert(int, char)
   */
  public synchronized StringFBuffer insert(int offset, char value) {
    origin.insert(adjustIndex(offset), value);
    return this;
  }

  /**
   * @see StringBuffer#insert(int, int)
   */
  public synchronized StringFBuffer insert(int offset, int value) {
    origin.insert(adjustIndex( offset), value);
    return this;
  }

  /**
   * @see StringBuffer#insert(int, long)
   */
  public synchronized StringFBuffer insert(int offset, long value) {
    origin.insert(adjustIndex(offset), value);
    return this;
  }

  /**
   * @see StringBuffer#insert(int, float)
   */
  public synchronized StringFBuffer insert(int offset, float value) {
    origin.insert(adjustIndex(offset), value);
    return this;
  }

  /**
   * @see StringBuffer#insert(int, double)
   */
  public synchronized StringFBuffer insert(int offset, double value) {
    origin.insert(adjustIndex(offset), value);
    return this;
  }

  /**
   * @see StringBuffer#indexOf(String)
   */
  public synchronized int indexOf(@NotNull String str) {
    return origin.indexOf(str);
  }

  /**
   * @see StringBuffer#indexOf(String, int)
   */
  public synchronized int indexOf(@NotNull String str, int index) {
    return origin.indexOf(str, adjustIndex(index));
  }

  /**
   * Like {@link StringBuffer#indexOf(String)} with the difference that this function provides the position of the
   * leftmost literal which could be found.
   * 
   * @param literals   A list of literals that will be checked. Maybe <code>null</code>.
   * 
   * @return   The index of the leftmost found literal or -1 if none matched.
   */
  public synchronized int indexOf(@Null String ... literals) {
    return indexOf(0, literals);
  }
  
  /**
   * Like {@link StringBuffer#indexOf(String)} with the difference that this function provides the position of the
   * leftmost literal which could be found.
   * 
   * @param index      The index used as the starting point for the lookup.
   * @param literals   A list of literals that will be checked. Maybe <code>null</code>.
   * 
   * @return   The index of the leftmost found literal or -1 if none matched.
   */
  public synchronized int indexOf(int index, @Null String ... literals) {
    index      = adjustIndex(index);
    var result = -1;
    if (literals != null) {
      for (var literal : literals) {
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
   * @see StringBuffer#lastIndexOf(String)
   */
  public synchronized int lastIndexOf(@NotNull String str) {
    return origin.lastIndexOf(str);
  }

  /**
   * @see StringBuffer#lastIndexOf(String, int)
   */
  public synchronized int lastIndexOf(@NotNull String str, int index) {
    return origin.lastIndexOf(str, adjustIndex(index));
  }

  /**
   * Like {@link StringBuffer#lastIndexOf(String,int)} with the difference that this function provides the position of 
   * the rightmost literal which could be found.
   * 
   * @param literals   A list of literals that will be checked. Maybe <code>null</code>.
   * 
   * @return   The index of the rightmost found literal or -1 if none matched.
   */
  public synchronized int lastIndexOf(@Null String ... literals) {
    return lastIndexOf(-1, literals);
  }
  
  /**
   * Like {@link StringBuffer#lastIndexOf(String,int)} with the difference that this function provides the position of 
   * the rightmost literal which could be found.
   * 
   * @param index      The index used as the starting point for the lookup.
   * @param literals   A list of literals that will be checked. Maybe <code>null</code>.
   * 
   * @return   The index of the rightmost found literal or -1 if none matched.
   */
  public synchronized int lastIndexOf(int index, @Null String ... literals) {
    index      = adjustIndex(index);
    var result = -1;
    if (literals != null) {
      for (var literal : literals) {
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
   * @see StringBuffer#reverse()
   */
  public synchronized StringFBuffer reverse() {
    origin.reverse();
    return this;
  }

  @Override
  public synchronized String toString() {
    return origin.toString();
  }
  
  /**
   * This function removes leading whitespace from this buffer.
   */
  public synchronized void trimLeading() {
    while ((length() > 0) && Character.isWhitespace(charAt(0))) {
      deleteCharAt(0);
    }
  }

  /**
   * This function removes trailing whitespace from this buffer.
   */
  public synchronized void trimTrailing() {
    while ((length() > 0) && Character.isWhitespace(charAt(-1))) {
      deleteCharAt(-1);
    }
  }

  /**
   * This function removes leading and trailing whitespace from this buffer.
   */
  public synchronized void trim() {
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
  public synchronized boolean startsWith(@NotNull String totest) {
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
  public synchronized boolean startsWith(boolean casesensitive, @NotNull String totest) {
    if (totest.length() > origin.length()) {
      return false;
    }
    var part = origin.substring(0, totest.length());
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
  public synchronized boolean endsWith(@NotNull String totest) {
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
  public synchronized boolean endsWith(boolean casesensitive, @NotNull String totest) {
    if (totest.length() > origin.length()) {
      return false;
    }
    var part = origin.substring(origin.length() - totest.length());
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
  public synchronized boolean equals(@NotNull String totest) {
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
  public synchronized boolean equals(boolean casesensitive, @NotNull String totest) {
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
  public synchronized StringFBuffer remove(@NotNull String toremove) {
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
  public synchronized String[] split(@NotNull String delimiters) {
    return Buckets.bucketArrayList().forInstance($ -> {
      var tokenizer = new StringTokenizer(origin.toString(), delimiters, false);
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
  public synchronized String[] splitRegex(@NotNull String regex) {
    return splitRegex(Pattern.compile(regex));
  }
  
  /**
   * Like {@link #split(String)} with the difference that this function accepts a regular expression for the splitting.
   * 
   * @param pattern   A pattern providing the regular expression used for the splitting. Not <code>null</code>.
   *                     
   * @return   A splitted list without fragments matching the supplied regular expression. Not <code>null</code>.
   */
  public synchronized String[] splitRegex(@NotNull Pattern pattern) {
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
  public synchronized StringFBuffer replace(char from, char to) {
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
  public synchronized StringFBuffer replaceAll(@NotNull String regex, @NotNull String replacement) {
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
  public synchronized StringFBuffer replaceAll(@NotNull Pattern pattern, @NotNull String replacement) {
    Buckets.<Integer>bucketArrayList().forInstanceDo($ -> {
      var matcher = pattern.matcher(origin);
      while (matcher.find()) {
        $.add(matcher.start());
        $.add(matcher.end());
      }
      for (var i = $.size() - 2; i >= 0; i -= 2) {
        var start = $.get(i);
        var end   = $.get(i + 1);
        origin.delete(start, end);
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
  public synchronized StringFBuffer replaceFirst(@NotNull String regex, @NotNull String replacement) {
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
  public synchronized StringFBuffer replaceFirst(@NotNull Pattern pattern, @NotNull String replacement) {
    var matcher = pattern.matcher(origin);
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
  public synchronized StringFBuffer replaceLast(@NotNull String regex, @NotNull String replacement) {
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
  public synchronized StringFBuffer replaceLast(@NotNull Pattern pattern, @NotNull String replacement) {
    var matcher = pattern.matcher(origin);
    var start   = -1;
    var end     = -1;
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
  public synchronized IntStream chars() {
    return origin.chars();
  }
  
  @Override
  public synchronized IntStream codePoints() {
    return origin.codePoints();
  }
  
  @Override
  public synchronized int compareTo(@NotNull StringFBuffer another) {
    if (this == another) {
      return 0;
    }
    return origin.compareTo(another.origin);
  }
  
  private void writeObject(ObjectOutputStream s) throws IOException {
    s.writeObject(origin);
  }

  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    origin = (StringBuffer) s.readObject();
  }

} /* ENDCLASS */

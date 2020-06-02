package com.kasisoft.libs.common.text;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.stream.IntStream;

import java.util.regex.Pattern;

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
public class StringFBuffer implements Serializable, StringLike<StringFBuffer> {
  
  private static final long serialVersionUID = 6094891463351971217L;
  
  // the original implementation
  StringBuffer   origin;
  
  /**
   * @see StringBuffer#StringBuffer()
   */
  public StringFBuffer() {
    origin = new StringBuffer();
  }

  /**
   * @see StringBuffer#StringBuffer(int)
   */
  public StringFBuffer(@Min(1) int capacity) {
    origin = new StringBuffer(capacity);
  }

  /**
   * @see StringBuffer#StringBuffer(CharSequence)
   */
  public StringFBuffer(@NotNull CharSequence seq) {
    origin = new StringBuffer(seq);
  }

  @Override
  public synchronized @Min(0) int length() {
    return origin.length();
  }

  @Override
  public synchronized @Min(1) int capacity() {
    return origin.capacity();
  }

  @Override
  public synchronized void ensureCapacity(@Min(1) int minimum) {
    origin.ensureCapacity(minimum);
  }

  @Override
  public synchronized void trimToSize() {
    origin.trimToSize();
  }

  @Override
  public synchronized void setLength(@Min(0) int newlength) {
    origin.setLength(newlength);
  }

  @Override
  public synchronized char charAt(int index) {
    return origin.charAt(adjustIndex(index, false));
  }

  @Override
  public synchronized int codePointAt(int index) {
    return origin.codePointAt(adjustIndex(index, false));
  }

  @Override
  public synchronized int codePointBefore(int index) {
    return origin.codePointBefore(adjustIndex(index, false));
  }

  @Override
  public synchronized int codePointCount(int begin, int end) {
    return origin.codePointCount(adjustIndex(begin, false), adjustIndex(end, true));
  }

  @Override
  public synchronized int offsetByCodePoints(int index, int codepointoffset) {
    return origin.offsetByCodePoints(adjustIndex(index, false), codepointoffset);
  }

  @Override
  public synchronized void getChars(int start, int end, @NotNull char[] destination, int destbegin) {
    origin.getChars(adjustIndex(start, false), adjustIndex(end, true), destination, adjustIndex(destination.length, destbegin, false));
  }

  @Override
  public synchronized void setCharAt(int index, char ch) {
    origin.setCharAt(adjustIndex(index, false), ch);
  }

  @Override
  public void setCodepointAt(int index, int codepoint) {
    index     = adjustIndex(index, false);
    var count = Character.charCount(codepoint);
    delete(index, index + count);
  }

  @Override
  public synchronized StringFBuffer append(@NotNull Object obj) {
    origin.append(obj);
    return this;
  }

  @Override
  public synchronized StringFBuffer append(@NotNull CharSequence sequence) {
    origin.append(sequence);
    return this;
  }

  @Override
  public synchronized StringFBuffer append(@NotNull CharSequence sequence, int start, int end) {
    origin.append(sequence, adjustIndex(sequence.length(), start, false), adjustIndex(sequence.length(), end, true));
    return this;
  }

  @Override
  public synchronized StringFBuffer append(@NotNull char[] charray) {
    origin.append(charray);
    return this;
  }

  @Override
  public synchronized StringFBuffer append(@NotNull char[] charray, int offset, int length) {
    origin.append(charray, adjustIndex(charray.length, offset, false), length);
    return this;
  }
  
  @Override
  public synchronized StringFBuffer append(boolean value) {
    origin.append(value);
    return this;
  }

  @Override
  public synchronized StringFBuffer append(char value) {
    origin.append(value);
    return this;
  }

  @Override
  public synchronized StringFBuffer append(int value) {
    origin.append(value);
    return this;
  }

  @Override
  public synchronized StringFBuffer appendCodePoint(int codepoint) {
    origin.appendCodePoint(codepoint);
    return this;
  }

  @Override
  public synchronized StringFBuffer append(long value) {
    origin.append(value);
    return this;
  }

  @Override
  public synchronized StringFBuffer append(float value) {
    origin.append(value);
    return this;
  }

  @Override
  public synchronized StringFBuffer append(double value) {
    origin.append(value);
    return this;
  }

  @Override
  public synchronized StringFBuffer delete(int start, int end) {
    origin.delete(adjustIndex(start, false), adjustIndex(end, true));
    return this;
  }

  @Override
  public synchronized StringFBuffer deleteCharAt(int index) {
    origin.deleteCharAt(adjustIndex(index, false));
    return this;
  }

  @Override
  public synchronized StringFBuffer replace(int start, int end, @NotNull String str) {
    origin.replace(adjustIndex(start, false), adjustIndex(end, true), str);
    return this;
  }

  @Override
  public synchronized String substring(int start) {
    return origin.substring(adjustIndex(start, false));
  }

  @Override
  public synchronized CharSequence subSequence(int start, int end) {
    return origin.subSequence(adjustIndex(start, false), adjustIndex(end, true));
  }

  @Override
  public synchronized String substring( int start, int end) {
    return origin.substring(adjustIndex(start, false), adjustIndex(end, true));
  }

  @Override
  public synchronized StringFBuffer insert(int index, @NotNull char[] charray, int offset, int length) {
    origin.insert(adjustIndex(index, false), charray, adjustIndex(charray.length, offset, false), length);
    return this;
  }

  @Override
  public synchronized StringFBuffer insert(int offset, @NotNull Object obj) {
    origin.insert(adjustIndex(offset, false), obj);
    return this;
  }

  @Override
  public synchronized StringFBuffer insert(int offset, @NotNull char[] value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public synchronized StringFBuffer insert(int offset, @NotNull CharSequence value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public synchronized StringFBuffer insert(int offset, @NotNull CharSequence value, int start, int end) {
    origin.insert(adjustIndex(offset, false), value, adjustIndex(value.length(), start, false), adjustIndex(value.length(), end, true));
    return this;
  }

  @Override
  public synchronized StringFBuffer insert(int offset, boolean value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public synchronized StringFBuffer insert(int offset, char value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public synchronized StringFBuffer insert(int offset, int value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public synchronized StringFBuffer insert(int offset, long value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public synchronized StringFBuffer insert(int offset, float value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public synchronized StringFBuffer insert(int offset, double value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public synchronized int indexOf(@NotNull String str) {
    return origin.indexOf(str);
  }

  @Override
  public synchronized int indexOf(@NotNull String str, int index) {
    return origin.indexOf(str, adjustIndex(index, false));
  }

  @Override
  public synchronized int lastIndexOf(@NotNull String str) {
    return origin.lastIndexOf(str);
  }

  @Override
  public synchronized int lastIndexOf(@NotNull String str, int index) {
    return origin.lastIndexOf(str, adjustIndex(index, false));
  }
  
  @Override
  public synchronized StringFBuffer reverse() {
    origin.reverse();
    return this;
  }

  @Override
  public synchronized String toString() {
    return origin.toString();
  }
  
  @Override
  public synchronized void trimLeading() {
    StringLike.super.trimLeading();
  }

  @Override
  public synchronized void trimTrailing() {
    StringLike.super.trimTrailing();
  }

  @Override
  public synchronized void trim() {
    StringLike.super.trim();
  }

  private int adjustIndex(int index, boolean isEnd) {
    return adjustIndex(origin.length(), index, isEnd);
  }

  /**
   * Returns an adjusted index since this extension supports negative indices as well.
   *
   * @param length  The length to be used for the calculation.
   * @param index   The index supplied by the user.
   * 
   * @return  The index to use for the original implementation.
   */
  private int adjustIndex(int length, int index, boolean isEnd) {
    if (index < 0) {
      return length + index;
    } else if ((index == 0) && isEnd) {
      return length;
    }
    return index;
  }

  @Override
  public synchronized boolean startsWith(@NotNull String totest) {
    return StringLike.super.startsWith(totest);
  }

  @Override
  public synchronized boolean startsWith(boolean casesensitive, @NotNull String totest) {
    return StringLike.super.startsWith(casesensitive, totest);
  }

  @Override
  public synchronized boolean endsWith(@NotNull String totest) {
    return StringLike.super.endsWith(totest);
  }

  @Override
  public synchronized boolean endsWith(boolean casesensitive, @NotNull String totest) {
    return StringLike.super.endsWith(casesensitive, totest);
  }
  
  @Override
  public synchronized boolean equals(@NotNull String totest) {
    return StringLike.super.equals(totest);
  }
  
  @Override
  public synchronized boolean equals(boolean casesensitive, @NotNull String totest) {
    return StringLike.super.equals(casesensitive, totest);
  }

  @Override
  public synchronized StringFBuffer remove(@NotNull String toremove) {
    return StringLike.super.remove(toremove);
  }

  @Override
  public synchronized String[] split(@NotNull String delimiters) {
    return StringLike.super.split(delimiters);
  }
  
  @Override
  public synchronized String[] splitRegex(@NotNull String regex) {
    return StringLike.super.splitRegex(regex);
  }
  
  @Override
  public synchronized String[] splitRegex(@NotNull Pattern pattern) {
    return StringLike.super.splitRegex(pattern);
  }

  @Override
  public synchronized StringFBuffer replace(char from, char to) {
    return StringLike.super.replace(from, to);
  }

  @Override
  public synchronized StringFBuffer replaceAll(@NotNull String regex, @NotNull String replacement) {
    return StringLike.super.replaceAll(regex, replacement);
  }
  
  @Override
  public synchronized StringFBuffer replaceAll(@NotNull Pattern pattern, @NotNull String replacement) {
    return StringLike.super.replaceAll(pattern, replacement);
  }

  @Override
  public synchronized StringFBuffer replaceFirst(@NotNull String regex, @NotNull String replacement) {
    return StringLike.super.replaceFirst(regex, replacement);
  }
  
  @Override
  public synchronized StringFBuffer replaceFirst(@NotNull Pattern pattern, @NotNull String replacement) {
    return StringLike.super.replaceFirst(pattern, replacement);
  }

  @Override
  public synchronized StringFBuffer replaceLast(@NotNull String regex, @NotNull String replacement) {
    return StringLike.super.replaceLast(regex, replacement);
  }
  
  @Override
  public synchronized StringFBuffer replaceLast(@NotNull Pattern pattern, @NotNull String replacement) {
    return StringLike.super.replaceLast(pattern, replacement);
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

package com.kasisoft.libs.common.text;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.stream.IntStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * StringF(ormatting)Builder  equivalent which supports formatting. This builder also supports negative indices which 
 * means that the original index is calculated beginning from the end of the buffer.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StringFBuilder implements Serializable, StringLike<StringFBuilder> {

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

  @Override
  public @Min(0) int length() {
    return origin.length();
  }

  @Override
  public @Min(1) int capacity() {
    return origin.capacity();
  }

  @Override
  public void ensureCapacity(@Min(1) int minimum) {
    origin.ensureCapacity(minimum);
  }

  @Override
  public void trimToSize() {
    origin.trimToSize();
  }

  @Override
  public void setLength(@Min(0) int newlength) {
    origin.setLength(newlength);
  }

  @Override
  public char charAt(int index) {
    return origin.charAt(adjustIndex(index));
  }

  @Override
  public int codePointAt(int index) {
    return origin.codePointAt(adjustIndex(index));
  }

  @Override
  public int codePointBefore(int index) {
    return origin.codePointBefore(adjustIndex(index));
  }

  @Override
  public int codePointCount(int begin, int end) {
    return origin.codePointCount(adjustIndex(begin), adjustIndex(end));
  }

  @Override
  public int offsetByCodePoints(int index, int codepointoffset) {
    return origin.offsetByCodePoints(adjustIndex(index), codepointoffset);
  }

  @Override
  public void getChars(int start, int end, @NotNull char[] destination, int destbegin) {
    origin.getChars(adjustIndex(start), adjustIndex(end), destination, adjustIndex(destination.length, destbegin));
  }

  @Override
  public void setCharAt(int index, char ch) {
    origin.setCharAt(adjustIndex(index), ch);
  }

  @Override
  public StringFBuilder append(@NotNull Object obj) {
    origin.append(obj);
    return this;
  }

  @Override
  public StringFBuilder append(@NotNull CharSequence sequence) {
    origin.append(sequence);
    return this;
  }

  @Override
  public StringFBuilder append(@NotNull CharSequence sequence, int start, int end) {
    origin.append(sequence, adjustIndex(sequence.length(), start), adjustIndex(sequence.length(), end));
    return this;
  }

  @Override
  public StringFBuilder append(@NotNull char[] charray) {
    origin.append(charray);
    return this;
  }

  @Override
  public StringFBuilder append(@NotNull char[] charray, int offset, int length) {
    origin.append(charray, adjustIndex(charray.length, offset), length);
    return this;
  }
  
  @Override
  public StringFBuilder append(boolean value) {
    origin.append(value);
    return this;
  }

  @Override
  public StringFBuilder append(char value) {
    origin.append(value);
    return this;
  }

  @Override
  public StringFBuilder append(int value) {
    origin.append(value);
    return this;
  }

  @Override
  public StringFBuilder appendCodePoint(int codepoint) {
    origin.appendCodePoint(codepoint);
    return this;
  }

  @Override
  public StringFBuilder append(long value) {
    origin.append(value);
    return this;
  }

  @Override
  public StringFBuilder append(float value) {
    origin.append(value);
    return this;
  }

  @Override
  public StringFBuilder append(double value) {
    origin.append(value);
    return this;
  }

  @Override
  public StringFBuilder delete(int start, int end) {
    origin.delete(adjustIndex(start), adjustIndex(end));
    return this;
  }

  @Override
  public StringFBuilder deleteCharAt(int index) {
    origin.deleteCharAt(adjustIndex(index));
    return this;
  }

  @Override
  public StringFBuilder replace(int start, int end, @NotNull String str) {
    origin.replace(adjustIndex(start), adjustIndex(end), str);
    return this;
  }

  @Override
  public String substring(int start) {
    return origin.substring(adjustIndex(start));
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return origin.subSequence(adjustIndex(start), adjustIndex(end));
  }

  @Override
  public String substring(int start, int end) {
    return origin.substring(adjustIndex(start), adjustIndex(end));
  }

  @Override
  public StringFBuilder insert(int index, @NotNull char[] charray, int offset, int length) {
    origin.insert(adjustIndex(index), charray, adjustIndex(charray.length, offset), length);
    return this;
  }

  @Override
  public StringFBuilder insert(int offset, @NotNull Object obj) {
    origin.insert(adjustIndex(offset), obj);
    return this;
  }

  @Override
  public StringFBuilder insert(int offset, @NotNull String value) {
    origin.insert(adjustIndex(offset), value);
    return this;
  }

  @Override
  public StringFBuilder insert(int offset, @NotNull char[] value) {
    origin.insert(adjustIndex(offset), value);
    return this;
  }

  @Override
  public StringFBuilder insert(int offset, @NotNull CharSequence value) {
    origin.insert(adjustIndex(offset), value);
    return this;
  }

  @Override
  public StringFBuilder insert(int offset, @NotNull CharSequence value, int start, int end) {
    origin.insert(adjustIndex(offset), value, adjustIndex(value.length(), start), adjustIndex(value.length(), end));
    return this;
  }

  @Override
  public StringFBuilder insert(int offset, boolean value) {
    origin.insert(adjustIndex(offset), value);
    return this;
  }

  @Override
  public StringFBuilder insert(int offset, char value) {
    origin.insert(adjustIndex(offset), value);
    return this;
  }

  @Override
  public StringFBuilder insert(int offset, int value) {
    origin.insert(adjustIndex(offset), value);
    return this;
  }

  @Override
  public StringFBuilder insert(int offset, long value) {
    origin.insert(adjustIndex(offset), value);
    return this;
  }

  @Override
  public StringFBuilder insert(int offset, float value) {
    origin.insert(adjustIndex(offset), value);
    return this;
  }

  @Override
  public StringFBuilder insert(int offset, double value) {
    origin.insert(adjustIndex(offset), value);
    return this;
  }

  @Override
  public int indexOf(@NotNull String str) {
    return origin.indexOf(str);
  }

  @Override
  public int indexOf(@NotNull String str, int index) {
    return origin.indexOf(str, adjustIndex(index));
  }

  @Override
  public int lastIndexOf(@NotNull String str) {
    return origin.lastIndexOf(str);
  }

  @Override
  public int lastIndexOf(@NotNull String str, int index) {
    return origin.lastIndexOf(str, adjustIndex(index));
  }
  
  @Override
  public StringFBuilder reverse() {
    origin = origin.reverse();
    return this;
  }

  @Override
  public String toString() {
    return origin.toString();
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

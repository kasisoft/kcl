package com.kasisoft.libs.common.text;

import com.kasisoft.libs.common.utils.MiscFunctions;

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
    origin = new StringBuilder(capacity);
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
  public StringFBuilder ensureCapacity(@Min(1) int minimum) {
    origin.ensureCapacity(minimum);
    return this;
  }

  @Override
  public StringFBuilder trimToSize() {
    origin.trimToSize();
    return this;
  }

  @Override
  public StringFBuilder setLength(@Min(0) int newlength) {
    origin.setLength(newlength);
    return this;
  }

  @Override
  public char charAt(int index) {
    return origin.charAt(adjustIndex(index, false));
  }

  @Override
  public int codePointAt(int index) {
    return origin.codePointAt(adjustIndex(index, false));
  }

  @Override
  public int codePointBefore(int index) {
    return origin.codePointBefore(adjustIndex(index, false));
  }

  @Override
  public int codePointCount(int begin, int end) {
    return origin.codePointCount(adjustIndex(begin, false), adjustIndex(end, true));
  }

  @Override
  public int offsetByCodePoints(int index, int codepointoffset) {
    return origin.offsetByCodePoints(adjustIndex(index, false), codepointoffset);
  }

  @Override
  public StringFBuilder getChars(int start, int end, @NotNull char[] destination, int destbegin) {
    origin.getChars(adjustIndex(start, false), adjustIndex(end, true), destination, MiscFunctions.adjustIndex(destination.length, destbegin, false));
    return this;
  }

  @Override
  public StringFBuilder setCharAt(int index, char ch) {
    origin.setCharAt(adjustIndex(index, false), ch);
    return this;
  }

  @Override
  public StringFBuilder setCodepointAt(int index, int codepoint) {
    index     = adjustIndex(index, false);
    var count = Character.charCount(codepoint);
    delete(index, index + count);
    return this;
  }

  @Override
  public @NotNull StringFBuilder append(@NotNull Object obj) {
    origin.append(obj);
    return this;
  }

  @Override
  public @NotNull StringFBuilder append(@NotNull CharSequence sequence) {
    origin.append(sequence);
    return this;
  }

  @Override
  public @NotNull StringFBuilder append(@NotNull CharSequence sequence, int start, int end) {
    origin.append(sequence, MiscFunctions.adjustIndex(sequence.length(), start, false), MiscFunctions.adjustIndex(sequence.length(), end, true));
    return this;
  }

  @Override
  public @NotNull StringFBuilder append(@NotNull char[] charray) {
    origin.append(charray);
    return this;
  }

  @Override
  public @NotNull StringFBuilder append(@NotNull char[] charray, int offset, int length) {
    origin.append(charray, MiscFunctions.adjustIndex(charray.length, offset, false), length);
    return this;
  }
  
  @Override
  public @NotNull StringFBuilder append(boolean value) {
    origin.append(value);
    return this;
  }

  @Override
  public @NotNull StringFBuilder append(char value) {
    origin.append(value);
    return this;
  }

  @Override
  public @NotNull StringFBuilder append(int value) {
    origin.append(value);
    return this;
  }

  @Override
  public @NotNull StringFBuilder appendCodePoint(int codepoint) {
    origin.appendCodePoint(codepoint);
    return this;
  }

  @Override
  public @NotNull StringFBuilder append(long value) {
    origin.append(value);
    return this;
  }

  @Override
  public @NotNull StringFBuilder append(float value) {
    origin.append(value);
    return this;
  }

  @Override
  public @NotNull StringFBuilder append(double value) {
    origin.append(value);
    return this;
  }

  @Override
  public @NotNull StringFBuilder delete(int start, int end) {
    origin.delete(adjustIndex(start, false), adjustIndex(end, true));
    return this;
  }

  @Override
  public @NotNull StringFBuilder deleteCharAt(int index) {
    origin.deleteCharAt(adjustIndex(index, false));
    return this;
  }

  @Override
  public @NotNull StringFBuilder replace(int start, int end, @NotNull String str) {
    origin.replace(adjustIndex(start, false), adjustIndex(end, true), str);
    return this;
  }

  @Override
  public @NotNull String substring(int start) {
    return origin.substring(adjustIndex(start, false));
  }

  @Override
  public @NotNull CharSequence subSequence(int start, int end) {
    return origin.subSequence(adjustIndex(start, false), adjustIndex(end, true));
  }

  @Override
  public @NotNull String substring(int start, int end) {
    return origin.substring(adjustIndex(start, false), adjustIndex(end, true));
  }

  @Override
  public @NotNull StringFBuilder insert(int index, @NotNull char[] charray, int offset, int length) {
    origin.insert(adjustIndex(index, false), charray, MiscFunctions.adjustIndex(charray.length, offset, false), length);
    return this;
  }

  @Override
  public @NotNull StringFBuilder insert(int offset, @NotNull Object obj) {
    origin.insert(adjustIndex(offset, false), obj);
    return this;
  }

  @Override
  public @NotNull StringFBuilder insert(int offset, @NotNull char[] value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public @NotNull StringFBuilder insert(int offset, @NotNull CharSequence value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public @NotNull StringFBuilder insert(int offset, @NotNull CharSequence value, int start, int end) {
    origin.insert(adjustIndex(offset, false), value, MiscFunctions.adjustIndex(value.length(), start, false), MiscFunctions.adjustIndex(value.length(), end, true));
    return this;
  }

  @Override
  public @NotNull StringFBuilder insert(int offset, boolean value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public @NotNull  StringFBuilder insert(int offset, char value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public @NotNull StringFBuilder insert(int offset, int value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public @NotNull StringFBuilder insert(int offset, long value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public @NotNull StringFBuilder insert(int offset, float value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public @NotNull StringFBuilder insert(int offset, double value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public int indexOf(@NotNull String str) {
    return origin.indexOf(str);
  }

  @Override
  public int indexOf(@NotNull String str, int index) {
    return origin.indexOf(str, adjustIndex(index, false));
  }

  @Override
  public int lastIndexOf(@NotNull String str) {
    return origin.lastIndexOf(str);
  }

  @Override
  public int lastIndexOf(@NotNull String str, int index) {
    return origin.lastIndexOf(str, adjustIndex(index, false));
  }
  
  @Override
  public @NotNull StringFBuilder reverse() {
    origin.reverse();
    return this;
  }

  @Override
  public @NotNull String toString() {
    return origin.toString();
  }
  
  private int adjustIndex(int index, boolean isEnd) {
    return MiscFunctions.adjustIndex(origin.length(), index, isEnd);
  }

  @Override
  public @NotNull IntStream chars() {
    return origin.chars();
  }
  
  @Override
  public @NotNull IntStream codePoints() {
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

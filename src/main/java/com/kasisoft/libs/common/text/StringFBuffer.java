package com.kasisoft.libs.common.text;

import com.kasisoft.libs.common.utils.MiscFunctions;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.function.Function;

import java.util.stream.IntStream;

import java.util.regex.Pattern;

import java.util.Map;

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
  public synchronized StringFBuffer ensureCapacity(@Min(1) int minimum) {
    origin.ensureCapacity(minimum);
    return this;
  }

  @Override
  public synchronized StringFBuffer trimToSize() {
    origin.trimToSize();
    return this;
  }

  @Override
  public synchronized StringFBuffer setLength(@Min(0) int newlength) {
    origin.setLength(newlength);
    return this;
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
  public synchronized StringFBuffer getChars(int start, int end, @NotNull char[] destination, int destbegin) {
    origin.getChars(adjustIndex(start, false), adjustIndex(end, true), destination, MiscFunctions.adjustIndex(destination.length, destbegin, false));
    return this;
  }

  @Override
  public synchronized StringFBuffer setCharAt(int index, char ch) {
    origin.setCharAt(adjustIndex(index, false), ch);
    return this;
  }

  @Override
  public StringFBuffer setCodepointAt(int index, int codepoint) {
    index     = adjustIndex(index, false);
    var count = Character.charCount(codepoint);
    delete(index, index + count);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer append(@NotNull Object obj) {
    origin.append(obj);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer append(@NotNull CharSequence sequence) {
    origin.append(sequence);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer append(@NotNull CharSequence sequence, int start, int end) {
    origin.append(sequence, MiscFunctions.adjustIndex(sequence.length(), start, false), MiscFunctions.adjustIndex(sequence.length(), end, true));
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer append(@NotNull char[] charray) {
    origin.append(charray);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer append(@NotNull char[] charray, int offset, int length) {
    origin.append(charray, MiscFunctions.adjustIndex(charray.length, offset, false), length);
    return this;
  }
  
  @Override
  public synchronized @NotNull StringFBuffer append(boolean value) {
    origin.append(value);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer append(char value) {
    origin.append(value);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer append(int value) {
    origin.append(value);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer appendCodePoint(int codepoint) {
    origin.appendCodePoint(codepoint);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer append(long value) {
    origin.append(value);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer append(float value) {
    origin.append(value);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer append(double value) {
    origin.append(value);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer delete(int start, int end) {
    origin.delete(adjustIndex(start, false), adjustIndex(end, true));
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer deleteCharAt(int index) {
    origin.deleteCharAt(adjustIndex(index, false));
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer replace(int start, int end, @NotNull String str) {
    origin.replace(adjustIndex(start, false), adjustIndex(end, true), str);
    return this;
  }

  @Override
  public synchronized @NotNull String substring(int start) {
    return origin.substring(adjustIndex(start, false));
  }

  @Override
  public synchronized @NotNull CharSequence subSequence(int start, int end) {
    return origin.subSequence(adjustIndex(start, false), adjustIndex(end, true));
  }

  @Override
  public synchronized @NotNull String substring( int start, int end) {
    return origin.substring(adjustIndex(start, false), adjustIndex(end, true));
  }

  @Override
  public synchronized @NotNull StringFBuffer insert(int index, @NotNull char[] charray, int offset, int length) {
    origin.insert(adjustIndex(index, false), charray, MiscFunctions.adjustIndex(charray.length, offset, false), length);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer insert(int offset, @NotNull Object obj) {
    origin.insert(adjustIndex(offset, false), obj);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer insert(int offset, @NotNull char[] value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer insert(int offset, @NotNull CharSequence value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer insert(int offset, @NotNull CharSequence value, int start, int end) {
    origin.insert(adjustIndex(offset, false), value, MiscFunctions.adjustIndex(value.length(), start, false), MiscFunctions.adjustIndex(value.length(), end, true));
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer insert(int offset, boolean value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer insert(int offset, char value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer insert(int offset, int value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer insert(int offset, long value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer insert(int offset, float value) {
    origin.insert(adjustIndex(offset, false), value);
    return this;
  }

  @Override
  public synchronized @NotNull StringFBuffer insert(int offset, double value) {
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
  public synchronized int indexOf(char ... characters) {
    return StringLike.super.indexOf(characters);
  }

  @Override
  public synchronized int indexOf(int index, char ... characters) {
    return StringLike.super.indexOf(index, characters);
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
  public synchronized int lastIndexOf(char ... characters) {
    return StringLike.super.lastIndexOf(characters);
  }

  @Override
  public synchronized int lastIndexOf(int index, char ... characters) {
    return StringLike.super.lastIndexOf(index, characters);
  }

  @Override
  public synchronized @NotNull StringFBuffer reverse() {
    origin.reverse();
    return this;
  }

  @Override
  public synchronized @NotNull String toString() {
    return origin.toString();
  }
  
  @Override
  public synchronized StringFBuffer trimLeading() {
    return StringLike.super.trimLeading();
  }

  @Override
  public synchronized StringFBuffer trimTrailing() {
    return StringLike.super.trimTrailing();
  }

  @Override
  public synchronized StringFBuffer trim() {
    return StringLike.super.trim();
  }

  private int adjustIndex(int index, boolean isEnd) {
    return MiscFunctions.adjustIndex(origin.length(), index, isEnd);
  }

  @Override
  public synchronized boolean startsWith(@NotNull CharSequence totest) {
    return StringLike.super.startsWith(totest);
  }

  @Override
  public synchronized boolean startsWith(boolean casesensitive, @NotNull CharSequence totest) {
    return StringLike.super.startsWith(casesensitive, totest);
  }

  @Override
  public synchronized <R extends CharSequence> R startsWithMany(R ... candidates) {
    return StringLike.super.startsWithMany(candidates);
  }

  @Override
  public synchronized <R extends CharSequence> R startsWithMany(boolean casesensitive, R ... candidates) {
    return StringLike.super.startsWithMany(casesensitive, candidates);
  }

  @Override
  public synchronized boolean endsWith(@NotNull String totest) {
    return StringLike.super.endsWith(totest);
  }
  
  @Override
  public synchronized <R extends CharSequence> R endsWithMany(R ... candidates) {
    return StringLike.super.endsWithMany(candidates);
  }

  @Override
  public synchronized <R extends CharSequence> R endsWithMany(boolean casesensitive, R ... candidates) {
    return StringLike.super.endsWithMany(casesensitive, candidates);
  }

  @Override
  public synchronized boolean endsWith(boolean casesensitive, @NotNull CharSequence totest) {
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
  public synchronized @NotNull StringFBuffer remove(@NotNull String toremove) {
    return StringLike.super.remove(toremove);
  }

  @Override
  public synchronized @NotNull String[] split(@NotNull String delimiters) {
    return StringLike.super.split(delimiters);
  }
  
  @Override
  public synchronized @NotNull String[] splitRegex(@NotNull String regex) {
    return StringLike.super.splitRegex(regex);
  }
  
  @Override
  public synchronized @NotNull String[] splitRegex(@NotNull Pattern pattern) {
    return StringLike.super.splitRegex(pattern);
  }

  @Override
  public synchronized @NotNull StringFBuffer replace(char from, char to) {
    return StringLike.super.replace(from, to);
  }

  @Override
  public synchronized @NotNull StringFBuffer replaceAll(@NotNull String regex, @NotNull String replacement) {
    return StringLike.super.replaceAll(regex, replacement);
  }
  
  @Override
  public synchronized @NotNull StringFBuffer replaceAll(@NotNull Pattern pattern, @NotNull String replacement) {
    return StringLike.super.replaceAll(pattern, replacement);
  }

  @Override
  public synchronized @NotNull StringFBuffer replaceFirst(@NotNull String regex, @NotNull String replacement) {
    return StringLike.super.replaceFirst(regex, replacement);
  }
  
  @Override
  public synchronized @NotNull StringFBuffer replaceFirst(@NotNull Pattern pattern, @NotNull String replacement) {
    return StringLike.super.replaceFirst(pattern, replacement);
  }

  @Override
  public synchronized @NotNull StringFBuffer replaceLast(@NotNull String regex, @NotNull String replacement) {
    return StringLike.super.replaceLast(regex, replacement);
  }
  
  @Override
  public synchronized @NotNull StringFBuffer replaceLast(@NotNull Pattern pattern, @NotNull String replacement) {
    return StringLike.super.replaceLast(pattern, replacement);
  }
  
  @Override
  public synchronized @NotNull StringFBuffer appendIfMissing(@NotNull CharSequence seq) {
    return StringLike.super.appendIfMissing(seq);
  }
  
  @Override
  public synchronized @NotNull StringFBuffer appendIfMissing(boolean ignoreCase, @NotNull CharSequence seq) {
    return StringLike.super.appendIfMissing(ignoreCase, seq);
  }

  @Override
  public synchronized @NotNull StringFBuffer prependIfMissing(@NotNull CharSequence seq) {
    return StringLike.super.prependIfMissing(seq);
  }
  
  @Override
  public synchronized @NotNull StringFBuffer prependIfMissing(boolean ignoreCase, @NotNull CharSequence seq) {
    return StringLike.super.prependIfMissing(ignoreCase, seq);
  }

  @Override
  public synchronized @NotNull IntStream chars() {
    return origin.chars();
  }
  
  @Override
  public synchronized @NotNull IntStream codePoints() {
    return origin.codePoints();
  }
  
  @Override
  public synchronized int compareTo(@NotNull StringFBuffer another) {
    if (this == another) {
      return 0;
    }
    return origin.compareTo(another.origin);
  }
  
  @Override
  public synchronized @NotNull StringFBuffer firstUp() {
    return StringLike.super.firstUp();
  }

  @Override
  public synchronized @NotNull StringFBuffer firstDown() {
    return StringLike.super.firstDown();
  }

  @Override
  public synchronized @NotNull StringFBuffer camelCase() {
    return StringLike.super.camelCase();
  }
  
  public synchronized @NotNull StringFBuffer toLowerCase() {
    return StringLike.super.toLowerCase();
  }

  public synchronized @NotNull StringFBuffer toUpperCase() {
    return StringLike.super.toUpperCase();
  }

  public synchronized @NotNull StringFBuffer replaceAll(@NotNull Map<String, String> replacements) {
    return StringLike.super.replaceAll(replacements);
  }

  public synchronized @NotNull StringFBuffer replaceAll(@NotNull Map<String, String> replacements, String fmt) {
    return StringLike.super.replaceAll(replacements, fmt);
  }

  public synchronized @NotNull StringFBuffer replaceRegions(@NotNull String open, @NotNull String replacement) {
    return StringLike.super.replaceRegions(open, replacement);
  }

  public synchronized @NotNull StringFBuffer replaceRegions(@NotNull String open, String close, @NotNull String replacement) {
    return StringLike.super.replaceRegions(open, close, replacement);
  }

  public synchronized @NotNull StringFBuffer replaceRegions(@NotNull String open, @NotNull Function<String, CharSequence> replacement) {
    return StringLike.super.replaceRegions(open, replacement);
  }
  
  public synchronized @NotNull StringFBuffer replaceRegions(@NotNull String open, String close, @NotNull Function<String, CharSequence> replacement) {
    return StringLike.super.replaceRegions(open, close, replacement);
  }

  public synchronized @NotNull StringFBuffer appendFilling(@Min(1) int count, char ch) {
    return StringLike.super.appendFilling(count, ch);
  }
  
  private synchronized void writeObject(ObjectOutputStream s) throws IOException {
    s.writeObject(origin);
  }

  private synchronized void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    origin = (StringBuffer) s.readObject();
  }

} /* ENDCLASS */

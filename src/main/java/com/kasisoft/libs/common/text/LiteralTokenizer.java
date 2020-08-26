package com.kasisoft.libs.common.text;

import javax.validation.constraints.NotNull;
import java.util.Enumeration;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * This tokenizer operates similar to the well known StringTokenizer class with the distinction that a complete literal 
 * can be used for the tokenization process.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LiteralTokenizer implements Enumeration<String> {

  int            pos;
  String[]       literals;
  StringLike     input;
  boolean        doreturn;
  String         next;
  
  /**
   * Prepares this tokenizer to operate using delimiting literals.
   * 
   * @param data             The String content that has to be tokenized.
   * @param delimiters       A list of delimiting literals.
   */
  public LiteralTokenizer(@NotNull String data, @NotNull String ... delimiters) {
    this(data, false, delimiters);
  }

  /**
   * Prepares this tokenizer to operate using delimiting literals.
   * 
   * @param data             The String content that has to be tokenized.
   * @param delimiters       A list of delimiting literals.
   */
  public LiteralTokenizer(@NotNull StringLike data, @NotNull String ... delimiters) {
    this(new StringFBuilder(data), false, delimiters);
  }

  /**
   * Prepares this tokenizer to operate using delimiting literals.
   * 
   * @param data             The String content that has to be tokenized.
   * @param returnliterals   <code>true</code> <=> Return delimiting literals as well.
   * @param delimiters       A list of delimiting literals.
   */
  public LiteralTokenizer(@NotNull String data, boolean returnLiterals, @NotNull String ... delimiters) {
    this(new StringFBuilder(data), returnLiterals, delimiters);
  }

  /**
   * Prepares this tokenizer to operate using delimiting literals.
   * 
   * @param data             The String content that has to be tokenized.
   * @param returnLiterals   <code>true</code> <=> Return delimiting literals as well.
   * @param delimiters       A list of delimiting literals.
   */
  public LiteralTokenizer(@NotNull StringLike data, boolean returnLiterals, @NotNull String ... delimiters) {
    input         = data;
    literals      = delimiters;
    doreturn      = returnLiterals;
    pos           = 0;
    next          = getNext();
  }

  @Override
  public boolean hasMoreElements() {
    return next != null;
  }

  @Override
  public String nextElement() {
    if (next == null) {
      return null;
    }
    var result = next;
    next       = getNext();
    return result;
  }
  
  /**
   * Returns the next literal that has to be returned by this tokenizer.
   * 
   * @return   The next literal that has to be returned by this tokenizer.
   */
  private String getNext() {
    if (pos == -1) {
      // there's no more content
      return null;
    }
    
    var firstdelimiter = firstDelimiter();
    var oldpos         = pos;
    
    if (firstdelimiter == null) {
      // there are no longer delimiting literals, so the rest becomes the next value
      pos = -1;
      return input.substring(oldpos);
    }
    
    var newpos = input.indexOf(firstdelimiter, pos);
    if (newpos == pos) {
      // we're directly pointing to a delimiter
      if (doreturn) {
        // the user wants to get the delimiting literal
        newpos = pos + firstdelimiter.length();
        pos    = newpos;
        if (pos >= input.length()) {
          pos = -1;
        }
        return input.substring(oldpos, newpos);
      } else {
        // the user wants to skip delimiting literals, so try to get the next literal after the delimiter
        pos = pos + firstdelimiter.length();
        if (pos >= input.length()) {
          pos = -1;
        }
        return getNext();
      }
    } else {
      pos = newpos;
      return input.substring(oldpos, newpos);
    }
  }
  
  /**
   * Returns the delimiting literal that will be detected first.
   * 
   * @return   The delimiting literal that will be detected first. 
   */
  private String firstDelimiter() {
    String result = null;
    var    next   = Integer.MAX_VALUE;
    for (var literal : literals) {
      var newnext = input.indexOf(literal, pos);
      if ((newnext < next) && (newnext >= pos)) {
        next    = newnext;
        result  = literal;
      }
    }
    return result;
  }

} /* ENDCLASS */

package com.kasisoft.libs.common.text;

import lombok.experimental.*;

import lombok.*;

import java.util.*;

/**
 * This tokenizer operates similar to the well known StringTokenizer class with the distinction that a complete literal 
 * can be used for the tokenization process.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LiteralTokenizer implements Enumeration<String> {

  int        pos;
  String[]   literals;
  String     input;
  boolean    doreturn;
  String     next;
  
  /**
   * Prepares this tokenizer to operate using delimiting literals.
   * 
   * @param data             The String content that has to be tokenized. Not <code>null</code>.
   * @param delimiters       A list of delimiting literals. Not <code>null</code>.
   */
  public LiteralTokenizer( String data, String ... delimiters ) {
    this( data, false, delimiters );
  }

  /**
   * Prepares this tokenizer to operate using delimiting literals.
   * 
   * @param data             The String content that has to be tokenized. Not <code>null</code>.
   * @param returnliterals   <code>true</code> <=> Return delimiting literals as well.
   * @param delimiters       A list of delimiting literals. Not <code>null</code>.
   */
  public LiteralTokenizer( @NonNull String data, boolean returnliterals, @NonNull String ... delimiters ) {
    input         = data;
    literals      = delimiters;
    doreturn      = returnliterals;
    pos           = 0;
    next          = getNext();
  }
  
  @Override
  public boolean hasMoreElements() {
    return next != null;
  }

  @Override
  public String nextElement() {
    if( next == null ) {
      return null;
    }
    String result = next;
    next          = getNext();
    return result;
  }
  
  /**
   * Returns the next literal that has to be returned by this tokenizer.
   * 
   * @return   The next literal that has to be returned by this tokenizer. Maybe <code>null</code>.
   */
  private String getNext() {
    if( pos == -1 ) {
      // there's no more content
      return null;
    }
    String firstdelimiter = firstDelimiter();
    int    oldpos         = pos;
    if( firstdelimiter == null ) {
      // there are no longer delimiting literals, so the rest becomes the next value
      pos = -1;
      return input.substring( oldpos );
    }
    int newpos = input.indexOf( firstdelimiter, pos );
    if( newpos == pos ) {
      // we're directly pointing to a delimiter
      if( doreturn ) {
        // the user wants to get the delimiting literal
        newpos = pos + firstdelimiter.length();
        pos    = newpos;
        if( pos >= input.length() ) {
          pos = -1;
        }
        return input.substring( oldpos, newpos );
      } else {
        // the user wants to skip delimiting literals, so try to get the next literal after the delimiter
        pos = pos + firstdelimiter.length();
        if( pos >= input.length() ) {
          pos = -1;
        }
        return getNext();
      }
    } else {
      pos = newpos;
      return input.substring( oldpos, newpos );
    }
  }
  
  /**
   * Returns the delimiting literal that will be detected first.
   * 
   * @return   The delimiting literal that will be detected first. Maybe <code>null</code>.
   */
  private String firstDelimiter() {
    String result = null;
    int    next   = Integer.MAX_VALUE;
    for( String literal : literals ) {
      int newnext = input.indexOf( literal, pos );
      if( (newnext < next) && (newnext >= pos) ) {
        next    = newnext;
        result  = literal;
      }
    }
    return( result );
  }

} /* ENDCLASS */

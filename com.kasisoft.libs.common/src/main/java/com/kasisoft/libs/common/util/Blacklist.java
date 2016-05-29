package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.base.*;

import com.kasisoft.libs.common.io.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.*;

import java.net.*;

import java.nio.file.*;

import java.io.*;

/**
 * A simple helper allowing to manage blacklists.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(of = {"list", "commentPrefix"})
@EqualsAndHashCode(of = {"list", "commentPrefix"})
public class Blacklist implements Predicate<String>, Function<CharSequence,StringBuilder> {
  
  static final Comparator<String> LENGTH_COMPARATOR = new Comparator<String>() {

    @Override
    public int compare( String o1, String o2 ) {
      Integer len1   = o1.length();
      Integer len2   = o2.length();
      int     result = len1.compareTo( len2 );
      if( result == 0 ) {
        result = o1.compareTo( o2 );
      }
      return result;
    }
    
  };
  
  List<String>          list;
  String                commentPrefix;
  Consumer<Exception>   errorHandler;
  
  /**
   * Sets up an empty blacklist.
   */
  public Blacklist() {
    list          = new ArrayList<>();
    commentPrefix = "#";
    errorHandler  = FailureException::errorHandler;
  }

  /**
   * Sets up an {@link Blacklist} instance initialized using the supplied resource.
   * 
   * @param url   The location of the resource. Not <code>null</code>.
   */
  public Blacklist( URL url ) {
    this();
    IoFunctions.forReaderDo( url, this::load );
  }

  /**
   * Sets up an {@link Blacklist} instance initialized using the supplied resource.
   * 
   * @param classpath   The location of a classpath resource. Neither <code>null</code> nor empty.
   */
  public Blacklist( String classpath ) {
    this();
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    if( cl == null ) {
      cl = getClass().getClassLoader();
    }
    URL url = cl.getResource( classpath );
    IoFunctions.forReaderDo( url, this::load );
  }

  /**
   * Sets up an {@link Blacklist} instance initialized using the supplied resource.
   * 
   * @param path   The location of the resource. Not <code>null</code>.
   */
  public Blacklist( Path path ) {
    this();
    IoFunctions.forReaderDo( path, this::load );
  }
  
  /**
   * Adds the supplied value to the black list.
   * 
   * @param blacklist   The new value to be used for black listing. Neither <code>null</code> nor empty. 
   */
  public void add( @NonNull String blacklist ) {
    String value = StringFunctions.cleanup( blacklist );
    if( value != null ) {
      list.add( value );
    }
  }

  /**
   * Changes the default error handler.
   * 
   * @param handler   The new default error handler. Maybe <code>null</code>.
   */
  public synchronized void setErrorHandler( Consumer<Exception> handler ) {
    errorHandler = handler;
  }
  
  /**
   * Changes the comment prefix.
   * 
   * @param prefix   The prefix to be used for comments. If blank the default <i>#</i> is being used.
   */
  public synchronized void setCommentPrefix( String prefix ) {
    commentPrefix = prefix;
    if( (commentPrefix == null) || commentPrefix.isEmpty() ) {
      commentPrefix = "#";
    }
  }
  
  /**
   * Returns the currently used comment prefix.
   * 
   * @return   The currently used comment prefix. Neither <code>null</code> nor empty.
   */
  public String getCommentPrefix() {
    return commentPrefix;
  }
  
  /**
   * Loads the blacklist supplied by the supplied reader.
   * 
   * @param reader   The reader providing the blacklisted content. Not <code>null</code>.
   */
  public synchronized void load( @NonNull Reader reader ) {
    BufferedReader buffered = IoFunctions.newBufferedReader( reader );
    try {
      String line   = buffered.readLine();
      while( line != null ) {
        line = StringFunctions.cleanup( line );
        if( (line != null) && (!line.startsWith( commentPrefix )) ) {
          list.add( line );
        }
        line = buffered.readLine();
      }
      Collections.sort( list, LENGTH_COMPARATOR );
    } catch( Exception ex ) {
      if( errorHandler != null ) {
        errorHandler.accept( ex );
      }
    }
  }
  
  /** 
   * Resets the inner state of this black listing class.
   */
  public synchronized void reset() {
    list.clear();
  }


  public Predicate<String> startsWith() {
    return this::testStartsWith;
  }
  
  public Predicate<String> endsWith() {
    return this::testEndsWith;
  }

  @Override
  public synchronized boolean test( String t ) {
    return list.contains(t);
  }

  private synchronized boolean testStartsWith( String t ) {
    for( String entry : list ) {
      if( t.startsWith( entry ) ) {
        return true;
      }
    }
    return false;
  }

  private synchronized boolean testEndsWith( String t ) {
    for( String entry : list ) {
      if( t.endsWith( entry ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public synchronized StringBuilder apply( CharSequence t ) {
    StringBuilder result = new StringBuilder();
    result.append(t);
    for( String entry : list ) {
      removeAll( result, entry );
    }
    return result;
  }
  
  private void removeAll( StringBuilder builder, String literal ) {
    int idx = builder.indexOf( literal );
    while( idx != -1 ) {
      builder.delete( idx, idx + literal.length() );
      idx = builder.indexOf( literal );
    }
  }
  
} /* ENDCLASS */

package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.base.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.text.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.*;

import java.net.*;

import java.nio.file.*;

import java.io.*;

/**
 * A simple helper allowing to manage blacklists. It obviously can be used as a whitelist. 
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(of = {"list", "commentPrefix"})
@EqualsAndHashCode(of = {"list", "commentPrefix"})
public class Blacklist implements Predicate<String> {
  
  static final Comparator<String> LENGTH_COMPARATOR = new Comparator<String>() {

    @Override
    public int compare( String o1, String o2 ) {
      Integer len1   = o1.length();
      Integer len2   = o2.length();
      int     result = len1.compareTo( len2 );
      if( result == 0 ) {
        result = o1.compareTo( o2 );
      }
      return -result;
    }
    
  };
  
  List<String>          list;
  List<String>          lowercaseList;
  String                commentPrefix;
  Consumer<Exception>   errorHandler;
  
  /**
   * Sets up an empty blacklist.
   */
  public Blacklist() {
    list          = new ArrayList<>();
    lowercaseList = new ArrayList<>();
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
    load( url );
  }

  /**
   * Sets up an {@link Blacklist} instance initialized using the supplied resource.
   * 
   * @param classpath   The location of a classpath resource. Neither <code>null</code> nor empty.
   */
  public Blacklist( String classpath ) {
    this();
    load( classpath );
  }

  /**
   * Sets up an {@link Blacklist} instance initialized using the supplied resource.
   * 
   * @param path   The location of the resource. Not <code>null</code>.
   */
  public Blacklist( Path path ) {
    this();
    load( path );
  }
  
  /**
   * Returns an unmodifiable list of blacklist entries.
   * 
   * @return   An unmodifiable list of blacklist entries. Not <code>null</code>.
   */
  public List<String> getBlacklisted() {
    return Collections.unmodifiableList( list );
  }
  
  /**
   * Adds the supplied value to the black list.
   * 
   * @param blacklisted   The new value to be used for black listing. Neither <code>null</code> nor empty. 
   */
  public void add( @NonNull String blacklisted ) {
    String value = StringFunctions.cleanup( blacklisted );
    if( value != null ) {
      int idx = Collections.binarySearch( list, value, LENGTH_COMPARATOR );
      if( idx < 0 ) {
        idx = -idx - 1;
        list.add( idx, value );
        lowercaseList.add( idx, value.toLowerCase() );
      }
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
   * @return   The currently used comment prefix. Not blank.
   */
  public String getCommentPrefix() {
    return commentPrefix;
  }

  /**
   * Loads the blacklist from the supplied url.
   * 
   * @param url   The URL pointing to the blacklist. Not <code>null</code>.
   */
  public synchronized void load( @NonNull URL url ) {
    IoFunctions.forReaderDo( url, this::load );
  }

  /**
   * Loads the blacklist from a resource on the classpath.
   * 
   * @param resource   A blacklist resource on the classpath. Not blank.
   */
  public synchronized void load( @NonNull String resource ) {
    URL url = MiscFunctions.getResource( getClass(), resource );
    IoFunctions.forReaderDo( url, this::load );
  }
  
  /**
   * Loads the blacklist from a specific filesystem location.
   * 
   * @param path   The filesystem location of the blacklist. Not <code>null</code>.
   */
  public synchronized void load( @NonNull Path path ) {
    IoFunctions.forReaderDo( path, this::load );
  }
  
  /**
   * Loads the blacklist supplied by the reader.
   * 
   * @param reader   The reader providing the blacklisted content. Not <code>null</code>.
   */
  public synchronized void load( @NonNull Reader reader ) {
    try( BufferedReader buffered = IoFunctions.newBufferedReader( reader ) ) {
      String line   = buffered.readLine();
      while( line != null ) {
        if( ! line.startsWith( commentPrefix ) ) {
          add( line );
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

  /**
   * Returns <code>true</code> if the supplied literal is blacklisted.
   * 
   * @param t   The text that shall be tested. Maybe <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied text is blacklisted.
   */
  @Override
  public synchronized boolean test( String t ) {
    if( t != null ) {
      return list.contains(t);
    }
    return false;
  }
  
  /**
   * Returns a predicate like {@link #test(String)} with the difference that the test ignores case sensitivity.
   * 
   * @return   A test ignoring case sensitivity. Not <code>null</code>.
   */
  public Predicate<String> testIgnoreCase() {
    return this::testCI;
  }

  /**
   * Implements the predicate returned by {@link #testIgnoreCase()}.
   * 
   * @param t   The text that is supposed to be tested. Maybe <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied text is blacklisted.
   */
  private synchronized boolean testCI( String t ) {
    if( t != null ) {
      String candidate = t.toLowerCase();
      return lowercaseList.contains( candidate );
    } else {
      return false;
    }
  }

  /**
   * Returns a startsWith test which checks whether a text starts with a blacklisted literal.
   * 
   * @param ignorecase   <code>true</code> <=> Ignore case.
   * 
   * @return   A startsWith test for this blacklist. Not <code>null</code>.
   */
  public Predicate<String> startsWith( boolean ignorecase ) {
    return ignorecase ? this::testStartsWithCI : this::testStartsWithCS;
  }

  /**
   * Returns a startsWith test which checks whether a text starts with a blacklisted literal (case sensitive).
   * 
   * @return   A startsWith test for this blacklist. Not <code>null</code>.
   */
  public Predicate<String> startsWith() {
    return startsWith( false );
  }
  
  /**
   * Implements the predicate returned by {@link #startsWith(boolean)} (case sensitive).
   * 
   * @param t   The text that is supposed to be tested. Maybe <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied text starts with a blacklisted literal.
   */
  private synchronized boolean testStartsWithCS( String t ) {
    if( t != null ) {
      return test( list, t::startsWith );
    }
    return false;
  }

  /**
   * Implements the predicate returned by {@link #startsWith(boolean)} (case insensitive).
   * 
   * @param t   The text that is supposed to be tested. Maybe <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied text starts with a blacklisted literal.
   */
  private synchronized boolean testStartsWithCI( String t ) {
    if( t != null ) {
      String candidate = t.toLowerCase();
      return test( lowercaseList, candidate::startsWith );
    }
    return false;
  }

  /**
   * Returns an endsWith test which checks whether a text ends with a blacklisted literal.  
   * 
   * @param ignorecase   <code>true</code> <=> Ignore case.
   * 
   * @return   A endsWith test for this blacklist. Not <code>null</code>.
   */
  public Predicate<String> endsWith( boolean ignorecase ) {
    return ignorecase ? this::testEndsWithCI : this::testEndsWithCS;
  }
  
  /**
   * Returns an endsWith test which checks whether a text ends with a blacklisted literal  (case sensitive).  
   * 
   * @return   A endsWith test for this blacklist. Not <code>null</code>.
   */
  public Predicate<String> endsWith() {
    return endsWith( false );
  }

  /**
   * Implements the predicate returned by {@link #endsWith(boolean)} (case sensitive).
   * 
   * @param t   The text that is supposed to be tested. Maybe <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied text ends with a blacklisted literal.
   */
  private synchronized boolean testEndsWithCS( String t ) {
    if( t != null ) {
      return test( list, t::endsWith );
    }
    return false;
  }

  /**
   * Implements the predicate returned by {@link #endsWith(boolean)} (case insensitive).
   * 
   * @param t   The text that is supposed to be tested. Maybe <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied text ends with a blacklisted literal.
   */
  private synchronized boolean testEndsWithCI( String t ) {
    if( t != null ) {
      String candidate = t.toLowerCase();
      return test( lowercaseList, candidate::endsWith );
    }
    return false;
  }

  /**
   * Returns a contains test which checks whether a text contains a blacklisted literal.
   * 
   * @param ignorecase   <code>true</code> <=> Ignore case.
   * 
   * @return   A contains test for this blacklist. Not <code>null</code>.
   */
  public Predicate<String> contains( boolean ignorecase ) {
    return ignorecase ? this::testContainsCI : this::testContainsCS;
  }

  /**
   * Returns a contains test which checks whether a text contains a blacklisted literal.
   * 
   * @return   A contains test for this blacklist. Not <code>null</code>.
   */
  public Predicate<String> contains() {
    return contains( false );
  }

  /**
   * Implements the predicate returned by {@link #contains(boolean)} (case sensitive).
   * 
   * @param t   The text that is supposed to be tested. Maybe <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied text contains a blacklisted literal.
   */
  private synchronized boolean testContainsCS( String t ) {
    if( t != null ) {
      return test( list, t::contains );
    }
    return false;
  }

  /**
   * Implements the predicate returned by {@link #contains(boolean)} (case insensitive).
   * 
   * @param t   The text that is supposed to be tested. Maybe <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied text contains a blacklisted literal.
   */
  private synchronized boolean testContainsCI( String t ) {
    if( t != null ) {
      String candidate = t.toLowerCase();
      return test( lowercaseList, candidate::contains );
    }
    return false;
  }

  /**
   * Returns <code>true</code> if the supplied predicate matches for at least one list entry.
   * 
   * @param list   The list entries that might be matched. Not <code>null</code>.
   * @param test   The predicate used for the test. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The predicate matched one entry.
   */
  private boolean test( List<String> list, Predicate<String> test ) {
    for( String element : list ) {
      if( test.test( element ) ) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns a function that converts a CharSequence into a StringBuilder while dropping the blacklisted portions.
   * This function is case sensitive.
   * 
   * @return   A function that converts a CharSequence into a StringBuilder while dropping the blacklisted portions. 
   *           Not <code>null</code>.
   */
  public <T extends CharSequence> Function<T, StringBuilder> cleanup() {
    return cleanup( false );
  }

  /**
   * Returns a function that converts a CharSequence into a StringBuilder while dropping the blacklisted portions.
   *
   * @param ignorecase   <code>true</code> <=> Ignore case sensitivity.
   * 
   * @return   A function that converts a CharSequence into a StringBuilder while dropping the blacklisted portions. 
   *           Not <code>null</code>.
   */
  public <T extends CharSequence> Function<T, StringBuilder> cleanup( boolean ignorecase ) {
    return cleanup( ignorecase, null );
  }

  /**
   * Returns a function that converts a CharSequence into a StringBuilder while dropping the blacklisted portions.
   * This function is case sensitive.
   * 
   * @param statistic   This Consumer instance allows to react on each detected blacklisted literal. 
   *                    Maybe <code>null</code>.
   * 
   * @return   A function that converts a CharSequence into a StringBuilder while dropping the blacklisted portions. 
   *           Not <code>null</code>.
   */
  public <T extends CharSequence> Function<T, StringBuilder> cleanup( Consumer<String> statistic ) {
    return cleanup( false, statistic );
  }
  
  /**
   * Returns a function that converts a CharSequence into a StringBuilder while dropping the blacklisted portions.
   * 
   * @param ignorecase   <code>true</code> <=> Ignore case sensitivity.
   * @param statistic    This Consumer instance allows to react on each detected blacklisted literal. 
   *                     Maybe <code>null</code>.
   * 
   * @return   A function that converts a CharSequence into a StringBuilder while dropping the blacklisted portions. 
   *           Not <code>null</code>.
   */
  public <T extends CharSequence> Function<T, StringBuilder> cleanup( boolean ignorecase, Consumer<String> statistic ) {
    return ignorecase ? $ -> applyCI( $, statistic ) : $ -> applyCS( $, statistic );
  }

  /**
   * Drops all black listed elements from the supplied sequence (case insensitive).
   * 
   * @param t           The text that shall be freed from blacklisted elements. Maybe <code>null</code>.
   * @param statistic   An optional Consumer to be notified when a blacklisted element had been detected.
   *                    Maybe <code>null</code>.
   * 
   * @return   A StringBuilder instance providing the cleansed text. Not <code>null</code> and may be altered 
   *           by the caller.
   */
  private synchronized StringBuilder applyCI( CharSequence t, Consumer<String> statistic ) {
    StringBuilder result = new StringBuilder();
    if( t != null ) {
      if( statistic == null ) {
        statistic = this::noStatistic;
      }
      result.append(t);
      StringBuilder lower = new StringBuilder( t.toString().toLowerCase() );
      for( String entry : lowercaseList ) {
        removeAll( lower, result, entry, statistic );
      }
    }
    return result;
  }

  /**
   * Drops all black listed elements from the supplied sequence (case sensitive).
   * 
   * @param t           The text that shall be freed from blacklisted elements. Maybe <code>null</code>.
   * @param statistic   An optional Consumer to be notified when a blacklisted element had been detected.
   *                    Maybe <code>null</code>.
   * 
   * @return   A StringBuilder instance providing the cleansed text. Not <code>null</code> and may be altered 
   *           by the caller.
   */
  private synchronized StringBuilder applyCS( CharSequence t, Consumer<String> statistic ) {
    StringBuilder result = new StringBuilder();
    if( t != null ) {
      if( statistic == null ) {
        statistic = this::noStatistic;
      }
      result.append(t);
      for( String entry : list ) {
        removeAll( result, null, entry, statistic );
      }
    }
    return result;
  }

  /**
   * Removes all occurrences of a literal within the supplied StringBuilder instances.
   *
   * @param builder1   The StringBuilder instance which contains the textual representation used to locate
   *                   the literals. Not <code>null</code>.
   * @param builder2   The StringBuilder instance which will be altered accordingly. Maybe <code>null</code>.
   * @param statistic   An optional Consumer to be notified when a blacklisted element had been detected.
   */
  private void removeAll( StringBuilder builder1, StringBuilder builder2, String literal, Consumer<String> statistic ) {
    int idx = builder1.lastIndexOf( literal );
    while( idx != -1 ) {
      remover( builder1, builder2, idx, literal, statistic );
      idx = builder1.lastIndexOf( literal );
    }
  }

  /**
   * Removes one occurrence of a literal.
   * 
   * @param builder1    The StringBuilder instance which contains the textual representation used to locate
   *                    the literals. Not <code>null</code>.
   * @param builder2    The StringBuilder instance which will be altered accordingly. Maybe <code>null</code>.
   * @param idx         The index where the literal had been located.
   * @param literal     The literal that had been detected. Not blank.
   * @param statistic   An optional Consumer to be notified when a blacklisted element had been detected.
   */
  private void remover( StringBuilder builder1, StringBuilder builder2, int idx, String literal, Consumer<String> statistic ) {
    builder1.delete( idx, idx + literal.length() );
    if( builder2 != null ) {
      builder2.delete( idx, idx + literal.length() );
    }
    statistic.accept( literal );
  }
  
  @SuppressWarnings("unused")
  private void noStatistic( String literal ) {
  }
  
} /* ENDCLASS */

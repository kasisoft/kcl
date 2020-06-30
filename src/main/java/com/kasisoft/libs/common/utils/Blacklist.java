package com.kasisoft.libs.common.utils;

import com.kasisoft.libs.common.constants.Empty;
import com.kasisoft.libs.common.constants.Encoding;

import com.kasisoft.libs.common.io.IoFunctions;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.comparator.Comparators;
import com.kasisoft.libs.common.text.StringFBuilder;
import com.kasisoft.libs.common.text.StringFunctions;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.net.URI;
import java.net.URL;

import java.nio.file.Path;

import java.io.File;
import java.io.Reader;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * A simple helper allowing to manage blacklists. It obviously can be used as a whitelist. 
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(of = {"list", "commentPrefix"})
@EqualsAndHashCode(of = {"list", "commentPrefix"})
public class Blacklist implements Predicate<String> {
  
  List<String>          list;
  List<String>          lowercaseList;
  String                commentPrefix;
  
  /**
   * Sets up an empty blacklist.
   */
  public Blacklist() {
    list          = new ArrayList<>();
    lowercaseList = new ArrayList<>();
    commentPrefix = "#";
  }

  public Blacklist(@NotNull Path path) {
    this();
    load(path);
  }

  public Blacklist(@NotNull Path path, @Null Encoding encoding) {
    this();
    load(path, encoding);
  }

  public Blacklist(@NotNull URL url) {
    this();
    load(url);
  }

  public Blacklist(@NotNull URL url, @Null Encoding encoding) {
    this();
    load(url, encoding);
  }

  /**
   * Returns an unmodifiable list of blacklist entries.
   * 
   * @return   An unmodifiable list of blacklist entries.
   */
  public @NotNull List<@NotNull String> getBlacklisted() {
    return Collections.unmodifiableList(list);
  }
  
  /**
   * Adds the supplied value to the black list.
   * 
   * @param blacklisted   The new value to be used for black listing. 
   */
  public void add(@Null String blacklisted ) {
    var value = StringFunctions.cleanup(blacklisted);
    if (value != null) {
      var idx = Collections.binarySearch(list, value, Comparators.LENGTH_LONGEST_FIRST);
      if (idx < 0) {
        idx = -idx - 1;
        list.add(idx, value);
        lowercaseList.add(idx, value.toLowerCase());
      }
    }
  }

  /**
   * Changes the comment prefix.
   * 
   * @param prefix   The prefix to be used for comments. If blank the default <i>#</i> is being used.
   */
  public synchronized void setCommentPrefix(@Null String prefix) {
    commentPrefix = StringFunctions.cleanup(prefix);
    if (commentPrefix == null) {
      commentPrefix = "#";
    }
  }
  
  /**
   * Returns the currently used comment prefix.
   * 
   * @return   The currently used comment prefix. Not blank.
   */
  public synchronized @NotNull String getCommentPrefix() {
    return commentPrefix;
  }

  public synchronized Blacklist load(@NotNull Path source) {
    return IoFunctions.forReader(source, null, this::load);
  }
  
  public synchronized Blacklist load(@NotNull Path source, @Null Encoding encoding) {
    return IoFunctions.forReader(source, encoding, this::load);
  }

  public synchronized Blacklist load(@NotNull URL source) {
    return IoFunctions.forReader(source, null, this::load);
  }
  
  public synchronized Blacklist load(@NotNull URL source, @Null Encoding encoding) {
    return IoFunctions.forReader(source, encoding, this::load);
  }

  public synchronized Blacklist load(@NotNull File source) {
    return IoFunctions.forReader(source, null, this::load);
  }
  
  public synchronized Blacklist load(@NotNull File source, @Null Encoding encoding) {
    return IoFunctions.forReader(source, encoding, this::load);
  }

  public synchronized Blacklist load(@NotNull URI source) {
    return IoFunctions.forReader(source, null, this::load);
  }
  
  public synchronized Blacklist load(@NotNull URI source, @Null Encoding encoding) {
    return IoFunctions.forReader(source, encoding, this::load);
  }

  /**
   * Loads the blacklist supplied by the reader.
   * 
   * @param reader   The reader providing the blacklisted content. Not <code>null</code>.
   */
  public synchronized Blacklist load(@NotNull Reader reader) {
    try (var buffered = IoFunctions.newBufferedReader(reader)) {
      var line = buffered.readLine();
      while (line != null) {
        line = StringFunctions.cleanup(line);
        if (line != null) {
          if (!line.startsWith(commentPrefix)) {
            add(line);
          }
        }
        line = buffered.readLine();
      }
      Collections.sort(list, Comparators.LENGTH_LONGEST_FIRST);
    } catch (Exception ex) {
      throw KclException.wrap(ex, "Failed to load blacklist !");
    }
    return this;
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
   * @param t   The text that shall be tested.
   * 
   * @return   <code>true</code> <=> The supplied text is blacklisted.
   */
  @Override
  public synchronized boolean test(@Null String t) {
    if (t != null) {
      return list.contains(t);
    }
    return false;
  }
  
  /**
   * Returns a predicate like {@link #test(String)} with the difference that the test ignores case sensitivity.
   * 
   * @return   A test ignoring case sensitivity.
   */
  public @NotNull Predicate<String> testIgnoreCase() {
    return this::testCI;
  }

  /**
   * Implements the predicate returned by {@link #testIgnoreCase()}.
   * 
   * @param t   The text that is supposed to be tested. Maybe <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied text is blacklisted.
   */
  private synchronized boolean testCI(@Null String t) {
    if (t != null) {
      return lowercaseList.contains(t.toLowerCase());
    }
    return false;
  }

  /**
   * Returns a startsWith test which checks whether a text starts with a blacklisted literal.
   * 
   * @param ignorecase   <code>true</code> <=> Ignore case.
   * 
   * @return   A startsWith test for this blacklist.
   */
  public @NotNull Predicate<@Null String> startsWith(boolean ignorecase) {
    return ignorecase ? this::testStartsWithCI : this::testStartsWith;
  }

  /**
   * Returns a startsWith test which checks whether a text starts with a blacklisted literal (case sensitive).
   * 
   * @return   A startsWith test for this blacklist.
   */
  public @NotNull Predicate<@Null String> startsWith() {
    return startsWith(false);
  }
  
  /**
   * Implements the predicate returned by {@link #startsWith(boolean)} (case sensitive).
   * 
   * @param t   The text that is supposed to be tested. Maybe <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied text starts with a blacklisted literal.
   */
  private synchronized boolean testStartsWith(@Null String t) {
    if (t != null) {
      return test(list, t::startsWith);
    }
    return false;
  }

  /**
   * Implements the predicate returned by {@link #startsWith(boolean)} (case insensitive).
   * 
   * @param t   The text that is supposed to be tested.
   * 
   * @return   <code>true</code> <=> The supplied text starts with a blacklisted literal.
   */
  private synchronized boolean testStartsWithCI(@Null String t) {
    if (t != null) {
      return test(lowercaseList, t.toLowerCase()::startsWith);
    }
    return false;
  }

  /**
   * Returns an endsWith test which checks whether a text ends with a blacklisted literal.  
   * 
   * @param ignorecase   <code>true</code> <=> Ignore case.
   * 
   * @return   A endsWith test for this blacklist.
   */
  public @NotNull Predicate<@Null String> endsWith(boolean ignorecase) {
    return ignorecase ? this::testEndsWithCI : this::testEndsWith;
  }
  
  /**
   * Returns an endsWith test which checks whether a text ends with a blacklisted literal  (case sensitive).  
   * 
   * @return   A endsWith test for this blacklist.
   */
  public @NotNull Predicate<@Null String> endsWith() {
    return endsWith(false);
  }

  /**
   * Implements the predicate returned by {@link #endsWith(boolean)} (case sensitive).
   * 
   * @param t   The text that is supposed to be tested.
   * 
   * @return   <code>true</code> <=> The supplied text ends with a blacklisted literal.
   */
  private synchronized boolean testEndsWith(@Null String t) {
    if (t != null) {
      return test(list, t::endsWith);
    }
    return false;
  }

  /**
   * Implements the predicate returned by {@link #endsWith(boolean)} (case insensitive).
   * 
   * @param t   The text that is supposed to be tested.
   * 
   * @return   <code>true</code> <=> The supplied text ends with a blacklisted literal.
   */
  private synchronized boolean testEndsWithCI(@Null String t) {
    if (t != null) {
      return test(lowercaseList, t.toLowerCase()::endsWith);
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
  public @NotNull Predicate<@Null String> contains(boolean ignorecase) {
    return ignorecase ? this::testContainsCI : this::testContains;
  }

  /**
   * Returns a contains test which checks whether a text contains a blacklisted literal.
   * 
   * @return   A contains test for this blacklist.
   */
  public @NotNull Predicate<@Null String> contains() {
    return contains(false);
  }

  /**
   * Implements the predicate returned by {@link #contains(boolean)} (case sensitive).
   * 
   * @param t   The text that is supposed to be tested. Maybe <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied text contains a blacklisted literal.
   */
  private synchronized boolean testContains(@Null String t) {
    if (t != null) {
      return test(list, t::contains);
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
  private synchronized boolean testContainsCI(@Null String t) {
    if (t != null) {
      return test(lowercaseList, t.toLowerCase()::contains);
    }
    return false;
  }

  /**
   * Returns <code>true</code> if the supplied predicate matches for at least one list entry.
   * 
   * @param list   The list entries that might be matched.
   * @param test   The predicate used for the test.
   * 
   * @return   <code>true</code> <=> The predicate matched one entry.
   */
  private boolean test(@NotNull List<String> list, @NotNull Predicate<@Null String> test) {
    for (var element : list) {
      if (test.test(element)) {
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
   */
  public <T extends CharSequence> @NotNull Function<T, StringFBuilder> cleanup() {
    return cleanup(false);
  }

  /**
   * Returns a function that converts a CharSequence into a StringBuilder while dropping the blacklisted portions.
   *
   * @param ignorecase   <code>true</code> <=> Ignore case sensitivity.
   * 
   * @return   A function that converts a CharSequence into a StringBuilder while dropping the blacklisted portions. 
   *           Not <code>null</code>.
   */
  public <T extends CharSequence> @NotNull Function<T, StringFBuilder> cleanup(boolean ignorecase) {
    return cleanup(ignorecase, null);
  }

  /**
   * Returns a function that converts a CharSequence into a StringBuilder while dropping the blacklisted portions.
   * This function is case sensitive.
   * 
   * @param statistic   This Consumer instance allows to react on each detected blacklisted literal. 
   * 
   * @return   A function that converts a CharSequence into a StringBuilder while dropping the blacklisted portions. 
   */
  public <T extends CharSequence> @NotNull Function<T, StringFBuilder> cleanup(@Null Consumer<String> statistic) {
    return cleanup(false, statistic);
  }
  
  /**
   * Returns a function that converts a CharSequence into a StringBuilder while dropping the blacklisted portions.
   * 
   * @param ignorecase   <code>true</code> <=> Ignore case sensitivity.
   * @param statistic    This Consumer instance allows to react on each detected blacklisted literal. 
   * 
   * @return   A function that converts a CharSequence into a StringBuilder while dropping the blacklisted portions. 
   *           Not <code>null</code>.
   */
  public <T extends CharSequence> @NotNull Function<T, StringFBuilder> cleanup(boolean ignorecase, @Null Consumer<String> statistic) {
    int regexFlags = ignorecase ? Pattern.CASE_INSENSITIVE : 0;
    return $ -> apply($, regexFlags, statistic);
  }

  /**
   * Drops all black listed elements from the supplied sequence (case insensitive).
   * 
   * @param t           The text that shall be freed from blacklisted elements.
   * @param statistic   An optional Consumer to be notified when a blacklisted element had been detected.
   * 
   * @return   A StringBuilder instance providing the cleansed text. Not <code>null</code> and may be altered 
   *           by the caller.
   */
  private synchronized @NotNull StringFBuilder apply(@Null CharSequence t, int regexFlags, @Null Consumer<String> statistic) {
    var result = new StringFBuilder();
    if (t != null) {
      if (statistic == null) {
        statistic = this::noStatistic;
      }
      Consumer<String> op = statistic;
      result.append(t);
      for (var entry : list) {
        result.replaceAll(Pattern.compile(Pattern.quote(entry), regexFlags), $ -> {
          op.accept($);
          return Empty.NO_STRING;
        });
      }
    }
    return result;
  }
 
  @SuppressWarnings("unused")
  private void noStatistic(String literal) {
  }
  
} /* ENDCLASS */

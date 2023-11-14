package com.kasisoft.libs.common.utils;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.comparator.Comparators;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.text.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.util.*;
import java.util.regex.Pattern;

import java.nio.file.*;

import java.net.*;

import java.io.*;

/**
 * A simple helper allowing to manage blacklists. It obviously can be used as a whitelist.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Blacklist implements Predicate<String> {

    private List<String> list;

    private List<String> lowercaseList;

    private String       commentPrefix;

    /**
     * Sets up an empty blacklist.
     */
    public Blacklist() {
        list          = new ArrayList<>();
        lowercaseList = new ArrayList<>();
        commentPrefix = "#";
    }

    public Blacklist(@NotNull Path path) {
        this(path, null);
    }

    public Blacklist(@NotNull Path path, Encoding encoding) {
        this();
        load(path, encoding);
    }

    public Blacklist(@NotNull URL url) {
        this(url, null);
    }

    public Blacklist(@NotNull URL url, Encoding encoding) {
        this();
        load(url, encoding);
    }

    public Blacklist(@NotNull File file) {
        this(file, null);
    }

    public Blacklist(@NotNull File file, Encoding encoding) {
        this();
        load(file, encoding);
    }

    public Blacklist(@NotNull URI uri) {
        this(uri, null);
    }

    public Blacklist(@NotNull URI uri, Encoding encoding) {
        this();
        load(uri, encoding);
    }

    /**
     * Returns an unmodifiable list of blacklist entries.
     *
     * @return An unmodifiable list of blacklist entries.
     */
    public @NotNull List<@NotNull String> getBlacklisted() {
        return Collections.unmodifiableList(list);
    }

    /**
     * Adds the supplied value to the black list.
     *
     * @param blacklisted
     *            The new value to be used for black listing.
     */
    public void add(String blacklisted) {
        var value = StringFunctions.cleanup(blacklisted);
        if (value != null) {
            var idx = Collections.binarySearch(list, value, Comparators.byStringLength(true));
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
     * @param prefix
     *            The prefix to be used for comments. If blank the default <i>#</i> is being used.
     */
    public synchronized void setCommentPrefix(String prefix) {
        commentPrefix = StringFunctions.cleanup(prefix);
        if (commentPrefix == null) {
            commentPrefix = "#";
        }
    }

    /**
     * Returns the currently used comment prefix.
     *
     * @return The currently used comment prefix. Not blank.
     */
    public synchronized @NotNull String getCommentPrefix() {
        return commentPrefix;
    }

    public synchronized Blacklist load(@NotNull Path source) {
        return IoFunctions.forReader(source, null, this::load);
    }

    public synchronized Blacklist load(@NotNull Path source, Encoding encoding) {
        return IoFunctions.forReader(source, encoding, this::load);
    }

    public synchronized Blacklist load(@NotNull URL source) {
        return IoFunctions.forReader(source, null, this::load);
    }

    public synchronized Blacklist load(@NotNull URL source, Encoding encoding) {
        return IoFunctions.forReader(source, encoding, this::load);
    }

    public synchronized Blacklist load(@NotNull File source) {
        return IoFunctions.forReader(source, null, this::load);
    }

    public synchronized Blacklist load(@NotNull File source, Encoding encoding) {
        return IoFunctions.forReader(source, encoding, this::load);
    }

    public synchronized Blacklist load(@NotNull URI source) {
        return IoFunctions.forReader(source, null, this::load);
    }

    public synchronized Blacklist load(@NotNull URI source, Encoding encoding) {
        return IoFunctions.forReader(source, encoding, this::load);
    }

    /**
     * Loads the blacklist supplied by the reader.
     *
     * @param reader
     *            The reader providing the blacklisted content.
     */
    public synchronized @NotNull Blacklist load(@NotNull Reader reader) {
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
            Collections.sort(list, Comparators.byStringLength(true));
        } catch (Exception ex) {
            throw KclException.wrap(ex, error_blacklist_loading_failure);
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
     * @param t
     *            The text that shall be tested.
     * @return <code>true</code> <=> The supplied text is blacklisted.
     */
    @Override
    public synchronized boolean test(String t) {
        if (t != null) {
            return list.contains(t);
        }
        return false;
    }

    /**
     * Returns a predicate like {@link #test(String)} with the difference that the test ignores case
     * sensitivity.
     *
     * @return A test ignoring case sensitivity.
     */
    public @NotNull Predicate<String> testIgnoreCase() {
        return this::testCI;
    }

    /**
     * Implements the predicate returned by {@link #testIgnoreCase()}.
     *
     * @param t
     *            The text that is supposed to be tested.
     * @return <code>true</code> <=> The supplied text is blacklisted.
     */
    private synchronized boolean testCI(String t) {
        if (t != null) {
            return lowercaseList.contains(t.toLowerCase());
        }
        return false;
    }

    /**
     * Returns a startsWith test which checks whether a text starts with a blacklisted literal.
     *
     * @param ignorecase
     *            <code>true</code> <=> Ignore case.
     * @return A startsWith test for this blacklist.
     */
    public @NotNull Predicate<String> startsWith(boolean ignorecase) {
        return ignorecase ? this::testStartsWithCI : this::testStartsWith;
    }

    /**
     * Returns a startsWith test which checks whether a text starts with a blacklisted literal (case
     * sensitive).
     *
     * @return A startsWith test for this blacklist.
     */
    public @NotNull Predicate<String> startsWith() {
        return startsWith(false);
    }

    /**
     * Implements the predicate returned by {@link #startsWith(boolean)} (case sensitive).
     *
     * @param t
     *            The text that is supposed to be tested.
     * @return <code>true</code> <=> The supplied text starts with a blacklisted literal.
     */
    private synchronized boolean testStartsWith(String t) {
        if (t != null) {
            return test(list, t::startsWith);
        }
        return false;
    }

    /**
     * Implements the predicate returned by {@link #startsWith(boolean)} (case insensitive).
     *
     * @param t
     *            The text that is supposed to be tested.
     * @return <code>true</code> <=> The supplied text starts with a blacklisted literal.
     */
    private synchronized boolean testStartsWithCI(String t) {
        if (t != null) {
            return test(lowercaseList, t.toLowerCase()::startsWith);
        }
        return false;
    }

    /**
     * Returns an endsWith test which checks whether a text ends with a blacklisted literal.
     *
     * @param ignorecase
     *            <code>true</code> <=> Ignore case.
     * @return A endsWith test for this blacklist.
     */
    public @NotNull Predicate<String> endsWith(boolean ignorecase) {
        return ignorecase ? this::testEndsWithCI : this::testEndsWith;
    }

    /**
     * Returns an endsWith test which checks whether a text ends with a blacklisted literal (case
     * sensitive).
     *
     * @return A endsWith test for this blacklist.
     */
    public @NotNull Predicate<String> endsWith() {
        return endsWith(false);
    }

    /**
     * Implements the predicate returned by {@link #endsWith(boolean)} (case sensitive).
     *
     * @param t
     *            The text that is supposed to be tested.
     * @return <code>true</code> <=> The supplied text ends with a blacklisted literal.
     */
    private synchronized boolean testEndsWith(String t) {
        if (t != null) {
            return test(list, t::endsWith);
        }
        return false;
    }

    /**
     * Implements the predicate returned by {@link #endsWith(boolean)} (case insensitive).
     *
     * @param t
     *            The text that is supposed to be tested.
     * @return <code>true</code> <=> The supplied text ends with a blacklisted literal.
     */
    private synchronized boolean testEndsWithCI(String t) {
        if (t != null) {
            return test(lowercaseList, t.toLowerCase()::endsWith);
        }
        return false;
    }

    /**
     * Returns a contains test which checks whether a text contains a blacklisted literal.
     *
     * @param ignorecase
     *            <code>true</code> <=> Ignore case.
     * @return A contains test for this blacklist.
     */
    public @NotNull Predicate<String> contains(boolean ignorecase) {
        return ignorecase ? this::testContainsCI : this::testContains;
    }

    /**
     * Returns a contains test which checks whether a text contains a blacklisted literal.
     *
     * @return A contains test for this blacklist.
     */
    public @NotNull Predicate<String> contains() {
        return contains(false);
    }

    /**
     * Implements the predicate returned by {@link #contains(boolean)} (case sensitive).
     *
     * @param t
     *            The text that is supposed to be tested.
     * @return <code>true</code> <=> The supplied text contains a blacklisted literal.
     */
    private synchronized boolean testContains(String t) {
        if (t != null) {
            return test(list, t::contains);
        }
        return false;
    }

    /**
     * Implements the predicate returned by {@link #contains(boolean)} (case insensitive).
     *
     * @param t
     *            The text that is supposed to be tested.
     * @return <code>true</code> <=> The supplied text contains a blacklisted literal.
     */
    private synchronized boolean testContainsCI(String t) {
        if (t != null) {
            return test(lowercaseList, t.toLowerCase()::contains);
        }
        return false;
    }

    /**
     * Returns <code>true</code> if the supplied predicate matches for at least one list entry.
     *
     * @param list
     *            The list entries that might be matched.
     * @param test
     *            The predicate used for the test.
     * @return <code>true</code> <=> The predicate matched one entry.
     */
    private boolean test(@NotNull List<String> list, @NotNull Predicate<String> test) {
        for (var element : list) {
            if (test.test(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a function that converts a CharSequence into a StringBuilder while dropping the
     * blacklisted portions. This function is case sensitive.
     *
     * @return A function that converts a CharSequence into a StringBuilder while dropping the
     *         blacklisted portions.
     */
    public <T extends CharSequence> @NotNull Function<T, StringBuilder> cleanup() {
        return cleanup(false);
    }

    /**
     * Returns a function that converts a CharSequence into a StringBuilder while dropping the
     * blacklisted portions.
     *
     * @param ignorecase
     *            <code>true</code> <=> Ignore case sensitivity.
     * @return A function that converts a CharSequence into a StringBuilder while dropping the
     *         blacklisted portions.
     */
    public <T extends CharSequence> @NotNull Function<T, StringBuilder> cleanup(boolean ignorecase) {
        return cleanup(ignorecase, null);
    }

    /**
     * Returns a function that converts a CharSequence into a StringBuilder while dropping the
     * blacklisted portions. This function is case sensitive.
     *
     * @param statistic
     *            This Consumer instance allows to react on each detected blacklisted literal.
     * @return A function that converts a CharSequence into a StringBuilder while dropping the
     *         blacklisted portions.
     */
    public <T extends CharSequence> @NotNull Function<T, StringBuilder> cleanup(Consumer<String> statistic) {
        return cleanup(false, statistic);
    }

    /**
     * Returns a function that converts a CharSequence into a StringBuilder while dropping the
     * blacklisted portions.
     *
     * @param ignorecase
     *            <code>true</code> <=> Ignore case sensitivity.
     * @param statistic
     *            This Consumer instance allows to react on each detected blacklisted literal.
     * @return A function that converts a CharSequence into a StringBuilder while dropping the
     *         blacklisted portions.
     */
    public <T extends CharSequence> @NotNull Function<T, StringBuilder> cleanup(boolean ignorecase, Consumer<String> statistic) {
        int regexFlags = ignorecase ? Pattern.CASE_INSENSITIVE : 0;
        return $ -> apply($, regexFlags, statistic);
    }

    /**
     * Drops all black listed elements from the supplied sequence (case insensitive).
     *
     * @param t
     *            The text that shall be freed from blacklisted elements.
     * @param statistic
     *            An optional Consumer to be notified when a blacklisted element had been detected.
     * @return A StringBuilder instance providing the cleansed text.
     */
    private synchronized @NotNull StringBuilder apply(CharSequence t, int regexFlags, Consumer<String> statistic) {
        var result = "";
        if (t != null) {
            if (statistic == null) {
                statistic = this::noStatistic;
            }
            Consumer<String> op = statistic;
            result = t.toString();
            for (var entry : list) {
                result = StringFunctions.replaceAll(result, Pattern.compile(Pattern.quote(entry), regexFlags), $ -> {
                    op.accept($);
                    return Empty.NO_STRING;
                });
            }
        }
        return new StringBuilder(result);
    }

    @SuppressWarnings("unused")
    private void noStatistic(String literal) {
    }

    @Override
    public int hashCode() {
        final int prime  = 31;
        int       result = 1;
        result = prime * result + ((commentPrefix == null) ? 0 : commentPrefix.hashCode());
        result = prime * result + ((list == null) ? 0 : list.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Blacklist other = (Blacklist) obj;
        if (commentPrefix == null) {
            if (other.commentPrefix != null)
                return false;
        } else if (!commentPrefix.equals(other.commentPrefix))
            return false;
        if (list == null) {
            if (other.list != null)
                return false;
        } else if (!list.equals(other.list))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Blacklist [list=" + list + ", commentPrefix=" + commentPrefix + "]";
    }

} /* ENDCLASS */

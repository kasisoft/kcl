package com.kasisoft.libs.common.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.io.IoFunctions;

import com.kasisoft.libs.common.AbstractTestCase;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class BlacklistTest extends AbstractTestCase {

  Blacklist   csblacklist;
  Blacklist   ciblacklist;
  String      expected;
  
  @BeforeTest
  public void setUp() {
    expected    = IoFunctions.readText(getResource("text_base.txt"));
    csblacklist = new Blacklist(getResource("blacklist_01.txt"));
    ciblacklist = new Blacklist(getResource("blacklist_02.txt"));
  }
  
  @Test(groups = "all")
  public void commentPrefix() {
    
    var prefixed = new Blacklist();
    prefixed.setCommentPrefix("$%");
    prefixed.load(getResource("blacklist_03.txt"));
    
    var original = csblacklist.getBlacklisted();
    var other    = prefixed.getBlacklisted();
    assertNotNull(original);
    assertNotNull(other);
    assertThat(other, is(original));
    
  }
  
  @Test(groups = "all")
  public void simpleCaseSensitiveMatch() {
    assertTrue(csblacklist.test("blacklisted"));
  }

  @Test(groups = "all")
  public void simpleCaseInsensitiveMatch() {
    assertTrue(csblacklist.testIgnoreCase().test("blackLISTED"));
  }

  @Test(groups = "all")
  public void startsWithCaseSensitiveMatch() {
    assertTrue(csblacklist.startsWith().test("blacklisted TEXT"));
  }

  @Test(groups = "all")
  public void startsWithCaseInsensitiveMatch() {
    assertTrue(csblacklist.startsWith(true).test("blackLISTED TEXT"));
  }

  @Test(groups = "all")
  public void endsWithCaseSensitiveMatch() {
    assertTrue(csblacklist.endsWith().test("TEXT blacklisted"));
  }

  @Test(groups = "all")
  public void endsWithCaseInsensitiveMatch() {
    assertTrue(csblacklist.endsWith(true).test("TEXT blackLISTED"));
  }

  @Test(groups = "all")
  public void containsCaseSensitiveMatch() {
    assertTrue(csblacklist.contains().test("TEXTA blacklisted TEXTB"));
  }

  @Test(groups="all")
  public void containsCaseInsensitiveMatch() {
    assertTrue( csblacklist.contains( true ).test( "TEXTA blackLISTEDTEXTB" ) );
  }

  @Test(groups = "all")
  public void cleanTextCaseSensitive() {
    var text = IoFunctions.readText(getResource("text_01.txt"));
    assertNotNull(text);
    assertFalse(text.equals(expected));
    assertFalse(csblacklist.test(text));
    assertThat(csblacklist.cleanup().apply(text).toString(), is(expected));
  }

  @Test(groups = "all")
  public void cleanTextCaseInsensitive() {
    var text = IoFunctions.readText(getResource("text_02.txt"));
    assertNotNull(text);
    assertFalse(text.equals(expected));
    assertTrue(ciblacklist.contains(true).test(text));
    assertThat(ciblacklist.cleanup(true).apply(text).toString(), is(expected));
  }

  @Test(groups = "all")
  public void cleanAndListTextCaseSensitive() {
    var text = IoFunctions.readText(getResource("text_01.txt"));
    assertNotNull(text);
    assertFalse(text.equals(expected));
    assertFalse(csblacklist.test(text));
    var collector = new HashSet<String>();
    assertThat(csblacklist.cleanup(collector::add).apply(text).toString(), is(expected));
    assertThat(collector.size(), is(2));
    assertTrue(collector.contains("blacklisted"));
    assertTrue(collector.contains("blacklisted2"));
  }

  @Test(groups = "all")
  public void cleanAndListTextCaseInsensitive() {
    var text = IoFunctions.readText(getResource("text_02.txt"));
    assertNotNull(text);
    assertFalse(text.equals(expected));
    assertTrue(ciblacklist.contains(true).test(text));
    var collector = new HashSet<String>();
    assertThat(ciblacklist.cleanup(true, collector::add).apply(text).toString(), is(expected));
    assertThat(collector.size(), is(3) );
    assertTrue(collector.contains("blackliSTed"));
    assertTrue(collector.contains("BLacklistED2"));
    assertTrue(collector.contains("blACKListed"));
  }

  @Test(groups = "all")
  public void cleanAndCountTextCaseSensitive() {
    var text = IoFunctions.readText(getResource("text_01.txt"));
    assertNotNull(text);
    assertFalse(text.equals(expected));
    assertFalse(csblacklist.test(text));
    var counts = new HashMap<String, Integer>();
    assertThat(csblacklist.cleanup($ -> statistic(counts, $)).apply(text).toString(), is(expected));
    assertThat(counts.size(), is(2));
    assertTrue(counts.containsKey("blacklisted"));
    assertTrue(counts.containsKey("blacklisted2"));
    assertThat(counts.get("blacklisted"), is( Integer.valueOf(2)));
    assertThat(counts.get("blacklisted2"), is( Integer.valueOf(1)));
  }

  @Test(groups = "all")
  public void cleanAndCountTextCaseInsensitive() {
    var text = IoFunctions.readText(getResource("text_02.txt"));
    assertNotNull(text);
    assertFalse(text.equals(expected));
    assertTrue(ciblacklist.contains(true).test(text));
    var counts = new HashMap<String,Integer>();
    assertThat(ciblacklist.cleanup(true, $ -> statistic(counts, $)).apply(text).toString(), is(expected));
    assertThat(counts.size(), is(2));
    assertTrue(counts.containsKey("blacklisted"));
    assertTrue(counts.containsKey("blacklisted2"));
    assertThat(counts.get("blacklisted"), is(Integer.valueOf(1)));
    assertThat(counts.get("blacklisted2"), is(Integer.valueOf(1)));
  }
  
  private void statistic(Map<String,Integer> values, String literal) {
    if (values.containsKey(literal)) {
      values.put(literal.toLowerCase(), Integer.valueOf(values.get(literal).intValue() + 1));
    } else {
      values.put(literal.toLowerCase(), Integer.valueOf(1));
    }
  }

} /* ENDCLASS */

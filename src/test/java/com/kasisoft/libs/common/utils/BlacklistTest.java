package com.kasisoft.libs.common.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.constants.Encoding;

import com.kasisoft.libs.common.io.IoFunctions;

import com.kasisoft.libs.common.AbstractTestCase;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class BlacklistTest extends AbstractTestCase {

  Blacklist   csblacklistPath;
  Blacklist   csblacklistURL;
  Blacklist   csblacklistFile;
  Blacklist   csblacklistURI;
  
  Blacklist   ciblacklistPath;
  Blacklist   ciblacklistURL;
  Blacklist   ciblacklistFile;
  Blacklist   ciblacklistURI;
  
  String      expected;
  
  @SuppressWarnings("deprecation")
  @BeforeTest
  public void setUp() throws Exception {
    
    expected        = IoFunctions.readText(getResource("text_base.txt"));
    
    csblacklistPath = new Blacklist(getResource("blacklist_01.txt"), Encoding.UTF8);
    csblacklistURL  = new Blacklist(getResource("blacklist_01.txt").toFile().toURL(), Encoding.UTF8);
    csblacklistFile = new Blacklist(getResource("blacklist_01.txt").toFile(), Encoding.UTF8);
    csblacklistURI  = new Blacklist(getResource("blacklist_01.txt").toUri(), Encoding.UTF8);
    
    ciblacklistPath = new Blacklist(getResource("blacklist_02.txt"), Encoding.UTF8);
    ciblacklistURL  = new Blacklist(getResource("blacklist_02.txt").toFile().toURL(), Encoding.UTF8);
    ciblacklistFile = new Blacklist(getResource("blacklist_02.txt").toFile(), Encoding.UTF8);
    ciblacklistURI  = new Blacklist(getResource("blacklist_02.txt").toUri(), Encoding.UTF8);
    
  }
  
  @DataProvider(name = "data_csblacklist")
  public Object[][] data_csblacklist() {
    return new Object[][] {
      { csblacklistPath },
      { csblacklistURL },
      { csblacklistFile },
      { csblacklistURI },
    };
  }
  
  @DataProvider(name = "data_ciblacklist")
  public Object[][] data_ciblacklist() {
    return new Object[][] {
      { ciblacklistPath },
      { ciblacklistURL },
      { ciblacklistFile },
      { ciblacklistURI },
    };
  }

  @Test(groups = "all", dataProvider = "data_csblacklist")
  public void commentPrefix(Blacklist csblacklist) {
    
    var prefixed = new Blacklist();
    prefixed.setCommentPrefix("$%");
    prefixed.load(getResource("blacklist_03.txt"));
    
    var original = csblacklist.getBlacklisted();
    var other    = prefixed.getBlacklisted();
    assertNotNull(original);
    assertNotNull(other);
    assertThat(other, is(original));
    
  }
  
  @Test(groups = "all", dataProvider = "data_csblacklist")
  public void simpleCaseSensitiveMatch(Blacklist csblacklist) {
    assertTrue(csblacklist.test("blacklisted"));
  }

  @Test(groups = "all", dataProvider = "data_csblacklist")
  public void simpleCaseInsensitiveMatch(Blacklist csblacklist) {
    assertTrue(csblacklist.testIgnoreCase().test("blackLISTED"));
  }

  @Test(groups = "all", dataProvider = "data_csblacklist")
  public void startsWithCaseSensitiveMatch(Blacklist csblacklist) {
    assertTrue(csblacklist.startsWith().test("blacklisted TEXT"));
  }

  @Test(groups = "all", dataProvider = "data_csblacklist")
  public void startsWithCaseInsensitiveMatch(Blacklist csblacklist) {
    assertTrue(csblacklist.startsWith(true).test("blackLISTED TEXT"));
  }

  @Test(groups = "all", dataProvider = "data_csblacklist")
  public void endsWithCaseSensitiveMatch(Blacklist csblacklist) {
    assertTrue(csblacklist.endsWith().test("TEXT blacklisted"));
  }

  @Test(groups = "all", dataProvider = "data_csblacklist")
  public void endsWithCaseInsensitiveMatch(Blacklist csblacklist) {
    assertTrue(csblacklist.endsWith(true).test("TEXT blackLISTED"));
  }

  @Test(groups = "all", dataProvider = "data_csblacklist")
  public void containsCaseSensitiveMatch(Blacklist csblacklist) {
    assertTrue(csblacklist.contains().test("TEXTA blacklisted TEXTB"));
  }

  @Test(groups="all", dataProvider = "data_csblacklist")
  public void containsCaseInsensitiveMatch(Blacklist csblacklist) {
    assertTrue(csblacklist.contains(true).test( "TEXTA blackLISTEDTEXTB"));
  }

  @Test(groups = "all", dataProvider = "data_csblacklist")
  public void cleanTextCaseSensitive(Blacklist csblacklist) {
    var text = IoFunctions.readText(getResource("text_01.txt"));
    assertNotNull(text);
    assertFalse(text.equals(expected));
    assertFalse(csblacklist.test(text));
    assertThat(csblacklist.cleanup().apply(text).toString(), is(expected));
  }

  @Test(groups = "all", dataProvider = "data_ciblacklist")
  public void cleanTextCaseInsensitive(Blacklist ciblacklist) {
    var text = IoFunctions.readText(getResource("text_02.txt"));
    assertNotNull(text);
    assertFalse(text.equals(expected));
    assertTrue(ciblacklist.contains(true).test(text));
    assertThat(ciblacklist.cleanup(true).apply(text).toString(), is(expected));
  }

  @Test(groups = "all", dataProvider = "data_csblacklist")
  public void cleanAndListTextCaseSensitive(Blacklist csblacklist) {
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

  @Test(groups = "all", dataProvider = "data_ciblacklist")
  public void cleanAndListTextCaseInsensitive(Blacklist ciblacklist) {
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

  @Test(groups = "all", dataProvider = "data_csblacklist")
  public void cleanAndCountTextCaseSensitive(Blacklist csblacklist) {
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

  @Test(groups = "all", dataProvider = "data_ciblacklist")
  public void cleanAndCountTextCaseInsensitive(Blacklist ciblacklist) {
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

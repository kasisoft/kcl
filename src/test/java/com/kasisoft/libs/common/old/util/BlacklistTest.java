package com.kasisoft.libs.common.old.util;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.kasisoft.libs.common.old.io.*;

import org.testng.annotations.*;

import java.util.*;

/**
 * Testcases for the class 'ArrayFunctions'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class BlacklistTest {

  Blacklist   csblacklist;
  Blacklist   ciblacklist;
  String      expected;
  
  @BeforeTest
  public void setUp() {
    
    expected    = IoFunctions.readTextFully( MiscFunctions.getResource( "/old/blacklist/text_base.txt" ) );
    assertNotNull( expected );
    
    csblacklist = new Blacklist( "/old/blacklist/blacklist_01.txt" );
    ciblacklist = new Blacklist( "/old/blacklist/blacklist_02.txt" );

  }
  
  @Test(groups="all")
  public void commentPrefix() {
    
    Blacklist prefixed = new Blacklist();
    prefixed.setCommentPrefix( "$%" );
    prefixed.load( "/old/blacklist/blacklist_03.txt" );
    
    List<String> original = csblacklist.getBlacklisted();
    List<String> other    = prefixed.getBlacklisted();
    assertNotNull( original );
    assertNotNull( other    );
    assertThat( other, is( original ) );
  }
  
  @Test(groups="all")
  public void simpleCaseSensitiveMatch() {
    assertTrue( csblacklist.test( "blacklisted" ) );
  }

  @Test(groups="all")
  public void simpleCaseInsensitiveMatch() {
    assertTrue( csblacklist.testIgnoreCase().test( "blackLISTED" ) );
  }

  @Test(groups="all")
  public void startsWithCaseSensitiveMatch() {
    assertTrue( csblacklist.startsWith().test( "blacklisted TEXT" ) );
  }

  @Test(groups="all")
  public void startsWithCaseInsensitiveMatch() {
    assertTrue( csblacklist.startsWith( true ).test( "blackLISTED TEXT" ) );
  }

  @Test(groups="all")
  public void endsWithCaseSensitiveMatch() {
    assertTrue( csblacklist.endsWith().test( "TEXT blacklisted" ) );
  }

  @Test(groups="all")
  public void endsWithCaseInsensitiveMatch() {
    assertTrue( csblacklist.endsWith( true ).test( "TEXT blackLISTED" ) );
  }

  @Test(groups="all")
  public void containsCaseSensitiveMatch() {
    assertTrue( csblacklist.contains().test( "TEXTA blacklisted TEXTB" ) );
  }

  @Test(groups="all")
  public void containsCaseInsensitiveMatch() {
    assertTrue( csblacklist.contains( true ).test( "TEXTA blackLISTEDTEXTB" ) );
  }

  @Test(groups="all")
  public void cleanTextCaseSensitive() {
    String text = IoFunctions.readTextFully( MiscFunctions.getResource( "/old/blacklist/text_01.txt" ) );
    assertNotNull( text );
    assertFalse( text.equals( expected ) );
    assertFalse( csblacklist.test( text ) );
    assertThat( csblacklist.cleanup().apply( text ).toString(), is( expected ) );
  }

  @Test(groups="all")
  public void cleanTextCaseInsensitive() {
    String text = IoFunctions.readTextFully( MiscFunctions.getResource( "/old/blacklist/text_02.txt" ) );
    assertNotNull( text );
    assertFalse( text.equals( expected ) );
    assertTrue( ciblacklist.contains( true ).test( text ) );
    assertThat( ciblacklist.cleanup( true ).apply( text ).toString(), is( expected ) );
  }

  @Test(groups="all")
  public void cleanAndListTextCaseSensitive() {
    String text = IoFunctions.readTextFully( MiscFunctions.getResource( "/old/blacklist/text_01.txt" ) );
    assertNotNull( text );
    assertFalse( text.equals( expected ) );
    assertFalse( csblacklist.test( text ) );
    Set<String> collector = new HashSet<>();
    assertThat( csblacklist.cleanup( collector::add ).apply( text ).toString(), is( expected ) );
    assertThat( collector.size(), is(2) );
    assertTrue( collector.contains( "blacklisted" ) );
    assertTrue( collector.contains( "blacklisted2" ) );
  }

  @Test(groups="all")
  public void cleanAndListTextCaseInsensitive() {
    String text = IoFunctions.readTextFully( MiscFunctions.getResource( "/old/blacklist/text_02.txt" ) );
    assertNotNull( text );
    assertFalse( text.equals( expected ) );
    assertTrue( ciblacklist.contains( true ).test( text ) );
    Set<String> collector = new HashSet<>();
    assertThat( ciblacklist.cleanup( true, collector::add ).apply( text ).toString(), is( expected ) );
    assertThat( collector.size(), is(2) );
    assertTrue( collector.contains( "blacklisted" ) );
    assertTrue( collector.contains( "blacklisted2" ) );
  }

  @Test(groups="all")
  public void cleanAndCountTextCaseSensitive() {
    String text = IoFunctions.readTextFully( MiscFunctions.getResource( "/old/blacklist/text_01.txt" ) );
    assertNotNull( text );
    assertFalse( text.equals( expected ) );
    assertFalse( csblacklist.test( text ) );
    Map<String,Integer> counts = new HashMap<>();
    assertThat( csblacklist.cleanup( $ -> statistic( counts, $ ) ).apply( text ).toString(), is( expected ) );
    assertThat( counts.size(), is(2) );
    assertTrue( counts.containsKey( "blacklisted" ) );
    assertTrue( counts.containsKey( "blacklisted2" ) );
    assertThat( counts.get( "blacklisted" ), is( Integer.valueOf(2) ) );
    assertThat( counts.get( "blacklisted2" ), is( Integer.valueOf(1) ) );
  }

  @Test(groups="all")
  public void cleanAndCountTextCaseInsensitive() {
    String text = IoFunctions.readTextFully( MiscFunctions.getResource( "/old/blacklist/text_02.txt" ) );
    assertNotNull( text );
    assertFalse( text.equals( expected ) );
    assertTrue( ciblacklist.contains( true ).test( text ) );
    Map<String,Integer> counts = new HashMap<>();
    assertThat( ciblacklist.cleanup( true, $ -> statistic( counts, $ ) ).apply( text ).toString(), is( expected ) );
    assertThat( counts.size(), is(2) );
    assertTrue( counts.containsKey( "blacklisted" ) );
    assertTrue( counts.containsKey( "blacklisted2" ) );
    assertThat( counts.get( "blacklisted" ), is( Integer.valueOf(2) ) );
    assertThat( counts.get( "blacklisted2" ), is( Integer.valueOf(1) ) );
  }
  
  private void statistic( Map<String,Integer> values, String literal ) {
    if( values.containsKey( literal ) ) {
      values.put( literal, Integer.valueOf( values.get( literal ).intValue() + 1 ) );
    } else {
      values.put( literal, Integer.valueOf(1) );
    }
  }

} /* ENDCLASS */

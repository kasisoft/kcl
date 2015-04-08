package com.kasisoft.libs.common.util;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

/**
 * Testcase for the class 'StringFBuilder'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class StringFBuilderTest {

  @DataProvider(name="dataStringBuffers")
  public Object[][] dataStringBuffers() {
    return new Object[][] {
      new Object[] { new StringFBuilder()      },
      new Object[] { new StringFBuilder( 256 ) },
      new Object[] { new StringFBuilder( "" )  },
    };
  }
  
  @Test(dataProvider="dataStringBuffers", groups="all")
  public void appendF( StringFBuilder buffer ) {
    buffer.appendF( "My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf( 17 ) );
    assertThat( buffer.toString(), is( "My test is this: Hello World ! Not 0x11 !" ) );
  }
  
  @Test(dataProvider="dataStringBuffers", groups="all")
  public void charAt( StringFBuilder buffer ) {
    buffer.appendF( "My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf( 17 ) );
    assertThat( buffer.charAt(0)  , is( 'M' ) );
    assertThat( buffer.charAt(-1) , is( '!' ) );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void substring( StringFBuilder buffer ) {
    buffer.appendF( "My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf( 17 ) );
    assertThat( buffer.substring( 0, 2 ) , is( "My" ) );
    assertThat( buffer.substring( -10 ) , is( "Not 0x11 !" ) );
  }
  
  @Test(dataProvider="dataStringBuffers", groups="all")
  public void trimLeading( StringFBuilder buffer ) {
    buffer.appendF( "\r\n   My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf( 17 ) );
    buffer.trimLeading();
    assertThat( buffer.toString(), is( "My test is this: Hello World ! Not 0x11 !" ) );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void trimTrailing( StringFBuilder buffer ) {
    buffer.appendF( "My test is this: %s ! Not 0x%02x !\r\n   ", "Hello World", Integer.valueOf( 17 ) );
    buffer.trimTrailing();
    assertThat( buffer.toString(), is( "My test is this: Hello World ! Not 0x11 !" ) );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void trim( StringFBuilder buffer ) {
    buffer.appendF( "\r\n   My test is this: %s ! Not 0x%02x !\r\n   ", "Hello World", Integer.valueOf( 17 ) );
    buffer.trim();
    assertThat( buffer.toString(), is( "My test is this: Hello World ! Not 0x11 !" ) );
  }
  
  @Test(dataProvider="dataStringBuffers", groups="all")
  public void endsWith( StringFBuilder buffer ) {
    buffer.appendF( "The frog is here !" );
    assertTrue  ( buffer.endsWith( true  , "here !" ) );
    assertFalse ( buffer.endsWith( true  , "HERE !" ) );
    assertTrue  ( buffer.endsWith( false , "HERE !" ) );
    assertTrue  ( buffer.endsWith( "here !" ) );
    assertFalse ( buffer.endsWith( "HERE !" ) );
    assertFalse ( buffer.endsWith( true  , "The frog is here ! Oops !" ) );
    assertFalse ( buffer.endsWith( false , "The frog is here ! Oops !" ) );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void startsWith( StringFBuilder buffer ) {
    buffer.appendF( "The frog is here !" );
    assertTrue  ( buffer.startsWith( true  , "The" ) );
    assertFalse ( buffer.startsWith( true  , "THE" ) );
    assertTrue  ( buffer.startsWith( false , "THE" ) );
    assertTrue  ( buffer.startsWith( "The" ) );
    assertFalse ( buffer.startsWith( "THE" ) );
    assertFalse ( buffer.startsWith( true  , "The frog is here ! Oops !" ) );
    assertFalse ( buffer.startsWith( false , "The frog is here ! Oops !" ) );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void equals( StringFBuilder buffer ) {
    buffer.appendF( "The frog is here !" );
    assertTrue  ( buffer.equals( true  , "The frog is here !" ) );
    assertFalse ( buffer.equals( true  , "THE FROG IS HERE !" ) );
    assertTrue  ( buffer.equals( false , "THE FROG IS HERE !" ) );
    assertTrue  ( buffer.equals( "The frog is here !" ) );
    assertFalse ( buffer.equals( "THE FROG IS HERE !" ) );
  }
  
  @Test(dataProvider="dataStringBuffers", groups="all")
  public void remove( StringFBuilder buffer ) {
    buffer.appendF( "Moloko was a great band !" );
    assertThat( buffer.remove( "oa" ).toString(), is( "Mlk ws  gret bnd !" ) );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void reverse( StringFBuilder buffer ) {
    buffer.append( "The frog is here !" );
    buffer.reverse();
    assertThat( buffer.toString(), is( "! ereh si gorf ehT" ) );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void indexOf( StringFBuilder buffer ) {
    buffer.append( "The frog is here !" );
    assertThat( buffer.indexOf( "frog"    ),  is( 4 ) );
    assertThat( buffer.indexOf( "Flansch" ), is( -1 ) );
    assertThat( buffer.indexOf( "frog"    , 5 ), is( -1 ) );
    assertThat( buffer.indexOf( "Flansch" , 5 ), is( -1 ) );
    assertThat( buffer.indexOf( "is", "frog"    ), is(  4 ) );
    assertThat( buffer.indexOf( "is", "Flansch" ), is(  9 ) );
    assertThat( buffer.indexOf( "co", "Flansch" ), is( -1 ) );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void lastIndexOf( StringFBuilder buffer ) {
    buffer.append( "The frog is here !" );
    assertThat( buffer.lastIndexOf( "frog"    ), is(  4 ) );
    assertThat( buffer.lastIndexOf( "Flansch" ), is( -1 ) );
    assertThat( buffer.lastIndexOf( "frog"    , 5 ), is(  4 ) );
    assertThat( buffer.lastIndexOf( "Flansch" , 5 ), is( -1 ) );
    assertThat( buffer.lastIndexOf( "frog", "is" ), is(  9 ) );
    assertThat( buffer.lastIndexOf( "Flansch"    ), is( -1 ) );
  }
  
  @Test(dataProvider="dataStringBuffers", groups="all")
  public void replace( StringFBuilder buffer ) {
    buffer.append( "The frog is here !" );
    assertThat( buffer.replace( 'e', 'a' ).toString(), is( "Tha frog is hara !" ) );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void split( StringFBuilder buffer ) {
    
    buffer.append( "This was my 3 birthday on the 2 street." );
    String[] expected = new String[] {
      "This", "was", "my", "3", "bir", "hday", "on", "he", "2", "s", "ree", "."
    };
    String[] parts    = buffer.split( " t" );
    assertThat( parts, is( notNullValue() ) );
    assertThat( parts.length, is( expected.length ) );
    assertThat( parts, is( expected ) );
    
    buffer.setLength(0);
    buffer.append( "58817162" );
    parts = buffer.split( "0123456789" );
    assertThat( parts, is( notNullValue() ) );
    assertThat( parts.length, is(0) );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void splitRegex( StringFBuilder buffer ) {
    
    buffer.append( "This was my 3 birthday on the 2 street." );
    String[] expected = new String[] {
      "This was my ", " birthday on the ", " street."
    };
    String[] parts    = buffer.splitRegex( "[0-9]+" );
    assertThat( parts, is( notNullValue() ) );
    assertThat( parts.length, is( expected.length ) );
    assertThat( parts, is( expected ) );
    
    buffer.setLength(0);
    buffer.append( "58817162" );
    parts = buffer.splitRegex( "[0-9]+" );
    assertThat( parts, is( notNullValue() ) );
    assertThat( parts.length, is(0) );
    
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void replaceAll( StringFBuilder buffer ) {
    
    buffer.append( "This was my 3 birthday on the 2 street." );
    buffer.replaceAll( "[0-9]+", "abce" );
    assertThat( buffer.toString(), is( "This was my abce birthday on the abce street." ) );
    
    buffer.setLength(0);
    buffer.append( "58817162" );
    buffer.replaceAll( "[0-9]+", "dodo" );
    assertThat( buffer.toString(), is( "dodo" ) );
   
    buffer.setLength(0);
    buffer.append( "This was my 43356 birthday on the 233120 street 2928010." );
    buffer.replaceAll( "[0-9]+", "abce" );
    assertThat( buffer.toString(), is( "This was my abce birthday on the abce street abce." ) );
    
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void replaceFirst( StringFBuilder buffer ) {
    
    buffer.append( "This was my 3 birthday on the 2 street." );
    buffer.replaceFirst( "[0-9]+", "abce" );
    assertThat( buffer.toString(), is( "This was my abce birthday on the 2 street." ) );
    
    buffer.setLength(0);
    buffer.append( "58817162" );
    buffer.replaceFirst( "[0-9]+", "dodo" );
    assertThat( buffer.toString(), is( "dodo" ) );
    
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void replaceLast( StringFBuilder buffer ) {
    
    buffer.append( "This was my 3 birthday on the 2 street." );
    buffer.replaceLast( "[0-9]+", "abce" );
    assertThat( buffer.toString(), is( "This was my 3 birthday on the abce street." ) );
    
    buffer.setLength(0);
    buffer.append( "58817162" );
    buffer.replaceLast( "[0-9]+", "dodo" );
    assertThat( buffer.toString(), is( "dodo" ) );
    
  }

} /* ENDCLASS */

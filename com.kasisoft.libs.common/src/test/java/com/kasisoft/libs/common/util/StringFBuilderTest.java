package com.kasisoft.libs.common.util;

import org.testng.annotations.*;

import org.testng.*;

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
    Assert.assertEquals( buffer.toString(), "My test is this: Hello World ! Not 0x11 !" );
  }
  
  @Test(dataProvider="dataStringBuffers", groups="all")
  public void charAt( StringFBuilder buffer ) {
    buffer.appendF( "My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf( 17 ) );
    Assert.assertEquals( buffer.charAt(0)  , 'M' );
    Assert.assertEquals( buffer.charAt(-1) , '!' );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void substring( StringFBuilder buffer ) {
    buffer.appendF( "My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf( 17 ) );
    Assert.assertEquals( buffer.substring( 0, 2 )  , "My" );
    Assert.assertEquals( buffer.substring( -10 ) , "Not 0x11 !" );
  }
  
  @Test(dataProvider="dataStringBuffers", groups="all")
  public void trimLeading( StringFBuilder buffer ) {
    buffer.appendF( "\r\n   My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf( 17 ) );
    buffer.trimLeading();
    Assert.assertEquals( buffer.toString(), "My test is this: Hello World ! Not 0x11 !" );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void trimTrailing( StringFBuilder buffer ) {
    buffer.appendF( "My test is this: %s ! Not 0x%02x !\r\n   ", "Hello World", Integer.valueOf( 17 ) );
    buffer.trimTrailing();
    Assert.assertEquals( buffer.toString(), "My test is this: Hello World ! Not 0x11 !" );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void trim( StringFBuilder buffer ) {
    buffer.appendF( "\r\n   My test is this: %s ! Not 0x%02x !\r\n   ", "Hello World", Integer.valueOf( 17 ) );
    buffer.trim();
    Assert.assertEquals( buffer.toString(), "My test is this: Hello World ! Not 0x11 !" );
  }
  
  @Test(dataProvider="dataStringBuffers", groups="all")
  public void endsWith( StringFBuilder buffer ) {
    buffer.appendF( "The frog is here !" );
    Assert.assertTrue  ( buffer.endsWith( true  , "here !" ) );
    Assert.assertFalse ( buffer.endsWith( true  , "HERE !" ) );
    Assert.assertTrue  ( buffer.endsWith( false , "HERE !" ) );
    Assert.assertTrue  ( buffer.endsWith( "here !" ) );
    Assert.assertFalse ( buffer.endsWith( "HERE !" ) );
    Assert.assertFalse ( buffer.endsWith( true  , "The frog is here ! Oops !" ) );
    Assert.assertFalse ( buffer.endsWith( false , "The frog is here ! Oops !" ) );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void startsWith( StringFBuilder buffer ) {
    buffer.appendF( "The frog is here !" );
    Assert.assertTrue  ( buffer.startsWith( true  , "The" ) );
    Assert.assertFalse ( buffer.startsWith( true  , "THE" ) );
    Assert.assertTrue  ( buffer.startsWith( false , "THE" ) );
    Assert.assertTrue  ( buffer.startsWith( "The" ) );
    Assert.assertFalse ( buffer.startsWith( "THE" ) );
    Assert.assertFalse ( buffer.startsWith( true  , "The frog is here ! Oops !" ) );
    Assert.assertFalse ( buffer.startsWith( false , "The frog is here ! Oops !" ) );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void equals( StringFBuilder buffer ) {
    buffer.appendF( "The frog is here !" );
    Assert.assertTrue  ( buffer.equals( true  , "The frog is here !" ) );
    Assert.assertFalse ( buffer.equals( true  , "THE FROG IS HERE !" ) );
    Assert.assertTrue  ( buffer.equals( false , "THE FROG IS HERE !" ) );
    Assert.assertTrue  ( buffer.equals( "The frog is here !" ) );
    Assert.assertFalse ( buffer.equals( "THE FROG IS HERE !" ) );
  }
  
  @Test(dataProvider="dataStringBuffers", groups="all")
  public void remove( StringFBuilder buffer ) {
    buffer.appendF( "Moloko was a great band !" );
    Assert.assertEquals( buffer.remove( "oa" ).toString(), "Mlk ws  gret bnd !" );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void reverse( StringFBuilder buffer ) {
    buffer.append( "The frog is here !" );
    buffer.reverse();
    Assert.assertEquals( buffer.toString(), "! ereh si gorf ehT" );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void indexOf( StringFBuilder buffer ) {
    buffer.append( "The frog is here !" );
    Assert.assertEquals( buffer.indexOf( "frog"    ),  4 );
    Assert.assertEquals( buffer.indexOf( "Flansch" ), -1 );
    Assert.assertEquals( buffer.indexOf( "frog"    , 5 ), -1 );
    Assert.assertEquals( buffer.indexOf( "Flansch" , 5 ), -1 );
    Assert.assertEquals( buffer.indexOf( "is", "frog"    ),  4 );
    Assert.assertEquals( buffer.indexOf( "is", "Flansch" ),  9 );
    Assert.assertEquals( buffer.indexOf( "co", "Flansch" ), -1 );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void lastIndexOf( StringFBuilder buffer ) {
    buffer.append( "The frog is here !" );
    Assert.assertEquals( buffer.lastIndexOf( "frog"    ),  4 );
    Assert.assertEquals( buffer.lastIndexOf( "Flansch" ), -1 );
    Assert.assertEquals( buffer.lastIndexOf( "frog"    , 5 ),  4 );
    Assert.assertEquals( buffer.lastIndexOf( "Flansch" , 5 ), -1 );
    Assert.assertEquals( buffer.lastIndexOf( "frog", "is" ),  9 );
    Assert.assertEquals( buffer.lastIndexOf( "Flansch"    ), -1 );
  }
  
  @Test(dataProvider="dataStringBuffers", groups="all")
  public void replace( StringFBuilder buffer ) {
    buffer.append( "The frog is here !" );
    Assert.assertEquals( buffer.replace( 'e', 'a' ).toString(), "Tha frog is hara !" );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void split( StringFBuilder buffer ) {
    
    buffer.append( "This was my 3 birthday on the 2 street." );
    String[] expected = new String[] {
      "This", "was", "my", "3", "bir", "hday", "on", "he", "2", "s", "ree", "."
    };
    String[] parts    = buffer.split( " t" );
    Assert.assertNotNull( parts );
    Assert.assertEquals( parts.length, expected.length );
    Assert.assertEquals( parts, expected );
    
    buffer.setLength(0);
    buffer.append( "58817162" );
    parts = buffer.split( "0123456789" );
    Assert.assertNotNull( parts );
    Assert.assertEquals( parts.length, 0 );
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void splitRegex( StringFBuilder buffer ) {
    
    buffer.append( "This was my 3 birthday on the 2 street." );
    String[] expected = new String[] {
      "This was my ", " birthday on the ", " street."
    };
    String[] parts    = buffer.splitRegex( "[0-9]+" );
    Assert.assertNotNull( parts );
    Assert.assertEquals( parts.length, expected.length );
    Assert.assertEquals( parts, expected );
    
    buffer.setLength(0);
    buffer.append( "58817162" );
    parts = buffer.splitRegex( "[0-9]+" );
    Assert.assertNotNull( parts );
    Assert.assertEquals( parts.length, 0 );
    
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void replaceAll( StringFBuilder buffer ) {
    
    buffer.append( "This was my 3 birthday on the 2 street." );
    buffer.replaceAll( "[0-9]+", "abce" );
    Assert.assertEquals( buffer.toString(), "This was my abce birthday on the abce street." );
    
    buffer.setLength(0);
    buffer.append( "58817162" );
    buffer.replaceAll( "[0-9]+", "dodo" );
    Assert.assertEquals( buffer.toString(), "dodo" );
   
    buffer.setLength(0);
    buffer.append( "This was my 43356 birthday on the 233120 street 2928010." );
    buffer.replaceAll( "[0-9]+", "abce" );
    Assert.assertEquals( buffer.toString(), "This was my abce birthday on the abce street abce." );
    
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void replaceFirst( StringFBuilder buffer ) {
    
    buffer.append( "This was my 3 birthday on the 2 street." );
    buffer.replaceFirst( "[0-9]+", "abce" );
    Assert.assertEquals( buffer.toString(), "This was my abce birthday on the 2 street." );
    
    buffer.setLength(0);
    buffer.append( "58817162" );
    buffer.replaceFirst( "[0-9]+", "dodo" );
    Assert.assertEquals( buffer.toString(), "dodo" );
    
  }

  @Test(dataProvider="dataStringBuffers", groups="all")
  public void replaceLast( StringFBuilder buffer ) {
    
    buffer.append( "This was my 3 birthday on the 2 street." );
    buffer.replaceLast( "[0-9]+", "abce" );
    Assert.assertEquals( buffer.toString(), "This was my 3 birthday on the abce street." );
    
    buffer.setLength(0);
    buffer.append( "58817162" );
    buffer.replaceLast( "[0-9]+", "dodo" );
    Assert.assertEquals( buffer.toString(), "dodo" );
    
  }

} /* ENDCLASS */

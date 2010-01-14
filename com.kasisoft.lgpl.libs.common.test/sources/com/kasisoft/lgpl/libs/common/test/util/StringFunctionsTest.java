/**
 * Name........: StringFunctionsTest
 * Description.: Tests for the class 'StringFunctions'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.util;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.libs.common.util.*;

import com.kasisoft.lgpl.libs.common.test.framework.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

/**
 * Tests for the class 'StringFunctions'.
 */
public class StringFunctionsTest {

  @Test
  public void allocateAndReleaseChars() {
    
    char[]  buffer1 = StringFunctions.allocateChars( null );
    Integer count   = CommonProperty.BufferCount.getValue();
    Assert.assertTrue( buffer1.length >= count.intValue() );
    
    Integer size    = Integer.valueOf( 4552 );
    char[]  buffer2 = StringFunctions.allocateChars( size );
    Assert.assertTrue( buffer2.length >= size.intValue() );
    
    StringFunctions.releaseChars( buffer1 );
    StringFunctions.releaseChars( buffer2 );

    // the following calls won't have any effect
    StringFunctions.releaseChars( buffer1 );
    StringFunctions.releaseChars( buffer2 );

  }
  
  @DataProvider(name="createIndexOf")
  public Object[][] createIndexOf() {
    return new Object[][] {
      { "Semper fidelis", "mf"  . toCharArray(), Integer.valueOf(0)   , Integer.valueOf(2)  },  
      { "Semper fidelis", "mf"  . toCharArray(), Integer.valueOf(100) , Integer.valueOf(-1) },  
      { "Semper fidelis", "xyz" . toCharArray(), Integer.valueOf(0)   , Integer.valueOf(-1) },  
    };
  }
  
  @Test(dataProvider="createIndexOf")
  public void indexOf( String text, char[] characters, int offset, int expected ) {
    int index = StringFunctions.indexOf( offset, text, characters );
    Assert.assertEquals( index, expected );
  }
  
  @DataProvider(name="createCleanup")
  public Object[][] createCleanup() {
    return new Object[][] {
      { null      , null    },
      { ""        , null    },
      { "\t"      , null    },
      { " "       , null    },
      { "\r\t\n"  , null    },
      { "a"       , "a"     },
      { "\ra\n"   , "a"     },
      { " a "     , "a"     },
      { " ab "    , "ab"    },
      { " a c "   , "a c"   },
    };
  }
  
  @Test(dataProvider="createCleanup")
  public void cleanup( String current, String expected ) {
    String result = StringFunctions.cleanup( current );
    Assert.assertEquals( result, expected );
  }
  
  @DataProvider(name="createContains")
  public Object[][] createContains() {
    return new Object[][] {
      { "20 Frösche fliegen über den Ozean", Utilities.toArray( "20", "fliegen" ), Boolean.TRUE },
      { "20 Frösche fliegen über den Ozean", Utilities.toArray( "blau", "grün" ), Boolean.FALSE },
    };
  }

  @DataProvider(name="createEndsWith")
  public Object[][] createEndsWith() {
    return new Object[][] {
      { "20 Frösche fliegen über den Ozean", Utilities.toArray( "20", "Ozean" ), Boolean.TRUE },
      { "20 Frösche fliegen über den Ozean", Utilities.toArray( "blau", "den" ), Boolean.FALSE },
    };
  }

  @DataProvider(name="createStartsWith")
  public Object[][] createStartsWith() {
    return new Object[][] {
      { "20 Frösche fliegen über den Ozean", Utilities.toArray( "20", "fliegen" ), Boolean.TRUE },
      { "20 Frösche fliegen über den Ozean", Utilities.toArray( "blau", "den" ), Boolean.FALSE },
    };
  }

  @Test(dataProvider="createContains")
  public void contains( String text, String[] candidates, boolean contained ) {
    boolean result = StringFunctions.contains( text, candidates );
    Assert.assertEquals( result, contained );
  }

  @Test(dataProvider="createEndsWith")
  public void endsWith( String text, String[] candidates, boolean contained ) {
    boolean result = StringFunctions.endsWith( text, candidates );
    Assert.assertEquals( result, contained );
  }

  @Test(dataProvider="createStartsWith")
  public void startsWith( String text, String[] candidates, boolean contained ) {
    boolean result = StringFunctions.startsWith( text, candidates );
    Assert.assertEquals( result, contained );
  }

  @Test
  public void decFormat2() {
    Assert.assertEquals( StringFunctions.decFormat2(0), "00" );
    Assert.assertEquals( StringFunctions.decFormat2(1), "01" );
    Assert.assertEquals( StringFunctions.decFormat2(10), "10" );
    Assert.assertEquals( StringFunctions.decFormat2(100), "100" );
  }

  @Test
  public void decFormat3() {
    Assert.assertEquals( StringFunctions.decFormat3(0), "000" );
    Assert.assertEquals( StringFunctions.decFormat3(1), "001" );
    Assert.assertEquals( StringFunctions.decFormat3(10), "010" );
    Assert.assertEquals( StringFunctions.decFormat3(100), "100" );
    Assert.assertEquals( StringFunctions.decFormat3(1200), "1200" );
  }

  @Test
  public void decFormat4() {
    Assert.assertEquals( StringFunctions.decFormat4(0), "0000" );
    Assert.assertEquals( StringFunctions.decFormat4(1), "0001" );
    Assert.assertEquals( StringFunctions.decFormat4(10), "0010" );
    Assert.assertEquals( StringFunctions.decFormat4(100), "0100" );
    Assert.assertEquals( StringFunctions.decFormat4(1200), "1200" );
    Assert.assertEquals( StringFunctions.decFormat4(12040), "12040" );
  }
  
  @DataProvider(name="createFillString")
  public Object[][] createFillString() {
    return new Object[][] {
      { Integer.valueOf(0), Character.valueOf('A'), "" },  
      { Integer.valueOf(1), Character.valueOf('A'), "A" },  
      { Integer.valueOf(5), Character.valueOf('A'), "AAAAA" },  
    };
  }
  
  @Test(dataProvider="createFillString")
  public void fillString( int length, char ch, String expected ) {
    String result = StringFunctions.fillString( length, ch );
    Assert.assertEquals( result, expected );
  }
  
  @Test
  public void replaceMap() {
    Map<String,String> replacements = new Hashtable<String,String>();
    replacements.put( "__name__"    , "Daniel Kasmeroglu" );
    replacements.put( "__company__" , "Kasisoft"          );
    String result = StringFunctions.replace( "The pseudo company __company__ is driven by __name__ [__company__]", replacements );
    Assert.assertEquals( result, "The pseudo company Kasisoft is driven by Daniel Kasmeroglu [Kasisoft]");
  }

  @Test
  public void replaceString() {
    Map<String,String> replacements = new Hashtable<String,String>();
    replacements.put( "__name__"    , "Daniel Kasmeroglu" );
    replacements.put( "__company__" , "Kasisoft"          );
    String result = StringFunctions.replace( "The pseudo company __company__ is driven by __name__ [__company__]", "__company__", "Kasisoft" );
    Assert.assertEquals( result, "The pseudo company Kasisoft is driven by __name__ [Kasisoft]");
  }
  
  @Test
  public void runToString() {
    
    Assert.assertEquals( StringFunctions.toString( (Object) null ), "null" );
    Assert.assertEquals( StringFunctions.toString( (Throwable) null ), "null" );
    Assert.assertEquals( StringFunctions.toString( (Object[]) null ), "null" );

    Assert.assertEquals( StringFunctions.toString( Integer.valueOf(17) ), "17" );
    Assert.assertEquals( StringFunctions.toString( new Object[] { null, Integer.valueOf(29), "Biscuit" } ), "null,29,Biscuit" );

  }

} /* ENDCLASS */

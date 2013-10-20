/**
 * Name........: StringFunctionsTest
 * Description.: Tests for the class 'StringFunctions'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.test.framework.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

/**
 * Tests for the class 'StringFunctions'.
 */
@Test(groups="all")
public class StringFunctionsTest {

  @Test
  public void allocateAndReleaseChars() {
    
    char[]  buffer1 = StringFunctions.allocateChars( null );
    Integer count   = CommonProperty.BufferCount.getValue( System.getProperties() );
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

  @DataProvider(name="createStringIndexOf")
  public Object[][] createStringIndexOf() {
    return new Object[][] {
      { "Semper fidelis", toStringArray( "mf" ), Integer.valueOf(0)   , Integer.valueOf(2)  },  
      { "Semper fidelis", toStringArray( "mf" ), Integer.valueOf(100) , Integer.valueOf(-1) },  
      { "Semper fidelis", toStringArray( "xyz"), Integer.valueOf(0)   , Integer.valueOf(-1) },  
    };
  }

  @DataProvider(name="createLastIndexOf")
  public Object[][] createLastIndexOf() {
    return new Object[][] {
      { "Semper fidelis", "mf"  . toCharArray(), Integer.valueOf(100) , Integer.valueOf(7)  },  
      { "Semper fidelis", "mf"  . toCharArray(), Integer.valueOf(0)   , Integer.valueOf(-1) },  
      { "Semper fidelis", "xyz" . toCharArray(), Integer.valueOf(100) , Integer.valueOf(-1) },  
    };
  }

  @DataProvider(name="createLastStringIndexOf")
  public Object[][] createLastStringIndexOf() {
    return new Object[][] {
      { "Semper fidelis", toStringArray( "mf" ), Integer.valueOf(100) , Integer.valueOf(7)  },  
      { "Semper fidelis", toStringArray( "mf" ), Integer.valueOf(0)   , Integer.valueOf(-1) },  
      { "Semper fidelis", toStringArray( "xyz"), Integer.valueOf(100) , Integer.valueOf(-1) },  
    };
  }

  private String[] toStringArray( String str ) {
    char[]    chars   = str.toCharArray();
    String[]  result  = new String[ chars.length ];
    for( int i = 0; i < chars.length; i++ ) {
      result[i] = String.valueOf( chars[i] );
    }
    return result;
  }

  @Test(dataProvider="createIndexOf")
  public void indexOf1( String text, char[] characters, int offset, int expected ) {
    int index = StringFunctions.indexOf( offset, text, characters );
    Assert.assertEquals( index, expected );
  }

  @Test(dataProvider="createStringIndexOf")
  public void indexOf2( String text, String[] literals, int offset, int expected ) {
    int index = StringFunctions.indexOf( offset, text, literals );
    Assert.assertEquals( index, expected );
  }

  @Test(dataProvider="createLastIndexOf")
  public void lastIndexOf1( String text, char[] characters, int offset, int expected ) {
    int index = StringFunctions.lastIndexOf( offset, text, characters );
    Assert.assertEquals( index, expected );
  }

  @Test(dataProvider="createLastStringIndexOf")
  public void lastIndexOf2( String text, String[] literals, int offset, int expected ) {
    int index = StringFunctions.lastIndexOf( offset, text, literals );
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

  @Test
  public void concatenate() {

    // without delimiter
    Assert.assertEquals( StringFunctions.concatenate( null ), "" );
    Assert.assertEquals( StringFunctions.concatenate( null, "A" ), "A" );
    Assert.assertEquals( StringFunctions.concatenate( null, "A", "B" ), "AB" );
    Assert.assertEquals( StringFunctions.concatenate( null, "A", "B", "C" ), "ABC" );
    Assert.assertEquals( StringFunctions.concatenate( null, "A", null, "C" ), "AC" );
    Assert.assertEquals( StringFunctions.concatenate( null, "A", "", "C" ), "AC" );

    // with delimiter
    Assert.assertEquals( StringFunctions.concatenate( "#" ), "" );
    Assert.assertEquals( StringFunctions.concatenate( "#", "A" ), "A" );
    Assert.assertEquals( StringFunctions.concatenate( "#", "A", "B" ), "A#B" );
    Assert.assertEquals( StringFunctions.concatenate( "#", "A", "B", "C" ), "A#B#C" );
    Assert.assertEquals( StringFunctions.concatenate( "#", "A", null, "C" ), "A#C" );
    Assert.assertEquals( StringFunctions.concatenate( "#", "A", "", "C" ), "A#C" );

  }
  
  @Test
  public void equals() {
    Assert.assertEquals( StringFunctions.equals( "Alpha", "alpha", false ), false );
    Assert.assertEquals( StringFunctions.equals( "Alpha", "alpha", true  ), true  );
    Assert.assertEquals( StringFunctions.equals( "Alpha", "Alpha", false ), true  );
  }
  
} /* ENDCLASS */

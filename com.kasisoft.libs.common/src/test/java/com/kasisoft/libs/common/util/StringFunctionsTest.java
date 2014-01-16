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
public class StringFunctionsTest {

  @DataProvider(name="createGetBasename")
  public Object[][] createGetBasename() {
    return new Object[][] {
      { "a/b/c/test"     , "a/b/c/test" },  
      { "a/b/c/test."    , "a/b/c/test" },  
      { "a/b/c/test.txt" , "a/b/c/test" },  
    };
  }

  @Test(dataProvider="createGetBasename", groups="all")
  public void getBasename( String name, String expected) {
    Assert.assertEquals( StringFunctions.getBasename( name ), expected );
  }

  @DataProvider(name="createChangeSuffix")
  public Object[][] createChangeSuffix() {
    return new Object[][] {
      { "a/b/c/test"     , "jpg"  , "a/b/c/test.jpg" },  
      { "a/b/c/test."    , "jpg"  , "a/b/c/test.jpg" },  
      { "a/b/c/test.txt" , "jpg"  , "a/b/c/test.jpg" },  
      { "a/b/c/test.txt" , ".jpg" , "a/b/c/test..jpg" },
    };
  }

  @Test(dataProvider="createChangeSuffix", groups="all")
  public void changeSuffix( String name, String suffix, String expected) {
    Assert.assertEquals( StringFunctions.changeSuffix( name, suffix ), expected );
  }
  
  @Test(groups="all")
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

  @Test(dataProvider="createIndexOf", groups="all")
  public void indexOf1( String text, char[] characters, int offset, int expected ) {
    int index = StringFunctions.indexOf( offset, text, characters );
    Assert.assertEquals( index, expected );
  }

  @Test(dataProvider="createStringIndexOf", groups="all")
  public void indexOf2( String text, String[] literals, int offset, int expected ) {
    int index = StringFunctions.indexOf( offset, text, literals );
    Assert.assertEquals( index, expected );
  }

  @Test(dataProvider="createLastIndexOf", groups="all")
  public void lastIndexOf1( String text, char[] characters, int offset, int expected ) {
    int index = StringFunctions.lastIndexOf( offset, text, characters );
    Assert.assertEquals( index, expected );
  }

  @Test(dataProvider="createLastStringIndexOf", groups="all")
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
  
  @Test(dataProvider="createCleanup", groups="all")
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

  @Test(dataProvider="createContains", groups="all")
  public void contains( String text, String[] candidates, boolean contained ) {
    boolean result = StringFunctions.contains( text, candidates );
    Assert.assertEquals( result, contained );
  }

  @Test(dataProvider="createEndsWith", groups="all")
  public void endsWith( String text, String[] candidates, boolean contained ) {
    boolean result = StringFunctions.endsWith( text, candidates );
    Assert.assertEquals( result, contained );
  }

  @Test(dataProvider="createStartsWith", groups="all")
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
  
  @Test(dataProvider="createFillString", groups="all")
  public void fillString( int length, char ch, String expected ) {
    String result = StringFunctions.fillString( length, ch );
    Assert.assertEquals( result, expected );
  }
  
  @Test(groups="all")
  public void replaceMap() {
    Map<String,String> replacements = new Hashtable<String,String>();
    replacements.put( "__name__"    , "Daniel Kasmeroglu" );
    replacements.put( "__company__" , "Kasisoft"          );
    String result = StringFunctions.replace( "The pseudo company __company__ is driven by __name__ [__company__]", replacements );
    Assert.assertEquals( result, "The pseudo company Kasisoft is driven by Daniel Kasmeroglu [Kasisoft]");
  }

  @Test(groups="all")
  public void replaceString() {
    Map<String,String> replacements = new Hashtable<String,String>();
    replacements.put( "__name__"    , "Daniel Kasmeroglu" );
    replacements.put( "__company__" , "Kasisoft"          );
    String result = StringFunctions.replace( "The pseudo company __company__ is driven by __name__ [__company__]", "__company__", "Kasisoft" );
    Assert.assertEquals( result, "The pseudo company Kasisoft is driven by __name__ [Kasisoft]");
  }
  
  @Test(groups="all")
  public void runToString() {
    
    Assert.assertEquals( StringFunctions.toString( (Object) null ), "null" );
    Assert.assertEquals( StringFunctions.toString( (Throwable) null ), "null" );
    Assert.assertEquals( StringFunctions.toString( (Object[]) null ), "null" );

    Assert.assertEquals( StringFunctions.toString( Integer.valueOf(17) ), "17" );
    Assert.assertEquals( StringFunctions.toString( new Object[] { null, Integer.valueOf(29), "Biscuit" } ), "null,29,Biscuit" );

  }

  @Test(groups="all")
  public void concatenate() {

    // String array
    
    // without delimiter
    Assert.assertEquals( StringFunctions.concatenate( null ), "" );
    Assert.assertEquals( StringFunctions.concatenate( null, new String[0] ), "" );
    Assert.assertEquals( StringFunctions.concatenate( null, "A" ), "A" );
    Assert.assertEquals( StringFunctions.concatenate( null, "A", "B" ), "AB" );
    Assert.assertEquals( StringFunctions.concatenate( null, "A", "B", "C" ), "ABC" );
    Assert.assertEquals( StringFunctions.concatenate( null, "A", null, "C" ), "AC" );
    Assert.assertEquals( StringFunctions.concatenate( null, "A", "", "C" ), "AC" );

    // with delimiter
    Assert.assertEquals( StringFunctions.concatenate( "#" ), "" );
    Assert.assertEquals( StringFunctions.concatenate( "#", new String[0] ), "" );
    Assert.assertEquals( StringFunctions.concatenate( "#", "A" ), "A" );
    Assert.assertEquals( StringFunctions.concatenate( "#", "A", "B" ), "A#B" );
    Assert.assertEquals( StringFunctions.concatenate( "#", "A", "B", "C" ), "A#B#C" );
    Assert.assertEquals( StringFunctions.concatenate( "#", "A", null, "C" ), "A#C" );
    Assert.assertEquals( StringFunctions.concatenate( "#", "A", "", "C" ), "A#C" );

    // String collection
    
    // without delimiter
    Assert.assertEquals( StringFunctions.concatenate( null ), "" );
    Assert.assertEquals( StringFunctions.concatenate( null, Arrays.asList( new String[0] ) ), "" );
    Assert.assertEquals( StringFunctions.concatenate( null, Arrays.asList( "A" ) ), "A" );
    Assert.assertEquals( StringFunctions.concatenate( null, Arrays.asList( "A", "B" ) ), "AB" );
    Assert.assertEquals( StringFunctions.concatenate( null, Arrays.asList( "A", "B", "C" ) ), "ABC" );
    Assert.assertEquals( StringFunctions.concatenate( null, Arrays.asList( "A", null, "C" ) ), "AC" );
    Assert.assertEquals( StringFunctions.concatenate( null, Arrays.asList( "A", "", "C" ) ), "AC" );

    // with delimiter
    Assert.assertEquals( StringFunctions.concatenate( "#" ), "" );
    Assert.assertEquals( StringFunctions.concatenate( "#", Arrays.asList( new String[0] ) ), "" );
    Assert.assertEquals( StringFunctions.concatenate( "#", Arrays.asList( "A" ) ), "A" );
    Assert.assertEquals( StringFunctions.concatenate( "#", Arrays.asList( "A", "B" ) ), "A#B" );
    Assert.assertEquals( StringFunctions.concatenate( "#", Arrays.asList( "A", "B", "C" ) ), "A#B#C" );
    Assert.assertEquals( StringFunctions.concatenate( "#", Arrays.asList( "A", null, "C" ) ), "A#C" );
    Assert.assertEquals( StringFunctions.concatenate( "#", Arrays.asList( "A", "", "C" ) ), "A#C" );

  }
  
  @Test(groups="all")
  public void equals() {
    Assert.assertEquals( StringFunctions.equals( "Alpha", "alpha", false ), false );
    Assert.assertEquals( StringFunctions.equals( "Alpha", "alpha", true  ), true  );
    Assert.assertEquals( StringFunctions.equals( "Alpha", "Alpha", false ), true  );
  }
  
  
  @Test(dataProvider="createReplaceSuffix", groups="all")
  public void replaceSuffix( String input, String newsuffix, String expected ) {
    Assert.assertEquals( StringFunctions.replaceSuffix( input, newsuffix ), expected );
  }

  @DataProvider(name="createReplaceSuffix")
  public Object[][] createReplaceSuffix() {
    return new Object[][] {
      { "sample"     , "dot" , null         },
      { "sample."    , "dot" , "sample.dot" },
      { "sample.pdf" , "dot" , "sample.dot" },
    };
  }
  
  @DataProvider(name="createRepeat")
  public Object[][] createRepeat() {
    return new Object[][] {
      { Integer.valueOf(0) , null , ""   },
      { Integer.valueOf(1) , null , ""   },
      { Integer.valueOf(2) , null , ""   },
      { Integer.valueOf(0) , ""   , ""   },
      { Integer.valueOf(1) , ""   , ""   },
      { Integer.valueOf(2) , ""   , ""   },
      { Integer.valueOf(0) , "A"  , ""   },
      { Integer.valueOf(1) , "A"  , "A"  },
      { Integer.valueOf(2) , "A"  , "AA" },
    };
  }

  @Test(dataProvider="createRepeat", groups="all")
  public void repeat( int n, String text, String expected ) {
    Assert.assertEquals( StringFunctions.repeat( n, text ), expected );
  }
  
  @Test(dataProvider="createLimit", groups="all")
  public void limit( String text, int limit, String expected ) {
    Assert.assertEquals( StringFunctions.limit( text, limit ), expected );
  }
  
  @DataProvider(name="createLimit")
  public Object[][] createLimit() {
    return new Object[][] {
      { null           , Integer.valueOf(0) , null    },
      { null           , Integer.valueOf(1) , null    },
      { ""             , Integer.valueOf(0) , ""      },
      { ""             , Integer.valueOf(1) , ""      },
      { "A"            , Integer.valueOf(0) , ""      },
      { "A"            , Integer.valueOf(1) , "A"     },
      { "A"            , Integer.valueOf(5) , "A"     },
      { "ABCDEFGHIJK"  , Integer.valueOf(0) , ""      },
      { "ABCDEFGHIJK"  , Integer.valueOf(1) , "A"     },
      { "ABCDEFGHIJK"  , Integer.valueOf(5) , "ABCDE" },
    };
  }

  @Test(dataProvider="createPadding", groups="all")
  public void padding( String text, int limit, char padding, boolean left, String expected ) {
    Assert.assertEquals( StringFunctions.padding( text, limit, padding, left ), expected );
  }
  
  @DataProvider(name="createPadding")
  public Object[][] createPadding() {
    return new Object[][] {
        
      { null, Integer.valueOf(0), Character.valueOf(' '), Boolean.TRUE  , ""      },
      { null, Integer.valueOf(5), Character.valueOf(' '), Boolean.TRUE  , "     " },

      { null, Integer.valueOf(0), Character.valueOf(' '), Boolean.FALSE , ""      },
      { null, Integer.valueOf(5), Character.valueOf(' '), Boolean.FALSE , "     " },

      { "", Integer.valueOf(0), Character.valueOf(' '), Boolean.TRUE  , ""      },
      { "", Integer.valueOf(5), Character.valueOf(' '), Boolean.TRUE  , "     " },

      { "", Integer.valueOf(0), Character.valueOf(' '), Boolean.FALSE , ""      },
      { "", Integer.valueOf(5), Character.valueOf(' '), Boolean.FALSE , "     " },

      { "ABC", Integer.valueOf(0), Character.valueOf(' '), Boolean.TRUE  , ""      },
      { "ABC", Integer.valueOf(5), Character.valueOf(' '), Boolean.TRUE  , "ABC  " },

      { "ABC", Integer.valueOf(0), Character.valueOf(' '), Boolean.FALSE , ""      },
      { "ABC", Integer.valueOf(5), Character.valueOf(' '), Boolean.FALSE , "  ABC" },

      { "ABCDEFG", Integer.valueOf(0), Character.valueOf(' '), Boolean.TRUE  , ""      },
      { "ABCDEFG", Integer.valueOf(5), Character.valueOf(' '), Boolean.TRUE  , "ABCDE" },

      { "ABCDEFG", Integer.valueOf(0), Character.valueOf(' '), Boolean.FALSE , ""      },
      { "ABCDEFG", Integer.valueOf(5), Character.valueOf(' '), Boolean.FALSE , "ABCDE" },

    };
  }

  @Test(dataProvider="createPaddingNoChar", groups="all")
  public void padding( String text, int limit, boolean left, String expected ) {
    Assert.assertEquals( StringFunctions.padding( text, limit, left ), expected );
  }
  
  @DataProvider(name="createPaddingNoChar")
  public Object[][] createPaddingNoChar() {
    return new Object[][] {
        
      { null, Integer.valueOf(0), Boolean.TRUE  , ""      },
      { null, Integer.valueOf(5), Boolean.TRUE  , "     " },

      { null, Integer.valueOf(0), Boolean.FALSE , ""      },
      { null, Integer.valueOf(5), Boolean.FALSE , "     " },

      { "", Integer.valueOf(0), Boolean.TRUE  , ""      },
      { "", Integer.valueOf(5), Boolean.TRUE  , "     " },

      { "", Integer.valueOf(0), Boolean.FALSE , ""      },
      { "", Integer.valueOf(5), Boolean.FALSE , "     " },

      { "ABC", Integer.valueOf(0), Boolean.TRUE  , ""      },
      { "ABC", Integer.valueOf(5), Boolean.TRUE  , "ABC  " },

      { "ABC", Integer.valueOf(0), Boolean.FALSE , ""      },
      { "ABC", Integer.valueOf(5), Boolean.FALSE , "  ABC" },

      { "ABCDEFG", Integer.valueOf(0), Boolean.TRUE  , ""      },
      { "ABCDEFG", Integer.valueOf(5), Boolean.TRUE  , "ABCDE" },

      { "ABCDEFG", Integer.valueOf(0), Boolean.FALSE , ""      },
      { "ABCDEFG", Integer.valueOf(5), Boolean.FALSE , "ABCDE" },

    };
  }

  @DataProvider(name="caseChangeData")
  public Object[][] caseChangeData() {
    return new Object[][] {
      { null, null },
      { new String[] { ""                    }, new String[] { ""                    } },
      { new String[] { "hello"               }, new String[] { "HELLO"               } },
      { new String[] { "hello", null, "bibo" }, new String[] { "HELLO", null, "BIBO" } },
    };
  }

  @Test(dataProvider="caseChangeData", groups="all")
  public void toUpperCase( String[] current, String[] expected ) {
    Assert.assertEquals( StringFunctions.toUpperCase( current ), expected );
  }

  @Test(dataProvider="caseChangeData", groups="all")
  public void toLowerCase( String[] expected, String[] current ) {
    Assert.assertEquals( StringFunctions.toLowerCase( current ), expected );
  }

  @DataProvider(name="duplicateData")
  public Object[][] duplicateData() {
    return new Object[][] {
      { null },
      { new String[] { ""                    } },
      { new String[] { "hello"               } },
      { new String[] { "hello", null, "BIBO" } },
    };
  }

  @Test(dataProvider="duplicateData", groups="all")
  public void duplicate( String[] input ) {
    Assert.assertEquals( StringFunctions.duplicate( input ), input );
  }

  @DataProvider(name="toLinesData")
  public Object[][] toLinesData() {
    return new Object[][] {
      { "", new ArrayList<String>() },
      { "Hello\r\nWorld", new ArrayList<String>( Arrays.asList( "Hello", "World" ) ) },
      { "  Hello\r\n\tWorld", new ArrayList<String>( Arrays.asList( "  Hello", "\tWorld" ) ) },
      { "  Hello\r\n\r\n\tWorld", new ArrayList<String>( Arrays.asList( "  Hello", "", "\tWorld" ) ) },
    };
  }
  
  @Test(dataProvider="toLinesData", groups="all")
  public void toLines( String current, List<String> expected ) {
    Assert.assertEquals( StringFunctions.toLines( current ), expected );
  }
  
  @DataProvider(name="trimData")
  public Object[][] trimData() {
    
    Object[][] data = new Object[][] {
        
      { ""              , " \t\r\n", null, ""       },
      { " "             , " \t\r\n", null, ""       },
      { "  "            , " \t\r\n", null, ""       },
      { "   "           , " \t\r\n", null, ""       },
      { "Sample   "     , " \t\r\n", null, "Sample" },
      { "  Sample"      , " \t\r\n", null, "Sample" },
      { " Sample "      , " \t\r\n", null, "Sample" },

      { "\t"            , " \t\r\n", null, ""       },
      { "\t\t"          , " \t\r\n", null, ""       },
      { "\t\t\t"        , " \t\r\n", null, ""       },
      { "Sample\t\t\t"  , " \t\r\n", null, "Sample" },
      { "\t\tSample"    , " \t\r\n", null, "Sample" },
      { "\tSample\t"    , " \t\r\n", null, "Sample" },
      
      { "\r"            , " \t\r\n", null, ""       },
      { "\r\r"          , " \t\r\n", null, ""       },
      { "\r\r\r"        , " \t\r\n", null, ""       },
      { "Sample\r\r\r"  , " \t\r\n", null, "Sample" },
      { "\r\rSample"    , " \t\r\n", null, "Sample" },
      { "\rSample\r"    , " \t\r\n", null, "Sample" },

      { "\n"            , " \t\r\n", null, ""       },
      { "\n\n"          , " \t\r\n", null, ""       },
      { "\n\n\n"        , " \t\r\n", null, ""       },
      { "Sample\n\n\n"  , " \t\r\n", null, "Sample" },
      { "\n\nSample"    , " \t\r\n", null, "Sample" },
      { "\nSample\n"    , " \t\r\n", null, "Sample" },

      { "\n"            , " \t", null, "\n"           },
      { "\n\n"          , " \t", null, "\n\n"         },
      { "\n\n\n"        , " \t", null, "\n\n\n"       },
      { "Sample\n\n\n"  , " \t", null, "Sample\n\n\n" },
      { "\n\nSample"    , " \t", null, "\n\nSample"   },
      { "\nSample\n"    , " \t", null, "\nSample\n"   },

      { ""              , " \t\r\n", Boolean.TRUE, ""             },
      { " "             , " \t\r\n", Boolean.TRUE, ""             },
      { "  "            , " \t\r\n", Boolean.TRUE, ""             },
      { "   "           , " \t\r\n", Boolean.TRUE, ""             },
      { "Sample   "     , " \t\r\n", Boolean.TRUE, "Sample   "    },
      { "  Sample"      , " \t\r\n", Boolean.TRUE, "Sample"       },
      { " Sample "      , " \t\r\n", Boolean.TRUE, "Sample "      },

      { "\t"            , " \t\r\n", Boolean.TRUE, ""             },
      { "\t\t"          , " \t\r\n", Boolean.TRUE, ""             },
      { "\t\t\t"        , " \t\r\n", Boolean.TRUE, ""             },
      { "Sample\t\t\t"  , " \t\r\n", Boolean.TRUE, "Sample\t\t\t" },
      { "\t\tSample"    , " \t\r\n", Boolean.TRUE, "Sample"       },
      { "\tSample\t"    , " \t\r\n", Boolean.TRUE, "Sample\t"     },
      
      { "\r"            , " \t\r\n", Boolean.TRUE, ""             },
      { "\r\r"          , " \t\r\n", Boolean.TRUE, ""             },
      { "\r\r\r"        , " \t\r\n", Boolean.TRUE, ""             },
      { "Sample\r\r\r"  , " \t\r\n", Boolean.TRUE, "Sample\r\r\r" },
      { "\r\rSample"    , " \t\r\n", Boolean.TRUE, "Sample"       },
      { "\rSample\r"    , " \t\r\n", Boolean.TRUE, "Sample\r"     },

      { "\n"            , " \t\r\n", Boolean.TRUE, ""             },
      { "\n\n"          , " \t\r\n", Boolean.TRUE, ""             },
      { "\n\n\n"        , " \t\r\n", Boolean.TRUE, ""             },
      { "Sample\n\n\n"  , " \t\r\n", Boolean.TRUE, "Sample\n\n\n" },
      { "\n\nSample"    , " \t\r\n", Boolean.TRUE, "Sample"       },
      { "\nSample\n"    , " \t\r\n", Boolean.TRUE, "Sample\n"     },

      { "\n"            , " \t", Boolean.TRUE, "\n"           },
      { "\n\n"          , " \t", Boolean.TRUE, "\n\n"         },
      { "\n\n\n"        , " \t", Boolean.TRUE, "\n\n\n"       },
      { "Sample\n\n\n"  , " \t", Boolean.TRUE, "Sample\n\n\n" },
      { "\n\nSample"    , " \t", Boolean.TRUE, "\n\nSample"   },
      { "\nSample\n"    , " \t", Boolean.TRUE, "\nSample\n"   },
      
      { ""              , " \t\r\n", Boolean.FALSE, ""             },
      { " "             , " \t\r\n", Boolean.FALSE, ""             },
      { "  "            , " \t\r\n", Boolean.FALSE, ""             },
      { "   "           , " \t\r\n", Boolean.FALSE, ""             },
      { "Sample   "     , " \t\r\n", Boolean.FALSE, "Sample"       },
      { "  Sample"      , " \t\r\n", Boolean.FALSE, "  Sample"     },
      { " Sample "      , " \t\r\n", Boolean.FALSE, " Sample"      },

      { "\t"            , " \t\r\n", Boolean.FALSE, ""             },
      { "\t\t"          , " \t\r\n", Boolean.FALSE, ""             },
      { "\t\t\t"        , " \t\r\n", Boolean.FALSE, ""             },
      { "Sample\t\t\t"  , " \t\r\n", Boolean.FALSE, "Sample"       },
      { "\t\tSample"    , " \t\r\n", Boolean.FALSE, "\t\tSample"   },
      { "\tSample\t"    , " \t\r\n", Boolean.FALSE, "\tSample"     },
      
      { "\r"            , " \t\r\n", Boolean.FALSE, ""             },
      { "\r\r"          , " \t\r\n", Boolean.FALSE, ""             },
      { "\r\r\r"        , " \t\r\n", Boolean.FALSE, ""             },
      { "Sample\r\r\r"  , " \t\r\n", Boolean.FALSE, "Sample"       },
      { "\r\rSample"    , " \t\r\n", Boolean.FALSE, "\r\rSample"   },
      { "\rSample\r"    , " \t\r\n", Boolean.FALSE, "\rSample"     },

      { "\n"            , " \t\r\n", Boolean.FALSE, ""             },
      { "\n\n"          , " \t\r\n", Boolean.FALSE, ""             },
      { "\n\n\n"        , " \t\r\n", Boolean.FALSE, ""             },
      { "Sample\n\n\n"  , " \t\r\n", Boolean.FALSE, "Sample"       },
      { "\n\nSample"    , " \t\r\n", Boolean.FALSE, "\n\nSample"   },
      { "\nSample\n"    , " \t\r\n", Boolean.FALSE, "\nSample"     },

      { "\n"            , " \t", Boolean.FALSE, "\n"           },
      { "\n\n"          , " \t", Boolean.FALSE, "\n\n"         },
      { "\n\n\n"        , " \t", Boolean.FALSE, "\n\n\n"       },
      { "Sample\n\n\n"  , " \t", Boolean.FALSE, "Sample\n\n\n" },
      { "\n\nSample"    , " \t", Boolean.FALSE, "\n\nSample"   },
      { "\nSample\n"    , " \t", Boolean.FALSE, "\nSample\n"   },
      
    };
    
    int        count  = 5;
    Object[][] result = new Object[ data.length * count ][];
    for( int i = 0; i < data.length; i++ ) {
      
      String  str           = (String) data[i][0];
      int     idx           = i * count;
      result[ idx + 0 ]     = data[i];
      
      result[ idx + 1 ]     = Arrays.copyOf( data[i], data[i].length );
      result[ idx + 1 ][0]  = new StringBuilder( str );

      result[ idx + 2 ]     = Arrays.copyOf( data[i], data[i].length );
      result[ idx + 2 ][0]  = new StringBuffer( str );

      result[ idx + 3 ]     = Arrays.copyOf( data[i], data[i].length );
      result[ idx + 3 ][0]  = new StringFBuilder( str );

      result[ idx + 4 ]     = Arrays.copyOf( data[i], data[i].length );
      result[ idx + 4 ][0]  = new StringFBuffer( str );

    }
    
    return result;
    
  }
  
  @Test(dataProvider="trimData", groups="all")
  public void trim( CharSequence current, String chars, Boolean left, String expected ) {
    Assert.assertEquals( StringFunctions.trim( current, chars, left ).toString(), expected );
  }
  
} /* ENDCLASS */

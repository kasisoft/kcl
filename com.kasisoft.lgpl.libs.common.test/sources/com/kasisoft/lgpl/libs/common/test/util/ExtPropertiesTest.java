/**
 * Name........: ExtPropertiesTest
 * Description.: Tests for the type 'ExtProperties'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.util;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.libs.common.util.*;

import com.kasisoft.lgpl.libs.common.io.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

import java.io.*;

/**
 * Tests for the type 'ExtProperties'.
 */
@Test(groups="all",sequential=true)
public class ExtPropertiesTest {

  @DataProvider(name="createConfigs")
  public Object[][] createConfigs() {
    return new Object[][] {
        
      { "="   , ExtProperties.KeyStyle.ArrayBraces  , "~"  },
      { "="   , ExtProperties.KeyStyle.ArrayBraces  , "#"  },
      { "="   , ExtProperties.KeyStyle.ArrayBraces  , "//" },
      
      { "="   , ExtProperties.KeyStyle.CurlyBraces  , "~"  },
      { "="   , ExtProperties.KeyStyle.CurlyBraces  , "#"  },
      { "="   , ExtProperties.KeyStyle.CurlyBraces  , "//" },
     
      { "="   , ExtProperties.KeyStyle.Braces       , "~"  },
      { "="   , ExtProperties.KeyStyle.Braces       , "#"  },
      { "="   , ExtProperties.KeyStyle.Braces       , "//" },

      { ":"   , ExtProperties.KeyStyle.ArrayBraces  , "~"  },
      { ":"   , ExtProperties.KeyStyle.ArrayBraces  , "#"  },
      { ":"   , ExtProperties.KeyStyle.ArrayBraces  , "//" },
      
      { ":"   , ExtProperties.KeyStyle.CurlyBraces  , "~"  },
      { ":"   , ExtProperties.KeyStyle.CurlyBraces  , "#"  },
      { ":"   , ExtProperties.KeyStyle.CurlyBraces  , "//" },
      
      { ":"   , ExtProperties.KeyStyle.Braces       , "~"  },
      { ":"   , ExtProperties.KeyStyle.Braces       , "#"  },
      { ":"   , ExtProperties.KeyStyle.Braces       , "//" },

      { "kr"  , ExtProperties.KeyStyle.ArrayBraces  , "~"  },
      { "kr"  , ExtProperties.KeyStyle.ArrayBraces  , "#"  },
      { "kr"  , ExtProperties.KeyStyle.ArrayBraces  , "//" },
      
      { "kr"  , ExtProperties.KeyStyle.CurlyBraces  , "~"  },
      { "kr"  , ExtProperties.KeyStyle.CurlyBraces  , "#"  },
      { "kr"  , ExtProperties.KeyStyle.CurlyBraces  , "//" },
      
      { "kr"  , ExtProperties.KeyStyle.Braces       , "~"  },
      { "kr"  , ExtProperties.KeyStyle.Braces       , "#"  },
      { "kr"  , ExtProperties.KeyStyle.Braces       , "//" },

    };
  }
  
  
  private File            file;
  private ExtProperties   props;
  
  @BeforeSuite
  public void init() {
    file = new File( "testdata/props/simple.properties" );
    Assert.assertTrue( file.isFile() );
  }
  
  @BeforeMethod
  public void setup() {
    props = null;
  }
  
  private void setupContent( String delimiter, ExtProperties.KeyStyle keystyle, String commentintro ) {
    List<String> text   = IoFunctions.readText( file, Encoding.UTF8 );
    StringBuffer buffer = new StringBuffer();
    for( int i = 0; i < text.size(); i++ ) {
      String line = text.get(i);
      line        = StringFunctions.replace( line, "~", commentintro );
      line        = StringFunctions.replace( line, "=", delimiter );
      if( keystyle == ExtProperties.KeyStyle.CurlyBraces ) {
        line = line.replace( '[', '{' );
        line = line.replace( ']', '}' );
      } else if( keystyle == ExtProperties.KeyStyle.Braces ) {
        line = line.replace( '[', '(' );
        line = line.replace( ']', ')' );
      }
      buffer.append( line );
      buffer.append( SystemProperty.LineSeparator );
    }
    props = new ExtProperties( delimiter, commentintro );
    props.setKeyStyle( keystyle );
    props.load( new CharArrayReader( buffer.toString().toCharArray() ) );
  }
  
  @Test(dataProvider="createConfigs")
  public void basicAccess( String delimiter, ExtProperties.KeyStyle keystyle, String commentintro ) {
    
    setupContent( delimiter, keystyle, commentintro );
    
    // check basic property access
    Assert.assertEquals( props.getSimpleProperty( "simple_property" ), ""   );
    Assert.assertEquals( props.getSimpleProperty( "simple.property" ), ""   );
    Assert.assertEquals( props.getSimpleProperty( "cucumber"        ), null );

    Assert.assertEquals( props.getSimpleProperty( "simple_property_with_value" ), "100"             );
    Assert.assertEquals( props.getSimpleProperty( "simple.property_with_value" ), "Fred Flintstone" );

    Assert.assertEquals( props.getAssociatedProperty( "car", "small"  ), "Smart"    );
    Assert.assertEquals( props.getAssociatedProperty( "car", "medium" ), "Golf"     );
    Assert.assertEquals( props.getAssociatedProperty( "car", "big"    ), "Maybach"  );
    Assert.assertEquals( props.getAssociatedProperty( "car", "bla"    ), null       );
    
    Assert.assertEquals( props.getIndexedProperty( "class", 1  ), "java.lang.String"    );
    Assert.assertEquals( props.getIndexedProperty( "class", 13 ), "java.util.List"      );
    Assert.assertEquals( props.getIndexedProperty( "class", 50 ), "java.util.ArrayList" );
    Assert.assertEquals( props.getIndexedProperty( "class", 99 ), null                  );
    
    Set<String>         expected = new HashSet<String>();
    expected.add( "simple_property" );
    expected.add( "simple.property" );
    expected.add( "simple_property_with_value" );
    expected.add( "simple.property_with_value" );
    expected.add( "car"   );
    expected.add( "class" );
    Enumeration<String> names = props.propertyNames();
    while( names.hasMoreElements() ) {
      expected.remove( names.nextElement() );
    }
    Assert.assertTrue( expected.isEmpty() );
    
  }

  @Test(dataProvider="createConfigs")
  public void checkSortings( String delimiter, ExtProperties.KeyStyle keystyle, String commentintro ) {
    
    setupContent( delimiter, keystyle, commentintro );

    List<String> list = props.getIndexedProperty( "class", null );
    Assert.assertNotNull( list );
    Assert.assertEquals( list.size(), 3 );
    Assert.assertEquals( list.get(0), "java.lang.String"    );
    Assert.assertEquals( list.get(1), "java.util.List"      );
    Assert.assertEquals( list.get(2), "java.util.ArrayList" );
    
  }

  @Test(dataProvider="createConfigs")
  public void specificRemovals( String delimiter, ExtProperties.KeyStyle keystyle, String commentintro ) {

    setupContent( delimiter, keystyle, commentintro );

    // these aren't working
    props.removeIndexedProperty( "notworking1", 1 );
    props.removeAssociatedProperty( "notworking1", "fluffy" );
    props.removeSimpleProperty( "groucho" );
    
    // these are working
    props.removeSimpleProperty( "simple_property_with_value" );
    props.removeAssociatedProperty( "car", "big" );
    props.removeIndexedProperty( "class", 13 );

    // check basic property access
    Assert.assertEquals( props.getSimpleProperty( "simple_property" ), ""   );
    Assert.assertEquals( props.getSimpleProperty( "simple.property" ), ""   );
    Assert.assertEquals( props.getSimpleProperty( "cucumber"        ), null );

    Assert.assertEquals( props.getSimpleProperty( "simple_property_with_value" ), null              );
    Assert.assertEquals( props.getSimpleProperty( "simple.property_with_value" ), "Fred Flintstone" );

    Assert.assertEquals( props.getAssociatedProperty( "car", "small"  ), "Smart"    );
    Assert.assertEquals( props.getAssociatedProperty( "car", "medium" ), "Golf"     );
    Assert.assertEquals( props.getAssociatedProperty( "car", "big"    ), null       );
    Assert.assertEquals( props.getAssociatedProperty( "car", "bla"    ), null       );
    
    Assert.assertEquals( props.getIndexedProperty( "class", 1  ), "java.lang.String"    );
    Assert.assertEquals( props.getIndexedProperty( "class", 13 ), null                  );
    Assert.assertEquals( props.getIndexedProperty( "class", 50 ), "java.util.ArrayList" );
    Assert.assertEquals( props.getIndexedProperty( "class", 99 ), null                  );
    
    Set<String>         expected = new HashSet<String>();
    expected.add( "simple_property" );
    expected.add( "simple.property" );
    expected.add( "simple.property_with_value" );
    expected.add( "car"   );
    expected.add( "class" );
    Enumeration<String> names = props.propertyNames();
    while( names.hasMoreElements() ) {
      expected.remove( names.nextElement() );
    }
    Assert.assertTrue( expected.isEmpty() );
    
  }

  @Test(dataProvider="createConfigs")
  public void removeAllIndexed( String delimiter, ExtProperties.KeyStyle keystyle, String commentintro ) {

    setupContent( delimiter, keystyle, commentintro );

    props.removeIndexedProperty( "class" );
    
    // check basic property access
    Assert.assertEquals( props.getSimpleProperty( "simple_property" ), ""   );
    Assert.assertEquals( props.getSimpleProperty( "simple.property" ), ""   );
    Assert.assertEquals( props.getSimpleProperty( "cucumber"        ), null );

    Assert.assertEquals( props.getSimpleProperty( "simple_property_with_value" ), "100"             );
    Assert.assertEquals( props.getSimpleProperty( "simple.property_with_value" ), "Fred Flintstone" );

    Assert.assertEquals( props.getAssociatedProperty( "car", "small"  ), "Smart"    );
    Assert.assertEquals( props.getAssociatedProperty( "car", "medium" ), "Golf"     );
    Assert.assertEquals( props.getAssociatedProperty( "car", "big"    ), "Maybach"  );
    Assert.assertEquals( props.getAssociatedProperty( "car", "bla"    ), null       );
    
    Assert.assertEquals( props.getIndexedProperty( "class", 1  ), null );
    Assert.assertEquals( props.getIndexedProperty( "class", 13 ), null );
    Assert.assertEquals( props.getIndexedProperty( "class", 50 ), null );
    Assert.assertEquals( props.getIndexedProperty( "class", 99 ), null );
    
    Set<String>         expected = new HashSet<String>();
    expected.add( "simple_property" );
    expected.add( "simple.property" );
    expected.add( "simple_property_with_value" );
    expected.add( "simple.property_with_value" );
    expected.add( "car"   );
    Enumeration<String> names = props.propertyNames();
    while( names.hasMoreElements() ) {
      expected.remove( names.nextElement() );
    }
    Assert.assertTrue( expected.isEmpty() );
    
  }

  @Test(dataProvider="createConfigs")
  public void removeAllAssociated( String delimiter, ExtProperties.KeyStyle keystyle, String commentintro ) {

    setupContent( delimiter, keystyle, commentintro );

    props.removeAssociatedProperty( "car" );
    
    // check basic property access
    Assert.assertEquals( props.getSimpleProperty( "simple_property" ), ""   );
    Assert.assertEquals( props.getSimpleProperty( "simple.property" ), ""   );
    Assert.assertEquals( props.getSimpleProperty( "cucumber"        ), null );

    Assert.assertEquals( props.getSimpleProperty( "simple_property_with_value" ), "100"             );
    Assert.assertEquals( props.getSimpleProperty( "simple.property_with_value" ), "Fred Flintstone" );

    Assert.assertEquals( props.getAssociatedProperty( "car", "small"  ), null );
    Assert.assertEquals( props.getAssociatedProperty( "car", "medium" ), null );
    Assert.assertEquals( props.getAssociatedProperty( "car", "big"    ), null );
    Assert.assertEquals( props.getAssociatedProperty( "car", "bla"    ), null );
    
    Assert.assertEquals( props.getIndexedProperty( "class", 1  ), "java.lang.String"    );
    Assert.assertEquals( props.getIndexedProperty( "class", 13 ), "java.util.List"      );
    Assert.assertEquals( props.getIndexedProperty( "class", 50 ), "java.util.ArrayList" );
    Assert.assertEquals( props.getIndexedProperty( "class", 99 ), null                  );
    
    Set<String>         expected = new HashSet<String>();
    expected.add( "simple_property" );
    expected.add( "simple.property" );
    expected.add( "simple_property_with_value" );
    expected.add( "simple.property_with_value" );
    expected.add( "class" );
    Enumeration<String> names = props.propertyNames();
    while( names.hasMoreElements() ) {
      expected.remove( names.nextElement() );
    }
    Assert.assertTrue( expected.isEmpty() );
    
  }

} /* ENDCLASS */

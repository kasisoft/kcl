/**
 * Name........: ExtPropertiesTest
 * Description.: Tests for the type 'ExtProperties'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util;

import com.kasisoft.lgpl.libs.common.constants.*;

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
    String[]   delimiters    = new String[] { "=", ":", "kr" };
    String[]   commentintros = new String[] { "~", "#", "//" };
    int        records       = delimiters.length * commentintros.length;
    int        index         = 0;
    Object[][] result        = new Object[ records ][2];
    for( int j = 0; j < delimiters.length; j++ ) {
      for( int k = 0; k < commentintros.length; k++ ) {
        result[ index ] = new Object[] { delimiters[j], commentintros[k] };
        index++;
      }
    }
    return result;
  }
  
  private File   simplefile;
  private File   evaluationfile;
  
  @BeforeSuite
  public void init() {
    simplefile      = new File( "testdata/props/simple.properties"     );
    evaluationfile  = new File( "testdata/props/evaluation.properties" );
    Assert.assertTrue( simplefile     . isFile() );
    Assert.assertTrue( evaluationfile . isFile() );
  }
  
  private ExtProperties setupContent( File file, String delimiter, String commentintro ) {
    List<String> text     = IoFunctions.readText( file, Encoding.UTF8 );
    StringBuffer buffer   = new StringBuffer();
    for( int i = 0; i < text.size(); i++ ) {
      String line = text.get(i);
      line        = StringFunctions.replace( line, "~", commentintro );
      line        = StringFunctions.replace( line, "=", delimiter );
      buffer.append( line );
      buffer.append( SystemProperty.LineSeparator );
    }
    ExtProperties result = new ExtProperties( delimiter, commentintro );
    result.load( new CharArrayReader( buffer.toString().toCharArray() ) );
    return result;
  }
  
  @Test(dataProvider="createConfigs")
  public void basicAccess( String delimiter, String commentintro ) {
    
    ExtProperties props = setupContent( simplefile, delimiter, commentintro );
    
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
  public void checkSortings( String delimiter, String commentintro ) {
    
    ExtProperties props = setupContent( simplefile, delimiter, commentintro );

    List<String> list = props.getIndexedProperty( "class", null );
    Assert.assertNotNull( list );
    Assert.assertEquals( list.size(), 3 );
    Assert.assertEquals( list.get(0), "java.lang.String"    );
    Assert.assertEquals( list.get(1), "java.util.List"      );
    Assert.assertEquals( list.get(2), "java.util.ArrayList" );
    
  }

  @Test(dataProvider="createConfigs")
  public void specificRemovals( String delimiter, String commentintro ) {

    ExtProperties props = setupContent( simplefile, delimiter, commentintro );

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
  public void removeAllIndexed( String delimiter, String commentintro ) {

    ExtProperties props = setupContent( simplefile, delimiter, commentintro );

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
  public void removeAllAssociated( String delimiter, String commentintro ) {

    ExtProperties props = setupContent( simplefile, delimiter, commentintro );

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
    
    Set<String> expected = new HashSet<String>();
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

  @Test(dataProvider="createConfigs")
  public void checkEvaluated( String delimiter, String commentintro ) {

    ExtProperties props = setupContent( evaluationfile, delimiter, commentintro );
    
//    Assert.assertEquals( props.getProperty( "simple.1" ), "A" );
//    Assert.assertEquals( props.getProperty( "simple.2" ), "A${}B" );
//
//    String tempval      = SystemProperty.TempDir.getValue();
//    String expected3    = String.format( "A%sB", tempval.substring( 0, tempval.length() - 1 ) );
//    Assert.assertEquals( props.getProperty( "simple.3" ), expected3 );
//
//    String expected4    = String.format( "refers to: %s oh no", expected3 );
//    Assert.assertEquals( props.getProperty( "simple.4" ), expected4 );
//
//    Assert.assertEquals( props.getProperty( "simple.5" ), "A;C;${simple.5};D;B" );
//    Assert.assertEquals( props.getProperty( "simple.6" ), "C;A;${simple.6};B;D" );
//    String expected7    = String.format( "bla-%s-blub", expected4 );
//    Assert.assertEquals( props.getProperty( "simple.7" ), expected7 );
//    Assert.assertEquals( props.getProperty( "simple.8" ), String.format( "flup:%s.", expected7 ) );

    Assert.assertEquals( props.getAssociatedProperty( "simple", "a" ), "hondo" );
    Assert.assertEquals( props.getAssociatedProperty( "simple", "b" ), "bla-hondo-blub" );
    
  }

} /* ENDCLASS */

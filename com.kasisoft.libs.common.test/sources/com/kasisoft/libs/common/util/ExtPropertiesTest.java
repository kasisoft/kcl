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
  
  /**
   * This function creates an ExtProperties instance configured with the supplied settings. The
   * underlying properties file is altered in order to verify that it can be loaded correctly by
   * the instance.
   * 
   * @param file            The location of the properties file to be read. 
   *                        Not <code>null</code> and must be a valid file.
   * @param delimiter       The delimiter to be used between a key and a value.
   *                        Neither <code>null</code> nor empty.
   * @param commentintro    The literal that introduces a comment. 
   *                        Neither <code>null</code> nor empty.
   * 
   * @return   The new instance of the allowing to access the properties. Not <code>null</code>.
   */
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
    
    Assert.assertEquals( props.getProperty( "simple.1" ), "A" );
    Assert.assertEquals( props.getProperty( "simple.2" ), "A${}B" );

    String tempval      = SystemProperty.TempDir.getValue();
    if( tempval.endsWith( File.separator ) ) {
      tempval = tempval.substring( 0, tempval.length() - File.separator.length() );
    }
    String expected3    = String.format( "A%sB", tempval );
    Assert.assertEquals( props.getProperty( "simple.3" ), expected3 );

    String expected4    = String.format( "refers to: %s oh no", expected3 );
    Assert.assertEquals( props.getProperty( "simple.4" ), expected4 );

    Assert.assertEquals( props.getProperty( "simple.5" ), "A;C;${simple.5};D;B" );
    Assert.assertEquals( props.getProperty( "simple.6" ), "C;A;${simple.6};B;D" );
    String expected7    = String.format( "bla-%s-blub", expected4 );
    Assert.assertEquals( props.getProperty( "simple.7" ), expected7 );
    Assert.assertEquals( props.getProperty( "simple.8" ), String.format( "flup:%s.", expected7 ) );

    Assert.assertEquals( props.getAssociatedProperty( "simple", "a" ), "hondo" );
    Assert.assertEquals( props.getAssociatedProperty( "simple", "b" ), "bla-hondo-blub" );
    
  }
  
  @Test(dataProvider="createConfigs")
  public void checkNameTraversal( String delimiter, String commentintro ) {
    
    ExtProperties       simpleprops = setupContent( simplefile, delimiter, commentintro );
    Enumeration<String> names       = simpleprops.propertyNames();
    Set<String>         set         = new HashSet<String>();
    while( names.hasMoreElements() ) {
      set.add( names.nextElement() );
    }
    
    Assert.assertEquals( set.size(), 6 );
    Assert.assertTrue( set.contains( "simple.property" ) );
    Assert.assertTrue( set.contains( "simple_property" ) );
    Assert.assertTrue( set.contains( "simple.property_with_value" ) );
    Assert.assertTrue( set.contains( "simple_property_with_value" ) );
    Assert.assertTrue( set.contains( "class" ) );
    Assert.assertTrue( set.contains( "car" ) );

    ExtProperties       evaluationprops = setupContent( evaluationfile, delimiter, commentintro );
    names                               = evaluationprops.propertyNames();
    set.clear();
    while( names.hasMoreElements() ) {
      set.add( names.nextElement() );
    }
    
    Assert.assertEquals( set.size(), 15 );
    Assert.assertTrue( set.contains( "simple.1"         ) );
    Assert.assertTrue( set.contains( "simple.2"         ) );
    Assert.assertTrue( set.contains( "simple.3"         ) );
    Assert.assertTrue( set.contains( "simple.4"         ) );
    Assert.assertTrue( set.contains( "simple.5"         ) );
    Assert.assertTrue( set.contains( "simple.6"         ) );
    Assert.assertTrue( set.contains( "simple.7"         ) );
    Assert.assertTrue( set.contains( "simple.8"         ) );
    Assert.assertTrue( set.contains( "simple"           ) );
    Assert.assertTrue( set.contains( "car"              ) );
    Assert.assertTrue( set.contains( "birthdate"        ) );
    Assert.assertTrue( set.contains( "indexedcar"       ) );
    Assert.assertTrue( set.contains( "indexedbirthdate" ) );
    Assert.assertTrue( set.contains( "validdate"        ) );
    Assert.assertTrue( set.contains( "invaliddate"      ) );

  }

  @Test(dataProvider="createConfigs")
  public void loadAndStore( String delimiter, String commentintro ) {
    
    // load/reload with simple.properties
    ExtProperties   simpleprops     = setupContent( simplefile, delimiter, commentintro );
    CharArrayWriter writer1         = new CharArrayWriter();
    simpleprops.store( writer1 );
    
    ExtProperties   simplereloaded  = new ExtProperties( delimiter, commentintro );
    simplereloaded.load( new CharArrayReader( writer1.toCharArray() ) );
    CharArrayWriter writer2         = new CharArrayWriter();
    simplereloaded.store( writer2 );
    
    char[]          charray1        = writer1.toCharArray();
    char[]          charray2        = writer2.toCharArray();
    Assert.assertEquals( charray2, charray1 );

    // load/reload with evaluation.properties
    ExtProperties   evaluationprops = setupContent( evaluationfile, delimiter, commentintro );
    CharArrayWriter writer3         = new CharArrayWriter();
    evaluationprops.store( writer3 );
    
    ExtProperties   evalreloaded    = new ExtProperties( delimiter, commentintro );
    evalreloaded.load( new CharArrayReader( writer3.toCharArray() ) );
    CharArrayWriter writer4     = new CharArrayWriter();
    evalreloaded.store( writer4 );
    
    char[]          charray3    = writer3.toCharArray();
    char[]          charray4    = writer4.toCharArray();
    Assert.assertEquals( charray4, charray3 );

  }

} /* ENDCLASS */

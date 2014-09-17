/**
 * Name........: ExtPropertiesTest
 * Description.: Tests for the type 'ExtProperties'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.io.*;
import com.kasisoft.libs.common.test.framework.*;
import com.kasisoft.libs.common.xml.adapters.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

import java.io.*;

/**
 * Tests for the type 'ExtProperties'.
 */
@Test(sequential=true)
public class ExtPropertiesTest {

  private File   simplefile;
  private File   evaluationfile;
  
  @BeforeTest
  public void init() {
    File dir        = Utilities.getTestdataDir();
    simplefile      = new File( dir, "props/simple.properties"     );
    evaluationfile  = new File( dir, "props/evaluation.properties" );
    Assert.assertTrue( simplefile     . isFile() );
    Assert.assertTrue( evaluationfile . isFile() );
  }
  
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
  private ExtProperties setupContent( File file, String delimiter, String commentintro, boolean emptyisnull ) {
    List<String>  text   = IoFunctions.readText( file, Encoding.UTF8 );
    StringBuilder buffer = new StringBuilder();
    for( int i = 0; i < text.size(); i++ ) {
      String line = text.get(i);
      line        = StringFunctions.replace( line, "~", commentintro );
      line        = StringFunctions.replace( line, "=", delimiter );
      buffer.append( line );
      buffer.append( SysProperty.LineSeparator.getValue( System.getProperties() ) );
    }
    ExtProperties result = new ExtProperties( delimiter, commentintro );
    result.setEmptyIsNull( emptyisnull );
    result.load( new CharArrayReader( buffer.toString().toCharArray() ) );
    return result;
  }
  
  @Test(dataProvider="createConfigs", groups="all")
  public void basicAccess( String delimiter, String commentintro ) {
    
    ExtProperties props = setupContent( simplefile, delimiter, commentintro, false );
    
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

  @Test(dataProvider="createConfigs", groups="all")
  public void checkSortings( String delimiter, String commentintro ) {
    
    ExtProperties props = setupContent( simplefile, delimiter, commentintro, false );

    List<String> list = props.getIndexedProperties( "class", null );
    Assert.assertNotNull( list );
    Assert.assertEquals( list.size(), 3 );
    Assert.assertEquals( list.get(0), "java.lang.String"    );
    Assert.assertEquals( list.get(1), "java.util.List"      );
    Assert.assertEquals( list.get(2), "java.util.ArrayList" );
    
  }

  @Test(dataProvider="createConfigs", groups="all")
  public void specificRemovals( String delimiter, String commentintro ) {

    ExtProperties props = setupContent( simplefile, delimiter, commentintro, false );

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

  @Test(dataProvider="createConfigs", groups="all")
  public void removeAllIndexed( String delimiter, String commentintro ) {

    ExtProperties props = setupContent( simplefile, delimiter, commentintro, false );

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

  @Test(dataProvider="createConfigs", groups="all")
  public void removeAllAssociated( String delimiter, String commentintro ) {

    ExtProperties props = setupContent( simplefile, delimiter, commentintro, false );

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

  @Test(dataProvider="createConfigs", groups="all")
  public void checkEvaluated( String delimiter, String commentintro ) {

    ExtProperties props = setupContent( evaluationfile, delimiter, commentintro, false );
    
    Assert.assertEquals( props.getProperty( "simple.1" ), "A" );
    Assert.assertEquals( props.getProperty( "simple.2" ), "A${}B" );

    String tempval      = SysProperty.TempDir.getTextualValue( System.getProperties() );
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
  
  @Test(dataProvider="createConfigs", groups="all")
  public void checkNameTraversal( String delimiter, String commentintro ) {
    
    ExtProperties       simpleprops = setupContent( simplefile, delimiter, commentintro, false );
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

    ExtProperties       evaluationprops = setupContent( evaluationfile, delimiter, commentintro, false );
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

  @Test(dataProvider="createConfigs", groups="all")
  public void loadAndStore( String delimiter, String commentintro ) {
    
    // load/reload with simple.properties
    ExtProperties   simpleprops     = setupContent( simplefile, delimiter, commentintro, false );
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
    ExtProperties   evaluationprops = setupContent( evaluationfile, delimiter, commentintro, false );
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

  @Test(dataProvider="createConfigs", groups="all")
  public void ipBasicAccess( String delimiter, String commentintro ) {
    
    ExtProperties           props     = setupContent( evaluationfile, delimiter, commentintro, true );
    List<String>            list      = props.getIndexedProperties( "indexedcar", null );
    Assert.assertNotNull( list );

    Assert.assertEquals( list.size(), 4 );

    Assert.assertEquals( list.get(0), "Golf"    );
    Assert.assertEquals( list.get(1), null      );
    Assert.assertEquals( list.get(2), "Maybach" );
    Assert.assertEquals( list.get(3), "Smart"   );
    
  }

  @Test(dataProvider="createConfigs", groups="all")
  public void ipBasicSingleAccess( String delimiter, String commentintro ) {
    
    ExtProperties           props     = setupContent( evaluationfile, delimiter, commentintro, true );

    Assert.assertEquals( props.getIndexedProperty( "indexedcar", 2 ), "Golf"    );
    Assert.assertEquals( props.getIndexedProperty( "indexedcar", 4 ), null      );
    Assert.assertEquals( props.getIndexedProperty( "indexedcar", 5 ), "Maybach" );
    Assert.assertEquals( props.getIndexedProperty( "indexedcar", 7 ), "Smart"   );
    
  }

  @Test(dataProvider="createConfigs", groups="all")
  public void ipRemoveComplete( String delimiter, String commentintro ) {
    
    ExtProperties props = setupContent( evaluationfile, delimiter, commentintro, true );
    
    // remove all associated entries
    props.removeIndexedProperty( "indexedcar" );

    // there are no more entries
    Assert.assertNull( props.getIndexedProperties( "indexedcar", null ) );

  }

  @SuppressWarnings("deprecation")
  @Test(dataProvider="createConfigs", groups="all")
  public void ipSpecificlyTypedAccess( String delimiter, String commentintro ) {
    
    ExtProperties         props     = setupContent( evaluationfile, delimiter, commentintro, true );
    props.registerTypeAdapter( "indexedbirthdate", new DateAdapter( "dd.MM.yyyy", Locale.GERMAN ) );
    
    List<Date>            list      = props.getIndexedProperties( "indexedbirthdate", null );
    Assert.assertNotNull( list );

    Assert.assertEquals( list.size(), 3 );

    Assert.assertEquals( list.get( 0 ), new Date( 65, 2, 12 ) );
    Assert.assertEquals( list.get( 1 ), null                  );
    Assert.assertEquals( list.get( 2 ), new Date( 87, 6, 4  ) );
    
  }
  
  @Test(dataProvider="createConfigs", groups="all")
  public void apBasicAccess( String delimiter, String commentintro ) {
    
    ExtProperties               props     = setupContent( evaluationfile, delimiter, commentintro, true );
    Map<String,String>          map       = props.getAssociatedProperties( "car", null );
    Assert.assertNotNull( map );

    Assert.assertEquals( map.size(), 4 );

    Assert.assertTrue( map.containsKey( "small"  ) );
    Assert.assertTrue( map.containsKey( "medium" ) );
    Assert.assertTrue( map.containsKey( "big"    ) );
    Assert.assertTrue( map.containsKey( "dodo"   ) );

    Assert.assertEquals( map.get( "small"  ), "Smart"   );
    Assert.assertEquals( map.get( "medium" ), "Golf"    );
    Assert.assertEquals( map.get( "big"    ), "Maybach" );
    Assert.assertEquals( map.get( "dodo"   ), null      );
    
  }

  @Test(dataProvider="createConfigs", groups="all")
  public void apBasicSingleAccess( String delimiter, String commentintro ) {
    
    ExtProperties               props     = setupContent( evaluationfile, delimiter, commentintro, true );

    Assert.assertEquals( props.getAssociatedProperty( "car", "small"  ), "Smart"   );
    Assert.assertEquals( props.getAssociatedProperty( "car", "medium" ), "Golf"    );
    Assert.assertEquals( props.getAssociatedProperty( "car", "big"    ), "Maybach" );
    Assert.assertEquals( props.getAssociatedProperty( "car", "dodo"   ), null      );
    
  }

  @Test(dataProvider="createConfigs", groups="all")
  public void apRemoveComplete( String delimiter, String commentintro ) {
    
    ExtProperties               props     = setupContent( evaluationfile, delimiter, commentintro, true );
    
    // remove all associated entries
    props.removeAssociatedProperty( "car" );

    // there are no more entries
    Assert.assertNull( props.getAssociatedProperties( "car", null ) );

  }

  @SuppressWarnings("deprecation")
  @Test(dataProvider="createConfigs", groups="all")
  public void apSpecificlyTypedAccess( String delimiter, String commentintro ) {
    
    ExtProperties             props     = setupContent( evaluationfile, delimiter, commentintro, true );
    props.registerTypeAdapter( "birthdate", new DateAdapter( "dd.MM.yyyy", Locale.GERMAN ) );
    
    Map<String,Date>    map       = props.getAssociatedProperties( "birthdate", null );
    Assert.assertNotNull( map );

    Assert.assertEquals( map.size(), 3 );

    Assert.assertTrue( map.containsKey( "mildred"  ) );
    Assert.assertTrue( map.containsKey( "hugo"     ) );
    Assert.assertTrue( map.containsKey( "ben"      ) );

    Assert.assertEquals( map.get( "mildred"  ), new Date( 87, 6, 4  ) );
    Assert.assertEquals( map.get( "hugo"     ), new Date( 65, 2, 12 ) );
    Assert.assertEquals( map.get( "ben"      ), null                  );
    
  }

  @Test(dataProvider="createConfigs", groups="all")
  public void spBasicAccess( String delimiter, String commentintro ) {
    ExtProperties           props     = setupContent( evaluationfile, delimiter, commentintro, true );
    Assert.assertEquals( props.getSimpleProperty( "simple" ), "glow" );
  }

  @Test(dataProvider="createConfigs", groups="all")
  public void spRemoveComplete( String delimiter, String commentintro ) {
    
    ExtProperties          props     = setupContent( evaluationfile, delimiter, commentintro, true );
    
    // remove all associated entries
    props.removeSimpleProperty( "simple" );

    // there are no more entries
    Assert.assertNull( props.getSimpleProperty( "simple" ) );

  }

  @SuppressWarnings("deprecation")
  @Test(dataProvider="createConfigs", groups="all")
  public void spSpecificlyTypedAccess( String delimiter, String commentintro ) {
    
    ExtProperties        props        = setupContent( evaluationfile, delimiter, commentintro, true );
    props.registerTypeAdapter( "validdate"   , new DateAdapter( "dd.MM.yyyy", Locale.GERMAN ) );
    props.registerTypeAdapter( "invaliddate" , new DateAdapter( "dd.MM.yyyy", Locale.GERMAN ) );
    
    Assert.assertEquals( props . getSimpleProperty( "validdate"   ), new Date( 65, 2, 12 ) );
    Assert.assertEquals( props . getSimpleProperty( "invaliddate" ), null                  );
    
  }
  
} /* ENDCLASS */

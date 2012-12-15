/**
 * Name........: IndexedPropertyTest
 * Description.: Tests for the type 'IndexedProperty'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.io.*;
import com.kasisoft.libs.common.xml.adapters.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

import java.io.*;

/**
 * Tests for the type 'IndexedProperty'.
 */
@Test(groups="all")
public class IndexedPropertyTest {

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
  
  private File   evaluationfile;
  
  @BeforeTest
  public void init() {
    evaluationfile  = new File( "testdata/props/evaluation.properties" );
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
    result.setEmptyIsNull( true );
    result.load( new CharArrayReader( buffer.toString().toCharArray() ) );
    return result;
  }
  
  @Test(dataProvider="createConfigs")
  public void basicAccess( String delimiter, String commentintro ) {
    
    ExtProperties           props     = setupContent( evaluationfile, delimiter, commentintro );
    IndexedProperty<String> property  = new IndexedProperty<String>( "indexedcar", new StringAdapter() );
    List<String>            list      = property.get( props );
    Assert.assertNotNull( list );

    Assert.assertEquals( list.size(), 4 );

    Assert.assertEquals( list.get(0), "Golf"    );
    Assert.assertEquals( list.get(1), null      );
    Assert.assertEquals( list.get(2), "Maybach" );
    Assert.assertEquals( list.get(3), "Smart"   );
    
  }

  @Test(dataProvider="createConfigs")
  public void basicSingleAccess( String delimiter, String commentintro ) {
    
    ExtProperties           props     = setupContent( evaluationfile, delimiter, commentintro );
    IndexedProperty<String> property  = new IndexedProperty<String>( "indexedcar", new StringAdapter() );

    Assert.assertEquals( property.get( props, 2 ), "Golf"    );
    Assert.assertEquals( property.get( props, 4 ), null      );
    Assert.assertEquals( property.get( props, 5 ), "Maybach" );
    Assert.assertEquals( property.get( props, 7 ), "Smart"   );
    
  }

  @Test(dataProvider="createConfigs")
  public void removeComplete( String delimiter, String commentintro ) {
    
    ExtProperties           props     = setupContent( evaluationfile, delimiter, commentintro );
    IndexedProperty<String> property  = new IndexedProperty<String>( "indexedcar", new StringAdapter() );
    
    // remove all associated entries
    property.remove( props );

    // there are no more entries
    Assert.assertNull( property.get( props ) );

  }

  @SuppressWarnings("deprecation")
  @Test(dataProvider="createConfigs")
  public void specificlyTypedAccess( String delimiter, String commentintro ) {
    
    ExtProperties         props     = setupContent( evaluationfile, delimiter, commentintro );
    
    IndexedProperty<Date> property  = new IndexedProperty<Date>( "indexedbirthdate", new DateAdapter( "dd.MM.yyyy", Locale.GERMAN ) );
    
    List<Date>            list      = property.get( props );
    Assert.assertNotNull( list );

    Assert.assertEquals( list.size(), 3 );

    Assert.assertEquals( list.get( 0 ), new Date( 65, 2, 12 ) );
    Assert.assertEquals( list.get( 1 ), null                  );
    Assert.assertEquals( list.get( 2 ), new Date( 87, 6, 4  ) );
    
  }

} /* ENDCLASS */

/**
 * Name........: SimplePropertyTest
 * Description.: Tests for the type 'SimpleProperty'.
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
 * Tests for the type 'SimpleProperty'.
 */
@Test(groups="all")
public class SimplePropertyTest {

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
    SimpleProperty<String>  property  = new SimpleProperty<String>( "simple", new StringAdapter() );
    Assert.assertEquals( property.get( props ), "glow" );
  }

  @Test(dataProvider="createConfigs")
  public void removeComplete( String delimiter, String commentintro ) {
    
    ExtProperties          props     = setupContent( evaluationfile, delimiter, commentintro );
    SimpleProperty<String> property  = new SimpleProperty<String>( "simple", new StringAdapter() );
    
    // remove all associated entries
    property.remove( props );

    // there are no more entries
    Assert.assertNull( property.get( props ) );

  }

  @SuppressWarnings("deprecation")
  @Test(dataProvider="createConfigs")
  public void specificlyTypedAccess( String delimiter, String commentintro ) {
    
    ExtProperties        props        = setupContent( evaluationfile, delimiter, commentintro );
    
    SimpleProperty<Date> validdate    = new SimpleProperty<Date>( "validdate"   , new DateAdapter( "dd.MM.yyyy", Locale.GERMAN ) );
    SimpleProperty<Date> invaliddate  = new SimpleProperty<Date>( "invaliddate" , new DateAdapter( "dd.MM.yyyy", Locale.GERMAN ) );
    
    Assert.assertEquals( validdate    . get( props ), new Date( 65, 2, 12 ) );
    Assert.assertEquals( invaliddate  . get( props ), null                  );
    
  }

} /* ENDCLASS */

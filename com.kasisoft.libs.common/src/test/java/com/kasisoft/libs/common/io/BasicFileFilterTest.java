/**
 * Name........: BasicFileFilterTest
 * Description.: Test for the 'BasicFileFilter'. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.test.framework.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

import java.io.*;

/**
 * Test for the 'BasicFileFilter'.
 */
public class BasicFileFilterTest {

  private File              testdata;
  private BasicFileFilter   filter;
  
  @BeforeTest
  public void setup() {
    testdata  = new File( "testdata" );
    filter    = new BasicFileFilter( ".txt" );
  }
  
  @DataProvider(name="listingData")
  public Object[][] listingData() {
    return new Object[][] {
      { "."             , Utilities.toList( "./_svn", "./bin", "./dir01", "./images", "./props", "./testfile.txt" ) },
      { "dir01"         , Utilities.toList( "dir01/_svn", "dir01/dir03", "dir01/file04.txt" ) },
      { "dir01/dir03"   , Utilities.toList( "dir01/dir03/_svn", "dir01/dir03/dir04", "dir01/dir03/file01.txt" ) }
    };
  }
  
  @Test(dataProvider="listingData", groups="all")
  public void listTest( String dir, List<String> childdirs ) {
    File    file  = new File( testdata, dir );
    File[]  files = file.listFiles( filter );
    Assert.assertNotNull( files );
    Assert.assertEquals( files.length, childdirs.size() );
    List<String> expected = new ArrayList<String>();
    List<String> actual   = new ArrayList<String>();
    for( int i = 0; i < files.length; i++ ) {
      File childdir = new File( testdata, childdirs.get(i).replace( '/', File.separatorChar ) );
      actual   . add( files[i].getAbsolutePath() );
      expected . add( childdir.getAbsolutePath() );
    }
    Collections.sort( actual   );
    Collections.sort( expected );
    for( int i = 0; i < actual.size(); i++ ) {
      Assert.assertEquals( actual.get(i), expected.get(i) );
    }
  }
  
} /* ENDCLASS */

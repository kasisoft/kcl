/**
 * Name........: EnhancedFileFilterTest
 * Description.: Test for the 'EnhancedFileFilter'. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.io;

import com.kasisoft.lgpl.libs.common.io.*;

import com.kasisoft.lgpl.libs.common.test.framework.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

import java.io.*;

/**
 * Test for the 'EnhancedFileFilter'.
 */
@Test(groups="all")
public class EnhancedFileFilterTest {

  private File                 testdata;
  private EnhancedFileFilter   filter;
  
  @BeforeSuite
  public void setup() {
    testdata  = new File( "testdata" );
    filter    = new EnhancedFileFilter( ".txt" );
    filter.addDefaultExcludes();
  }
  
  @DataProvider(name="listingData")
  public Object[][] listingData() {
    return new Object[][] {
      { "."             , Utilities.toList( "./bin", "./dir01", "./dir02", "./images", "./props", "./testfile.txt" ) },
      { "dir01"         , Utilities.toList( "dir01/dir03", "dir01/file04.txt" ) },
      { "dir01/dir03"   , Utilities.toList( "dir01/dir03/dir04", "dir01/dir03/file01.txt" ) }
    };
  }
  
  @Test(dataProvider="listingData")
  public void listTest( String dir, List<String> childdirs ) {
    File    file  = new File( testdata, dir );
    File[]  files = file.listFiles( filter );
    Assert.assertNotNull( files );
    Assert.assertEquals( files.length, childdirs.size() );
    for( int i = 0; i < files.length; i++ ) {
      File childdir = new File( testdata, childdirs.get(i).replace( '/', File.separatorChar ) );
      Assert.assertEquals( files[i], childdir );
    }
  }
  
} /* ENDCLASS */

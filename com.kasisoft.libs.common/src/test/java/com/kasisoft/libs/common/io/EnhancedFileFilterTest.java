package com.kasisoft.libs.common.io;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

import com.kasisoft.libs.common.test.framework.*;

import lombok.experimental.*;

import lombok.*;

import java.util.*;

import java.io.*;

/**
 * Test for the 'EnhancedFileFilter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnhancedFileFilterTest {

  File                 testdata;
  EnhancedFileFilter   filter;
  
  @BeforeTest
  public void setup() {
    testdata  = Utilities.getTestdataDir();
    filter    = new EnhancedFileFilter( ".txt" );
    filter.addDefaultExcludes();
  }
  
  @DataProvider(name="listingData")
  public Object[][] listingData() {
    return new Object[][] {
      { "."             , Utilities.toList( "./bin", "./dir01", "./images", "./props", "./testfile.txt" ) },
      { "dir01"         , Utilities.toList( "dir01/dir03", "dir01/file04.txt" ) },
      { "dir01/dir03"   , Utilities.toList( "dir01/dir03/dir04", "dir01/dir03/file01.txt" ) }
    };
  }
  
  @Test(dataProvider="listingData", groups="all")
  public void listTest( String dir, List<String> childdirs ) {
    File    file  = new File( testdata, dir );
    File[]  files = file.listFiles( filter );
    assertThat( files, is( notNullValue() ) );
    assertThat( files.length, is( childdirs.size() ) );
    List<String> expected = new ArrayList<>();
    List<String> actual   = new ArrayList<>();
    for( int i = 0; i < files.length; i++ ) {
      File childdir = new File( testdata, childdirs.get(i).replace( '/', File.separatorChar ) );
      actual   . add( files[i].getAbsolutePath() );
      expected . add( childdir.getAbsolutePath() );
    }
    Collections.sort( actual   );
    Collections.sort( expected );
    for( int i = 0; i < actual.size(); i++ ) {
      assertThat( actual.get(i), is( expected.get(i) ) );
    }
  }
  
} /* ENDCLASS */

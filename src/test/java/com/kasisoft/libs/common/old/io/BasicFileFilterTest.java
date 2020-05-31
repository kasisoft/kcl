package com.kasisoft.libs.common.old.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.kasisoft.libs.common.old.test.framework.Utilities;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.io.File;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Test for the 'BasicFileFilter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BasicFileFilterTest {

  File              testdata;
  BasicFileFilter   filter;
  
  @BeforeTest
  public void setup() {
    testdata  = Utilities.getTestdataDir();
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

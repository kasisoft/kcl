/**
 * Name........: VersionTest
 * Description.: Tests for the class 'Version'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import org.testng.annotations.*;

import org.testng.*;

import java.text.*;

import java.util.*;

/**
 * Tests for the class 'Version'.
 */
public class VersionTest {

  @DataProvider(name="createInvalidVersions")
  public Object[][] createInvalidVersions() {
    return new Object[][] {
      { null               , Boolean.TRUE  , Boolean.TRUE  },
      { "1"                , Boolean.TRUE  , Boolean.TRUE  },
      { "1.1"              , Boolean.TRUE  , Boolean.TRUE  },
      { "1.1.1"            , Boolean.TRUE  , Boolean.TRUE  },
      { "1.1.qualifier"    , Boolean.TRUE  , Boolean.TRUE  },
      { "1q.1.1.qualifier" , Boolean.TRUE  , Boolean.TRUE  },
      { "1.1q.1.qualifier" , Boolean.TRUE  , Boolean.TRUE  },
      { "1.1.1q.qualifier" , Boolean.TRUE  , Boolean.TRUE  },
      { "1.1.qualifier"    , Boolean.TRUE  , Boolean.FALSE },
    };
  }
  
  @Test(dataProvider="createInvalidVersions", expectedExceptions={ParseException.class, NullPointerException.class}, groups="all")
  public void failingVersions( String version, boolean hasmicro, boolean hasqualifier ) throws Exception {
    new Version( version, hasmicro, hasqualifier );
  }

  @DataProvider(name="createVersions")
  public Object[][] createVersions() {
    return new Object[][] {
      { "1.1"              , Boolean.FALSE , Boolean.FALSE , new Version( 1, 1 ) },
      { "1.1.1"            , Boolean.TRUE  , Boolean.FALSE , new Version( 1, 1, 1 ) },
      { "1.1.qualifier"    , Boolean.FALSE , Boolean.TRUE  , new Version( 1, 1, "qualifier" ) },
      { "1.1.1.qualifier"  , Boolean.TRUE  , Boolean.TRUE  , new Version( 1, 1, 1, "qualifier" ) },
      { "1.1.1_qualifier"  , Boolean.TRUE  , Boolean.TRUE  , new Version( 1, 1, 1, "qualifier" ) },
    };
  }

  @Test(dataProvider="createVersions", groups="all")
  public void versions( String version, boolean hasmicro, boolean hasqualifier, Version expected ) throws Exception {
    Assert.assertEquals( new Version( version, hasmicro, hasqualifier ), expected );
  }

  @DataProvider(name="createVersionsAll")
  public Object[][] createVersionsAll() {
    return new Object[][] {
      { "1.1"              , new Version( 1, 1 ) },
      { "1.1.1"            , new Version( 1, 1, 1 ) },
      { "1.1.qualifier"    , new Version( 1, 1, "qualifier" ) },
      { "1.1.1.qualifier"  , new Version( 1, 1, 1, "qualifier" ) },
      { "1.1.1_qualifier"  , new Version( 1, 1, 1, "qualifier" ) },
    };
  }

  @Test(dataProvider="createVersionsAll", groups="all")
  public void versionsAll( String version, Version expected ) throws Exception {
    Assert.assertEquals( new Version( version ), expected );
  }

  @DataProvider(name="createSort")
  public Object[][] createSort() {
    try {
      
      return new Object[][] {
          { 
            asList( new Version( "2.1", false, false ), new Version( "1.1", false, false ) ), 
            asList( new Version( "1.1", false, false ), new Version( "2.1", false, false ) ) 
          },
          { 
            asList( new Version( "1.2", false, false ), new Version( "1.1", false, false ) ), 
            asList( new Version( "1.1", false, false ), new Version( "1.2", false, false ) ) 
          },
          { 
            asList( new Version( "1.1.2", true, false ), new Version( "1.1.1", true, false ) ), 
            asList( new Version( "1.1.1", true, false ), new Version( "1.1.2", true, false ) ) 
          },
          { 
            asList( new Version( "1.1.1.zz", true, true ), new Version( "1.1.1.aa", true, true ) ), 
            asList( new Version( "1.1.1.aa", true, true ), new Version( "1.1.1.zz", true, true ) ) 
          },
          { 
            asList( new Version( "1.1.1_zz", true, true ), new Version( "1.1.1.aa", true, true ) ), 
            asList( new Version( "1.1.1_aa", true, true ), new Version( "1.1.1.zz", true, true ) ) 
          },
      };
      
    } catch( ParseException ex ) {
      throw new RuntimeException(ex);
    }
  }
  
  private List<Version> asList( Version ... versions ) {
    List<Version> result = new ArrayList<Version>();
    for( int i = 0; i < versions.length; i++ ) {
      result.add( versions[i] );
    }
    return result;
  }
  
  @Test(dataProvider="createSort", groups="all")
  public void sort( List<Version> versions, List<Version> expected ) {
    Collections.sort( versions );
    Assert.assertEquals( versions, expected );
  }
  
} /* ENDCLASS */

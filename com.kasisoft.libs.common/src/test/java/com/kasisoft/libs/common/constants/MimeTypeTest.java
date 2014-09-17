package com.kasisoft.libs.common.constants;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

/**
 * Tests for the enumeration 'MimeType'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class MimeTypeTest {

  @Test(groups="all")
  public void simple() {
    Set<MimeType> result = MimeType.valuesBySuffix( "tex" );
    Assert.assertNotNull( result );
    Assert.assertEquals( result.size(), 2 );
    Assert.assertTrue( result.contains( MimeType.LaTeX ) );
    Assert.assertTrue( result.contains( MimeType.TeX ) );
  }
  
} /* ENDCLASS */

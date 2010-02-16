/**
 * Name........: MimeTypeTest
 * Description.: Tests for the enumeration 'MimeType'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.constants;

import com.kasisoft.lgpl.libs.common.constants.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

/**
 * Tests for the enumeration 'MimeType'.
 */
@Test(groups="all")
public class MimeTypeTest {

  @Test
  public void simple() {
    Set<MimeType> result = MimeType.valuesBySuffix( "tex" );
    Assert.assertNotNull( result );
    Assert.assertEquals( result.size(), 2 );
    Assert.assertTrue( result.contains( MimeType.LaTeX ) );
    Assert.assertTrue( result.contains( MimeType.TeX ) );
  }
  
} /* ENDCLASS */

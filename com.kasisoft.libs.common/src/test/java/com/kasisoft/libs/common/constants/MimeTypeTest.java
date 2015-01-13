package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

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
    assertThat( result, is( notNullValue() ) );
    assertThat( result.size(), is(2) );
    assertTrue( result.contains( MimeType.LaTeX ) );
    assertTrue( result.contains( MimeType.TeX ) );
  }
  
} /* ENDCLASS */

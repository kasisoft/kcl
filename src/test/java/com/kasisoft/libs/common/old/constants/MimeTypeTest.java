package com.kasisoft.libs.common.old.constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import java.util.Set;

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

  @Test(groups="all")
  public void withOption() {
    MimeType result = MimeType.valueByMimeType( "text/html;charset=UTF-8" );
    assertThat( result, is( notNullValue() ) );
    assertThat( result, is( MimeType.Html ) );
  }

} /* ENDCLASS */

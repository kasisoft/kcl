/**
 * Name........: I18NSupportTest
 * Description.: Tests for the i18n class 'I18NSupport'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.i18n;

import org.testng.annotations.Test;

import org.testng.*;

/**
 * Tests for the i18n class 'I18NSupport'.
 * 
 * Requires the default locale to be Locale#GERMANY.
 */
@Test(groups="all")
public class I18NSupportTest {

  public void correctMessages1() {
    Assert.assertEquals( Messages1.m0, "Default.0"     );
    Assert.assertEquals( Messages1.m1, "Translation.1" );
    Assert.assertEquals( Messages1.m2, "Translation.2" );
    Assert.assertEquals( Messages1.m3, "Translation.3" );
    Assert.assertEquals( Messages1.m4, "correct"       );
  }

  public void correctMessages2() {
    Assert.assertEquals( Messages2.m0, "Default.0"     );
    Assert.assertEquals( Messages2.m1, "Translation.1" );
    Assert.assertEquals( Messages2.m2, "Translation.2" );
    Assert.assertEquals( Messages2.m3, "Translation.3" );
    Assert.assertEquals( Messages2.m4, "correct"       );
  }

  public void correctMessages3() {
    Assert.assertEquals( Messages3.m0, "Default.0"     );
    Assert.assertEquals( Messages3.m1, "Translation.1" );
    Assert.assertEquals( Messages3.m2, "Translation.2" );
    Assert.assertEquals( Messages3.m3, "Translation.3" );
    Assert.assertEquals( Messages3.m4, "correct"       );
  }

  public void correctMessages4() {
    
    Assert.assertEquals( Messages4.m0, "Default.0" );
    Assert.assertEquals( Messages4.m1, "Default.1" );
    Assert.assertEquals( Messages4.m3, "Default.3" );
    Assert.assertEquals( Messages4.m4, "Default.4" );
    
    Assert.assertEquals( Messages4.m2.toString(), "Default.%s.2" );
    Assert.assertEquals( Messages4.m2.format( "test" ), "Default.test.2" );
    
  }

} /* ENDCLASS */

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

import java.util.*;

/**
 * Tests for the i18n class 'I18NSupport'.
 * 
 * Requires the default locale to be Locale#GERMANY.
 */
public class I18NSupportTest {

  @Test(groups="all")
  public void correctMessages1() {
    Assert.assertEquals( Messages1.m0, "Default.0"     );
    Assert.assertEquals( Messages1.m1, "Translation.1" );
    Assert.assertEquals( Messages1.m2, "Translation.2" );
    Assert.assertEquals( Messages1.m3, "Translation.3" );
    Assert.assertEquals( Messages1.m4, "correct"       );
  }

  @Test(groups="all")
  public void correctMessages2() {
    Assert.assertEquals( Messages2.m0, "Default.0"     );
    Assert.assertEquals( Messages2.m1, "Translation.1" );
    Assert.assertEquals( Messages2.m2, "Translation.2" );
    Assert.assertEquals( Messages2.m3, "Translation.3" );
    Assert.assertEquals( Messages2.m4, "correct"       );
  }

  @Test(groups="all")
  public void correctMessages3() {
    Assert.assertEquals( Messages3.m0, "Default.0"     );
    Assert.assertEquals( Messages3.m1, "Translation.1" );
    Assert.assertEquals( Messages3.m2, "Translation.2" );
    Assert.assertEquals( Messages3.m3, "Translation.3" );
    Assert.assertEquals( Messages3.m4, "correct"       );
  }

  @Test(groups="all")
  public void correctMessages4() {
    
    Assert.assertEquals( Messages4.m0, "Default.0" );
    Assert.assertEquals( Messages4.m1, "Default.1" );
    Assert.assertEquals( Messages4.m3, "Default.3" );
    Assert.assertEquals( Messages4.m4, "Default.4" );
    
    Assert.assertEquals( Messages4.m2.toString(), "Default.%s.2" );
    Assert.assertEquals( Messages4.m2.format( "test" ), "Default.test.2" );
    
  }

  @Test(groups="all")
  public void correctMessages5() {
    
    Assert.assertEquals( Messages5.m0, "Default.0"     );
    Assert.assertEquals( Messages5.m1, "Übersetzung.1" );
    Assert.assertEquals( Messages5.m2, "Übersetzung.2" );
    Assert.assertEquals( Messages5.m3, "Translation.3" );
    Assert.assertEquals( Messages5.m4, "correct"       );
    Assert.assertEquals( Messages5.m5.format( "Huppi" ), "Der Text war 'Huppi'" );
    
    // change the language
    I18NSupport.initialize( Locale.US, Messages5.class );

    Assert.assertEquals( Messages5.m0, "Default.0"     );
    Assert.assertEquals( Messages5.m1, "Translation.1" );
    Assert.assertEquals( Messages5.m2, "Translation.2" );
    Assert.assertEquals( Messages5.m3, "Translation.3" );
    Assert.assertEquals( Messages5.m4, "correct"       );
    Assert.assertEquals( Messages5.m5.format( "Huppi" ), "The us text was 'Huppi'" );

  }

} /* ENDCLASS */

package com.kasisoft.libs.common.i18n;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

import com.kasisoft.libs.common.constants.*;

/**
 * Tests for the i18n class 'I18NSupport'.
 * 
 * Requires the default locale to be Locale#GERMANY.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class I18NFormatterTest {

  I18NFormatter   formatterWithArgs    = new I18NFormatter( "Hello '%s' %s" );
  I18NFormatter   formatterWithoutArgs = new I18NFormatter( "Constant string" );
  
  @Test(groups="all")
  public void format() {
    
    assertThat( formatterWithArgs    . format( null             ), is( "Hello '%s' %s"   ) );
    assertThat( formatterWithArgs    . format( Empty.NO_OBJECTS ), is( "Hello '%s' %s"   ) );
    assertThat( formatterWithoutArgs . format( null             ), is( "Constant string" ) );
    assertThat( formatterWithoutArgs . format( Empty.NO_OBJECTS ), is( "Constant string" ) );
    
    assertThat( formatterWithArgs    . format( "World"          ), is( "Hello '%s' %s"   ) );
    assertThat( formatterWithoutArgs . format( "World"          ), is( "Constant string" ) );
    
  }

} /* ENDCLASS */

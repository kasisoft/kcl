package com.kasisoft.libs.common.old.ui;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

/**
 * Test for the class 'FontFamily'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FontFamilyTest {

  @DataProvider(name="valueByNameData")
  public Object[][] valueByNameData() {
    FontFamily[] values = FontFamily.values();
    Object[][]   result = new Object[ values.length + 1 ][2];
    int          idx    = 0;
    for( FontFamily value : values ) {
      result[ idx++ ] = new Object[] { value.getFamilyname().toLowerCase(), value };
    }
    result[ idx ] = new Object[] { "kaslsksk", null };
    return result;
  }
  
  @Test(dataProvider="valueByNameData",groups="all")
  public void valueByName( String name, FontFamily expected ) {
    assertThat( expected, is( FontFamily.valueByFamilyname( name ) ) );
  }
  
} /* ENDCLASS */

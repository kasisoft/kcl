package com.kasisoft.libs.common.ui;

import org.testng.annotations.*;

import org.testng.*;

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
    Assert.assertEquals( expected, FontFamily.valueByFamilyname( name ) );
  }
  
} /* ENDCLASS */

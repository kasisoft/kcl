/**
 * Name........: PrimitiveTest
 * Description.: Tests for the constants 'Primitive'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.constants;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Tests for the constants 'Primitive'.
 */
@Test(groups="all")
public class PrimitiveTest {
  
  @DataProvider(name="createData")
  public Object[][] createData() {
    Primitive[] values = Primitive.values();
    Object[][]  result = new Object[ values.length ][];
    for( int i = 0; i < values.length; i++ ) {
      result[i] = new Object[] { values[i], Integer.valueOf( 20 ) };
    }
    return result;
  }

  @Test(dataProvider="createData")
  public void arrayCreation( Primitive primitive, Integer size ) {
    Assert.assertNotNull( primitive.getArrayClass() );
    Assert.assertNotNull( primitive.getObjectClass() );
    Assert.assertNotNull( primitive.getPrimitiveClass() );
    Object array = primitive.newArray( size.intValue() );
    Assert.assertEquals( primitive.length( array ), size.intValue() );
  }
  
} /* ENDCLASS */

/**
 * Name........: BuffersTest
 * Description.: Test for the class 'Buffers'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.constants.*;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Test for the class 'Buffers'.
 */
@Test(groups="all")
public class BuffersTest {
  
  @DataProvider(name="createDataTypes")
  public Object[][] createDataTypes() {
    Primitive[] primitives  = Primitive.values();
    Object[][]  result      = new Object[ primitives.length ][];
    for( int i = 0; i < primitives.length; i++ ) {
      result[i] = new Object[] { primitives[i].newArray(0) };
    }
    return result;
  }

  @Test(dataProvider="createDataTypes")
  public <T> void runAllocations( T type ) {
    Primitive  primitive  = Primitive.byArrayType( type );
    Assert.assertNotNull( primitive );
    Buffers<T> buffers    = Buffers.newBuffers( primitive );
    T          datablock1 = buffers.allocate();
    Assert.assertNotNull( datablock1 );
    Integer    count      = CommonProperty.BufferCount.getValue();
    Assert.assertTrue( primitive.length( datablock1 ) >= count.intValue() );
    T          datablock2 = buffers.allocate( null );
    Assert.assertNotNull( datablock2 );
    Assert.assertTrue( primitive.length( datablock2 ) >= count.intValue() );
    T          datablock3 = buffers.allocate( Integer.valueOf( 100 ) );
    Assert.assertNotNull( datablock3 );
    Assert.assertTrue( primitive.length( datablock3 ) >= 100 );
    Assert.assertTrue( primitive.length( datablock3 ) < count.intValue() );
  }
  
} /* ENDCLASS */
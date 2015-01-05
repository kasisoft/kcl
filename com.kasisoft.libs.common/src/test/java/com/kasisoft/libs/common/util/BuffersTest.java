package com.kasisoft.libs.common.util;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.kasisoft.libs.common.constants.*;

import org.testng.annotations.*;

/**
 * Test for the class 'Buffers'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
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

  @Test(dataProvider="createDataTypes", groups="all")
  public <T> void runAllocations( T type ) {
    
    Primitive  primitive  = Primitive.byType( type );
    assertThat( primitive, is( notNullValue() ) );
    
    Integer    count      = CommonProperty.BufferCount.getValue( System.getProperties() );
    
    Buffers<T> buffers    = Buffers.newBuffers( primitive );
    T          datablock1 = buffers.allocate();
    assertThat( datablock1, is( notNullValue() ) );
    assertTrue( primitive.length( datablock1 ) >= count.intValue() );
    
    T          datablock2 = buffers.allocate( null );
    assertThat( datablock2, is( notNullValue() ) );
    assertTrue( primitive.length( datablock2 ) >= count.intValue() );
    
    T          datablock3 = buffers.allocate( Integer.valueOf( 100 ) );
    assertThat( datablock3, is( notNullValue() ) );
    assertTrue( primitive.length( datablock3 ) >= 100 );
    assertTrue( primitive.length( datablock3 ) < count.intValue() );
    
  }
  
} /* ENDCLASS */
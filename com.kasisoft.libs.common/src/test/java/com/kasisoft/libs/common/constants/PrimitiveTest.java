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

  @Test(dataProvider="createData",groups="all")
  public void arrayCreation( Primitive primitive, Integer size ) {
    Assert.assertNotNull( primitive.getArrayClass() );
    Assert.assertNotNull( primitive.getObjectClass() );
    Assert.assertNotNull( primitive.getPrimitiveClass() );
    Object array = primitive.newArray( size.intValue() );
    Assert.assertEquals( primitive.length( array ), size.intValue() );
  }
  
  @Test(dataProvider="createData",groups="all")
  public void arrayObjectCreation( Primitive primitive, Integer size ) {
    Assert.assertNotNull( primitive.getArrayClass() );
    Assert.assertNotNull( primitive.getObjectClass() );
    Assert.assertNotNull( primitive.getPrimitiveClass() );
    Object array = primitive.newObjectArray( size.intValue() );
    Assert.assertEquals( primitive.length( array ), size.intValue() );
  }

  @DataProvider(name="createTypes")
  public Object[][] createTypes() {
    Primitive[] primitives = Primitive.values();
    Object[][]  result     = new Object[ primitives.length + 1 ][];
    for( int i = 0; i < primitives.length; i++ ) {
      result[i] = new Object[] { primitives[i].newArray(0), primitives[i] };
    }
    result[ result.length - 1 ] = new Object[] { new String[0], null };
    return result;
  }
  
  @Test(dataProvider="createTypes",groups="all")
  public void byArrayType( Object array, Primitive expected ) {
    Assert.assertEquals( Primitive.byArrayType( array ), expected );
  }

  @DataProvider(name="createObjectTypes")
  public Object[][] createObjectTypes() {
    return new Object[][] {
      { Boolean   . valueOf( true                  ), Primitive . PBoolean },
      { Byte      . valueOf( Byte      . MIN_VALUE ), Primitive . PByte    },
      { Character . valueOf( Character . MIN_VALUE ), Primitive . PChar    },
      { Short     . valueOf( Short     . MIN_VALUE ), Primitive . PShort   },
      { Integer   . valueOf( Integer   . MIN_VALUE ), Primitive . PInt     },
      { Long      . valueOf( Long      . MIN_VALUE ), Primitive . PLong    },
      { Float     . valueOf( Float     . MIN_VALUE ), Primitive . PFloat   },
      { Double    . valueOf( Double    . MIN_VALUE ), Primitive . PDouble  },
      { ""                                          , null               }
    };
  }

  @Test(dataProvider="createObjectTypes",groups="all")
  public void byObjectType( Object array, Primitive expected ) {
    Assert.assertEquals( Primitive.byObjectType( array ), expected );
  }

} /* ENDCLASS */

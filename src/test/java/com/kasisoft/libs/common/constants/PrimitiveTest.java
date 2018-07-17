package com.kasisoft.libs.common.constants;

import static com.kasisoft.libs.common.base.LibConfig.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

/**
 * Tests for the constants 'Primitive'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
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
    assertThat( primitive.getArrayClass     (), is( notNullValue() ) );
    assertThat( primitive.getObjectClass    (), is( notNullValue() ) );
    assertThat( primitive.getPrimitiveClass (), is( notNullValue() ) );
    Object array = primitive.newArray( size.intValue() );
    assertThat( primitive.length( array ), is( size.intValue() ) );
  }
  
  @Test(dataProvider="createData",groups="all")
  public void arrayObjectCreation( Primitive primitive, Integer size ) {
    assertThat( primitive.getArrayClass     (), is( notNullValue() ) );
    assertThat( primitive.getObjectClass    (), is( notNullValue() ) );
    assertThat( primitive.getPrimitiveClass (), is( notNullValue() ) );
    Object array = primitive.newObjectArray( size.intValue() );
    assertThat( primitive.length( array ), is( size.intValue() ) );
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
      { ""                                          , null                 }
    };
  }

  @DataProvider(name="createAllTypes")
  public Object[][] createAllTypes() {
    return new Object[][] {
        
      // object types
      { Boolean   . valueOf( true                  ), Primitive . PBoolean },
      { Byte      . valueOf( Byte      . MIN_VALUE ), Primitive . PByte    },
      { Character . valueOf( Character . MIN_VALUE ), Primitive . PChar    },
      { Short     . valueOf( Short     . MIN_VALUE ), Primitive . PShort   },
      { Integer   . valueOf( Integer   . MIN_VALUE ), Primitive . PInt     },
      { Long      . valueOf( Long      . MIN_VALUE ), Primitive . PLong    },
      { Float     . valueOf( Float     . MIN_VALUE ), Primitive . PFloat   },
      { Double    . valueOf( Double    . MIN_VALUE ), Primitive . PDouble  },
      
      // array of object types
      { new Boolean   [0], Primitive . PBoolean },
      { new Byte      [0], Primitive . PByte    },
      { new Character [0], Primitive . PChar    },
      { new Short     [0], Primitive . PShort   },
      { new Integer   [0], Primitive . PInt     },
      { new Long      [0], Primitive . PLong    },
      { new Float     [0], Primitive . PFloat   },
      { new Double    [0], Primitive . PDouble  },

      // array of primitive types
      { new boolean   [0], Primitive . PBoolean },
      { new byte      [0], Primitive . PByte    },
      { new char      [0], Primitive . PChar    },
      { new short     [0], Primitive . PShort   },
      { new int       [0], Primitive . PInt     },
      { new long      [0], Primitive . PLong    },
      { new float     [0], Primitive . PFloat   },
      { new double    [0], Primitive . PDouble  },

      // non-primitive related
      { ""   , null },
      
      // null argument
      { null , null }
      
    };
  }

  @Test(dataProvider="createAllTypes",groups="all")
  public void byType( Object array, Primitive expected ) {
    assertThat( Primitive.byType( array ), is( expected ) );
  }

  @DataProvider(name="createRunAllocations")
  public Object[][] createRunAllocations() {
    return new Object[][] {
        
      // object types
      { Boolean   . valueOf( true                  ), Primitive . PBoolean },
      { Byte      . valueOf( Byte      . MIN_VALUE ), Primitive . PByte    },
      { Character . valueOf( Character . MIN_VALUE ), Primitive . PChar    },
      { Short     . valueOf( Short     . MIN_VALUE ), Primitive . PShort   },
      { Integer   . valueOf( Integer   . MIN_VALUE ), Primitive . PInt     },
      { Long      . valueOf( Long      . MIN_VALUE ), Primitive . PLong    },
      { Float     . valueOf( Float     . MIN_VALUE ), Primitive . PFloat   },
      { Double    . valueOf( Double    . MIN_VALUE ), Primitive . PDouble  },
      
      // array of object types
      { new Boolean   [0], Primitive . PBoolean },
      { new Byte      [0], Primitive . PByte    },
      { new Character [0], Primitive . PChar    },
      { new Short     [0], Primitive . PShort   },
      { new Integer   [0], Primitive . PInt     },
      { new Long      [0], Primitive . PLong    },
      { new Float     [0], Primitive . PFloat   },
      { new Double    [0], Primitive . PDouble  },

      // array of primitive types
      { new boolean   [0], Primitive . PBoolean },
      { new byte      [0], Primitive . PByte    },
      { new char      [0], Primitive . PChar    },
      { new short     [0], Primitive . PShort   },
      { new int       [0], Primitive . PInt     },
      { new long      [0], Primitive . PLong    },
      { new float     [0], Primitive . PFloat   },
      { new double    [0], Primitive . PDouble  },

    };
  }
  
  @Test(dataProvider="createRunAllocations", groups="all")
  public void runAllocations( Object array, Primitive expected ) {
    
    Primitive  primitive  = Primitive.byType( array );
    assertThat( primitive, is( expected ) );
    
    Integer    count      = cfgBufferSize();
    
    Object     datablock1 = primitive.allocate();
    assertThat( datablock1, is( notNullValue() ) );
    assertTrue( primitive.length( datablock1 ) >= count.intValue() );
    
    Object     datablock2 = primitive.allocate( null );
    assertThat( datablock2, is( notNullValue() ) );
    assertTrue( primitive.length( datablock2 ) >= count.intValue() );
    
    Object     datablock3 = primitive.allocate( Integer.valueOf( 100 ) );
    assertThat( datablock3, is( notNullValue() ) );
    assertTrue( primitive.length( datablock3 ) >= 100 );
    assertTrue( primitive.length( datablock3 ) < count.intValue() );
    
  }

} /* ENDCLASS */

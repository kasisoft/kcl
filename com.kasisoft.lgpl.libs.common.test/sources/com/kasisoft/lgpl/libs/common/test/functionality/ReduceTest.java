/**
 * Name........: ReduceTest
 * Description.: A test for the interface 'Reduce'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.functionality;

import com.kasisoft.lgpl.libs.common.functionality.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

/**
 * A test for the interface 'Reduce'.
 */
@Test(groups="all")
public class ReduceTest {

  private Sum   reducefunction;
  
  @BeforeSuite
  public void setup() {
    reducefunction = new Sum();
  }
  
  private Integer calc( int n ) {
    int abs = Math.abs(n);
    return Integer.valueOf( (n < 0 ? -1 : 1) * (abs + 1) * (abs / 2) );
  }
  
  private List<Byte> toList( int n ) {
    List<Byte> result = new ArrayList<Byte>();
    if( n > 0 ) {
      for( int i = 1; i <= n; i++ ) {
        result.add( Byte.valueOf( (byte) i ) );
      }
    } else if( n < 0 ) {
      for( int i = n; i <= 0; i++ ) {
        result.add( Byte.valueOf( (byte) i ) );
      }
    }
    return result;
  }
  
  @DataProvider(name="createValues")
  public Object[][] createValues() {
    return new Object[][] {
      { toList( -100 ) , calc( -100 ) },
      { toList( 0    ) , calc( 0    ) },
      { toList( 100  ) , calc( 100  ) }
    };
  }
  
  @DataProvider(name="invalidUses")
  public Object[][] invalidUses() {
    return new Object[][] {
      { null            , toList( -100 )  , calc( -100 ) },
      { reducefunction  , null            , calc( -100 ) },
      { reducefunction  , toList( -100 )  , null         },
    };
  }
  
  @Test(dataProvider="invalidUses",expectedExceptions={NullPointerException.class})
  public void invalidUse( Reduce<Byte,Integer> reduce, List<Byte> list, Integer initial ) {
    FuFunctions.reduce( reduce, list, initial );
  }
  
  @Test(dataProvider="createValues")
  public void sumValues( List<Byte> list, Integer expected ) {
    Integer result = FuFunctions.reduce( reducefunction, list, Integer.valueOf(0) );
    Assert.assertNotNull( result );
    Assert.assertEquals( result, expected );
  }
  
  /**
   * Simply sums up a value.
   */
  private static class Sum implements Reduce<Byte,Integer> {

    /**
     * {@inheritDoc}
     */
    public Integer reduce( Byte input, Integer initial ) {
      return Integer.valueOf( input.byteValue() + initial.intValue() );
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */

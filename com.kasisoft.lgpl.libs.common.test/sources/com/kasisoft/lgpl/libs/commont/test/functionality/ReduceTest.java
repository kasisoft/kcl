/**
 * Name........: ReduceTest
 * Description.: A test for the interface 'Reduce'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.commont.test.functionality;

import com.kasisoft.lgpl.libs.common.functionality.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

/**
 * A test for the interface 'Reduce'.
 */
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

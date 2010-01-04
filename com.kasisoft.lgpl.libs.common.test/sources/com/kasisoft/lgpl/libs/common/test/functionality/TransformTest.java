/**
 * Name........: TransformTest
 * Description.: A test for the interface 'Transform'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.functionality;

import com.kasisoft.lgpl.libs.common.functionality.*;

import com.kasisoft.lgpl.libs.common.test.framework.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

/**
 * A test for the interface 'Transform'.
 */
public class TransformTest {

  private Wrapper   wrapper;
  
  @BeforeSuite
  public void setup() {
    wrapper = new Wrapper();
  }
  
  @DataProvider(name="invalidUses")
  public Object[][] invalidUses() {
    return new Object[][] {
      { null    , Utilities.toList(3) },
      { wrapper , null                },
    };
  }
  
  @Test(dataProvider="invalidUses", expectedExceptions={NullPointerException.class})
  public void invalidUse( Transform<Integer,Integer> transform, List<Integer> list ) {
    FuFunctions.map( transform, list );
  }
  
  @Test
  public void runWrapper() {
    List<List<Integer>> result = FuFunctions.map( wrapper, Utilities.toList( 3 ) );
    Assert.assertNotNull( result );
    Assert.assertEquals( result.size(), 1 );
    List<Integer>       first  = result.get(0);
    Assert.assertEquals( first.get(0), Integer.valueOf(3) );
  }
  
  /**
   * Creates a wrapper for an Integer.
   */
  private static class Wrapper implements Transform<Integer,List<Integer>> {

    /**
     * {@inheritDoc}
     */
    public List<Integer> map( Integer input ) {
      List<Integer> result = new ArrayList<Integer>();
      result.add( input );
      return result;
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */

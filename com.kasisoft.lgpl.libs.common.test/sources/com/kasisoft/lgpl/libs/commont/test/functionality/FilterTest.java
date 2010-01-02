/**
 * Name........: FilterTest
 * Description.: A test for the interface 'Filter'.
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
 * A test for the interface 'Filter'.
 */
public class FilterTest {

  private Filter<Integer>   filter;
  
  private List<Integer> toList( int ... args ) {
    List<Integer> result = new ArrayList<Integer>();
    for( int value : args ) {
      result.add( Integer.valueOf( value ) );
    }
    return result;
  }
  
  @BeforeSuite
  public void setup() {
    filter = new EvenFilter();
  }

  @DataProvider(name="filterForOne")
  public Object[][] createForOne() {
    return new Object[][] {
      { Integer.valueOf(0), toList(7) },
      { Integer.valueOf(1), toList(8) },
    };
  }

  @DataProvider(name="filterForMultiple")
  public Object[][] createForMultiple() {
    return new Object[][] {
      { Integer.valueOf(2), toList( 7, 8, 13, 14 ) },
      { Integer.valueOf(2), toList( 8, 13, 14 ) },
      { Integer.valueOf(0), toList( 7, 13 ) },
    };
  }

  @Test
  public void filterEmpty() {
    List<Integer> result = FuFunctions.filter( filter, new ArrayList<Integer>() );
    Assert.assertNotNull( result );
    Assert.assertTrue( result.isEmpty() );
  }
  
  @Test(dataProvider="filterForOne")
  public void filterOne( Integer expectedsize, List<Integer> values ) {
    List<Integer> result = FuFunctions.filter( filter, values );
    Assert.assertNotNull( result );
    Assert.assertEquals( result.size(), expectedsize.intValue() );
  }
  
  @Test(dataProvider="filterForMultiple")
  public void filterMultiple( Integer expectedsize, List<Integer> values ) {
    List<Integer> result = FuFunctions.filter( filter, values );
    Assert.assertNotNull( result );
    Assert.assertEquals( result.size(), expectedsize.intValue() );
  }

  /**
   * Sample class used to filter even numbers.
   */
  private static class EvenFilter implements Filter<Integer> {

    /**
     * {@inheritDoc}
     */
    public boolean accept( Integer input ) {
      return input.intValue() % 2 == 0;
    }
    
  } /* ENDCLASS */

} /* ENDCLASS */

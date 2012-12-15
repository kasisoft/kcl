/**
 * Name........: FilterTest
 * Description.: A test for the interface 'Filter'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.functionality;

import com.kasisoft.libs.common.test.framework.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

/**
 * A test for the interface 'Filter'.
 */
@Test(groups="all")
public class FilterTest {

  private Filter<Integer>   filter;
  
  @BeforeTest
  public void setup() {
    filter = new EvenFilter();
  }

  @DataProvider(name="createForOne")
  public Object[][] createForOne() {
    return new Object[][] {
      { Integer.valueOf(0), Utilities.intsToList(7) },
      { Integer.valueOf(1), Utilities.intsToList(8) },
    };
  }

  @DataProvider(name="createForMultiple")
  public Object[][] createForMultiple() {
    return new Object[][] {
      { Integer.valueOf(2), Utilities.intsToList( 7, 8, 13, 14 ) },
      { Integer.valueOf(2), Utilities.intsToList( 8, 13, 14 ) },
      { Integer.valueOf(0), Utilities.intsToList( 7, 13 ) },
    };
  }
  
  @DataProvider(name="invalidUses")
  public Object[][] invalidUses() {
    return new Object[][] {
      { filter, null },
      { null,   Utilities.intsToList( 2, 4 ) },
      { null, null }
    };
  }

  @Test(expectedExceptions={NullPointerException.class,ArrayIndexOutOfBoundsException.class},dataProvider="invalidUses")
  public void invalidUse( Filter<Integer> filter, List<Integer> list ) {
    FuFunctions.filter( filter, list );
  }
  
  @Test
  public void filterEmpty() {
    List<Integer> result = FuFunctions.filter( filter, new ArrayList<Integer>() );
    Assert.assertNotNull( result );
    Assert.assertTrue( result.isEmpty() );
  }
  
  @Test(dataProvider="createForOne")
  public void filterOne( Integer expectedsize, List<Integer> values ) {
    List<Integer> result = FuFunctions.filter( filter, values );
    Assert.assertNotNull( result );
    Assert.assertEquals( result.size(), expectedsize.intValue() );
  }
  
  @Test(dataProvider="createForMultiple")
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

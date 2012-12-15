/**
 * Name........: PredefinedTest
 * Description.: Tests for the class 'Predefined'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.functionality;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

/**
 * Tests for the class 'Predefined'.
 */
@Test(groups="all")
public class PredefinedTest {

  private Filter<Object>   integerfilter;
  private Filter<Object>   isevenfilter;
  private List<Object>     inputlist;
  
  @BeforeSuite
  public void setup() {
    integerfilter = new IsInteger();
    isevenfilter  = new IsEven();
    inputlist     = Arrays.asList( new Object[] { "STR", Integer.valueOf( 34 ), Double.valueOf(2.0), Boolean.TRUE, Integer.valueOf( 37 ) } );
  }
  
  @DataProvider(name="createAndValues")
  public Object[][] createAndValues() {
    return new Object[][] {
      { inputlist, Arrays.asList( Integer.valueOf( 34 ) ) }  
    };
  }

  @DataProvider(name="createOrValues")
  public Object[][] createOrValues() {
    return new Object[][] {
      { inputlist, Arrays.asList( Integer.valueOf( 34 ), Integer.valueOf( 37 ) ) }  
    };
  }

  @DataProvider(name="createNotValues")
  public Object[][] createNotValues() {
    return new Object[][] {
      { inputlist, Arrays.asList( "STR", Double.valueOf(2.0), Boolean.TRUE ) }  
    };
  }

  @Test(dataProvider="createAndValues")
  public void filterAnd( List<Object> objects, List<Object> expected ) {
    Filter<Object> filter = Predefined.and( integerfilter, isevenfilter );
    List<Object>   result = FuFunctions.filter( filter, objects );
    Assert.assertEquals( result, expected );
  }

  @Test(dataProvider="createOrValues")
  public void filterOr( List<Object> objects, List<Object> expected ) {
    Filter<Object> filter = Predefined.or( integerfilter, isevenfilter );
    List<Object>   result = FuFunctions.filter( filter, objects );
    Assert.assertEquals( result, expected );
  }

  @Test(dataProvider="createNotValues")
  public void filterNot( List<Object> objects, List<Object> expected ) {
    Filter<Object> filter = Predefined.not( integerfilter );
    List<Object>   result = FuFunctions.filter( filter, objects );
    Assert.assertEquals( result, expected );
  }

  /**
   * This filter is supposed to test for integer objects.
   */
  private static final class IsInteger implements Filter<Object> {

    /**
     * {@inheritDoc}
     */
    public boolean accept( Object input ) {
      return input instanceof Integer;
    }
    
  } /* ENDCLASS */

  /**
   * This filter is supposed to test for even numbers.
   */
  private static final class IsEven implements Filter<Object> {

    /**
     * {@inheritDoc}
     */
    public boolean accept( Object input ) {
      if( input instanceof Integer ) {
        Integer intobj = (Integer) input;
        return (intobj.intValue() % 2) == 0;
      } else {
        return false;
      }
    }
    
  } /* ENDCLASS */

} /* ENDCLASS */

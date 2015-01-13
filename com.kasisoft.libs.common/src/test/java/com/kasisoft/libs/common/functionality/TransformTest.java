package com.kasisoft.libs.common.functionality;

import com.kasisoft.libs.common.test.framework.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

/**
 * A test for the interface 'Transform'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransformTest {

  Wrapper   wrapper;
  
  @BeforeTest
  public void setup() {
    wrapper = new Wrapper();
  }
  
  @DataProvider(name="invalidUses")
  public Object[][] invalidUses() {
    return new Object[][] {
      { null    , Utilities.intsToList(3) },
      { wrapper , null                    },
    };
  }
  
  @Test(dataProvider="invalidUses", expectedExceptions={NullPointerException.class}, groups="all")
  public void invalidUse( Transform<Integer,Integer> transform, List<Integer> list ) {
    FuFunctions.map( transform, list );
  }
  
  @Test(groups="all")
  public void runWrapper() {
    List<List<Integer>> result = FuFunctions.map( wrapper, Utilities.intsToList( 3 ) );
    Assert.assertNotNull( result );
    Assert.assertEquals( result.size(), 1 );
    List<Integer>       first  = result.get(0);
    Assert.assertEquals( first.get(0), Integer.valueOf(3) );
  }
  
  /**
   * Creates a wrapper for an Integer.
   */
  private static class Wrapper implements Transform<Integer,List<Integer>> {

    @Override
    public List<Integer> map( Integer input ) {
      List<Integer> result = new ArrayList<>();
      result.add( input );
      return result;
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */

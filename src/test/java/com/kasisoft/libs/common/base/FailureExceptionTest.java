package com.kasisoft.libs.common.base;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

import java.util.*;

/**
 * Tests for 'FailureException'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FailureExceptionTest {

  @DataProvider(name="newException")
  public Object[][] newExceptionData() {
    List<Object[]> list = new ArrayList<>();
    for( FailureCode code : FailureCode.values() ) {
      list.add( new Object[] { code.newException(), code } );
    }
    return list.toArray( new Object[list.size()][2] );
  }
  
  @Test(dataProvider = "newException", groups = "all")
  public void newException( FailureException ex, FailureCode code ) {
    assertThat( ex.getFailurecode(), is( code ) );
  }

  
} /* ENDCLASS */

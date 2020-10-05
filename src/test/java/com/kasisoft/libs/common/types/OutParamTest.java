package com.kasisoft.libs.common.types;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class OutParamTest {

  private void callingWithOutParam(OutParam out) {
    out.setValue("Bibo");
  }
  
  @Test
  public void processOutParam() {
    
    var outParam = new OutParam();
    assertNull(outParam.getValue());
    
    callingWithOutParam(outParam);
    assertThat(outParam.getValue(), is("Bibo"));
    
  }
  
} /* ENDCLASS */

package com.kasisoft.libs.common.types;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class OutParamTest {

  private void callingWithOutParam(OutParam<String> out) {
    out.setValue("Bibo");
  }
  
  @Test
  public void processOutParam() {
    
    var outParam = new OutParam<String>();
    assertNull(outParam.getValue());
    
    callingWithOutParam(outParam);
    assertThat(outParam.getValue(), is("Bibo"));
    
  }
  
} /* ENDCLASS */

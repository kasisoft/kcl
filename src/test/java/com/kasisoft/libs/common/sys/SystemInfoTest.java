package com.kasisoft.libs.common.sys;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

/**
 * Tests for the type 'SystemInfo'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Test
public class SystemInfoTest {

  @Test(groups="all")
  public void oneMustBeActive() {
    SystemInfo info = SystemInfo.getRunningOS();
    assertThat( info, is( notNullValue() ) );
    assertThat( SystemInfo.ThisMachine, is( notNullValue() ) );
  }
  
} /* ENDCLASS */

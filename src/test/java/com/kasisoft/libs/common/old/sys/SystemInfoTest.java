package com.kasisoft.libs.common.old.sys;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.testng.annotations.Test;

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

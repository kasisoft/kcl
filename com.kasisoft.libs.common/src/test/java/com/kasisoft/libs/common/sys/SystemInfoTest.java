package com.kasisoft.libs.common.sys;

import org.testng.annotations.*;

import org.testng.*;

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
    Assert.assertNotNull( info );
    Assert.assertNotNull( SystemInfo.ThisMachine );
  }
  
} /* ENDCLASS */

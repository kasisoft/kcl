/**
 * Name........: SystemInfoTest
 * Description.: Tests for the type 'SystemInfo'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.sys;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Tests for the type 'SystemInfo'.
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

/**
 * Name........: SystemProcessTest
 * Description.: Test for the class 'SystemProcess'. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.sys;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.libs.common.sys.*;

import org.testng.annotations.*;

import org.testng.*;

import java.io.*;

/**
 * Test for the class 'SystemProcess'.
 */
@Test(groups="all")
public class SystemProcessTest {

  private File                    exefile;
  private SystemProcess           systemprocess;
  private ByteArrayOutputStream   byteout;
  private ByteArrayOutputStream   byteerr;
  
  @BeforeSuite
  public void setup() {
    exefile       = new File( "testdata/bin/testprocess.win32.exe" );
    Assert.assertTrue( exefile.canExecute() );
    byteout       = new ByteArrayOutputStream();
    byteerr       = new ByteArrayOutputStream();
    systemprocess = new SystemProcess( exefile );
    systemprocess.setErrorStream( byteerr );
    systemprocess.setOutputStream( byteout );
  }
  
  @BeforeMethod
  public void executionSetup() {
    byteerr.reset();
    byteout.reset();
  }
  
  private String getStdout() {
    // we need to trim as the c function generates a platform specific '\n'
    String str = new String( byteout.toByteArray() );
    return str.trim();
  }

  private String getStderr() {
    // we need to trim as the c function generates a platform specific '\n'
    String str = new String( byteerr.toByteArray() );
    return str.trim();
  }

  @Test
  public void causeReturncode() {
    FailureCode failurecode = systemprocess.execute( "-rc", "17" );
    Assert.assertEquals( failurecode, FailureCode.Success );
    Assert.assertEquals( systemprocess.getReturncode(), 17 );
    Assert.assertEquals( new String( byteerr.toByteArray() ), "" );
    Assert.assertEquals( new String( byteout.toByteArray() ), "" );
  }

  @Test
  public void toStdout() {
    FailureCode failurecode = systemprocess.execute();
    Assert.assertEquals( failurecode, FailureCode.Success );
    Assert.assertEquals( systemprocess.getReturncode(), 0 );
    Assert.assertEquals( getStderr(), "" );
    Assert.assertEquals( getStdout(), "Hello World !" );
  }

  @Test
  public void toStderr() {
    FailureCode failurecode = systemprocess.execute( "-stderr" );
    Assert.assertEquals( failurecode, FailureCode.Success );
    Assert.assertEquals( systemprocess.getReturncode(), 0 );
    Assert.assertEquals( getStdout(), "" );
    Assert.assertEquals( getStderr(), "Hello World !" );
  }

  /**
   * @todo [15-Jan-2010:KASI]   I currently don't know a way to identify a crash in an executed
   *                            application. There's no exception so even if the user affirms the
   *                            crashed application it will be identified as if it has been executed
   *                            successfully. There must be a proper way to identify such misbehaviours
   *                            even if it's rarely occuring.
   * 
  @Test
  public void errorApplication() {
    FailureCode failurecode = systemprocess.execute( "-error" );
    Assert.assertEquals( failurecode, FailureCode.Success );
    Assert.assertEquals( getStdout(), "" );
    Assert.assertEquals( getStderr(), "" );
  }
   */

} /* ENDCLASS */

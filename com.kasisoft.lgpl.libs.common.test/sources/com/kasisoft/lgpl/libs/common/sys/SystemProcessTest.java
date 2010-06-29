/**
 * Name........: SystemProcessTest
 * Description.: Test for the class 'SystemProcess'. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.sys;

import com.kasisoft.lgpl.libs.common.constants.*;

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
    exefile        = null;
    File   dir     = null;
    String basedir = System.getProperty( "test.basedir" );
    if( (basedir != null) && (basedir.length() > 0) ) {
      dir = new File( basedir );
    }
    String path    = null;
    if( SystemInfo.getRunningOS().isUnixLike() ) {
      path = "testdata/bin/testprocess.unix.exe";
    } else {
      path = "testdata/bin/testprocess.win32.exe";
    }
    if( dir != null ) {
      exefile       = new File( dir, path );
    } else {
      exefile       = new File( path );
    }
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
  
  private void checkResult( FailureCode code, int returncode, String err, String out ) {
    String  errstr = getStderr();
    String  outstr = getStdout();
    boolean goterr = ( code != FailureCode.Success ) ||
                     ( systemprocess.getReturncode() != returncode ) ||
                     ( ! err.equals( errstr ) ) ||
                     ( ! out.equals( outstr ) );
    if( goterr ) {
      System.out.printf( "FailureCode = %s\n", code );
      System.out.printf( "ReturnCode  = %d (expected = %d)\n", Integer.valueOf( systemprocess.getReturncode() ), Integer.valueOf( returncode ) );
      System.out.printf( "StdErr      = %s\n", errstr );
      System.out.printf( "StdOut      = %s\n", outstr );
      if( systemprocess.getException() != null ) {
        Assert.fail( systemprocess.getException().getMessage(), systemprocess.getException() );
      } else {
        Assert.fail();
      }
    }
  }

  @Test
  public void causeReturncode() {
    FailureCode failurecode = systemprocess.execute( "-rc", "17" );
    checkResult( failurecode, 17, "", "" );
  }

  @Test
  public void toStdout() {
    FailureCode failurecode = systemprocess.execute();
    checkResult( failurecode, 0, "", "Hello World !" );
  }

  @Test
  public void toStderr() {
    FailureCode failurecode = systemprocess.execute( "-stderr" );
    checkResult( failurecode, 0, "", "Hello World !" );
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

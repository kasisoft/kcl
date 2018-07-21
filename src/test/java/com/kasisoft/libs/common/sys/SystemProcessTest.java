package com.kasisoft.libs.common.sys;

import static org.testng.Assert.*;

import com.kasisoft.libs.common.test.framework.*;

import org.testng.annotations.*;

import java.io.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Test for the class 'SystemProcess'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Test(groups="all")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemProcessTest {

  File                    exefile;
  SystemProcess           systemprocess;
  ByteArrayOutputStream   byteout;
  ByteArrayOutputStream   byteerr;
  
  @BeforeTest
  public void setup() {
    exefile        = null;
    File   dir     = Utilities.getTestdataDir();
    String path    = null;
    if( SystemInfo.getRunningOS().isUnixLike() ) {
      path = "bin/testprocess.unix.exe";
    } else {
      path = "bin/testprocess.win32.exe";
    }
    exefile       = new File( dir, path );
    exefile.setExecutable( true );
    assertTrue( exefile.canExecute() );
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
  
  private void checkResult( int code, int returncode, String err, String out ) {
    String  errstr = getStderr();
    String  outstr = getStdout();
    boolean goterr = ( code != 0 ) ||
                     ( systemprocess.getReturncode() != returncode ) ||
                     ( ! err.equals( errstr ) ) ||
                     ( ! out.equals( outstr ) );
    if( goterr ) {
      System.out.printf( "FailureCode = %s\n", code );
      System.out.printf( "ReturnCode  = %d (expected = %d)\n", Integer.valueOf( systemprocess.getReturncode() ), Integer.valueOf( returncode ) );
      System.out.printf( "StdErr      = %s\n", errstr );
      System.out.printf( "StdOut      = %s\n", outstr );
      if( systemprocess.getException() != null ) {
        fail( systemprocess.getException().getMessage(), systemprocess.getException() );
      } else {
        fail();
      }
    }
  }

  @Test
  public void causeReturncode() {
    int failurecode = systemprocess.execute( "-rc", "17" );
    checkResult( failurecode, 17, "", "" );
  }

  @Test
  public void toStdout() {
    int failurecode = systemprocess.execute();
    checkResult( failurecode, 0, "", "Hello World !" );
  }

  @Test
  public void toStderr() {
    int failurecode = systemprocess.execute( "-stderr" );
    checkResult( failurecode, 0, "Hello World !", "" );
  }

} /* ENDCLASS */

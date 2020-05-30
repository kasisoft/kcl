package com.kasisoft.libs.common.old.thread;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.kasisoft.libs.common.old.io.*;
import com.kasisoft.libs.common.old.test.framework.*;

import org.testng.annotations.*;

import java.io.*;

/**
 * Test for the class 'FileDeleteRunnable'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FileDeleteRunnableTest {
  
  @Test(groups="all")
  public void deleteFiles() throws InterruptedException {
    File                tempdir   = IoFunctions.newTempFile( "temp-", null );
    tempdir.mkdirs();
    Utilities.createFileSystemStructure( tempdir );
    File[]              files     = tempdir.listFiles();
    assertNotNull( files );
    FileDeleteRunnable  runnable  = new FileDeleteRunnable( files );
    Thread              thread    = new Thread( runnable );
    thread.start();
    thread.join();
    assertTrue( runnable.hasCompleted() );
    File[] children = tempdir.listFiles();
    assertThat( children, is( notNullValue() ) );
    assertThat( children.length, is(0) );
  }
  
} /* ENDCLASS */

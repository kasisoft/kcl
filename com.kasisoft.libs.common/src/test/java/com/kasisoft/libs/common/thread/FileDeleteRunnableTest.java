package com.kasisoft.libs.common.thread;

import com.kasisoft.libs.common.io.*;
import com.kasisoft.libs.common.test.framework.*;

import org.testng.annotations.*;

import org.testng.*;

import java.io.*;

/**
 * Test for the class 'FileDeleteRunnable'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Test(groups="all")
public class FileDeleteRunnableTest {
  
  @Test
  public void deleteFiles() throws InterruptedException {
    File                tempdir   = IoFunctions.newTempFile( "temp-", null );
    tempdir.mkdirs();
    Utilities.createFileSystemStructure( tempdir );
    FileDeleteRunnable  runnable  = new FileDeleteRunnable( tempdir.listFiles() );
    Thread              thread    = new Thread( runnable );
    thread.start();
    thread.join();
    Assert.assertTrue( runnable.hasCompleted() );
    File[] children = tempdir.listFiles();
    Assert.assertNotNull( children );
    Assert.assertEquals( children.length, 0 );
  }
  
} /* ENDCLASS */

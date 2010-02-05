/**
 * Name........: FileDeleteRunnableTest
 * Description.: Test for the class 'FileDeleteRunnable'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.thread;

import com.kasisoft.lgpl.libs.common.thread.*;

import com.kasisoft.lgpl.libs.common.io.*;

import org.testng.annotations.*;

import org.testng.*;

import java.io.*;

/**
 * Test for the class 'FileDeleteRunnable'.
 */
public class FileDeleteRunnableTest extends AbstractFileSystemTest {
  
  @Test
  public void deleteFiles() throws InterruptedException {
    File                tempdir   = IoFunctions.newTempFile( "temp-", null );
    tempdir.mkdirs();
    createFileSystemStructure( tempdir );
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

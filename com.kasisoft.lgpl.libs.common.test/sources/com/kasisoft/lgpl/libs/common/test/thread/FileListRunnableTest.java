/**
 * Name........: FileListRunnableTest
 * Description.: Test for the class 'FileListRunnable'.
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

import java.util.*;

import java.io.*;

/**
 * Test for the class 'FileListRunnable'.
 */
public class FileListRunnableTest extends AbstractFileSystemTest {
  
  @Test
  public void listFiles() throws InterruptedException {
    File                tempdir   = IoFunctions.newTempFile( "temp-", null );
    tempdir.mkdirs();
    List<File>          created   = createFileSystemStructure( tempdir );
    Collections.sort( created );
    FileListRunnable    runnable  = new FileListRunnable( tempdir.listFiles() );
    Thread              thread    = new Thread( runnable );
    thread.start();
    thread.join();
    Assert.assertTrue( runnable.hasCompleted() );
    List<File> files = runnable.getAllFiles();
    Assert.assertEquals( files.size(), created.size() );
    Collections.sort( files );
    Assert.assertEquals( files, created );
  }
  
} /* ENDCLASS */
